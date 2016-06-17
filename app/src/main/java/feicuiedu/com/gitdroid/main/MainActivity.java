package feicuiedu.com.gitdroid.main;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.commons.LogUtils;
import feicuiedu.com.gitdroid.favorite.FavoriteFragment;
import feicuiedu.com.gitdroid.gank.gankpager.GankPagerFragment;
import feicuiedu.com.gitdroid.github.network.AvatarLoadOptions;
import feicuiedu.com.gitdroid.github.model.CurrentUser;
import feicuiedu.com.gitdroid.github.login.LoginActivity;
import feicuiedu.com.gitdroid.github.repos.HotRepoFragment;

/**
 * This activity is a typical "drawer menu screen". We use {@link DrawerLayout} and {@link NavigationView}
 * to implement sliding menu. An alternative way is to use 3rd libs <a href="https://github.com/jfeinstein10/SlidingMenu">SlidingMenu</a>.
 *
 * Real content is different fragments, when click the menu items, we switch from one fragment to another.
 *
 * <p/>
 *
 * 这个activity是一个典型的“侧滑菜单页面”。我们使用google官方的DrawerLayout和NavigationView来实现侧滑菜单。
 * 另一种实现方式是使用三方库SlidingMenu。
 *
 * 页面上具体的内容是不同的Fragment，当点击侧滑菜单项时，会从一个Fragment切换到另一个Fragment。
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // 抽屉布局
    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    // 侧滑菜单视图
    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private ImageView ivIcon;
    private Button btnLogin;
    private ActivityUtils activityUtils;

    // 热门仓库页面
    private HotRepoFragment hotRepoFragment;
    // 每日干货页面
    private GankPagerFragment gankPagerFragment;
    // 我的收藏页面
    private FavoriteFragment favoriteFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.trace(this);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_main);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        // 获取侧滑菜单Header中的控件，这些控件无法直接通过绑定获取
        ivIcon = ButterKnife.findById(navigationView.getHeaderView(0), R.id.ivIcon);
        btnLogin = ButterKnife.findById(navigationView.getHeaderView(0), R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activityUtils.startActivity(LoginActivity.class);
            }
        });

        setSupportActionBar(toolbar);

        // 设置Toolbar左上角切换侧滑菜单的按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 设置菜单的选择监听器
        navigationView.setNavigationItemSelectedListener(this);

        // 初始化时默认显示热门仓库Fragment
        hotRepoFragment = new HotRepoFragment();
        replaceFragment(new HotRepoFragment());

    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onStart() {
        super.onStart();
        LogUtils.trace(this);
        if (CurrentUser.isEmpty()) {
            // 如果当前没有用户登录
            btnLogin.setText(R.string.login_github);
            return;
        }

        // 当前有用户登录
        btnLogin.setText(R.string.switch_account);
        // 将Toolbar标题设置为GitHub用户名
        getSupportActionBar().setTitle(CurrentUser.getUser().getName());
        // 设置用户头像
        ImageLoader.getInstance().displayImage(CurrentUser.getUser().getAvatar(), ivIcon, AvatarLoadOptions.build(this));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * @param item The selected item (被选中的菜单项)
     * @return true to display the item as the selected item (返回true代表将此项显示为选中状态)
     */
    @Override public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.github_hot_repo){
            if (! hotRepoFragment.isAdded()) {
                replaceFragment(hotRepoFragment);
            }

        } else if (item.getItemId() == R.id.tips_daily){
            if (gankPagerFragment == null) gankPagerFragment = new GankPagerFragment();
            if (! gankPagerFragment.isAdded()) {
                replaceFragment(gankPagerFragment);
            }
        } else if (item.getItemId() == R.id.arsenal_my_repo) {
            if (favoriteFragment == null) favoriteFragment = new FavoriteFragment();
            if (! favoriteFragment.isAdded()) {
                replaceFragment(favoriteFragment);
            }
        }

        // 将其它菜单项重置为非选中状态
        for (int i = 0; i < navigationView.getMenu().size(); i++){
            SubMenu subMenu = navigationView.getMenu().getItem(i).getSubMenu();
            for (int j = 0; j < subMenu.size(); j++) {
                subMenu.getItem(j).setChecked(false);
            }
        }
        drawerLayout.post(new Runnable() {
            @Override public void run() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // 返回true代表将该菜单项变为checked状态
        return true;
    }

    // 替换不同的内容Fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
