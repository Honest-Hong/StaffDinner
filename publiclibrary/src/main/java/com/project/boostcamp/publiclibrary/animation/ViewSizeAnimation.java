package com.project.boostcamp.publiclibrary.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import com.project.boostcamp.publiclibrary.util.Logger;

/**
 * Created by Hong Tae Joon on 2017-08-17.
 */

public class ViewSizeAnimation {
    private View target;
    private int fromWidth = -1;
    private int fromHeight = -1;
    private int toWidth = -1;
    private int toHeight = -1;
    private int diffWidth;
    private int diffHeight;
    private Animation animation;

    public ViewSizeAnimation() {
        animation =  new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int newWidth = (int) (fromWidth - diffWidth * interpolatedTime);
                int newHeight = (int) (fromHeight - diffHeight * interpolatedTime);
                if(newWidth > 0 && newHeight > 0) {
                    setTargetLayoutParams(newWidth, newHeight);
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                target.setVisibility(View.VISIBLE);
                setTargetLayoutParams(fromWidth, fromHeight);
                diffWidth = fromWidth - toWidth;
                diffHeight = fromHeight - toHeight;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(toWidth == 0 || toHeight == 0){
                    target.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public ViewSizeAnimation setTarget(View view) {
        target = view;
        return this;
    }

    public ViewSizeAnimation from(int width, int height) {
        fromWidth = width;
        fromHeight = height;
        return this;
    }

    public ViewSizeAnimation to(int width, int height) {
        toWidth = width;
        toHeight = height;
        return this;
    }

    public ViewSizeAnimation setInterpolater(Interpolator interpolater) {
        animation.setInterpolator(interpolater);
        return this;
    }

    public ViewSizeAnimation setDuration(long duration) {
        animation.setDuration(duration);
        return this;
    }

    public void start() {
        target.startAnimation(animation);
    }

    private void setTargetLayoutParams(int width, int height) {
        target.getLayoutParams().width = width;
        target.getLayoutParams().height = height;
        target.requestLayout();
    }
}
