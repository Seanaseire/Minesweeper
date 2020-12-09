package com.example.minesweeper;

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MineView extends View {

    Rect square;
    int rectBounds;
    int gridSize = 10;
    int[][] gridLayout;

    private Paint black, grey;

    // default constructor for the class that takes in a context
    public MineView(Context c) {
        super(c);
        init();
    }
    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public MineView(Context c, AttributeSet attrs) {
        super(c, attrs);
        init();
    }
    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certian way
    public MineView(Context c, AttributeSet attrs, int default_style) {
        super(c, attrs, default_style);
        init();
    }
    // refactored init method as most of this code is shared by all the
    // constructors
    private void init() {
        gridLayout = new int[gridSize][gridSize];
    }
    // public method that needs to be overridden to draw the contents of this
    // widget
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        setBackgroundColor(Color.WHITE);

        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);

        grey = new Paint(Paint.ANTI_ALIAS_FLAG);
        grey.setColor(Color.GRAY);


        //canvas.drawRect(-100, -120, 50, 80, p );
        canvas.save();
        canvas.translate(0,0);

        int xorig = 10;
        int yorig = 10;
        int sideLength = (getWidth()/gridSize) - 1;
        rectBounds = sideLength;


        square = new Rect(xorig, yorig, sideLength, sideLength);

        int i;
        int j;

        float x = paddingLeft;
        float y = paddingTop;

        //Nested for loops for multiple rows of squares.
        //i is columns (y coord). j is rows (x coord).
        for(i=0; i<=gridSize-1; i++) {

            //Save the canvas origin onto the stack.
            canvas.save();

            //Draw rows.
            for(j=0; j<=gridSize-1; j++){
                //Save the current origin.
                canvas.save();

                //Move to origin of this column.
                canvas.translate( (i * rectBounds), (j * rectBounds));

                if(gridLayout[i][j] == 1){
                    //Draw a square in this i,j position.
                    canvas.drawRect(square, grey);
                }else if(gridLayout[i][j] == 0){
                    //Draw a square in this i,j position.
                    canvas.drawRect(square, black);
                }

                //Restore to the starting origin.
                canvas.restore();

            }//for j inner loop.

            //Restore the canvas to the starting origin. Remember that restores must match saves (stack/LIFO).
            canvas.restore();

        }//for i outer loop.

    }
    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
        // determine what kind of touch event we have
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            //Get where the event occurred.
            float x = event.getX();
            float y = event.getY();

            //This width and height of the touch area.
            float width = getWidth();
            float height = rectBounds * gridSize;

            //float height = getHeight();

            //10 x 10 matrix. The row height and column width.
            float rowHeight = height/gridSize;
            float colWidth = width/gridSize;

            //Get the square that was touched.
            int row = 0;
            int col = 0;
            int[][] rowCol = new int[gridSize][gridSize];

            int i, j;
            for(i=1; i<=gridSize; i++) {
                if (x < (i * colWidth)) {
                    col = i;
                    break;
                }// if cols
            }//for cols

            for (j=1; j<=gridSize; j++) {
                if (y < (j * rowHeight)) {
                    row = j;
                    gridLayout[col-1][row-1] = 1;
                    break;
                }//if rows.
            }//for rows.

            if(row == 0 || col == 0){

            }else{
                Toast.makeText(getContext(), "Row:" + row + " Col:" + col, Toast.LENGTH_SHORT).show();
            }

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
