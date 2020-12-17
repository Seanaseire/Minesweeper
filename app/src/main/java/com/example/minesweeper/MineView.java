package com.example.minesweeper;

// imports
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collections;

public class MineView extends View {

    // Initialise variables
    Rect square;
    int rectBounds;
    int gridSize = 10;
    int mines = 20;
    String mineText = "M";
    String flagText = "F";
    int gridSizeTotal = gridSize*gridSize;
    int[] mine_placement;
    int[][] gridLayout;
    int[][] flagLayout;
    int[][] gridMineWarning;
    boolean gameOver = false;
    boolean flagFlag = false;
    private float mTextHeight;

    private Paint black, grey, red, yellow, green;

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
    // style in case the view is to be styled in a certain way
    public MineView(Context c, AttributeSet attrs, int default_style) {
        super(c, attrs, default_style);
        init();
    }
    // refactored init method as most of this code is shared by all the
    // constructors
    void init() {
        // Set Variables
        gameOver = false;
        mine_placement = new int[gridSizeTotal];
        gridLayout = new int[gridSize][gridSize];
        flagLayout = new int[gridSize][gridSize];
        gridMineWarning = new int[gridSize][gridSize];

        int i,j,k,l;
        ArrayList<Integer> randomList = new ArrayList<Integer>();
        // Set and place mines
        for (i = 1; i <= gridSizeTotal; ++i) randomList.add(i);
        Collections.shuffle(randomList);

        for(i=0; i<=mines-1; i++) {
            mine_placement[i] = randomList.get(i);
        }

        l = 0;
        // Nested for loops for multiple rows of squares.
        // j is columns (y coord). k is rows (x coord).
        for(j=0; j<=gridSize-1; j++) {
            //Count rows.
            for(k=0; k<=gridSize-1; k++){
                l++;
                for(i=0; i<=mines; i++) {
                    if( mine_placement[i]==l){
                        gridLayout[k][j] = 2;
                    }
                }
            }// for k inner loop.
        }// for j outer loop.

        // Set up a default TextPaint object
        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;

        gridMineWarning = getMineWarnArray();
    }
    // public method that needs to be overridden to draw the contents of this
    // widget
    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setPadding(10, 10, 10, 10);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        setBackgroundColor(Color.WHITE);

        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.BLACK);

        grey = new Paint(Paint.ANTI_ALIAS_FLAG);
        grey.setColor(Color.GRAY);

        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setColor(Color.RED);

        yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellow.setColor(Color.YELLOW);

        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        green.setColor(Color.GREEN);

        canvas.save();
        canvas.translate(0,0);

        int xorig = 10;
        int yorig = 10;
        int sideLength = (getWidth()/gridSize) - 1;
        rectBounds = sideLength;


        square = new Rect(xorig, yorig, sideLength, sideLength);

        int i, j;
        int paddingOffset = paddingLeft+paddingTop;

        // Nested for loops for multiple rows of squares.
        // i is columns (y coord). j is rows (x coord).
        for(i=0; i<=gridSize-1; i++) {

            // Save the canvas origin onto the stack.
            canvas.save();

            // Draw rows.
            for(j=0; j<=gridSize-1; j++){
                // Save the current origin.
                canvas.save();

                // Move to origin of this column.
                canvas.translate( (i * rectBounds), (j * rectBounds));

                if(flagLayout[i][j] == 1 && gridLayout[i][j] != 1){
                    canvas.drawRect(square, yellow);
                    Paint textPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
                    textPainter.setColor(Color.BLACK);
                    textPainter.setTextSize(50);
                    canvas.drawText(flagText,((sideLength/2) + (mTextHeight-paddingOffset)), ((sideLength/2) + (mTextHeight+paddingOffset)), textPainter);
                }
                else if(gridLayout[i][j] == 2 && gameOver == true){
                    // Draw a square in this i,j position.
                    canvas.drawRect(square, red);
                    Paint textPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
                    textPainter.setColor(Color.BLACK);
                    textPainter.setTextSize(50);
                    canvas.drawText(mineText,((sideLength/2) + (mTextHeight-paddingOffset)), ((sideLength/2) + (mTextHeight+paddingOffset)), textPainter);
                }
                else if(gridLayout[i][j] == 0 || gridLayout[i][j] == 2){
                    // Draw a square in this i,j position.
                    canvas.drawRect(square, black);

                }
                else if(gridLayout[i][j] == 1 && flagLayout[i][j] == 0){
                    // Draw a square in this i,j position.
                    canvas.drawRect(square, grey);

                    // Draw number indicator
                    if(gridMineWarning[i][j] != 0 && gridMineWarning[i][j] != 9){
                        Paint numberPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
                        numberPainter.setColor(Color.GREEN);
                        numberPainter.setTextSize(50);
                        canvas.drawText(String.valueOf(gridMineWarning[i][j]),
                                ((sideLength / 2) + (mTextHeight - paddingOffset)),
                                ((sideLength / 2) + (mTextHeight + paddingOffset)),
                                numberPainter);
                    }
                }

                //Restore to the starting origin.
                canvas.restore();

            }//for j inner loop.

            //Restore the canvas to the starting origin. Remember that restores must match saves (stack/LIFO).
            canvas.restore();

        }//for i outer loop.

        //Formatting Screen
        this.setLayoutParams(new LinearLayout.LayoutParams(getWidth(), getWidth()));


    }
    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
        // determine what kind of touch event we have
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && gameOver == false) {

            //Get where the event occurred.
            float x = event.getX();
            float y = event.getY();

            //This width and height of the touch area.
            float width = getWidth();
            float height = rectBounds * gridSize;

            //float height = getHeight();

            //10 x 10 matrix. The row height and column width.
            float rowHeight = height / gridSize;
            float colWidth = width / gridSize;

            //Get the square that was touched.
            int row = 0;
            int col = 0;

            int i, j;
            for (i = 1; i <= gridSize; i++) {
                if (x < (i * colWidth)) {
                    col = i;
                    break;
                }// if cols
            }//for cols

            for (j = 1; j <= gridSize; j++) {
                if (y < (j * rowHeight)) {
                    row = j;
                    // Set/Unset flag
                    if ((flagFlag && flagLayout[col - 1][row - 1] == 0) && (gridLayout[col - 1][row - 1] != 1)) {
                        flagLayout[col - 1][row - 1] = 1;
                    }
                    else if (flagFlag) {
                        flagLayout[col - 1][row - 1] = 0;
                    } // Check if game over if not uncover block
                    else if (!flagFlag && flagLayout[col - 1][row - 1] == 0) {
                        if (gridLayout[col - 1][row - 1] == 2) {
                            gameOver = true;
                        } else {
                            gridLayout[col - 1][row - 1] = 1;
                        }
                    }
                    break;
                }//if rows.
            }//for rows.

            invalidate();
            return true;
        }
        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead
        return super.onTouchEvent(event);

    }

    // Toggles flag indicator
    public boolean toggleFlag(){
        flagFlag = !flagFlag;
        return flagFlag;
    }

    // Return 2d array of flags
    public int[][] getFlags(){
        return flagLayout;
    }

    // Return Grid size
    public int getGridSize(){
        return gridSize;
    }



    // Get number indicator for grid as seen in original game
    public int[][] getMineWarnArray(){

        int[][] MineWarning = new int[gridSize][gridSize];
        int i, j, mineNumberWarn;

        // Nested for loops for multiple rows of squares.
        // i is columns (y coord). j is rows (x coord).
        for(i=0; i<=gridSize-1; i++) {

            //Search rows.
            for(j=0; j<=gridSize-1; j++){

                if(gridLayout[i][j] == 1 || gridLayout[i][j] == 0){

                    mineNumberWarn = 0;

                    if(i==0 && j==0){                                   // Top Left
                        if(gridLayout[i][j+1] == 2){mineNumberWarn++;}     // Center Bottom
                        if(gridLayout[i+1][j+1] == 2){mineNumberWarn++;}   // Right Bottom
                        if(gridLayout[i+1][j] == 2){mineNumberWarn++;}     // Right Center
                    }
                    else if(i==0 && j==gridSize-1){                     // Bottom Left
                        if(gridLayout[i][j-1] == 2){mineNumberWarn++;}     // Center Top
                        if(gridLayout[i+1][j-1] == 2){mineNumberWarn++;}   // Right Top
                        if(gridLayout[i+1][j] == 2){mineNumberWarn++;}     // Right Center
                    }
                    else if(i==gridSize-1 && j==gridSize-1){            // Bottom Right
                        if(gridLayout[i][j-1] == 2){mineNumberWarn++;}     // Center Top
                        if(gridLayout[i-1][j-1] == 2){mineNumberWarn++;}   // Left Top
                        if(gridLayout[i-1][j] == 2){mineNumberWarn++;}     // Left Center
                    }
                    else if(i==gridSize-1 && j==0){                     // Top Right
                        if(gridLayout[i-1][j] == 2){mineNumberWarn++;}     // Left Center
                        if(gridLayout[i-1][j+1] == 2){mineNumberWarn++;}   // Left Bottom
                        if(gridLayout[i][j+1] == 2){mineNumberWarn++;}     // Center Bottom
                    }
                    else if(i==0 && (j!=0 || j!=gridSize-1)){           // Left Side
                        if(gridLayout[i][j+1] == 2){mineNumberWarn++;}     // Center Bottom
                        if(gridLayout[i][j-1] == 2){mineNumberWarn++;}     // Center Top
                        if(gridLayout[i+1][j+1] == 2){mineNumberWarn++;}   // Right Bottom
                        if(gridLayout[i+1][j-1] == 2){mineNumberWarn++;}   // Right Top
                        if(gridLayout[i+1][j] == 2){mineNumberWarn++;}     // Right Center
                    }
                    else if(i==gridSize-1 && (j!=0 || j!=gridSize-1)){  // Right Side
                        if(gridLayout[i][j+1] == 2){mineNumberWarn++;}     // Center Bottom
                        if(gridLayout[i][j-1] == 2){mineNumberWarn++;}     // Center Top
                        if(gridLayout[i-1][j-1] == 2){mineNumberWarn++;}   // Left Top
                        if(gridLayout[i-1][j+1] == 2){mineNumberWarn++;}   // Left Bottom
                        if(gridLayout[i-1][j] == 2){mineNumberWarn++;}     // Left Center
                    }
                    else if(j==0 && (i!=0 || i!=gridSize-1)){           // Top Side
                        if(gridLayout[i][j+1] == 2){mineNumberWarn++;}     // Center Bottom
                        if(gridLayout[i+1][j+1] == 2){mineNumberWarn++;}   // Right Bottom
                        if(gridLayout[i-1][j+1] == 2){mineNumberWarn++;}   // Left Bottom
                        if(gridLayout[i-1][j] == 2){mineNumberWarn++;}     // Left Center
                        if(gridLayout[i+1][j] == 2){mineNumberWarn++;}     // Right Center
                    }
                    else if(j==gridSize-1 && (i!=0 || i!=gridSize-1)){  // Bottom Side
                        if(gridLayout[i][j-1] == 2){mineNumberWarn++;}     // Center Top
                        if(gridLayout[i+1][j-1] == 2){mineNumberWarn++;}   // Right Top
                        if(gridLayout[i-1][j-1] == 2){mineNumberWarn++;}   // Left Top
                        if(gridLayout[i-1][j] == 2){mineNumberWarn++;}     // Left Center
                        if(gridLayout[i+1][j] == 2){mineNumberWarn++;}     // Right Center
                    }
                    else{                                               // Central Grid
                        if(gridLayout[i][j+1] == 2){mineNumberWarn++;}     // Center Bottom
                        if(gridLayout[i][j-1] == 2){mineNumberWarn++;}     // Center Top
                        if(gridLayout[i+1][j+1] == 2){mineNumberWarn++;}   // Right Bottom
                        if(gridLayout[i+1][j-1] == 2){mineNumberWarn++;}   // Right Top
                        if(gridLayout[i+1][j] == 2){mineNumberWarn++;}     // Right Center
                        if(gridLayout[i-1][j-1] == 2){mineNumberWarn++;}   // Left Top
                        if(gridLayout[i-1][j+1] == 2){mineNumberWarn++;}   // Left Bottom
                        if(gridLayout[i-1][j] == 2){mineNumberWarn++;}     // Left Center
                    }
                    // Enters number on grid
                    MineWarning[i][j] = mineNumberWarn;

                }else{
                    // Set bombs to 9
                    MineWarning[i][j] = 9;
                }
            }//for j inner loop.
        }//for i outer loop.

        return MineWarning;
    }



}