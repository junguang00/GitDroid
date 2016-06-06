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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.commons.ActivityUtils;
import feicuiedu.com.gitdroid.commons.LogUtils;
import feicuiedu.com.gitdroid.gank.gankpager.GankPagerFragment;
import feicuiedu.com.gitdroid.github.network.AvatarLoadOptions;
import feicuiedu.com.gitdroid.github.model.CurrentUser;
import feicuiedu.com.gitdroid.github.login.LoginActivity;
import feicuiedu.com.gitdroid.github.repos.HotRepoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private ImageView ivIcon;
    private Button btnLogin;
    private ActivityUtils activityUtils;

    private HotRepoFragment hotRepoFragment;
    private GankPagerFragment gankPagerFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.trace(this);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_main);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        ivIcon = ButterKnife.findById(navigationView.getHeaderView(0), R.id.ivIcon);
        btnLogin = ButterKnife.findById(navigationView.getHeaderView(0), R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activityUtils.startActivity(LoginActivity.class);
            }
        });

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        hotRepoFragment = new HotRepoFragment();
        replaceFragment(new HotRepoFragment());

        navigationView.getMenu().findItem(R.id.github_hot_repo).setChecked(true);
    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onStart() {
        super.onStart();
        LogUtils.trace(this);
        if (CurrentUser.isEmpty()) {
            btnLogin.setText(R.string.login_github);
            return;
        }
        btnLogin.setText(R.string.switch_account);
        getSupportActionBar().setTitle(CurrentUser.getUser().getName());
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
        }

        for (int i = 0; i < navigationView.getMenu().size(); i++){
            MenuItem menuItem = navigationView.getMenu().getItem(i);
            if (menuItem != item) menuItem.setChecked(false);
        }
        drawerLayout.post(new Runnable() {
            @Override public void run() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // 返回true代表将该菜单项变为checked状态
        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
