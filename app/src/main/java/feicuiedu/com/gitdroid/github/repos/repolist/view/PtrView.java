package feicuiedu.com.gitdroid.github.repos.repolist.view;


import com.hannesdorfmann.mosby.mvp.MvpView;

public interface PtrView<M> extends MvpView{

    // 停止刷新
    void stopRefresh();

    // 刷新数据
    void refreshData(M data);

    // 页面上是否有数据
    boolean isEmpty();

    void showMessage(String message);

    void showContentView();

    void showEmptyView();

    void showErrorView(String info);
}
