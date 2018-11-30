package ru.nsuorg.shiftorg.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class LoadingView {

    private FrameLayout loadingView;
    private long openingTime, closingTime;
    private int backgroundColor;
    private boolean isAnimating;

    private Animation.AnimationListener animationListenerStart, animationListenerStop;
    private Float alpha;

    @SuppressLint("ClickableViewAccessibility")
    public LoadingView(Context context, ViewGroup container) {
        this.loadingView = new FrameLayout(context);
        isAnimating = false;
        loadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ProgressBar bar = new ProgressBar(context);
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER;
        bar.setLayoutParams(param);
        loadingView.addView(bar);
        setBackgroundColor(Color.BLACK);
        this.loadingView.setVisibility(View.GONE);
        this.loadingView.setAlpha(1.0F);
        container.addView(this.loadingView);
        setOpeningTime(250);
        setClosingTime(250);
        setAlpha(0.6F);
        loadingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.animationListenerStart = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                loadingView.setVisibility(View.VISIBLE);
                loadingView.setAlpha(alpha);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        this.animationListenerStop = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingView.setVisibility(View.GONE);
                loadingView.setAlpha(0.0F);
                isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    public boolean isAnimating()
    {
        return isAnimating;
    }

    public void startAnimation() {
        AlphaAnimation startAnimation;
        startAnimation = new AlphaAnimation(0.0f, alpha);
        startAnimation.setDuration(this.openingTime);
        startAnimation.setAnimationListener(this.animationListenerStart);
        //startAnimation.setFillAfter(true);
        loadingView.setVisibility(View.VISIBLE);
        isAnimating = true;
        loadingView.startAnimation(startAnimation);
    }

    public void stopAnimation() {
        AlphaAnimation stopAnimation;
        stopAnimation = new AlphaAnimation(this.alpha, 0.0F);
        stopAnimation.setDuration(this.closingTime);
        //stopAnimation.setFillAfter(true);
        stopAnimation.setAnimationListener(this.animationListenerStop);
        loadingView.startAnimation(stopAnimation);
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        loadingView.setBackground(new ColorDrawable(this.backgroundColor));
    }

    public void setOpeningTime(long ms) { this.openingTime = ms; }

    public void setClosingTime(long ms) { this.closingTime = ms; }

    public void setAlpha(Float alpha) { this.alpha = alpha; }

    public int getBackgroundColor() { return this.backgroundColor; }

    public long getOpeningTime() { return this.openingTime; }

    public long getClosingTime() { return this.closingTime; }

    public Float getAlpha() { return this.alpha; }
}