package feicuiedu.com.gitdroid.github.login;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.github.network.GitHubApi;
import feicuiedu.com.gitdroid.main.MainActivity;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView{

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.webView) WebView webView;
    @Bind(R.id.gifImageView) GifImageView gifImageView;

    private ActivityUtils activityUtils;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_login);
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initWebView();
    }

    @NonNull @Override public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() {
        navigateToMain();
    }

    private final WebViewClient webViewClient = new WebViewClient() {
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Uri uri = Uri.parse(url);
            if (GitHubApi.CALL_BACK.equals(uri.getScheme())) {
                String code = uri.getQueryParameter("code");
                presenter.login(code);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    private final WebChromeClient webChromeClient = new WebChromeClient(){
        @Override public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100){
                gifImageView.setVisibility(View.GONE);
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(webView);
            }
        }
    };


    @SuppressWarnings("deprecation")
    private void initWebView() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        webView.loadUrl(GitHubApi.AUTH_URL);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
    }

    @Override public void showProgress() {
        gifImageView.setVisibility(View.VISIBLE);
    }

    @Override public void resetWeb() {
        initWebView();
    }

    @Override public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override public void navigateToMain() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }
}
