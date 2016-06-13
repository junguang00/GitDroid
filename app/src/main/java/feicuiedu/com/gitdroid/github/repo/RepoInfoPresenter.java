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

    // 获取仓库Readme的回调
    private final Callback<RepoContent> contentCallback = new Callback<RepoContent>() {
        @Override public void onResponse(Call<RepoContent> call, Response<RepoContent> response) {
            String content = response.body().getContent();
            // Base64解码
            byte[] data = Base64.decode(content, Base64.DEFAULT);
            String markdown = new String(data);
            // 根据Markdown格式的内容获取HTML格式的内容
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), markdown);
            markdownCall = GitHubClient.getInstance().markdown(body);
            markdownCall.enqueue(markdownCallback);
        }

        @Override public void onFailure(Call<RepoContent> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage("Error1: " + t.getMessage());
        }
    };

    // 获取Readme对应的HTML的回调
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


    /**
     * This is the most import method in this presenter. View will invoke this method to
     * get readme content of this repository.
     *
     * <p/>
     * 此方法是本Presenter中最重要的方法。视图会调用这个方法来获取该仓库的Readme内容。
     *
     * @param repo 要获取Readme的仓库
     */
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
