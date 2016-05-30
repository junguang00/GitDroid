package feicuiedu.com.gitdroid.github.repos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class RepoListFragment extends Fragment{

    private static final String KEY_LANGUAGE = "key_language";

    public static RepoListFragment getInstance(Language language) {
        RepoListFragment fragment = new RepoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LANGUAGE, language);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.ptrClassicFrameLayout) PtrClassicFrameLayout ptrFrameLayout;
    @Bind(R.id.lvRepos) ListView listView;
    private ReposAdapter adapter;
    private ProgressBar footer;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ReposAdapter();
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        listView.setAdapter(adapter);

        initPullToRefresh();
        initInfiniteScroll();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    // 初始化下拉刷新
    private void initPullToRefresh(){
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);
        ptrFrameLayout.setDurationToCloseHeader(1500);

        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, 60, 0, 60);
        header.initWithString("I like " + getLanguage().getName());

        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);

        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override public void run() {
                        ptrFrameLayout.refreshComplete();
                        adapter.clear();
                        addFakeData("Refresh");
                    }
                }, 2000);
            }
        });
    }

    private void initInfiniteScroll(){
        footer = new ProgressBar(getContext());

        Mugen.with(listView, new MugenCallbacks() {
            @Override public void onLoadMore() {
                listView.addFooterView(footer);
                listView.postDelayed(new Runnable() {
                    @Override public void run() {
                        listView.removeFooterView(footer);
                        addFakeData("Load More");

                    }
                }, 2000);
            }

            @Override public boolean isLoading() {
                return listView.getFooterViewsCount() > 0;
            }

            @Override public boolean hasLoadedAllItems() {
                return false;
            }
        }).start();
    }

    private Language getLanguage(){
        return (Language)getArguments().getSerializable(KEY_LANGUAGE);
    }

    private void addFakeData(String data){
        for (int i = 0; i< 20; i++){
            adapter.add(data + " : " + i);
        }
    }
}
