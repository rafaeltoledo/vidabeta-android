package net.rafaeltoledo.vidabeta.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalActivity extends Activity {
	
	private Button botaoSair;
	private Button botaoPodcasts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        
        botaoSair = (Button) findViewById(R.id.botao_sair);
        botaoSair.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();				
			}
		});
        
        botaoPodcasts = (Button) findViewById(R.id.botao_podcasts);
        botaoPodcasts.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(PrincipalActivity.this, ListaPodcastsActivity.class);
				startActivity(i);
			}
		});
    }
}
