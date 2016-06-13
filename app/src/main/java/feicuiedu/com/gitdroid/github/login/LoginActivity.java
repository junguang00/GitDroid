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

/**
 * This class is a concrete implementation of {@link LoginView}. The main content is a {@link WebView},
 * we will use it to load GitHub's login website. Furthermore, we use 3rd party lib <a href="https://github.com/koral--/android-gif-drawable">android-gif-drawable</a>
 * to show loading animation.
 *
 * <p/>
 * 本类是LoginView的具体实现。主要内容是一个WebView，我们使用该WebView来加载GitHub的登录页面。
 * 另外，我们使用三方库android-gif-drawable来显示加载动画。
 */
public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView{

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.webView) WebView webView;

    // 显示一个Gif图片作为加载动画
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
        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initWebView();
    }

    /**
     * This method will be invoked in {@link MvpActivity#onCreate(Bundle)} method.
     *
     * <p/>
     * 此方法将在MvpActivity的onCreate()方法中调用。
     * @return LoginPresenter 不可为null。
     */
    @NonNull @Override public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        // 按下Toolbar左上角的返回按钮。
        if (item.getItemId() == android.R.id.home) {
            // 执行按下返回键的操作
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // 按下返回键，导航至主页面
    @Override public void onBackPressed() {
        navigateToMain();
    }

    private final WebViewClient webViewClient = new WebViewClient() {
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Uri uri = Uri.parse(url);

            // 检测加载的新url是否以我们规定好的callback开头
            if (GitHubApi.CALL_BACK.equals(uri.getScheme())) {
                // 获取授权码
                String code = uri.getQueryParameter("code");
                // 执行登录用例
                presenter.login(code);
                // 返回true代表我们想离开当前WebView，自己来处理这个url
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    private final WebChromeClient webChromeClient = new WebChromeClient(){
        @Override public void onProgressChanged(WebView view, int newProgress) {
            // 100代表WebView加载完成
            if (newProgress == 100){
                // 隐藏加载动画
                gifImageView.setVisibility(View.GONE);
                // 用一个视图动画来显示WebView
                YoYo.with(Techniques.ZoomIn).duration(500).playOn(webView);
            }
        }
    };


    // WebView的初始化
    @SuppressWarnings("deprecation")
    private void initWebView() {
        // 删除所有的Cookie，主要是为了清除以前的登录历史记录
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
