package feicuiedu.com.gitdroid.github.repos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;

/**
 * This fragment is added in {@link feicuiedu.com.gitdroid.main.MainActivity}, it's just a
 * viewpager with a corresponding {@link TabLayout}.
 *
 * On the viewpager, every page is an {@link feicuiedu.com.gitdroid.github.repos.repolist.RepoListFragment}.
 *
 * <p/>
 * 本Fragment是被添加到MainActivity中。它上面有一个ViewPager和一个相对应的TabLayout。
 *
 * 在ViewPager上，每一个页面都是一个RepoListFragment。
 */
public class HotRepoFragment extends Fragment{

    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_repo, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        // 注意此处是在Fragment中添加Fragment，属于嵌套Fragment
        viewPager.setAdapter(new LanguagesPagerAdapter(getChildFragmentManager(), getContext()));

        // 将ViewPager绑定到TabLayout上
        tabLayout.setupWithViewPager(viewPager);
        /** 此处可以直接使用{@link android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener} **/
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
