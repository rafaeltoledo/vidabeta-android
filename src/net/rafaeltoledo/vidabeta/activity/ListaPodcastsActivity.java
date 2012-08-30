package net.rafaeltoledo.vidabeta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListaPodcastsActivity extends Activity {

	private Button botaoVoltar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_podcasts);

		botaoVoltar = (Button) findViewById(R.id.botao_voltar);
		botaoVoltar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
	}
}
