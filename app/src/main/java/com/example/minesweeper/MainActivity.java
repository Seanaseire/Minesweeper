package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button reset_btn;
    MineView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = (MineView) findViewById(R.id.view_minesweeper);

        reset_btn = (Button) findViewById(R.id.reset_btn);

        // add a click listener to the button
        reset_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                gameView.init();
            }
        });
    }
}