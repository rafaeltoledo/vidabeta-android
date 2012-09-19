package net.rafaeltoledo.vidabeta;

import java.util.ArrayList;
import java.util.List;

import net.rafaeltoledo.vidabeta.model.Podcast;
import net.rafaeltoledo.vidabeta.util.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class ListaPodcastService extends IntentService {
	
	private final String KEY = "podcast";
	private final String KEY_TITULO = "titulo";
	private final String KEY_DATA = "data";
	private final String KEY_DESCRICAO = "descricao";
	private final String KEY_IMAGEM = "imagem";
	private final String KEY_LINK = "link";
	
	public static final String MESSENGER_EXTRA = "net.rafaeltoledo.MESSENGER_EXTRA";
	public static final String FEED_LOCATION = "net.rafaeltoledo.FEED";
	
	public ListaPodcastService() {
		super("PodcastService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {		
		XMLParser parser = new XMLParser();
		
		Messenger messenger = (Messenger) intent.getExtras().get(MESSENGER_EXTRA);
		String feed = intent.getExtras().getString(FEED_LOCATION);
		Message msg = Message.obtain();
		
		try {
			String xml = parser.getXmlFromUrl(feed);
			Document doc = parser.getDomElement(xml);
			NodeList nl = doc.getElementsByTagName(KEY);			
			List<Podcast> resultado = new ArrayList<Podcast>();
			
			for (int i = 0; i < nl.getLength(); i++) {
				Element e = (Element) nl.item(i);

				Podcast cast = new Podcast();
				cast.setTitulo(parser.getValue(e, KEY_TITULO));
				cast.setFoto(parser.getValue(e, KEY_IMAGEM));
				cast.setData(parser.getValue(e, KEY_DATA));
				cast.setDescricao(parser.getValue(e, KEY_DESCRICAO));
				cast.setLink(parser.getValue(e, KEY_LINK));
				resultado.add(cast);
			}
			msg.arg1 = Activity.RESULT_OK;
			msg.obj = resultado;
		} catch (Exception ex) {
			Log.e(getString(R.string.app_name), "Falha de sincronização", ex);
			msg.arg1 = Activity.RESULT_CANCELED;
			msg.obj = ex;
		}
		
		try {
			messenger.send(msg);
		} catch (Exception ex) {
			Log.w(getString(R.string.app_name),"Falha de sincronização (messenger)", ex);
		}
	}
}
