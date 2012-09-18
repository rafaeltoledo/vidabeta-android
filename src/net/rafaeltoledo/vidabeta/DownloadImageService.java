package net.rafaeltoledo.vidabeta;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class DownloadImageService extends IntentService {

	public static final String MESSENGER_EXTRA = "net.rafaeltoledo.MESSENGER_EXTRA";
	public static final String IMAGE_LOCATION = "net.rafaeltoledo.IMAGE_LOCATION";

	public DownloadImageService() {
		super("DownloadImageService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Messenger messenger = (Messenger) intent.getExtras().get(
				MESSENGER_EXTRA);
		Message msg = Message.obtain();
		try {
			Drawable imagem = getDrawable(intent.getStringExtra(IMAGE_LOCATION));
			msg.arg1 = Activity.RESULT_OK;
			msg.obj = imagem;
		} catch (Exception ex) {
			Log.e(getString(R.string.app_name), "Falha no download!", ex);
			msg.arg1 = Activity.RESULT_CANCELED;
			msg.obj = ex;
		}
		
		try {
			messenger.send(msg);
		} catch (Exception ex) {
			Log.w(getString(R.string.app_name), "Falha ao enviar mensagem (download)", ex);
		}
	}

	private Drawable getDrawable(String url) throws Exception {
		URL u = new URL(url);
		InputStream is = (InputStream) u.getContent();
		return Drawable.createFromStream(is, "src");
	}
}
