package com.ange.keyboardhidedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity implements View.OnLayoutChangeListener, View.OnFocusChangeListener {
    private final static String TAG = "BaseActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 在子类里调用 initKeyBoardListener
    }

    /**
     * 要键盘弹出时，scrollView做滑动需，调用此方法
     *
     * @return
     */
    protected void initKeyBoardListener(ScrollView scrollView) {
        this.mainScrollView = scrollView;
        this.editTexts = new ArrayList<>();
        findEditText(scrollView);
        setFoucesListener();
    }

    protected void initKeyBoardListener(ScrollView scrollView, int offset) {
        this.mainScrollView = scrollView;
        this.editTexts = new ArrayList<>();
        findEditText(scrollView);
        setFoucesListener();
        this.offset = offset;
    }

    //偏移量
    private int offset;
    //当前页面获取了焦点的editText
    private ScrollView mainScrollView;
    private Runnable scrollRunnable;
    private boolean normalCanScroll = true;
    private EditText currentFocusEt;
    //当前页面获取了所有的editText
    List<EditText> editTexts;

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        getContentView().addOnLayoutChangeListener(this);
    }

    public View getContentView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, final int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) { //获取屏幕高度
        int screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        int keyHeight = screenHeight / 3;
        if (mainScrollView != null && normalCanScroll) {
            normalCanScroll = mainScrollView.canScrollVertically(1);
            Log.e(TAG, "mainScrollView:canScroll:" + normalCanScroll);
        }

        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起

        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            Log.e(TAG, "弹起");
            if (currentFocusEt != null) {
                Log.e(TAG, currentFocusEt.getText().toString());
                int[] location = new int[2];
                currentFocusEt.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                int height = currentFocusEt.getHeight();
                y = y + height;
                Log.e(TAG, "bottom:" + bottom + " currentFocusEt.bottom:" + y);
                if (y > bottom && mainScrollView != null) {
                    final int finalY = y;
                    if (normalCanScroll) {
                        scrollRunnable = new Runnable() {
                            @Override
                            public void run() {
                                mainScrollView.scrollBy(0, finalY - bottom + offset);
                            }
                        };
                        mainScrollView.postDelayed(scrollRunnable, 100);
                    } else {
                        mainScrollView.scrollBy(0, finalY - bottom + offset);
                    }
                }
            }
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            Log.e(TAG, "收回");
        }
    }

    /**
     * 监听焦点获取当前的获取焦点的editText
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && v instanceof EditText) {
            currentFocusEt = (EditText) v;
        }
    }

    /**
     * 找出当前页面的所有editText
     *
     * @param view
     */
    private void findEditText(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                findEditText(v);
            }
        } else if (view instanceof EditText) {
            editTexts.add((EditText) view);
        }
    }

    /**
     * 当前页面的所有editText设置焦点监听
     */
    private void setFoucesListener() {
        for (EditText et : editTexts) {
            et.setOnFocusChangeListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainScrollView != null) {
            mainScrollView.removeCallbacks(scrollRunnable);
        }
    }
}