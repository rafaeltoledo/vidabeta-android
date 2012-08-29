package net.rafaeltoledo.vidabeta.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class PrincipalActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_principal, menu);
        return true;
    }
}
