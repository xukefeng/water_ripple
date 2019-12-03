package com.app.waterripple.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author BlueCat
 * @description:
 * @date : 2019/12/3 11:39
 */
public class RippleView extends View {
    /**
     * 圆心位置
     */
    private float cx, cy;
    /**
     * 圆的半径
     */
    private float radius;

    RippleAnimatorView rippleAnimatorView;

    public RippleView(RippleAnimatorView rippleAnimatorView) {
        this(rippleAnimatorView.getContext());
        this.rippleAnimatorView = rippleAnimatorView;
        this.setVisibility(VISIBLE);
    }

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.cx = w / 2f;
        this.cy = h / 2f;
        this.radius = Math.min(cx, cy);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆
        canvas.drawCircle(cx, cy, radius - rippleAnimatorView.strokeWidth / 2f, rippleAnimatorView.mPaint);
    }
}
