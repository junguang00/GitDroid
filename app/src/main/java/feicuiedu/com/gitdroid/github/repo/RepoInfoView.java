package feicuiedu.com.gitdroid.github.repo;


import com.hannesdorfmann.mosby.mvp.MvpView;

interface RepoInfoView extends MvpView{

    void showProgress();

    void hideProgress();

    void setData(String data);

    void showMessage(String msg);
}
