package feicuiedu.com.gitdroid.github.repos.repolist.view;


import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * View Abstraction of function "load more", load next page's data when scroll to the end,
 * this mode is always called Endless Scroll or Infinite Scroll.
 *
 * <p/>
 *
 * “加载更多”的视图抽象，向上滑动分页加载更多数据，这种模式也常被称为
 * Endless Scroll 或 Infinite Scroll。
 *
 * @param <M> 分页加载的数据实体类型，一般是一个List
 */
public interface LoadMoreView<M> extends MvpView{

    // 加载更多 -- 加载中
    void showLoadMoreLoading();

    // 加载更多 -- 加载发生错误
    void showLoadMoreError(String msg);

    // 加载更多 -- 没有更多数据
    void showLoadMoreEnd();

    // 隐藏加载更多的视图
    void hideLoadMore();

    // 添加更多数据
    void addMoreData(M data);
}
