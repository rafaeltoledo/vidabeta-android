package net.rafaeltoledo.vidabeta.activity;

import net.rafaeltoledo.vidabeta.activity.util.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		
		teste();
	}
	
	private void teste() {
		XMLParser parser = new XMLParser();
		//String feed = parser.getXmlFromUrl("http://www.google.ca/reader/atom/feed/http://vidabeta.com.br/feed?hl=en&n=200");
		String feed = parser.getXmlFromUrl("http://vidabeta.com.br/feed");
		System.out.println(feed.length());
		Document doc = parser.getDomElement(feed);
		NodeList l = doc.getElementsByTagName("item");
		System.out.println("Tamanho: " + l.getLength());
		for (int i = 0; i < l.getLength(); i++) {
			Node node = l.item(i);
			NodeList children = node.getChildNodes();
			System.out.println("Tamanho (c): " + children.getLength());
			for (int j = 0; j < children.getLength(); j++) {
				Node child = children.item(j);
				System.out.println(child.getNodeName());
			}
		}
		
		/* O que me interessa:
		 * <title>
		 * <pubDate>
		 * <category></category> <-- tem que ser Vida Beta Cast
		 * <description>
		 * <itunes:duration>
		 * <enclosure> <-- link do Mp3
		 * imagem? banner_post. dentro de <content:encoded>
		 * */
	}
}
