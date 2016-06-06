package feicuiedu.com.gitdroid.gank.dailygank;


import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import feicuiedu.com.gitdroid.gank.model.GankItem;

public interface GankView extends MvpView{

    void setData(List<GankItem> gankItems);

    void showEmpty();

    void showMessage(String message);

    void hideEmpty();
}
