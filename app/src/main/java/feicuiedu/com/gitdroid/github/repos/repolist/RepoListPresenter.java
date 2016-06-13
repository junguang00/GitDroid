package feicuiedu.com.gitdroid.github.repos.repolist;


import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import feicuiedu.com.gitdroid.github.network.GitHubClient;
import feicuiedu.com.gitdroid.github.model.Language;
import feicuiedu.com.gitdroid.github.model.RepoResult;
import feicuiedu.com.gitdroid.github.repos.repolist.view.PtrPageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This presenter handles the "business logic" of querying repository list,
 * refresh the result set and loading next page's data.
 *
 * <p/>
 *
 * 此Presenter处理如下“业务逻辑”：查询仓库列表，刷新结果集和加载下一页的数据。
 */
public class RepoListPresenter extends MvpNullObjectBasePresenter<PtrPageView>{


    private Call<RepoResult> reposCall;

    // 此变量记录下一页数据的索引
    private int nextPage;

    // 下拉刷新的回调
    private final Callback<RepoResult> refreshCallback = new Callback<RepoResult>() {
        @Override public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            getView().stopRefresh();

            RepoResult repoResult = response.body();
            if (repoResult == null){
                showError("result is null!");
                return;
            }

            if (repoResult.getTotalCount() == 0) {
                getView().refreshData(null);
                getView().showEmptyView();
            } else {
                getView().refreshData(repoResult.getRepoList());
                getView().showContentView();
                nextPage = 2;
            }
        }

        @Override public void onFailure(Call<RepoResult> call, Throwable t) {
            getView().stopRefresh();

            showError(t.getMessage());
        }
    };

    // 上拉加载的回调
    private final Callback<RepoResult> loadMoreCallback = new Callback<RepoResult>() {
        @Override public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            getView().hideLoadMore();

            RepoResult repoResult = response.body();
            if (repoResult == null) {
                getView().showLoadMoreError("result is null!");
                return;
            }

            if (repoResult.getTotalCount() > 0){
                getView().addMoreData(response.body().getRepoList());
                nextPage ++;
            } else {
                getView().showLoadMoreEnd();
            }
        }

        @Override public void onFailure(Call<RepoResult> call, Throwable t) {
            getView().hideLoadMore();
            getView().showLoadMoreError(t.getMessage());
        }
    };

    private final Language language;

    public RepoListPresenter(Language language){
        this.language = language;
    }

    public void refresh(){
        getView().hideLoadMore();
        getView().showContentView();
        reposCall = buildCall(language, 1);
        reposCall.enqueue(refreshCallback);
    }

    public void loadMore(){
        getView().showLoadMoreLoading();
        reposCall = buildCall(language, nextPage);
        reposCall.enqueue(loadMoreCallback);
    }

    private Call<RepoResult> buildCall(Language language, int page){
        return GitHubClient.getInstance().searchRepos("language:" + language.getPath(), page);
    }

    @Override public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance){
            reposCall.cancel();
        }
    }

    private void showError(String msg){
        if (getView().isEmpty()){
            getView().showErrorView(msg);
        } else {
            getView().showMessage("Refresh Fail: " + msg);
        }
    }
}
