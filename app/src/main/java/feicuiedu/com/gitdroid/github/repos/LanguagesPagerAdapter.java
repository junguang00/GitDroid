package feicuiedu.com.gitdroid.github.repos;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import feicuiedu.com.gitdroid.github.model.Language;
import feicuiedu.com.gitdroid.github.repos.repolist.RepoListFragment;

/**
 * This adapter is used in {@link HotRepoFragment}, each page is a {@link RepoListFragment}.
 *
 * <p/>
 * The fragment of each page the user visits will be kept in memory, though its
 * view hierarchy may be destroyed when not visible.  This can result in using
 * a significant amount of memory since fragment instances can hold on to an
 * arbitrary amount of state.
 *
 * <p/>
 * This is acceptable under our circumstance, otherwise, we should consider using
 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
 *
 * <p/>
 *
 * 此适配器用于HotRepoFragment, 每一页都是一个RepoListFragment.
 *
 * <p/>
 * 用户浏览过的所有子页面fragment都会保存在内存中，但当它们不可见时，其上的View可能被摧毁。
 * 这可能导致占用大量的内存，因为fragment实例能保存任意量的状态值。
 *
 * <p/>
 * 在我们的应用内，这是可以接收的，否则应该考虑使用FragmentStatePagerAdapter。
 */
class LanguagesPagerAdapter extends FragmentPagerAdapter {

    private final List<Language> languages;

    public LanguagesPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        languages = Language.getDefaultLanguages(context);
    }

    @Override public Fragment getItem(int position) {
        return RepoListFragment.getInstance(languages.get(position));
    }

    @Override public int getCount() {
        return languages.size();
    }

    /**
     * The return value of this method will be used in {@link android.support.design.widget.TabLayout}'s
     * Tabs.
     *
     * <p/>
     *
     * 本方法的返回值将用于TabLayout的Tab标签上。
     *
     * @param position The position of the title requested.
     * @return This title of this page.
     */
    @Override public CharSequence getPageTitle(int position) {
        return languages.get(position).getName();
    }

}
