package feicuiedu.com.gitdroid.github.login;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import feicuiedu.com.gitdroid.github.model.AccessTokenRsp;
import feicuiedu.com.gitdroid.github.model.CurrentUser;
import feicuiedu.com.gitdroid.github.network.GitHubApi;
import feicuiedu.com.gitdroid.github.network.GitHubClient;
import feicuiedu.com.gitdroid.github.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class handle login use case, and invoke {@link LoginView}'s corresponding methods while
 * executing the use case.
 * <p/>
 * Login Process follow the standard OAuth2.0 protocol. User login to GitHub through {@link android.webkit.WebView},
 * if login is successful, and user give us authentification, GitHub will call our callback and give us a code.
 * Then we use this code to acquire an access token. Eventually we will have the right to access user's information.
 * <p/>
 * AccessToken is saved in {@link CurrentUser}, and used in {@link feicuiedu.com.gitdroid.github.network.TokenInterceptor}.
 *
 * <p/>
 * 此类负责处理登录用例，并在登录用例执行过程中调用LoginView的相应方法。
 * <p/>
 * 登录过程遵循标准的OAuth2.0协议。用户通过WebView登录GitHub网站，如果登录成功，且用户给我们授权，
 * GitHub会访问我们的回调地址，给我们一个授权码。然后，我们通过授权码获得访问令牌。最终，
 * 我们就有权利访问用户信息了。
 * <p/>
 * 访问令牌存储在CurrentUser类中，在TokenInterceptor中使用。
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    // 获取AccessToken的回调
    private final Callback<AccessTokenRsp> tokenCallback = new Callback<AccessTokenRsp>() {
        @Override public void onResponse(Call<AccessTokenRsp> call, Response<AccessTokenRsp> response) {
            // 保存获取到的AccessToken
            CurrentUser.setAccessToken(response.body().getAccessToken());

            // 获取用户相关信息
            userCall = GitHubClient.getInstance().getUserInfo();
            userCall.enqueue(userCallback);
        }

        @Override public void onFailure(Call<AccessTokenRsp> call, Throwable t) {
            getView().showMessage("Fail: " + t.getMessage());

            // 失败则重置WebView
            getView().showProgress();
            getView().resetWeb();
        }
    };

    // 获取用户信息的回调
    @SuppressWarnings("all")
    private Callback<User> userCallback = new Callback<User>() {
        @Override public void onResponse(Call<User> call, Response<User> response) {
            // 缓存当前用户
            CurrentUser.setUser(response.body());
            getView().showMessage("Login Success!");
            // 导航至主页面
            getView().navigateToMain();
        }

        @Override public void onFailure(Call<User> call, Throwable t) {
            // 清除缓存的用户信息，并重置WebView
            CurrentUser.clear();
            getView().showMessage("Fail: " + t.getMessage());
            getView().showProgress();
            getView().resetWeb();
        }
    };

    private Call<AccessTokenRsp> tokenCall;
    private Call<User> userCall;

    /**
     * This is the most import method in this presenter. View will invoke this method to
     * trigger login use case.
     *
     * <p/>
     * 此方法是本Presenter中最重要的方法。视图会调用这个方法来触发登录用例。
     *
     * @param code 用户登录GitHub后，GitHub给我们的访问令牌。
     */
    public void login(String code){
        // 显示加载动画
        getView().showProgress();
        getAccessToken(code);
    }

    // 通过授权码(code)获取访问令牌(AccessToken)
    private void getAccessToken(String code){
        cancelAll();
        tokenCall = GitHubClient.getInstance().getOAuthToken(GitHubApi.CLIENT_ID, GitHubApi.CLIENT_SECRET, code);
        tokenCall.enqueue(tokenCallback);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (!retainInstance){
            cancelAll();
        }
    }

    /**
     * Cancel all {@link Call}s in this presenter.
     */
    private void cancelAll(){
        if (tokenCall != null) tokenCall.cancel();
        if (userCall != null) userCall.cancel();
    }
}
