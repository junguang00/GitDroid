package feicuiedu.com.gitdroid.github.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface LoginView extends MvpView{

    void showProgress();

    void resetWeb();

    void showMessage(String msg);

    void navigateToMain();

}
