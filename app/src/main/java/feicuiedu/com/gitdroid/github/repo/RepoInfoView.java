package feicuiedu.com.gitdroid.github.repo;


import com.hannesdorfmann.mosby.mvp.MvpView;

interface RepoInfoView extends MvpView{

    // 显示加载进度
    void showProgress();

    // 隐藏加载进度
    void hideProgress();

    // 设置数据(html格式的Readme文件)
    void setData(String data);

    // 显示信息
    void showMessage(String msg);
}
