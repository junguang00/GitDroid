package feicuiedu.com.gitdroid.gank.dailygank;


import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import feicuiedu.com.gitdroid.gank.model.GankItem;

interface GankView extends MvpView{

    void setData(List<GankItem> gankItems);

    // 显示空白页面
    void showEmpty();

    void showMessage(String message);

    // 隐藏空白页面
    void hideEmpty();
}
