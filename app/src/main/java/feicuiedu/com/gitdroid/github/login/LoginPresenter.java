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

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView>{

    private final Callback<AccessTokenRsp> tokenCallback = new Callback<AccessTokenRsp>() {
        @Override public void onResponse(Call<AccessTokenRsp> call, Response<AccessTokenRsp> response) {
            CurrentUser.setAccessToken(response.body().getAccessToken());
            userCall = GitHubClient.getInstance().getUserInfo();
            userCall.enqueue(userCallback);
        }

        @Override public void onFailure(Call<AccessTokenRsp> call, Throwable t) {
            getView().showMessage("Fail: " + t.getMessage());
            getView().showProgress();
            getView().resetWeb();
        }
    };

    @SuppressWarnings("all")
    private Callback<User> userCallback = new Callback<User>() {
        @Override public void onResponse(Call<User> call, Response<User> response) {
            CurrentUser.setUser(response.body());
            getView().showMessage("Login Success!");
            getView().navigateToMain();
        }

        @Override public void onFailure(Call<User> call, Throwable t) {
            CurrentUser.clear();
            getView().showMessage("Fail: " + t.getMessage());
            getView().showProgress();
            getView().resetWeb();
        }
    };

    private Call<AccessTokenRsp> tokenCall;
    private Call<User> userCall;

    public void login(String code){
        getView().showProgress();
        getAccessToken(code);
    }

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

    private void cancelAll(){
        if (tokenCall != null) tokenCall.cancel();
        if (userCall != null) userCall.cancel();
    }
}
