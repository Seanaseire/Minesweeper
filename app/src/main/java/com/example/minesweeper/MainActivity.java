package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Initialise variables
    private Button flag_btn;
    int[][] flags;
    int flagNumber = 0;
    int gridSize;
    MineView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get gameView instance and gridsize
        gameView = (MineView) findViewById(R.id.view_minesweeper);
        gridSize = gameView.getGridSize();

        // Add actions to reset button
        Button reset_btn = (Button) findViewById(R.id.reset_btn);
        reset_btn.setText("Reset");
        // add a click listener to the button
        reset_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Re-initialise game
                gameView.init();

                // Reset the flag counter
                flagNumber = 0;
                Resources resources = getResources();
                String flag_number = resources.getQuantityString(R.plurals.flag_plural, flagNumber, flagNumber);

                TextView flag_text = (TextView) findViewById(R.id.tv_flags);
                flag_text.setText(flag_number);
            }
        });

        // Set flag counter on start
        flag_btn = (Button) findViewById(R.id.flag_btn);
        flag_btn.setText("Flag");
        // add a click listener to the button
        flag_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Set indicator for flag button
                if(gameView.toggleFlag()){
                    flag_btn.setBackgroundColor(getResources().getColor(R.color.teal_700));
                }
                else{
                    flag_btn.setBackgroundColor(getResources().getColor(R.color.teal_200));
                }
            }
        });

        Resources resources = getResources();
        String flag_number = resources.getQuantityString(R.plurals.flag_plural, flagNumber, flagNumber);

        TextView flag_text = (TextView) findViewById(R.id.tv_flags);
        flag_text.setText(flag_number);

    }

    // Get touch to update the flag

    public boolean onTouchEvent(MotionEvent event) {
        // determine what kind of touch event we have
        if (event.getActionMasked() == MotionEvent.ACTION_UP) {

            flags = gameView.getFlags();
            flagNumber = 0;

            int i, j;
            //i is columns (y coord). j is rows (x coord).
            for(i=0; i<=gridSize-1; i++) {
                //Comb rows.
                for(j=0; j<=gridSize-1; j++){
                    if(flags[i][j] == 1){
                        flagNumber++;
                    }
                }//for j inner loop.
            }//for i outer loop.

            Resources resources = getResources();
            String flag_number = resources.getQuantityString(R.plurals.flag_plural, flagNumber, flagNumber);

            TextView flag_text = (TextView) findViewById(R.id.tv_flags);
            flag_text.setText(flag_number);

            return true;
        }
        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead
        return super.onTouchEvent(event);
    }
}