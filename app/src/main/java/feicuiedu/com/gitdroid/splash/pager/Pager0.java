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

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.content_pager_0, this, true);
        ButterKnife.bind(this);
        ivDesktop.setVisibility(View.INVISIBLE);
        ivTablet.setVisibility(View.INVISIBLE);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (ivDesktop.getVisibility() == View.INVISIBLE) {
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivDesktop.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInRight).duration(650).playOn(ivDesktop);
                }
            }, 50);
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivTablet.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInRight).duration(650).playOn(ivTablet);
                    YoYo.with(Techniques.SlideInDown).duration(650).playOn(ivTablet);
                }
            }, 50);
        }
    }
}
