package feicuiedu.com.gitdroid.github.repos.repolist.view;


import com.hannesdorfmann.mosby.mvp.MvpView;

public interface LoadMoreView<M> extends MvpView{

    // 加载更多 -- 加载中
    void showLoadMoreLoading();

    // 加载更多 -- 加载发生错误
    void showLoadMoreError(String msg);

    // 加载更多 -- 没有更多数据
    void showLoadMoreEnd();

    // 隐藏加载更多的视图
    void hideLoadMore();

    void addMoreData(M data);
}
