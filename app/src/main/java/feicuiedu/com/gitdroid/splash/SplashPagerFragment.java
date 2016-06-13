package feicuiedu.com.gitdroid.splash;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;
import feicuiedu.com.gitdroid.splash.pager.Pager2;
import feicuiedu.com.gitdroid.splash.pager.SplashPagerAdapter;
import me.relex.circleindicator.CircleIndicator;

/**
 * This fragment holds the pagers on {@link SplashActivity}.
 * It is responsible for controlling all the animation effects on splash screen.
 *
 * <p/>
 * 这个Fragment包含SplashActivity上滑动的Pager，它负责闪屏页面上所有的动画效果。
 */
public class SplashPagerFragment extends Fragment{

    // 此布局用于显示背景色的渐变
    @Bind(R.id.content) FrameLayout frameLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;

    /**
     * Use open source lib CircleIndicator("https://github.com/ongakuer/CircleIndicator")
     * to implement ViewPager's dot indicator.
     *
     * <p/>
     * 使用开源库CircleIndicator来实现ViewPager的圆点指示器。
     */
    @Bind(R.id.indicator) CircleIndicator indicator;

    // 屏幕中央“手机”的外层布局，用于手机视图的平移，即View.scrollBy()方法。
    @Bind(R.id.layoutPhone) FrameLayout layoutPhone;
    // 屏幕中央“手机”的内层布局，用于手机视图的缩放，即View.setScaleX()和View.setScaleY()方法。
    @Bind(R.id.layoutPhoneInner)FrameLayout layoutPhoneInner;
    @Bind(R.id.ivPhone) ImageView ivPhone;

    // ViewPager上三个页面对应的背景颜色。
    @BindColor(R.color.colorGreen) int colorGreen;
    @BindColor(R.color.colorRed) int colorRed;
    @BindColor(R.color.colorYellow) int colorYellow;

    private SplashPagerAdapter adapter;
    // 首次显示时，手机视图的进入动画。
    private ValueAnimator animator;

    // 此监听器主要负责背景颜色的渐变，和最后一个页面视图动画的显示。
    private final ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        final ArgbEvaluator evaluator = new ArgbEvaluator();

        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // 设置背景色的渐变
            if (position == 0){ // 第一个页面到第二个页面之间
                // 颜色取绿色到红色的中间值，偏移量positionOffset是[0, 1]
                int color = (Integer)evaluator.evaluate(positionOffset, colorGreen, colorRed);
                frameLayout.setBackgroundColor(color);
            } else if (position == 1){ // 第二个页面到第三个页面之间
                // 颜色取红色到黄色的中间值，偏移量positionOffset是[0, 1]
                int color = (Integer)evaluator.evaluate(positionOffset, colorRed, colorYellow);
                frameLayout.setBackgroundColor(color);
            }

        }

        @Override public void onPageSelected(int position) {
            if (position == 2){
                // 显示最后一个页面的视图动画。
                ((Pager2)adapter.getView(position)).showAnimationIfNotInit();
            }
        }

        @Override public void onPageScrollStateChanged(int state) {}
    };

    // 此监听器负责控制“手机”视图的动画效果，包括平移、缩放和透明度变化。
    private final ViewPager.OnPageChangeListener phoneViewHandler = new ViewPager.OnPageChangeListener() {
        int position;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            this.position = position;
            if (position == 0) { // ViewPager在第一个页面和第二个页面之间

                // 设置手机视图的缩放比例
                float scale = 0.3f + 0.7f * positionOffset;
                layoutPhoneInner.setScaleX(scale);
                layoutPhoneInner.setScaleY(scale);

                if (positionOffsetPixels > 0 && animator.isRunning()){
                    // 一旦ViewPager开始滑动，停止手机视图的进入动画。
                    animator.cancel();
                    layoutPhone.setVisibility(View.VISIBLE);
                }

                // 设置手机视图上内容的透明度
                ivPhone.setAlpha(positionOffset);

                int scroll = - (int)((positionOffset - 1) * 400);
                layoutPhone.scrollTo(scroll, scroll / 2);

            } else if (position == 1) {
                // 当ViewPager在第二个页面和第三个页面之间时，手机视图要和ViewPager一起平移。
                int scrollX = layoutPhone.getScrollX();
                layoutPhone.scrollBy(positionOffsetPixels - scrollX, 0);
            }

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) { // ViewPager为停顿状态
                if (position == 1) { // 停顿在第二个页面

                    // 将手机视图还原为原始状态
                    layoutPhoneInner.setScaleX(1);
                    layoutPhoneInner.setScaleY(1);
                    layoutPhone.scrollTo(0, 0);
                }

            }
        }
    };

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash_pager, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 视图和Fragment的绑定
        ButterKnife.bind(this, view);

        // ViewPager的初始化
        adapter = new SplashPagerAdapter(getContext());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageListener);
        viewPager.addOnPageChangeListener(phoneViewHandler);
        indicator.setViewPager(viewPager);

        // 手机视图的进入动画
        layoutPhone.setVisibility(View.INVISIBLE);
        viewPager.postDelayed(new Runnable() {
            @Override public void run() {
                animator = ValueAnimator.ofInt(800, 400).setDuration(800);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        layoutPhone.setVisibility(View.VISIBLE);
                        layoutPhone.scrollTo((Integer) animation.getAnimatedValue(), 200);
                    }
                });
                animator.start();
            }
        }, 50);

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        // 视图的解绑定
        ButterKnife.unbind(this);
    }

}
