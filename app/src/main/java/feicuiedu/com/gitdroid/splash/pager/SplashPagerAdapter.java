package feicuiedu.com.gitdroid.splash.pager;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * This class is a very simple {@link PagerAdapter}, used by {@link feicuiedu.com.gitdroid.splash.SplashPagerFragment},
 * no new stuff here.
 *
 * <p/>
 * 此类是一个简单的PagerAdapter，用于SplashPagerFragment，没有什么新知识点。
 */
public class SplashPagerAdapter extends PagerAdapter {

    private final View[] views;

    public SplashPagerAdapter(Context context) {
        views = new View[]{
                new Pager0(context),
                new Pager1(context),
                new Pager2(context)
        };
    }

    @Override public int getCount() {
        return views.length;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views[position], 0);
        return views[position];
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public View getView(int position){
        return views[position];
    }
}
