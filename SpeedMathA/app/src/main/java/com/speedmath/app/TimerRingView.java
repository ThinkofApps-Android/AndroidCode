package com.speedmath.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TimerRingView extends View {

    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF oval = new RectF();

    private int maxTime = 60;
    private int timeLeft = 60;
    private int ringColor = Color.parseColor("#7c3aed");

    public TimerRingView(Context context) {
        super(context);
        init();
    }

    public TimerRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerRingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(Color.parseColor("#2a2a3d"));
        bgPaint.setStrokeWidth(8f);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);

        fgPaint.setStyle(Paint.Style.STROKE);
        fgPaint.setColor(ringColor);
        fgPaint.setStrokeWidth(8f);
        fgPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
        invalidate();
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        invalidate();
    }

    public void setRingColor(int color) {
        this.ringColor = color;
        fgPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float stroke = 8f;
        float inset = stroke / 2f + 2f;
        oval.set(inset, inset, w - inset, h - inset);

        // Background ring
        canvas.drawArc(oval, 0, 360, false, bgPaint);

        // Foreground arc
        float progress = maxTime > 0 ? (float) timeLeft / maxTime : 0f;
        float sweep = -360f * progress;  // counter-clockwise
        // Draw clockwise from top
        float startAngle = -90f;
        float sweepAngle = 360f * progress;
        canvas.drawArc(oval, startAngle, sweepAngle, false, fgPaint);
    }
}
