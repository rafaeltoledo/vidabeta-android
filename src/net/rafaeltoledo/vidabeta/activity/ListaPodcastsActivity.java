package net.rafaeltoledo.vidabeta.activity;

import java.util.ArrayList;
import java.util.List;

import net.rafaeltoledo.vidabeta.model.Podcast;
import net.rafaeltoledo.vidabeta.util.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	}

	@Override
	protected void onStart() {
		super.onStart();
		teste();
		preencherLista();
	}

	private void preencherLista() {
		adaptador = new ArrayAdapter<Podcast>(this,
				android.R.layout.simple_list_item_1, podcasts);
		lista.setAdapter(adaptador);
	}

	private void teste() {
		String KEY = "podcast";
		String KEY_TITULO = "titulo";
		String KEY_DATA = "data";
		String KEY_DESCRICAO = "descricao";
		String KEY_DURACAO = "duracao";
		String KEY_IMAGEM = "imagem";
		String KEY_LINK = "link";
		try {
			XMLParser parser = new XMLParser();
			String xml = parser
					.getXmlFromUrl("http://www.rafaeltoledo.net/vidabeta.xml");
			Document doc = parser.getDomElement(xml);
			NodeList nl = doc.getElementsByTagName(KEY);

			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);

				Podcast cast = new Podcast();
				cast.setTitulo(parser.getValue(e, KEY_TITULO));
				cast.setFoto(parser.getValue(e, KEY_IMAGEM));
				cast.setDuracao(parser.getValue(e, KEY_DURACAO));
				cast.setData(parser.getValue(e, KEY_DATA));
				cast.setDescricao(parser.getValue(e, KEY_DESCRICAO));
				cast.setLink(parser.getValue(e, KEY_LINK));
				podcasts.add(cast);
			}

		} catch (Exception ex) {
			Log.e("VidaBeta", ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(this, PodcastActivity.class);
		i.putExtra("cast", podcasts.get((int) id));
		startActivity(i);
	}
}
