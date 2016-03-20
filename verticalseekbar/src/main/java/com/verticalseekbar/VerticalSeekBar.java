package com.verticalseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * VerticalSeekBar
 */
public class VerticalSeekBar extends SeekBar {
    private Drawable customThumb;
    private boolean mMirrorForRtl = false;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray customAttr = context.obtainStyledAttributes(attrs,
                R.styleable.VerticalSeekBar,
                defStyleAttr,
                0);

        Drawable customThumb = customAttr.getDrawable(R.styleable.VerticalSeekBar_customThumb);
        setCustomThumb(customThumb);

        int[] mirrorForRtlAttr = new int[] { android.R.attr.mirrorForRtl };
        TypedArray attributes = context.obtainStyledAttributes(attrs, mirrorForRtlAttr);
        mMirrorForRtl = attributes.getBoolean(0, false);

        customAttr.recycle();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);
        drawThumb(c); //redrawing thumb

        super.onDraw(c);
    }

    private void drawThumb(Canvas canvas) {
        Drawable customThumb = getCustomThumb();

        if (customThumb != null) {
            int available = getHeight() - getPaddingTop() - getPaddingBottom();
            final int thumbWidth = customThumb.getIntrinsicWidth();
            available -= thumbWidth;
            // The extra space for the thumb to move on the track
            available += getThumbOffset() * 2;

            int thumbPos = (int) (getScale() * available + 0.5f);

            final int top, bottom;
            if (getThumbOffset() == Integer.MIN_VALUE) {
                final Rect oldBounds = customThumb.getBounds();
                top = oldBounds.top;
                bottom = oldBounds.bottom;
            } else {
                top = 0;
                bottom = customThumb.getIntrinsicHeight();
            }
            final int left = (isLayoutRtl() && mMirrorForRtl) ? available - thumbPos : thumbPos;
            final int right = left + thumbWidth;

            Rect thumbBounds = customThumb.getBounds();
            customThumb.setBounds(left, top, right, bottom);

            canvas.save();
            canvas.rotate(90, thumbBounds.exactCenterX(), thumbBounds.exactCenterY());
            customThumb.draw(canvas);
            canvas.restore();
        }
    }

    private boolean isLayoutRtl() {
        return (getLayoutDirection() == LAYOUT_DIRECTION_RTL);
    }

    private float getScale() {
        final int max = getMax();
        return max > 0 ? getProgress() / (float) max : 0;
    }

    public Drawable getCustomThumb() {
        return customThumb;
    }

    public void setCustomThumb(Drawable customThumb) {
        this.customThumb = customThumb;
        invalidate();
    }
}
