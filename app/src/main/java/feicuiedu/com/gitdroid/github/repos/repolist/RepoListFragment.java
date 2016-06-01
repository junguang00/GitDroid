package feicuiedu.com.gitdroid.github.repos.repolist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.github.model.Language;
import feicuiedu.com.gitdroid.github.model.Repo;
import feicuiedu.com.gitdroid.github.repo.RepoInfoActivity;
import feicuiedu.com.gitdroid.github.repos.repolist.view.PtrPageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class RepoListFragment extends MvpFragment<PtrPageView, RepoListPresenter>
implements PtrPageView{

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
    @Bind(R.id.emptyView) TextView emptyView;
    @Bind(R.id.errorView) TextView errorView;

    @BindString(R.string.refresh_error) String refreshError;

    private ReposAdapter adapter;
    private FooterView footerView;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ReposAdapter(getContext());
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }

    @NonNull @Override public RepoListPresenter createPresenter() {
        return new RepoListPresenter(getLanguage());
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepoInfoActivity.open(getContext(), adapter.getItem(position));
            }
        });

        initPullToRefresh();
        initInfiniteScroll();

        if (adapter.getCount() == 0){
            ptrFrameLayout.postDelayed(new Runnable() {
                @Override public void run() {
                    ptrFrameLayout.autoRefresh();
                }
            }, 200);
        }

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
                presenter.refresh();
            }
        });
    }

    private void initInfiniteScroll(){

        footerView = new FooterView(getContext());

        footerView.setErrorClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                presenter.loadMore();
            }
        });

        Mugen.with(listView, new MugenCallbacks() {
            @Override public void onLoadMore() {
                presenter.loadMore();
            }

            @Override public boolean isLoading() {
                return isShowFooterView() && footerView.isLoading();
            }

            @Override public boolean hasLoadedAllItems() {
                return isShowFooterView() && footerView.isComplete();
            }
        }).start();
    }

    private Language getLanguage(){
        return (Language)getArguments().getSerializable(KEY_LANGUAGE);
    }

    @Override public void stopRefresh() {
        ptrFrameLayout.refreshComplete();
    }

    @Override public void refreshData(List<Repo> data) {
        adapter.clear();

        if (data != null) {
            adapter.addAll(data);
        }

    }

    @Override public boolean isEmpty() {
        return adapter.isEmpty();
    }

    @Override public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override public void showContentView() {
        listView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override public void showEmptyView() {
        listView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override public void showErrorView(String info) {
        listView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);

        errorView.setText(String.format(refreshError, info));
    }

    @OnClick({R.id.errorView, R.id.emptyView})
    public void autoRefresh(){
        ptrFrameLayout.autoRefresh();
    }


    @Override public void showLoadMoreLoading() {
        addFooterView();
        footerView.showLoading();
    }

    @Override public void showLoadMoreError(String msg) {
        addFooterView();
        footerView.showError(msg);
    }

    @Override  public void showLoadMoreEnd() {
        addFooterView();
        footerView.showComplete();
    }

    @Override public void hideLoadMore() {
        listView.removeFooterView(footerView);
    }

    @Override public void addMoreData(List<Repo> data) {
        adapter.addAll(data);
    }

    private void addFooterView(){
        if (listView.getFooterViewsCount() == 0) {
            listView.addFooterView(footerView);
        }
    }

    private boolean isShowFooterView(){
        return listView.getFooterViewsCount() > 0;
    }


}
