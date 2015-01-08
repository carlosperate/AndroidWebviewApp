package com.banasiak.android.webviewapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;


public class WebviewActivity extends ActionBarActivity {

    private static final String TAG = WebviewActivity.class.getSimpleName();
    
    private static final String ERROR_URL = "file:///android_asset/no_chat.html";
    
    private WebView webview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        webview = (WebView) findViewById(R.id.webview);
        
        WebSettings settings = webview.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        
        webview.setWebViewClient(new WebViewClient(){
            @Override public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                webview.loadUrl(ERROR_URL);
                Log.e(TAG, "Error loading URL: " + failingUrl + " | errorCode = " + errorCode + " | description = " + description);
                Toast.makeText(getApplicationContext(), "Error: " + description, Toast.LENGTH_SHORT).show();
            }
        });
        
        String url = Settings.getUrlPref(this);
        Log.d(TAG, "Loading url: " + url);
        webview.loadUrl(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_goto_url:
                popupUrlDialog();
                return true;
            case R.id.action_reload:
                //webview.reload();
                String url = Settings.getUrlPref(this);
                Log.d(TAG, "Loading url: " + url);
                webview.loadUrl(url);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void popupUrlDialog() {
        final Context context = this;
        final EditText input = new EditText(context);
        input.setHint("http://");
        input.setText(Settings.getUrlPref(context));
        
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Enter URL");
        alert.setIcon(R.drawable.ic_action_url);
        alert.setView(input);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString();
                Settings.setUrlPref(context, url);
                Log.d(TAG, "Loading url: " + url);
                webview.loadUrl(url);
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
    
}
