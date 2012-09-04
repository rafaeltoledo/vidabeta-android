package net.rafaeltoledo.vidabeta.activity;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContentImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

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
		try {
			URL url = new URL("http://vidabeta.com.br/category/podcast/feed");
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(url));
			System.out.println("Feed title: " + feed.getAuthor());
			
			for (Iterator<SyndEntry> i = feed.getEntries().iterator(); i.hasNext();) {
				SyndEntry entry = i.next();				
				System.out.println(entry.getTitle());
				List<SyndContentImpl> conteudos = (List<SyndContentImpl>) entry.getContents(); 
				for (SyndContentImpl content : conteudos) {
					System.out.println(content.getMode() + " " + content.getType() + " " + content.getValue());
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		/*
		 * try { FeedFetcher feedFetcher = new HttpURLFeedFetcher(); SyndFeed
		 * feed = feedFetcher.retrieveFeed(new
		 * URL("http://vidabeta.com.br/feed")); System.out.println("Tamanho: " +
		 * feed.getEntries().size());
		 * 
		 * for (int i = 0; i < feed.getEntries().size(); i++) { SyndEntry entry
		 * = (SyndEntry) feed.getEntries().get(i);
		 * 
		 * System.out.println(entry.getTitle()); } } catch (Exception ex) {
		 * System.err.println(ex.getMessage()); }
		 */

		/*
		 * try { XMLParser parser = new XMLParser(); String feed =
		 * parser.getXmlFromUrl("http://vidabeta.com.br/category/podcast/feed");
		 * System.out.println(feed); Document doc = parser.getDomElement(feed);
		 * NodeList l = doc.getElementsByTagName("item");
		 * System.out.println("Tamanho: " + l.getLength()); for (int i = 0; i <
		 * l.getLength(); i++) { Node node = l.item(i); NodeList children =
		 * node.getChildNodes(); for (int j = 0; j < children.getLength(); j++)
		 * { Node child = children.item(j); if
		 * ("title".equals(child.getNodeName())) {
		 * System.out.println(child.getNodeValue()); } } } } catch (Exception
		 * ex) { System.err.println(ex.getMessage()); }
		 */

		/*
		 * O que me interessa: <title> <pubDate> <category></category> <-- tem
		 * que ser Vida Beta Cast <description> <itunes:duration> <enclosure>
		 * <-- link do Mp3 imagem? banner_post. dentro de <content:encoded>
		 */
	}
}
