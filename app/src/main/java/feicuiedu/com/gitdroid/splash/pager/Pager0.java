package feicuiedu.com.gitdroid.splash.pager;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import feicuiedu.com.gitdroid.R;

/**
 * The first page in {@link feicuiedu.com.gitdroid.splash.SplashPagerFragment}.
 *
 * <p/>
 * SplashPagerFragment中的第一个页面。
 */
public class Pager0 extends FrameLayout{

    @Bind(R.id.ivDesktop) ImageView ivDesktop;
    @Bind(R.id.ivTablet) ImageView ivTablet;

    public Pager0(Context context) {
        this(context, null);
    }

    public Pager0(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Pager0(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 视图的初始化，主要是将视图绑定到自身。
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.content_pager_0, this, true);
        ButterKnife.bind(this);
        ivDesktop.setVisibility(View.INVISIBLE);
        ivTablet.setVisibility(View.INVISIBLE);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // 使用开源库 https://github.com/daimajia/AndroidViewAnimations 显示视图动画。
        // 通过对视图可见性的判断，让此动画只显示一次。
        if (ivDesktop.getVisibility() == View.INVISIBLE) {
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivDesktop.setVisibility(View.VISIBLE);
                    // 从右侧滑入
                    YoYo.with(Techniques.SlideInRight).duration(650).playOn(ivDesktop);
                }
            }, 50); // 延迟50毫秒是为了让动画能流畅地显示。
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivTablet.setVisibility(View.VISIBLE);
                    // 从右上角滑入
                    YoYo.with(Techniques.SlideInRight).duration(650).playOn(ivTablet);
                    YoYo.with(Techniques.SlideInDown).duration(650).playOn(ivTablet);
                }
            }, 50);
        }
    }
}
