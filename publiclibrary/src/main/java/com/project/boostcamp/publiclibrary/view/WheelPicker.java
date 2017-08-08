package com.project.boostcamp.publiclibrary.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by Hong Tae Joon on 2017-07-27.
 */


public class WheelPicker extends ScrollView {
    private Context context;
    private LinearLayout container;
    private WheelPickerAdapter adapter;
    private int itemViewHeight = 50;
    private OnSelectedChangeListener onSelectedChangeListener;
    private final static float ALPHA_NOT_SELECTED = 0.3f;
    private final static float ALPHA_SELECTED = 1.0f;
    private int selectedIndex;
    private boolean enableScroll = true;

    public WheelPicker(Context context) {
        super(context);
        initView(context);
    }

    public WheelPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        this.setVerticalScrollBarEnabled(false);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);
        this.addView(container);
    }

    public void setAdapter(WheelPickerAdapter adapter) {
        this.adapter = adapter;
        createViews();
        measureItemViewHeight();
        addEmptyView();
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
        container.getChildAt(index + 1).setAlpha(1.0f);
        post(moveTask);
    }

    private void createViews() {
        if(adapter == null) {
            return;
        }

        container.removeAllViews();
        for(int i=0; i<adapter.getCount(); i++) {
            View v = adapter.getView(LayoutInflater.from(context), container, i);
            v.setAlpha(ALPHA_NOT_SELECTED);
            container.addView(v);
        }
    }

    private void measureItemViewHeight() {
        if(adapter.getCount() == 0) {
            return;
        }
        container.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        itemViewHeight = container.getMeasuredHeight();
        itemViewHeight /= adapter.getCount();
    }

    private void addEmptyView() {
        View v1 = new View(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, itemViewHeight);
        v1.setLayoutParams(params);
        container.addView(v1, 0);
        View v2 = new View(context);
        v2.setLayoutParams(params);
        container.addView(v2);
    }

    private int getIndexFromY(int y) {
        int index = 0;
        try {
            index = (y + itemViewHeight * 3 / 2) / itemViewHeight - 1;
        } catch (ArithmeticException e) {
            index = -1;
        }
        return index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    private Runnable scrollTask = new Runnable() {
        @Override
        public void run() {
            smoothScrollTo(0, itemViewHeight * getIndexFromY(getScrollY()));
        }
    };

    private Runnable moveTask = new Runnable() {
        @Override
        public void run() {
            setScrollY(itemViewHeight * selectedIndex);
        }
    };

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int childIndex = getIndexFromY(scrollY) + 1;
        selectedIndex = childIndex - 1;
        if(childIndex > 0) {
            container.getChildAt(childIndex - 1).setAlpha(ALPHA_NOT_SELECTED);
        }
        container.getChildAt(childIndex).setAlpha(ALPHA_SELECTED);
        if(onSelectedChangeListener != null) {
            onSelectedChangeListener.onSelectedChange(adapter.getItem(selectedIndex));
        }
        if(childIndex < adapter.getCount() + 1) {
            container.getChildAt(childIndex + 1).setAlpha(ALPHA_NOT_SELECTED);
        }

        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!enableScroll) {
            return true;
        }
        if(ev.getAction() == MotionEvent.ACTION_UP) {
            this.post(scrollTask);
            getParent().requestDisallowInterceptTouchEvent(false);
        } else if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        if(heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            height = itemViewHeight * 3;
        }
        container.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        setMeasuredDimension(container.getMeasuredWidth(), height);
    }

    public boolean isEnableScroll() {
        return enableScroll;
    }

    public void setEnableScroll(boolean enableScroll) {
        this.enableScroll = enableScroll;
    }
}
