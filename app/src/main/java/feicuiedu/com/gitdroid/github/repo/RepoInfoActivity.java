package feicuiedu.com.gitdroid.github.repo;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.github.network.AvatarLoadOptions;
import feicuiedu.com.gitdroid.github.model.Repo;

/**
 * This class show a repository's key information. It also use a {@link WebView} to load
 * the repository's readme file.
 *
 * <p/>
 * 本Activity用来展示GitHub仓库的关键信息，它还用一个WebView来显示该仓库的Readme文件。
 */
public class RepoInfoActivity extends MvpActivity<RepoInfoView, RepoInfoPresenter> implements RepoInfoView{

    private static final String KEY_REPO = "key_repo";

    @Bind(R.id.ivIcon) ImageView ivIcon;
    @Bind(R.id.tvRepoInfo) TextView tvRepoInfo;
    @Bind(R.id.tvRepoStars) TextView tvRepoStars;
    @Bind(R.id.tvRepoName) TextView tvRepoName;
    @Bind(R.id.webView) WebView webView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    /**
     * Use this static method to start this activity to remind you that you have to put an {@link Repo}
     * in the Intent's Bundle.
     *
     * <p/>
     * 使用此静态方法来启动Activity，用来提醒你需要在Intent的Bundle中存一个Repo对象。
     *
     * @param context an instance of context
     * @param repo 将在本页面上显示的GitHub仓库
     */
    public static void open(Context context, @NonNull Repo repo) {
        Intent intent = new Intent(context, RepoInfoActivity.class);
        intent.putExtra(KEY_REPO, repo);
        context.startActivity(intent);
    }

    @Bind(R.id.toolbar) Toolbar toolbar;

    private Repo repo;
    private ActivityUtils activityUtils;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_repo_info);
        // 获取此仓库的Readme文件
        presenter.getReadme(repo);
    }

    @SuppressWarnings("ConstantConditions")
    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        repo = (Repo) getIntent().getSerializableExtra(KEY_REPO);

        setSupportActionBar(toolbar);
        // 显示Toolbar的返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 将Toolbar的标题设置为仓库名称
        getSupportActionBar().setTitle(repo.getName());

        // 加载仓库拥有者的头像，设置仓库关键信息(描述，全名，star和fork数量)
        ImageLoader.getInstance().displayImage(repo.getOwner().getAvatar(), ivIcon, AvatarLoadOptions.build(this));
        tvRepoInfo.setText(repo.getDescription());
        tvRepoName.setText(repo.getFullName());
        tvRepoStars.setText(String.format("star: %d  fork: %d", repo.getStarCount(), repo.getForkCount()));

    }

    @NonNull @Override public RepoInfoPresenter createPresenter() {
        return new RepoInfoPresenter();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override public void setData(String data) {
        webView.loadData(data, "text/html", "UTF-8");
    }

    @Override public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
