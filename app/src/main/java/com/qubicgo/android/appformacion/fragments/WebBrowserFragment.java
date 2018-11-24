package com.qubicgo.android.appformacion.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qubicgo.android.appformacion.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebBrowserFragment extends Fragment {

    @BindView(R.id.webView)
    WebView webView;

    Unbinder unbinder;
    Context mContext;

    public WebBrowserFragment() { }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web_browser, container, false);
        unbinder = ButterKnife.bind(this, view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Bundle bundle = getArguments();

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        final ProgressDialog progressBar = ProgressDialog.show(getActivity(), bundle.getString("title"), "Cargando...");
        webView.loadUrl(bundle.getString("url"));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Página no disponible, intentelo luego.");
                alertDialog.show();

                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
