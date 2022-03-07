package com.wmg.rwsl.animationTest.test1.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.wmg.rwsl.R;

public class TranslationImage extends androidx.appcompat.widget.AppCompatImageView {

    public TranslationImage(Context context) {
        this(context, null);
    }

    public TranslationImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TranslationImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.TranslationImage,
                0, 0);
        enterMode = a.getInt(R.styleable.TranslationImage_enter_mode, 0);
        mills_duration = a.getInt(R.styleable.TranslationImage_mills_duration, 0);
        animatorType = a.getInt(R.styleable.TranslationImage_animator_type, 0);
        a.recycle();
        if (animatorType == 0) {
            mCountDownTimer = new CountDownTimer(mills_duration, 10) {
                @Override
                public void onTick(long millisLeft) {
                    float percentPassed = (mills_duration - millisLeft) * 1.0f / mills_duration;
                    if (enterMode == 0) {
                        if (mRepeatFlag == 0) {
                            dx = -(mWidth * percentPassed);
                        } else {
                            dx = (mWidth * (1 - percentPassed));
                        }
                    } else {
                        if (mRepeatFlag == 0) {
                            dx = (mWidth * (1 - percentPassed));
                        } else {
                            dx = -(mWidth * percentPassed);
                        }
                    }
                    postInvalidate();  //执行任务
                }

                @Override
                public void onFinish() {
                    if (mCountDownTimer != null) {
                        mRepeatFlag ^= 1;
                        mCountDownTimer.start();
                    }
                }
            };
            mCountDownTimer.start();
        } else if (animatorType == 1){
            mValueAnimator = ValueAnimator.ofFloat(0, -1);
            mValueAnimator.setDuration(mills_duration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percentPassed = (float) animation.getAnimatedValue();
                    if (enterMode == 0) {
                        if (mRepeatFlag == 0) {
                            dx = (mWidth * percentPassed);
                        } else {
                            dx = (mWidth * (1 + percentPassed));
                        }
                    } else {
                        if (mRepeatFlag == 0) {
                            dx = (mWidth * (1 + percentPassed));
                        } else {
                            dx = (mWidth * percentPassed);
                        }
                    }
                    postInvalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation, boolean isReverse) {
                    super.onAnimationStart(animation, isReverse);
                }

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    super.onAnimationEnd(animation, isReverse);
                    mRepeatFlag ^= 1;
                    mValueAnimator.start();
                }
            });
            mValueAnimator.start();
        }
    }

    private int enterMode = 0;
    private int mills_duration = 0;
    private int animatorType = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mRepeatFlag = 0;
    private float dx = 0;
    private float dy = 0;
    private CountDownTimer mCountDownTimer;
    private ValueAnimator mValueAnimator;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (measureWidth > 0) {
            mWidth = measureWidth;
        }
        if (measureHeight > 0) {
            mHeight = measureHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(dx, dy);
        super.onDraw(canvas);
    }
}
