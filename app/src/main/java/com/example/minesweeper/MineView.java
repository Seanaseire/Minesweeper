package com.example.minesweeper;

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MineView extends View {
    // default constructor for the class that takes in a context
    public MineView(Context c) {
        super(c);
        init();
    }
    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public MineView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }
    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certian way
    public MineView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }
    // refactored init method as most of this code is shared by all the
    // constructors
    private void init() {

    }
    // public method that needs to be overridden to draw the contents of this
    // widget
    public void onDraw(Canvas canvas) {

    }
    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
        // determine what kind of touch event we have
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            invalidate();
            return true;
        } else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
            invalidate();
            return true;
        } else if(event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            invalidate();
            return true;
        }else if(event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            invalidate();
            return true;
        } else if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
            invalidate();
            return true;
        }
        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead
        return super.onTouchEvent(event);

    }
    // private fields that are necessary for rendering the view
    // the colours of our squares
    private boolean touches[]; // which fingers providing input
    private float touchx[]; // x position of each touch
    private float touchy[]; // y position of each touch
    private int first; // the first touch to be rendered
    private boolean touch; // do we have at least on touch


}
