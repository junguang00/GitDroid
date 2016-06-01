package feicuiedu.com.gitdroid.github.repos;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import feicuiedu.com.gitdroid.github.model.Language;
import feicuiedu.com.gitdroid.github.repos.repolist.RepoListFragment;

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

    @Override public CharSequence getPageTitle(int position) {
        return languages.get(position).getName();
    }

}
