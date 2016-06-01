package feicuiedu.com.gitdroid.github.repo;


import android.util.Base64;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import feicuiedu.com.gitdroid.github.network.GitHubClient;
import feicuiedu.com.gitdroid.github.model.Repo;
import feicuiedu.com.gitdroid.github.model.RepoContent;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoInfoPresenter extends MvpNullObjectBasePresenter<RepoInfoView>{

    private Call<RepoContent> contentCall;

    private Call<ResponseBody> markdownCall;

    private final Callback<RepoContent> contentCallback = new Callback<RepoContent>() {
        @Override public void onResponse(Call<RepoContent> call, Response<RepoContent> response) {
            String content = response.body().getContent();
            byte[] data = Base64.decode(content, Base64.DEFAULT);
            String markdown = new String(data);
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), markdown);
            markdownCall = GitHubClient.getInstance().markdown(body);
            markdownCall.enqueue(markdownCallback);
        }

        @Override public void onFailure(Call<RepoContent> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage("Error1: " + t.getMessage());
        }
    };

    private final Callback<ResponseBody> markdownCallback = new Callback<ResponseBody>() {
        @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                getView().setData(response.body().string());
                getView().hideProgress();
            } catch (IOException e) {
                onFailure(call, e);
            }
        }

        @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage("Error2: " + t.getMessage());
        }
    };


    public void getReadme(Repo repo) {
        cancelAll();
        getView().showProgress();
        contentCall = GitHubClient.getInstance().getReadme(repo.getOwner().getLogin(), repo.getName());
        contentCall.enqueue(contentCallback);
    }

    private void cancelAll(){
        if (contentCall != null) contentCall.cancel();
        if (markdownCall != null) markdownCall.cancel();
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance){
            cancelAll();
        }
    }
}
