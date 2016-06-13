package feicuiedu.com.gitdroid.gank.gankpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 一种简单实现垂直ViewPager的方法，思路如下：
 *
 * <ul>
 *    <li/> 交换MotionEvent的x和y轴数值，将用户的垂直触屏动作篡改为水平触屏动作
 *    <li/> 设置ViewPager页面切换动画为垂直切换。
 * </ul>
 *
 * 注意这一简单实现方案并不完美，滑动体验没有原始的ViewPager那么好，
 * 也无法处理ViewPager中嵌入ListView的场景。
 */
public class VerticalViewPager extends ViewPager {

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置ViewPager切换动画为垂直切换
        setPageTransformer(false, new DefaultTransformer());
        // 消除滑动到边缘时，左右两边的阴影效果
        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    // 交换触屏事件的x、y值
    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 根据交换x、y值后的MotionEvent，判断是否需要拦截触屏事件
        boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(event));
        // 再把触屏事件还原
        swapTouchEvent(event);
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 交换MotionEvent的x和y轴数值，将用户的垂直触屏动作篡改为水平触屏动作
        return super.onTouchEvent(swapTouchEvent(ev));
    }

    // ViewPager的垂直切换效果
    private static class DefaultTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            float alpha = 0;
            if (0 <= position && position <= 1) {
                alpha = 1 - position;
            } else if (-1 < position && position < 0) {
                alpha = position + 1;
            }
            view.setAlpha(alpha);
            view.setTranslationX(view.getWidth() * -position);
            float yPosition = position * view.getHeight();
            view.setTranslationY(yPosition);
        }
    }

}
