package com.banasiak.android.webviewapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;


public class WebviewActivity extends ActionBarActivity {

    private static final String TAG = WebviewActivity.class.getSimpleName();
    
    private static final String ERROR_URL = "file:///android_asset/no_chat.html";

    private static final int SETTINGS_REQUEST_CODE = 100;
    
    private WebView webview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        webview = (WebView) findViewById(R.id.webview);
        updateWebviewSettings();

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

            @Override
            public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
                super.onReceivedSslError(view, handler, error);

                Log.e(TAG, "SSL Error (" + error.getPrimaryError() + ") " + error.getUrl());
                new AlertDialog.Builder(WebviewActivity.this)
                        .setMessage(getResources().getString(R.string.error_ssl, error.getUrl()))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                handler.proceed();
                                Log.w(TAG, "Loading " + error.getUrl() + " after prompting user.");
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.cancel();
                                Log.w(TAG, "Skipping: " + error.getUrl() + " after prompting user.");
                            }
                        })
                        .create().show();
            }

            @Override
            public void onPageStarted(final WebView view, String url, final Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (favicon != null) {
                    getSupportActionBar().setIcon(new BitmapDrawable(getResources(), favicon));
                }
                Log.i(TAG, "Loading Page: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "Complete: " + url);
            }

            @Override
            public void onLoadResource(final WebView view, final String url) {
                super.onLoadResource(view, url);
                Log.i(TAG, "Loading Resource: " + url);
            }
        });
        
        String url = SettingsActivity.getUrlPref(this);
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
                webview.reload();
                return true;
            case R.id.action_settings:
                startActivityForResult(new Intent(getBaseContext(), SettingsActivity.class), SETTINGS_REQUEST_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SETTINGS_REQUEST_CODE:
                if (webview != null) {
                    updateWebviewSettings();
                    webview.reload();
                }
                break;
        }
    }

    private void updateWebviewSettings() {
        Context context = getBaseContext();
        WebSettings settings = webview.getSettings();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_ALLOW_UNIVERSAL_ACCESS_FROM_FILE_URLS, true));
            settings.setAllowFileAccessFromFileURLs(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_ALLOW_FILE_ACCESS_FROM_FILE_URLS, true));
        }

        settings.setAllowContentAccess(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_ALLOW_CONTENT_ACCESS, true));
        settings.setAllowFileAccess(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_ALLOW_FILE_ACCESS, true));
        settings.setBlockNetworkImage(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_BLOCK_NETWORK_IMAGE, false));
        settings.setBlockNetworkLoads(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_BLOCK_NETWORK_LOADS, false));
        settings.setBuiltInZoomControls(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_BUILTIN_ZOOM_CONTROLS, true));
//        settings.setDatabaseEnabled();  has to happen before any loads are called
        settings.setDisplayZoomControls(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_DISPLAY_ZOOM_CONTROLS, true));
        settings.setDomStorageEnabled(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_DOM_STORAGE, true));
        settings.setGeolocationEnabled(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_GEOLOCATION_ENABLED, true));
        settings.setJavaScriptCanOpenWindowsAutomatically(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_JAVASCRIPT_CAN_OPEN_WINDOWS_AUTOMATICALLY, true));
        settings.setJavaScriptEnabled(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_JAVASCRIPT_ENABLED, true));
        settings.setLoadsImagesAutomatically(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_LOADS_IMAGES_AUTOMATICALLY, true));
        settings.setSaveFormData(SettingsActivity.getBoolPref(context, SettingsActivity.KEY_SAVE_FORM_DATA, true));
    }

    private void popupUrlDialog() {
        final Context context = this;
        final EditText input = new EditText(context);
        input.setHint("http://");
        input.setText(SettingsActivity.getUrlPref(context));
        
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Enter URL");
        alert.setIcon(R.drawable.ic_action_url);
        alert.setView(input);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString();
                SettingsActivity.setUrlPref(context, url);
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
