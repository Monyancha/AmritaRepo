/*
 * MIT License
 *
 * Copyright (c) 2018  RAJKUMAR S
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package in.co.rajkumaar.amritarepo.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import in.co.rajkumaar.amritarepo.R;

public class WebViewActivity extends AppCompatActivity {


    ProgressDialog dialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        try {
            Bundle b = getIntent().getExtras();
            String webviewlink = b.getString("webview");
            this.setTitle(b.getString("title"));
            WebView mywebview = (WebView) findViewById(R.id.webView);
            mywebview.getSettings().setJavaScriptEnabled(true);
            dialog = new ProgressDialog(WebViewActivity.this);
            mywebview.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorBackground));
            mywebview.setBackgroundColor(getResources().getColor(R.color.colorBackground));
            if (b.getBoolean("zoom")) {
                mywebview.getSettings().setSupportZoom(true);
                mywebview.getSettings().setBuiltInZoomControls(true);
            }
            mywebview.getSettings().setLoadWithOverviewMode(true);
            mywebview.getSettings().setUseWideViewPort(true);
            showProgressDialog();
            mywebview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    dismissProgressDialog();
                }
            });
            mywebview.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    dismissProgressDialog();
                }
            });
            mywebview.loadUrl(webviewlink);

        } catch (Exception e) {
            e.printStackTrace();
            finish();
            Toast.makeText(WebViewActivity.this, "Unexpected error. Please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    public void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showProgressDialog() {

        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

