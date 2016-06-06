package feicuiedu.com.gitdroid.gank.dailygank;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.gank.model.GankItem;

public class GankFragment extends MvpFragment<GankView, GankPresenter> implements GankView{

    private static final String KEY_DATE = "key_date";


    @Bind(R.id.tvDate) TextView tvDate;
    @Bind(R.id.content) LinearLayout content;
    @Bind(R.id.emptyView) FrameLayout emptyView;

    private Date date;

    private ActivityUtils activityUtils;

    public static GankFragment getInstance(Date date){
        GankFragment gankFragment = new GankFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATE, date);
        gankFragment.setArguments(args);
        return gankFragment;
    }


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        date = (Date)getArguments().getSerializable(KEY_DATE);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gank, container, false);
    }

    @NonNull @Override public GankPresenter createPresenter() {
        return new GankPresenter();
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        tvDate.setText(simpleDateFormat.format(date));

        presenter.init(date);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override public void setData(List<GankItem> gankItems) {
        content.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i< gankItems.size() ; i++){
            final GankItem gankItem = gankItems.get(i);
            final Button button = (Button)inflater.inflate(R.layout.layout_item_gank, content, false);
            button.setText(gankItem.getDesc());
            content.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    activityUtils.startBrowser(gankItem.getUrl());
                }
            });
        }
    }

    @Override public void showEmpty() {
        emptyView.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(500).playOn(emptyView);
    }

    @Override public void showMessage(String message) {
        activityUtils.showToast(message);
    }

    @Override public void hideEmpty() {
        emptyView.setVisibility(View.INVISIBLE);
    }
}
