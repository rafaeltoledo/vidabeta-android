package net.rafaeltoledo.vidabeta;

import java.util.ArrayList;
import java.util.List;

import net.rafaeltoledo.vidabeta.model.Podcast;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ListaPodcastsActivity extends Activity implements
		AdapterView.OnItemClickListener {

	private ListView lista;
	private Button botaoVoltar;
	List<Podcast> podcasts = new ArrayList<Podcast>();
	ArrayAdapter<Podcast> adaptador = null;
	
	public static ProgressDialog dialog = null;
	private StatusInstancia status = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_podcasts);

		lista = (ListView) findViewById(R.id.lista_casts);
		lista.setOnItemClickListener(this);

		botaoVoltar = (Button) findViewById(R.id.botao_voltar);
		botaoVoltar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
		dialog = ProgressDialog.show(this, "Baixando Lista de Podcasts",
                "Por favor, aguarde... Isso vai levar só alguns segundos...", true);
		status = (StatusInstancia) getLastNonConfigurationInstance();

		String feed = getIntent().getExtras().getString("FEED_LOCATION");
		Log.i("VidaBeta", feed);
        if (status == null) {
            status = new StatusInstancia();
            status.handler = new HandlerSincronizacao(this);

            Intent i = new Intent(this, ListaPodcastService.class);
            i.putExtra(ListaPodcastService.MESSENGER_EXTRA, new Messenger(status.handler));
            i.putExtra(ListaPodcastService.FEED_LOCATION, feed);
            startService(i);
        } else {
            if (status.handler != null) {
                status.handler.anexar(this);
            }
            
            if (status.podcasts != null) {
				preencherLista(status.podcasts);
			}
        }
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!podcasts.isEmpty()) {
			
		}
	}

	private void preencherLista(List<Podcast> podcasts) {
		this.podcasts = podcasts;
		adaptador = new ArrayAdapter<Podcast>(this,
				android.R.layout.simple_list_item_1, podcasts);
		lista.setAdapter(adaptador);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(this, PodcastActivity.class);
		i.putExtra("cast", podcasts.get((int) id));
		startActivity(i);
	}
	
	private void atirarErro(Throwable t) {
        Builder builder = new Builder(this);
        builder.setTitle("Erro!").setMessage(t.toString()).setPositiveButton("OK", null).show();
    }
	
	@Override
    public Object onRetainNonConfigurationInstance() {
        if (status.handler != null) {
            status.handler.desanexar();
        }

        return status;
    }
	
	private static class StatusInstancia {
		List<Podcast> podcasts = null;
        HandlerSincronizacao handler = null;
    }
	
	private static class HandlerSincronizacao extends Handler {
        private ListaPodcastsActivity activity = null;

        HandlerSincronizacao(ListaPodcastsActivity activity) {
            anexar(activity);
        }

        void anexar(ListaPodcastsActivity activity) {
            this.activity = activity;
        }

        void desanexar() {
            activity = null;
        }

        @SuppressWarnings("unchecked")
		@Override
        public void handleMessage(Message msg) {
        	ListaPodcastsActivity.dialog.dismiss();
            if (msg.arg1 == RESULT_OK) {
                activity.preencherLista((List<Podcast>) msg.obj);
            } else {
                activity.atirarErro((Exception) msg.obj);
            }
        }
    }
}
