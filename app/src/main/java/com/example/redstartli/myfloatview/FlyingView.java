package com.example.redstartli.myfloatview;


import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by redstartli on 2018/3/29.
 */

public class FlyingView extends RelativeLayout {

    private View _finger;
    private int parentLeft;
    private int parentTop;
    private int parentRight;
    private int parentBottom;

    private Handler _handler;

    private int standard = (int) (getResources().getDisplayMetrics().density * 25);

    public FlyingView(Context context) {
        super(context);
        init();
    }

    public FlyingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlyingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        _handler = new Handler();
        _finger = new View(getContext());
        _finger.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        addView(_finger);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(standard + standard, standard + standard);
        _finger.setLayoutParams(params);
    }

    private boolean initParentRect;

    private void initParentRect() {
        if (!initParentRect) {
            int[] location = new int[2];
            getLocationOnScreen(location);
            parentLeft = location[0];
            parentTop = location[1];
            parentRight = parentLeft + getWidth();
            parentBottom = parentTop + getHeight();
            initParentRect = true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            int[] fingerLocation = new int[2];
            _finger.getLocationOnScreen(fingerLocation);
            if (x > fingerLocation[0] && x < fingerLocation[0] + standard + standard && y > fingerLocation[1] && y < fingerLocation[1] + standard + standard) {
                return true;
            } else {
                return false;
            }
        }

        initParentRect();
        if (x < parentLeft + standard) {
            x = parentLeft + standard;
        }
        if (x > parentRight - standard) {
            x = parentRight - standard;
        }
        if (y < parentTop + standard) {
            y = parentTop + standard;
        }
        if (y > parentBottom - standard) {
            y = parentBottom - standard;
        }
        x -= parentLeft;
        y -= parentTop;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                _finger.layout((int) (x - standard), (int) (y - standard), (int) (x + standard), (int) (y + standard));
            }
            break;
            case MotionEvent.ACTION_UP: {
                RelativeLayout.LayoutParams params = (LayoutParams) _finger.getLayoutParams();
                params.setMargins((int) (x - standard), (int) (y - standard), 0, 0);
                _finger.setLayoutParams(params);
            }
            break;
        }
        return true;
    }
}
