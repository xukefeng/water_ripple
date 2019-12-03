package com.app.waterripple.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import com.app.waterripple.R;
import com.app.waterripple.utils.UIUtils;

import java.util.ArrayList;

/**
 * @author BlueCat
 * @description:
 * @date : 2019/12/3 11:43
 */
public class RippleAnimatorView extends RelativeLayout {
    public Paint mPaint;
    private int rippleColor;//水波纹颜色
    private int radius;//半径
    int strokeWidth;//线宽

    private ArrayList<RippleView> viewList = new ArrayList<>();//水波纹集合
    private AnimatorSet animatorSet;
    private boolean animationRunning = false;//当前动画状态
    private ArrayList<Animator> animatorList;//存放所有波纹动画

    public RippleAnimatorView(Context context) {
        this(context, null);
    }

    public RippleAnimatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        animatorList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleAnimatorView);
        this.rippleColor = typedArray.getColor(R.styleable.RippleAnimatorView_ripple_anim_color, Color.YELLOW);
        int rippleType = typedArray.getInt(R.styleable.RippleAnimatorView_ripple_anim_type, 0);
        this.radius = typedArray.getInteger(R.styleable.RippleAnimatorView_radius, 50);
        this.strokeWidth = typedArray.getInteger(R.styleable.RippleAnimatorView_strokeWidth, 1);
        mPaint.setStyle(rippleType == 0 ? Paint.Style.FILL : Paint.Style.STROKE);//设置水波纹类型
        mPaint.setColor(rippleColor);
        mPaint.setStrokeWidth(strokeWidth);
        typedArray.recycle();

        //添加水波纹控件(适配)
        LayoutParams rippleParams = new LayoutParams(UIUtils.getInstance().getWidth(radius + strokeWidth / 2), UIUtils.getInstance().getWidth(radius + strokeWidth / 2));
        //子控件居中
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);
        //计算最大放大倍速
        float maxValue = UIUtils.displayMetricsWidth * 1.6f / UIUtils.getInstance().getWidth(radius + strokeWidth / 2);
        //延时
        int rippleDuration = 4800;
        int singleDelay = rippleDuration / 6;//间隔时间
        for (int i = 0; i < 6; i++) {
            RippleView rippleView = new RippleView(this);
            addView(rippleView, rippleParams);
            viewList.add(rippleView);

            //由大变小
//            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX", maxValue, 1);
//            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY", maxValue, 1);
//            PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("alpha", 0, 1);
            //由小变大
            PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX", 1, maxValue);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY", 1, maxValue);
            PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("alpha", 1, 0);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(rippleView, holder1, holder2, holder3);
            animator.setDuration(rippleDuration);
            animator.setRepeatCount(ValueAnimator.INFINITE);//重复动画
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.setStartDelay(i * singleDelay);//延时执行
            animatorList.add(animator);
        }
        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(animatorList);//开启同步
    }

    //获取当前控件动画状态
    public boolean isAnimationRunning() {
        return animationRunning;
    }

    //开启动画
    public void startRippleAnimation() {
        //动画是否启用
        if (!isAnimationRunning()) {
             ArrayList<Animator> childAnimations= animatorSet.getChildAnimations();
             for (Animator childAnimation: childAnimations)
             {
                 ((ObjectAnimator)childAnimation).setRepeatCount(ValueAnimator.INFINITE);
             }
            for (RippleView rippleView : viewList) {
                rippleView.setVisibility(VISIBLE);
            }
            animatorSet.start();
            animationRunning = true;
        }

    }

    //停止动画
    public void stopRippAnimation() {
        //动画是否开启
        if (isAnimationRunning()) {
            //for (RippleView rippleView : viewList) {
               // rippleView.setVisibility(INVISIBLE);
           // }
            final ArrayList<Animator> childAnimations= animatorSet.getChildAnimations();
            final int []count=new int[1];
            for (Animator childAnimation:childAnimations)
            {
                ((ObjectAnimator)childAnimation).setRepeatCount(0);
                childAnimation.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        count[0]++;
                        if (count[0]==childAnimations.size())
                        {
                            animatorSet.end();
                            animationRunning = false;
                        }
                    }
                });
            }
        }
    }


}
