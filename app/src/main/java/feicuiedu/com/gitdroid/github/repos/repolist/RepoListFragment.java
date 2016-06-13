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

/**
 * The main content of this fragment is a ListView, we query the hottest repositories of
 * a specific programming language from GitHub API, sorted by stars. This GitHub API has
 * pagination feature, page index starts with number 1, and each page has 30 items by default.
 * You can check <a href="https://developer.github.com/v3/#pagination">GitHub API Doc</a>
 * for more details.
 *
 * <p/>
 *
 * I use 3rd part library <a href="https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh">android-Ultra-Pull-To-Refresh</a>
 * to implement "pull-to-refresh" feature, this is an excellent library written by a Chinese programmer.
 * You can try google's {@link android.support.v4.widget.SwipeRefreshLayout} instead on your own,
 * it should be quite easy.
 *
 * <p/>
 *
 * Regarding "endless scroll" feature, there are a lot of different implementations out there,
 * such as <a href="https://github.com/Maxwin-z/XListView-Android">XListView</a>. I use a micro
 * library <a href="https://github.com/vinaysshenoy/mugen">Mugen</a> to save my time, but it's not
 * very hard to implement it from the very beginning.
 *
 * <p/>
 * 本Fragment的主要内容是一个ListView，我们通过GitHub API查询某种编程语言最热门的开源库，
 * 查询结果根据star数量排序。此GitHub API有分页特性，分页索引以1开始，每页默认有30项。
 * 更多内容可以查阅GitHub API文档。
 *
 * <p/>
 * 我使用了三方库android-Ultra-Pull-To-Refresh来实现下拉刷新特性，这是一位中国程序员构建的优秀的开源库。
 * 你可以自己尝试使用google的SwipeRefreshLayout来替代，这一工作应该是很简单的。
 *
 * <p/>
 * 至于无穷滚动特性，有很多不同的开源实现，例如XListView。我使用了一个微型库Mugen的实现来节省时间，
 * 当然，即使从头自己来实现这一功能也并不困难。
 */
public class RepoListFragment extends MvpFragment<PtrPageView, RepoListPresenter>
implements PtrPageView{

    private static final String KEY_LANGUAGE = "key_language";

    /**
     * @param language This language is used in GitHub API.
     * @return A instance of RepoListFragment, it's argument(Bundle) will contain the language object.
     */
    public static RepoListFragment getInstance(Language language) {
        RepoListFragment fragment = new RepoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LANGUAGE, language);
        fragment.setArguments(args);
        return fragment;
    }

    // 用于下拉刷新
    @Bind(R.id.ptrClassicFrameLayout) PtrClassicFrameLayout ptrFrameLayout;

    // 本Fragment的主要内容
    @Bind(R.id.lvRepos) ListView listView;

    // 下拉刷新如果得到的结果是空的，显示此视图
    @Bind(R.id.emptyView) TextView emptyView;
    // 下拉刷新发生异常，显示此视图
    @Bind(R.id.errorView) TextView errorView;

    @BindString(R.string.refresh_error) String refreshError;

    // ListView的适配器
    private ReposAdapter adapter;
    // 上拉加载时，显示在ListView最后的视图
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
        // 注意此处必须调用父类方法
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        listView.setAdapter(adapter);
        // 点击单项跳转到仓库信息页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepoInfoActivity.open(getContext(), adapter.getItem(position));
            }
        });

        initPullToRefresh();
        initInfiniteScroll();

        // 如果当前页面没有数据，开始自动刷新
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
        // 使用本对象作为key，来记录上一次刷新时间，如果两次下拉间隔太近，不会触发刷新方法
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);
        // 关闭Header所耗时长
        ptrFrameLayout.setDurationToCloseHeader(1500);

        // 以下的代码只是一个好玩的Header效果，可以全部去掉，
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.setPadding(0, 60, 0, 60);
        header.initWithString("I like " + getLanguage().getName());
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);

        // 这是下拉刷新功能的核心代码
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refresh();
            }
        });
    }

    // 初始化无穷滚动功能
    private void initInfiniteScroll(){

        footerView = new FooterView(getContext());

        // 当加载更多失败时，用户点击FooterView，会再次触发加载
        footerView.setErrorClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                presenter.loadMore();
            }
        });

        Mugen.with(listView, new MugenCallbacks() {
            // ListView滚动到底部，触发加载更多，此处要执行加载更多的业务逻辑
            @Override public void onLoadMore() {
                presenter.loadMore();
            }

            // 是否正在加载，此方法用来避免重复加载
            @Override public boolean isLoading() {
                return isShowFooterView() && footerView.isLoading();
            }

            // 是否所有数据都已加载
            @Override public boolean hasLoadedAllItems() {
                return isShowFooterView() && footerView.isComplete();
            }
        }).start();
    }

    private Language getLanguage(){
        return (Language)getArguments().getSerializable(KEY_LANGUAGE);
    }

    // 关闭下拉刷新的UI效果，继承自下拉刷新视图接口
    @Override public void stopRefresh() {
        ptrFrameLayout.refreshComplete();
    }


    // 下拉刷新成功后刷新ListView上的数据，继承自下拉刷新视图接口
    @Override public void refreshData(List<Repo> data) {
        adapter.clear();

        if (data != null) {
            adapter.addAll(data);
        }

    }

    // ListView上是否有数据，继承自下拉刷新视图接口
    @Override public boolean isEmpty() {
        return adapter.isEmpty();
    }

    // 显示信息，继承自下拉刷新视图接口
    @Override public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // 显示内容视图，继承自下拉刷新视图接口
    @Override public void showContentView() {
        listView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    // 显示空白视图，继承自下拉刷新视图接口
    @Override public void showEmptyView() {
        listView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    // 显示错误视图，继承自下拉刷新视图接口
    @Override public void showErrorView(String info) {
        listView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);

        errorView.setText(String.format(refreshError, info));
    }

    // 点击错误视图或空白视图时触发自动刷新
    @OnClick({R.id.errorView, R.id.emptyView})
    public void autoRefresh(){
        ptrFrameLayout.autoRefresh();
    }


    // 显示加载中，继承自加载更多视图接口
    @Override public void showLoadMoreLoading() {
        addFooterView();
        footerView.showLoading();
    }

    // 显示加载发生错误，继承自加载更多视图接口
    @Override public void showLoadMoreError(String msg) {
        addFooterView();
        footerView.showError(msg);
    }

    // 显示没有更多数据，继承自加载更多视图接口
    @Override  public void showLoadMoreEnd() {
        addFooterView();
        footerView.showComplete();
    }

    // 隐藏加载更多视图，继承自加载更多视图接口
    @Override public void hideLoadMore() {
        listView.removeFooterView(footerView);
    }

    // 加载成功后，添加加载到的数据，继承自加载更多视图接口
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
