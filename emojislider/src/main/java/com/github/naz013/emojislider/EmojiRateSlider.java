package com.github.naz013.emojislider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

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

    private static final String TAG = "EmojiRateSlider";
    private static final float FIRST_LAYER = 0.82f;
    private static final float SECOND_LAYER = 0.72f;
    private static final float THIRD_LAYER = 0.95f;
    private static final float THIRD_LAYER_TAPPED = 0.98f;

    private Rect[] mRects = new Rect[]{};
    private Paint mShadowPaint;
    private Paint mColorPaint;

    private int mMax = 5;
    private int mSelectedItem = 2;
    private float mPrevX;
    private boolean hasWeird = true;
    private boolean wasSlided = false;
    private boolean isTapped = false;

    @ColorInt
    private int mSadColor = Color.RED;
    @ColorInt
    private int mHappyColor = Color.GREEN;
    @ColorInt
    private int mWeirdColor = Color.YELLOW;
    @ColorInt
    private int mBgColor = Color.WHITE;

    private Drawable mSmileIcon;
    private Drawable mSadIcon;
    private Drawable mWeirdIcon;
    @Nullable
    private OnMoodChangeListener onMoodChangeListener;
    @Nullable
    private Animator mAnimator;

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

        mSmileIcon = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_smile, null);
        mSadIcon = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_sad, null);
        mWeirdIcon = VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_weird, null);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EmojiRateSlider, defStyleAttr, 0);
            try {
                hasWeird = a.getBoolean(R.styleable.EmojiRateSlider_ers_has_weird, hasWeird);

                int max = a.getInt(R.styleable.EmojiRateSlider_ers_max, mMax);
                if (max >= 3) mMax = max;

                int selected = a.getInt(R.styleable.EmojiRateSlider_ers_progress, mSelectedItem);
                if (selected >= 0 && selected < mMax) mSelectedItem = selected;

                mSadColor = a.getColor(R.styleable.EmojiRateSlider_ers_color_sad, mSadColor);
                mHappyColor = a.getColor(R.styleable.EmojiRateSlider_ers_color_happy, mHappyColor);
                mWeirdColor = a.getColor(R.styleable.EmojiRateSlider_ers_color_weird, mWeirdColor);
                mBgColor = a.getColor(R.styleable.EmojiRateSlider_ers_color_bg, mBgColor);

                int idHappy = a.getResourceId(R.styleable.EmojiRateSlider_ers_icon_happy, 0);
                if (idHappy != 0) {
                    mSmileIcon = VectorDrawableCompat.create(getContext().getResources(), idHappy, null);
                }

                int idSad = a.getResourceId(R.styleable.EmojiRateSlider_ers_icon_sad, 0);
                if (idSad != 0) {
                    mSadIcon = VectorDrawableCompat.create(getContext().getResources(), idSad, null);
                }

                int idWeird = a.getResourceId(R.styleable.EmojiRateSlider_ers_icon_weird, 0);
                if (idWeird != 0) {
                    mWeirdIcon = VectorDrawableCompat.create(getContext().getResources(), idWeird, null);
                }
            } catch (Exception ignored) {
            } finally {
                a.recycle();
            }
        }
    }

    public void setHasWeird(boolean hasWeird) {
        this.hasWeird = hasWeird;
        invalidate();
    }

    public void setBgColor(@ColorInt int color) {
        this.mBgColor = color;
        invalidate();
    }

    public void setHappyColor(@ColorInt int color) {
        this.mHappyColor = color;
        invalidate();
    }

    public void setSadColor(@ColorInt int color) {
        this.mSadColor = color;
        invalidate();
    }

    public void setWeirdColor(@ColorInt int color) {
        this.mWeirdColor = color;
        invalidate();
    }

    public void setMax(int max) {
        if (max < 3) {
            throw new IllegalArgumentException("Max must be greater or equal of 3");
        }
        this.mMax = max;
        invalidate();
    }

    public void setOnMoodChangeListener(@Nullable OnMoodChangeListener onMoodChangeListener) {
        this.onMoodChangeListener = onMoodChangeListener;
    }

    @Nullable
    public OnMoodChangeListener getOnMoodChangeListener() {
        return onMoodChangeListener;
    }

    public boolean isHasWeird() {
        return hasWeird;
    }

    @ColorInt
    public int getBgColor() {
        return mBgColor;
    }

    @ColorInt
    public int getHappyColor() {
        return mHappyColor;
    }

    public int getMax() {
        return mMax;
    }

    @ColorInt
    public int getSadColor() {
        return mSadColor;
    }

    public int getSelectedItem() {
        return mSelectedItem;
    }

    @ColorInt
    public int getWeirdColor() {
        return mWeirdColor;
    }

    public void setSelectedItem(int selectedItem) {
        if (selectedItem >= mMax) {
            throw new IllegalArgumentException("You cannot select value greater than max");
        }
        this.mSelectedItem = selectedItem;
        invalidate();
    }

    private boolean processTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            wasSlided = false;
            isTapped = true;
            if (mAnimator != null) mAnimator.cancel();
            mPrevX = event.getX();
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(event.getX() - mPrevX) > 25) {
                wasSlided = true;
            }
            updateView(event.getX(), event.getY(), true);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isTapped = false;
            if (!wasSlided) animate(mSelectedItem, findIndex(event.getX(), event.getY()));
            else invalidate();
        }
        return false;
    }

    private void animate(int from, int to) {
        if (from != to) {
            Rect rFrom = mRects[from];
            Rect rTo = mRects[to];

            ValueAnimator animator = ValueAnimator.ofFloat(rFrom.centerX(), rTo.centerX());
            animator.setDuration(150);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    updateView(value, mRects[0].centerY(), false);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    mAnimator = null;
                    notifyChanged();
                }

                @Override
                public void onAnimationStart(android.animation.Animator animation) {
                }
            });
            animator.start();
            mAnimator = animator;
        }
    }

    private void updateView(float x, float y, boolean notify) {
        int selected = findIndex(x, y);
        if (selected != mSelectedItem) {
            mSelectedItem = selected;
            invalidate();
            if (notify) notifyChanged();
        }
    }

    private int findIndex(float x, float y) {
        int selected = 0;
        float minDist = calcDist(x, y, mRects[0]);
        for (int i = 0; i < mRects.length; i++) {
            Rect rect = mRects[i];
            if (rect != null) {
                float dist = calcDist(x, y, rect);
                if (dist < minDist) {
                    minDist = dist;
                    selected = i;
                }
            }
        }
        return selected;
    }

    private float calcDist(float x, float y, Rect rect) {
        float xDiff = rect.centerX() - x;
        float yDiff = rect.centerY() - y ;
        return (float) Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
    }

    private void notifyChanged() {
        if (getOnMoodChangeListener() != null) {
            getOnMoodChangeListener().onMoodChanged(this, mSelectedItem);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackgroundLayer(canvas);
        drawNegative(canvas);
        drawPositive(canvas);
        drawCurrent(canvas);
    }

    private void drawCurrent(Canvas canvas) {
        Rect rect = mRects[mSelectedItem];

        int h = (int) (rect.height() * (isTapped ? THIRD_LAYER_TAPPED : THIRD_LAYER));
        int radius = h / 2;
        int m = (rect.height() - h) / 2;

        if (isPositive()) mShadowPaint.setColor(mHappyColor);
        else if (isNegative()) mShadowPaint.setColor(mSadColor);
        else if (hasWeird) mShadowPaint.setColor(mWeirdColor);

        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, mShadowPaint);

        if (isPositive()) {
            mSmileIcon.setBounds(rect.left + m, rect.top + m, rect.right - m, rect.bottom - m);
            mSmileIcon.draw(canvas);
        } else if (isNegative()) {
            mSadIcon.setBounds(rect.left + m, rect.top + m, rect.right - m, rect.bottom - m);
            mSadIcon.draw(canvas);
        } else if (hasWeird) {
            mWeirdIcon.setBounds(rect.left + m, rect.top + m, rect.right - m, rect.bottom - m);
            mWeirdIcon.draw(canvas);
        }
    }

    private boolean isNegative() {
        return mSelectedItem < mMax / 2;
    }

    private boolean isPositive() {
        if (hasWeird) {
            return mSelectedItem > mMax / 2;
        } else return mSelectedItem >= mMax / 2;
    }

    private void drawNegative(Canvas canvas) {
        if (mSelectedItem < mMax - 1) {
            Rect start = mRects[mSelectedItem];
            Rect end = mRects[mMax - 1];

            mColorPaint.setColor(mSadColor);
            mColorPaint.setStyle(Paint.Style.FILL);

            int h = (int) (start.height() * SECOND_LAYER);
            int m = (start.height() - h) / 2;

            canvas.drawRect(start.centerX(), start.top + m, end.centerX(),
                    end.bottom - m, mColorPaint);

            mShadowPaint.setColor(mSadColor);

            for (int i = mMax - 1; i > mSelectedItem; i--) {
                Rect rect = mRects[i];
                canvas.drawCircle(rect.centerX(), rect.centerY(), (int) (rect.height() * SECOND_LAYER / 2), mShadowPaint);
            }
        }
    }

    private void drawPositive(Canvas canvas) {
        if (mSelectedItem > 0) {
            Rect start = mRects[0];
            Rect end = mRects[mSelectedItem];

            mColorPaint.setColor(mHappyColor);
            mColorPaint.setStyle(Paint.Style.FILL);

            int h = (int) (start.height() * SECOND_LAYER);
            int m = (start.height() - h) / 2;

            canvas.drawRect(start.centerX(), start.top + m, end.centerX(),
                    end.bottom - m, mColorPaint);

            mShadowPaint.setColor(mHappyColor);

            for (int i = 0; i < mSelectedItem; i++) {
                Rect rect = mRects[i];
                canvas.drawCircle(rect.centerX(), rect.centerY(), (int) (rect.height() * SECOND_LAYER / 2), mShadowPaint);
            }
        }
    }

    private void drawBackgroundLayer(Canvas canvas) {
        Rect start = mRects[0];
        Rect end = mRects[mRects.length - 1];

        mShadowPaint.setColor(mBgColor);

        int h = (int) (start.height() * FIRST_LAYER);
        int m = (start.height() - h) / 2;

        canvas.drawRect(start.centerX() + m, start.top + m, end.centerX() - m, end.bottom - m, mShadowPaint);
        canvas.drawCircle(start.centerX(), start.centerY(), (int) (start.height() * FIRST_LAYER / 2), mShadowPaint);
        canvas.drawCircle(end.centerX(), end.centerY(), (int) (end.height() * FIRST_LAYER / 2), mShadowPaint);

        mColorPaint.setColor(mBgColor);
        mColorPaint.setStyle(Paint.Style.FILL);

        canvas.drawRect(start.centerX(), start.top + m, end.centerX(), end.bottom - m, mColorPaint);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle savedInstance = new Bundle();
        savedInstance.putParcelable("super", super.onSaveInstanceState());
        savedInstance.putInt("position", mSelectedItem);
        return savedInstance;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle savedInstance = (Bundle) state;
            mSelectedItem = savedInstance.getInt("position", mSelectedItem);
            super.onRestoreInstanceState(savedInstance.getParcelable("super"));
        } else {
            super.onRestoreInstanceState(state);
        }
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
        void onMoodChanged(View view, int mood);
    }
}
