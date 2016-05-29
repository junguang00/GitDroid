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

public class Pager2 extends FrameLayout{

    @Bind(R.id.ivBubble1) ImageView ivBubble1;
    @Bind(R.id.ivBubble2) ImageView ivBubble2;
    @Bind(R.id.ivBubble3) ImageView ivBubble3;

    public Pager2(Context context) {
        this(context, null);
    }

    public Pager2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Pager2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.content_pager_2, this, true);
        ButterKnife.bind(this);

        ivBubble1.setVisibility(View.INVISIBLE);
        ivBubble2.setVisibility(View.INVISIBLE);
        ivBubble3.setVisibility(View.INVISIBLE);
    }

    public void showAnimationIfNotInit(){
        if (ivBubble1.getVisibility() == View.INVISIBLE) {
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivBubble1.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInLeft).duration(300).playOn(ivBubble1);
                }
            }, 50);
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivBubble2.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInLeft).duration(300).playOn(ivBubble2);
                }
            }, 450);
            postDelayed(new Runnable() {
                @Override public void run() {
                    ivBubble3.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInLeft).duration(300).playOn(ivBubble3);
                }
            }, 850);
        }
    }

}
