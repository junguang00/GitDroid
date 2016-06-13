package feicuiedu.com.gitdroid.gank.gankpager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import feicuiedu.com.gitdroid.gank.dailygank.GankFragment;

class GankPagerAdapter extends FragmentPagerAdapter{

    private final HashMap<String, Fragment> fragments;
    private final Date date;

    private final SimpleDateFormat simpleDateFormat;


    public GankPagerAdapter(FragmentManager fm) {
        super(fm);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        fragments = new HashMap<>();
        date = new Date(System.currentTimeMillis());
    }

    @Override public Fragment getItem(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -position);
        String key = simpleDateFormat.format(calendar.getTime());


        if (fragments.get(key) == null){
            fragments.put(key, GankFragment.getInstance(calendar.getTime()));
        }

        return fragments.get(key);
    }

    @Override public int getCount() {
        return Integer.MAX_VALUE;
    }
}
