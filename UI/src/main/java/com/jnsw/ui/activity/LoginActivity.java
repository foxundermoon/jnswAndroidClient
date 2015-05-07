package com.jnsw.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.xmpp.ServiceManager;
import com.jnsw.core.xmpp.receiver.XmppStatusReceiver;
import com.jnsw.ui.R;

public class LoginActivity extends ActionBarActivity {

    private XmppStatusReceiver xmppStatusReceiver;
    private ServiceManager serviceManager;
    Handler handler;

    @Override
    protected void onStop() {
        unregisterXmppReceiver();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void unregisterXmppReceiver() {
        unregisterReceiver(xmppStatusReceiver);
    }

    private View.OnClickListener loginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

}
