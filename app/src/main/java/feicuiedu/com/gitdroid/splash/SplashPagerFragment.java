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

public class SplashPagerFragment extends Fragment{

    @Bind(R.id.content) FrameLayout frameLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.indicator) CircleIndicator indicator;

    @Bind(R.id.layoutPhone) FrameLayout layoutPhone;
    @Bind(R.id.layoutPhoneInner)FrameLayout layoutPhoneInner;
    @Bind(R.id.ivPhone) ImageView ivPhone;

    @BindColor(R.color.colorGreen) int colorGreen;
    @BindColor(R.color.colorRed) int colorRed;
    @BindColor(R.color.colorYellow) int colorYellow;

    private SplashPagerAdapter adapter;
    private ValueAnimator animator;

    private final ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        final ArgbEvaluator evaluator = new ArgbEvaluator();

        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == 0){
                int color = (Integer)evaluator.evaluate(positionOffset, colorGreen, colorRed);
                frameLayout.setBackgroundColor(color);
            } else if (position == 1){
                int color = (Integer)evaluator.evaluate(positionOffset, colorRed, colorYellow);
                frameLayout.setBackgroundColor(color);
            }

        }

        @Override public void onPageSelected(int position) {
            if (position == 2){
                ((Pager2)adapter.getView(position)).showAnimationIfNotInit();
            }
        }

        @Override public void onPageScrollStateChanged(int state) {}
    };

    private final ViewPager.OnPageChangeListener phoneViewHandler = new ViewPager.OnPageChangeListener() {
        int position;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            this.position = position;
            if (position == 0) {
                float scale = 0.3f + 0.7f * positionOffset;
                layoutPhoneInner.setScaleX(scale);
                layoutPhoneInner.setScaleY(scale);

                if (positionOffsetPixels > 0 && animator.isRunning()){
                    animator.cancel();
                    layoutPhone.setVisibility(View.VISIBLE);
                }

                ivPhone.setAlpha(positionOffset);

                int scroll = - (int)((positionOffset - 1) * 400);
                layoutPhone.scrollTo(scroll, scroll / 2);

            } else if (position == 1) {
                int scrollX = layoutPhone.getScrollX();
                layoutPhone.scrollBy(positionOffsetPixels - scrollX, 0);
            }

        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (position == 1) {
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
        ButterKnife.bind(this, view);

        adapter = new SplashPagerAdapter(getContext());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(pageListener);
        viewPager.addOnPageChangeListener(phoneViewHandler);
        indicator.setViewPager(viewPager);

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
        ButterKnife.unbind(this);
    }

}
