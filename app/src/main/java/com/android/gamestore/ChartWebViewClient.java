package com.android.gamestore;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hp pc on 11-07-2015.
 */
public class ChartWebViewClient extends WebViewClient {

    ProgressDialog mDialog;

    protected ChartWebViewClient(Context context) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.loading_chart));
        mDialog.show();

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }
}
