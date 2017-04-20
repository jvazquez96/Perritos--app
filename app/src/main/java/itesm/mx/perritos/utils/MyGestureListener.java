package itesm.mx.perritos.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by jorgevazquez on 4/20/17.
 */

public class MyGestureListener implements GestureDetector.OnGestureListener {

    private Context context;
    private OnLongPressListener mListener;

    public MyGestureListener(Context context) {
        this.context = context;
    }

    public void setmListener(OnLongPressListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mListener.onLongPressListener();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface OnLongPressListener {
        void onLongPressListener();
    }

}
