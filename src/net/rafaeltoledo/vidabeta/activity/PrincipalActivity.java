package net.rafaeltoledo.vidabeta.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
				if (redeDisponivel()) {				
					Intent i = new Intent(PrincipalActivity.this, ListaPodcastsActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(PrincipalActivity.this, getApplicationContext().
							getString(R.string.sem_rede), Toast.LENGTH_LONG).show();
				}
			}
		});
    }
    
    private boolean redeDisponivel() {
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		return info != null;
    }
}
