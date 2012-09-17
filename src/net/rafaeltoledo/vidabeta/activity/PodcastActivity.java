package net.rafaeltoledo.vidabeta.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.rafaeltoledo.vidabeta.model.Podcast;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
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
		OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

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
		capa.setImageDrawable(this.ImageOperations(this, podcast.getFoto()));

		playPause = (ImageButton) findViewById(R.id.play_pause);
		playPause.setOnClickListener(this);

		progresso = (SeekBar) findViewById(R.id.progresso);
		progresso.setMax(99);
		progresso.setOnTouchListener(this);

		media = new MediaPlayer();
		media.setOnBufferingUpdateListener(this);
		media.setOnCompletionListener(this);
	}

	private Drawable ImageOperations(Context ctx, String url) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			return Drawable.createFromStream(is, "src");
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public Object fetch(String address) throws MalformedURLException,
			IOException {

		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		media.stop();
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		progresso.setSecondaryProgress(percent);
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
			try {
				media.setDataSource(podcast.getLink());
				media.prepare();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			mediaFileLengthInMilliseconds = media.getDuration();

			if (!media.isPlaying()) {
				media.start();
				playPause.setImageResource(R.drawable.pause);
			} else {
				media.pause();
				playPause.setImageResource(R.drawable.play);
			}

			atualizaBarraPrimaria();
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
}
