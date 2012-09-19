package net.rafaeltoledo.vidabeta;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SobreActivity extends Activity {

	private Button botaoVoltar;
	private TextView descricao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sobre);
		
		botaoVoltar = (Button) findViewById(R.id.botao_voltar);
		botaoVoltar.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
		descricao = (TextView) findViewById(R.id.texto_sobre);
		descricao.setText(Html.fromHtml(getString(R.string.descricao_vidabeta) + "<br /><br/><font color=\"#009900\">www.vidabeta.com.br</font>"));
	}
}
