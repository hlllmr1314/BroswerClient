package com.haley.browserclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 用于加载Web页面
 *
 * @author sheyong
 */
@SuppressLint({"SetJavaScriptEnabled", "ValidFragment"})
public class BroswerWebFragment extends BroswerBaseFragment {

    public BroswerWebFragment() {
        super();
    }

    public BroswerWebFragment(TabElement tab) {
        super(tab);
    }

    private String urlLoad;

    private WebView webView = null;
    private ProgressDialog mDialog = null;
    private View view = null;
    private EditText webUrlEditText;

    public void setUrl(String url) {
        url = "http://" + url.replaceAll("^https{0,1}://", "");
        this.urlLoad = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null)
            return null;

        if (!isLoaded) {
            view = inflater.inflate(R.layout.fragment_web_layout, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.webUrlEditText = (EditText) view.findViewById(R.id.web_url);
        this.webUrlEditText.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND
                                || (event != null
                                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            setUrl(webUrlEditText.getText().toString());
                            webView.loadUrl(urlLoad);
                            return true;
                        }
                        return false;
                    }
                }
        );

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!isLoaded) {
            mDialog = new ProgressDialog(getActivity());

            webView = (WebView) getActivity().findViewById(R.id.web_view);
            webView.getLayoutParams().height =
                    getResources().getDisplayMetrics().heightPixels;

            WebSettings setting = webView.getSettings();
            setting.setAllowContentAccess(true);
            setting.setAllowFileAccess(true);
            setting.setBlockNetworkLoads(false);
            setting.setBlockNetworkImage(false);
            setting.setJavaScriptEnabled(true);
            setting.setJavaScriptCanOpenWindowsAutomatically(true);
            setting.setLoadsImagesAutomatically(true);
            setting.setPluginState(PluginState.ON);
            setting.setRenderPriority(RenderPriority.HIGH);
            setting.setAppCacheEnabled(true);
            setting.setSupportMultipleWindows(true);

            webView.setWebViewClient(new MyWebClient());
            webView.setWebChromeClient(new MyWebChromeClient());

            this.webUrlEditText.setText(this.urlLoad);

            new WebViewTask().execute();

            isLoaded = true;
        }
    }

    private class WebViewTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Boolean doInBackground(Void... param) {
            // this is very important - THIS IS THE HACK
            return false;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            webView.loadUrl(urlLoad);
        }
    }

    class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mDialog.setMessage("正在加载...");
            mDialog.setIndeterminate(true);// 设置进度条是否为不明确
            mDialog.setCancelable(false);// 设置进度条是否可以按退回键取消
            mDialog.show();
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mDialog.dismiss();
        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            mDialog.dismiss();
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            }).show();
            return true;
        }


        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            WebView childView = new WebView(getActivity());
            final WebSettings settings = childView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setSupportMultipleWindows(true);
            childView.setWebChromeClient(this);
            WebView.WebViewTransport transport =
                    (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(childView);
            resultMsg.sendToTarget();

            return true;
        }
    }

}
