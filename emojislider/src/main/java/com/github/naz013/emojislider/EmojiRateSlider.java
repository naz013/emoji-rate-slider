package com.github.naz013.emojislider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Copyright 2018 Nazar Suhovich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class EmojiRateSlider extends View {

    private static final float FIRST_LAYER = 0.82f;
    private static final float SECOND_LAYER = 0.72f;

    private Rect[] mRects = new Rect[]{};
    private Paint mShadowPaint;
    private Paint mColorPaint;
    private int mMax = 9;
    private int mSelectedItem = 4;

    private Drawable mSmile;
    private Drawable mSad;
    private Drawable mWeird;
    @Nullable
    private OnMoodChangeListener onMoodChangeListener;

    public EmojiRateSlider(Context context) {
        this(context, null);
    }

    public EmojiRateSlider(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiRateSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return processTouch(event);
            }
        });

        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(Color.WHITE);
        mShadowPaint.setShadowLayer(dp2px(3), 0, 0, Color.parseColor("#40000000"));
        mShadowPaint.setStyle(Paint.Style.FILL);

        setLayerType(LAYER_TYPE_SOFTWARE, mShadowPaint);

        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setColor(Color.BLACK);
        mColorPaint.setStyle(Paint.Style.STROKE);

        mSmile = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_smile, null);
        mSad = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_sad, null);
        mWeird = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_weird, null);
    }

    public void setOnMoodChangeListener(@Nullable OnMoodChangeListener onMoodChangeListener) {
        this.onMoodChangeListener = onMoodChangeListener;
    }

    @Nullable
    public OnMoodChangeListener getOnMoodChangeListener() {
        return onMoodChangeListener;
    }

    private boolean processTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
        else if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
            updateView(event.getX(), event.getY());
            return true;
        }
        return false;
    }

    private void updateView(float x, float y) {
        int selected = 0;
        float minDist = calcDist(x, y, mRects[0]);
        for (int i = 0; i < mRects.length; i++) {
            Rect rect = mRects[i];
            if (rect != null && rect.contains((int) x, (int) y)) {
                float dist = calcDist(x, y, rect);
                if (dist < minDist) selected = i;
            }
        }
        if (selected != mSelectedItem) {
            mSelectedItem = selected;
            notifyChanged();
            invalidate();
        }
    }

    private float calcDist(float x, float y, Rect rect) {
        return (float) Math.sqrt(Math.pow(rect.centerX() - x, 2) + Math.pow(rect.centerY() - y, 2));
    }

    private void notifyChanged() {
        if (getOnMoodChangeListener() != null) {
            getOnMoodChangeListener().onMoodChanged(mSelectedItem);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWhiteLayer(canvas);
        drawNegative(canvas);
        drawPositive(canvas);
        drawCurrent(canvas);
    }

    private void drawCurrent(Canvas canvas) {
        Rect rect = mRects[mSelectedItem];

        int h = (int) (rect.height() * 0.95f);
        int radius = h / 2;
        int m = (rect.height() - h) / 2;

        if (isPositive()) mShadowPaint.setColor(Color.GREEN);
        else if (isNegative()) mShadowPaint.setColor(Color.RED);
        else mShadowPaint.setColor(Color.YELLOW);

        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, mShadowPaint);

//        mColorPaint.setColor(Color.BLACK);
//        mColorPaint.setStyle(Paint.Style.STROKE);
//        mColorPaint.setStrokeWidth(dp2px(1));
//
//        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, mColorPaint);

        if (isPositive()) {
            mSmile.setBounds(rect.left + m, rect.top + m, rect.right - m, rect.bottom - m);
            mSmile.draw(canvas);
        } else if (isNegative()) {
            mSad.setBounds(rect.left + m, rect.top + m, rect.right - m, rect.bottom - m);
            mSad.draw(canvas);
        } else {
            mWeird.setBounds(rect.left + m, rect.top + m, rect.right - m, rect.bottom - m);
            mWeird.draw(canvas);
        }
    }

    private boolean isNegative() {
        return mSelectedItem < mMax / 2;
    }

    private boolean isPositive() {
        return mSelectedItem > mMax / 2;
    }

    private void drawPositive(Canvas canvas) {
        if (mSelectedItem < mMax - 1) {
            Rect start = mRects[mSelectedItem];
            Rect end = mRects[mMax - 1];

            mColorPaint.setColor(Color.RED);
            mColorPaint.setStyle(Paint.Style.FILL);

            int h = (int) (start.height() * SECOND_LAYER);
            int m = (start.height() - h) / 2;

            canvas.drawRect(start.centerX(), start.top + m, end.centerX(),
                    end.bottom - m, mColorPaint);

            mShadowPaint.setColor(Color.RED);

            for (int i = mMax - 1; i > mSelectedItem; i--) {
                Rect rect = mRects[i];
                canvas.drawCircle(rect.centerX(), rect.centerY(), (int) (rect.height() * SECOND_LAYER / 2), mShadowPaint);
            }
        }
    }

    private void drawNegative(Canvas canvas) {
        if (mSelectedItem > 0) {
            Rect start = mRects[0];
            Rect end = mRects[mSelectedItem];

            mColorPaint.setColor(Color.GREEN);
            mColorPaint.setStyle(Paint.Style.FILL);

            int h = (int) (start.height() * SECOND_LAYER);
            int m = (start.height() - h) / 2;

            canvas.drawRect(start.centerX(), start.top + m, end.centerX(),
                    end.bottom - m, mColorPaint);

            mShadowPaint.setColor(Color.GREEN);

            for (int i = 0; i < mSelectedItem; i++) {
                Rect rect = mRects[i];
                canvas.drawCircle(rect.centerX(), rect.centerY(), (int) (rect.height() * SECOND_LAYER / 2), mShadowPaint);
            }
        }
    }

    private void drawWhiteLayer(Canvas canvas) {
        Rect start = mRects[0];
        Rect end = mRects[mRects.length - 1];

        mShadowPaint.setColor(Color.WHITE);

        int h = (int) (start.height() * FIRST_LAYER);
        int m = (start.height() - h) / 2;

        canvas.drawRect(start.centerX() + m, start.top + m, end.centerX() - m, end.bottom - m, mShadowPaint);
        canvas.drawCircle(start.centerX(), start.centerY(), (int) (start.height() * FIRST_LAYER / 2), mShadowPaint);
        canvas.drawCircle(end.centerX(), end.centerY(), (int) (end.height() * FIRST_LAYER / 2), mShadowPaint);

        mColorPaint.setColor(Color.WHITE);
        mColorPaint.setStyle(Paint.Style.FILL);

        canvas.drawRect(start.centerX(), start.top + m, end.centerX(), end.bottom - m, mColorPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int width = (int) (height * (1 + (mMax - 1) * 0.5f));
        width = (int) (width * 1.05f);
        int newWidthSpec = View.MeasureSpec.makeMeasureSpec(width, heightMode);
        super.onMeasure(newWidthSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
        calculateRectangles(height);
    }

    private void calculateRectangles(int rectSize) {
        int height = getMeasuredHeight();
        mRects = new Rect[mMax];
        int shift = 0;
        for (int i = 0; i < mMax; i++) {
            mRects[i] = new Rect(rectSize * i - shift, 0, rectSize * (i + 1) - shift, height);
            shift += rectSize / 2;
        }
    }

    @Px
    private int dp2px(int dp) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = null;
        if (wm != null) {
            display = wm.getDefaultDisplay();
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        if (display != null) {
            display.getMetrics(displaymetrics);
        }
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public interface OnMoodChangeListener {
        void onMoodChanged(int mood);
    }
}
