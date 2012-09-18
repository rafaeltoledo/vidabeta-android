package net.rafaeltoledo.vidabeta;

import net.rafaeltoledo.vidabeta.model.Podcast;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PodcastActivity extends Activity implements OnClickListener,
		OnTouchListener, OnCompletionListener, OnBufferingUpdateListener,
		OnPreparedListener {

	public static final String IMAGE_LOCATION = "net.rafaeltoledo.IMAGE_LOCATION";
	private StatusInstancia status = null;
	public static ProgressDialog dialog = null;
	private static boolean capaAtualizada = false;
	public boolean audioOk = false;

	private Podcast podcast;
	private TextView data;
	private Button botaoVoltar;
	private ImageButton playPause;
	private SeekBar progresso;
	private TextView descricao;
	private ImageView capa;
	private TextView info;
	private MediaPlayer media;

	private int mediaFileLengthInMilliseconds;
	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_podcast);

		botaoVoltar = (Button) findViewById(R.id.botao_voltar);
		botaoVoltar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		podcast = (Podcast) getIntent().getSerializableExtra("cast");

		descricao = (TextView) findViewById(R.id.descricao);
		descricao.setText(podcast.getDescricao());

		data = (TextView) findViewById(R.id.data_publicacao);
		data.setText(podcast.getData());

		info = (TextView) findViewById(R.id.info);
		info.setText(Html.fromHtml("<b>Duração:</b> " + podcast.getDuracao()));

		capa = (ImageView) findViewById(R.id.capa);

		playPause = (ImageButton) findViewById(R.id.play_pause);
		playPause.setOnClickListener(this);

		progresso = (SeekBar) findViewById(R.id.progresso);
		progresso.setMax(99);
		progresso.setOnTouchListener(this);

		media = new MediaPlayer();
		media.setOnBufferingUpdateListener(this);
		media.setOnCompletionListener(this);
		media.setOnPreparedListener(this);
		media.setScreenOnWhilePlaying(true);

		if (!capaAtualizada) {
			dialog = ProgressDialog.show(this, "Aguarde",
					"Carregando informações do podcast...", true);
			status = (StatusInstancia) getLastNonConfigurationInstance();
			if (status == null) {
				status = new StatusInstancia();
				status.handler = new HandlerDownload(this);

				Intent i = new Intent(this, DownloadImageService.class);
				i.putExtra(DownloadImageService.IMAGE_LOCATION,
						podcast.getFoto());
				i.putExtra(DownloadImageService.MESSENGER_EXTRA, new Messenger(
						status.handler));
				startService(i);
			} else {
				if (status.handler != null) {
					status.handler.anexar(this);
				}

				if (status.capa != null) {
					carregarCapa(status.capa);
				}
			}
			capaAtualizada = true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		media.stop();
		capaAtualizada = false;
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		progresso.setSecondaryProgress(percent);

		// Atualizar contador
		info.setText(Html.fromHtml("<b>Duração:</b> "
				+ converterTempo(mediaFileLengthInMilliseconds * percent / 100)
				+ " / " + converterTempo(mediaFileLengthInMilliseconds)));
	}

	public void onCompletion(MediaPlayer mp) {
		playPause.setImageResource(R.drawable.play);
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.progresso) {
			if (media.isPlaying()) {
				SeekBar sb = (SeekBar) v;
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100)
						* sb.getProgress();
				media.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.play_pause) {
			if (!audioOk) {
				try {
					media.setAudioStreamType(AudioManager.STREAM_MUSIC);
					media.setDataSource(podcast.getLink());
					media.prepareAsync();
					playPause.setImageResource(R.drawable.ic_dialog_time);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				mediaFileLengthInMilliseconds = media.getDuration();

				if (!media.isPlaying()) {
					media.start();
					// playPause.setImageResource(R.drawable.pause);
					playPause
							.setImageResource(android.R.drawable.ic_media_pause);
				} else {
					media.pause();
					// playPause.setImageResource(R.drawable.play);
					playPause
							.setImageResource(android.R.drawable.ic_media_play);
				}

				atualizaBarraPrimaria();
			}
		}
	}

	private void atualizaBarraPrimaria() {
		progresso
				.setProgress((int) (((float) media.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
		if (media.isPlaying()) {
			Runnable notification = new Runnable() {
				public void run() {
					atualizaBarraPrimaria();
				}
			};
			handler.postDelayed(notification, 1000);
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (status.handler != null) {
			status.handler.desanexar();
		}
		return status;
	}

	private void carregarCapa(Drawable capa) {
		this.capa.setImageDrawable(capa);
	}

	private void atirarErro(Throwable t) {
		Builder builder = new Builder(this);
		builder.setTitle("Erro!").setMessage(t.toString())
				.setPositiveButton("OK", null).show();
	}

	private static class HandlerDownload extends Handler {
		private PodcastActivity activity = null;

		HandlerDownload(PodcastActivity activity) {
			anexar(activity);
		}

		void anexar(PodcastActivity activity) {
			this.activity = activity;
		}

		void desanexar() {
			activity = null;
		}

		@Override
		public void handleMessage(Message msg) {
			PodcastActivity.dialog.dismiss();
			if (msg.arg1 == RESULT_OK) {
				activity.carregarCapa((Drawable) msg.obj);
			} else {
				activity.atirarErro((Exception) msg.obj);
			}
		}
	}

	private static class StatusInstancia {
		Drawable capa = null;
		HandlerDownload handler = null;
	}

	public void onPrepared(MediaPlayer mp) {
		audioOk = true;
		media.start();
		// playPause.setImageResource(R.drawable.pause);
		playPause.setImageResource(android.R.drawable.ic_media_pause);
		mediaFileLengthInMilliseconds = media.getDuration();
		// atualizaBarraPrimaria();
	}

	private String converterTempo(int millis) {
		int segundos = millis / 1000;
		int minutos = segundos / 60;
		segundos = segundos % 60;
		int horas = minutos / 60;
		minutos = minutos % 60;

		return String.format("%02d:%02d:%02d", horas, minutos, segundos);
	}
}
