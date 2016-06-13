package feicuiedu.com.gitdroid.github.repos.repolist.view;


import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * View Abstraction of function "pull-to-refresh".
 *
 * <p/>
 *
 * “下拉刷新” (Pull-to-Refresh)的视图抽象。
 * @param <M> 下拉刷新的数据实体类型，通常是一个List。
 */
public interface PtrView<M> extends MvpView{

    // 停止刷新
    void stopRefresh();

    // 刷新数据
    void refreshData(M data);

    // 页面上是否有数据
    boolean isEmpty();

    // 显示信息
    void showMessage(String message);

    // 显示内容视图
    void showContentView();

    // 显示空白页面
    void showEmptyView();

    // 显示错误页面
    void showErrorView(String info);
}
