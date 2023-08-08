package com.example.pingpong;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.pingpong.PongGameView;

public class MainActivity extends Activity {
    private PongGameView gameView;
    private Handler handler;
    private final int FRAME_RATE = 30; // Adjust the frame rate as desired

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new PongGameView(this, null);
        setContentView(gameView);

        handler = new Handler();
        // Start the game loop
        gameLoop.run();
    }

    private Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            gameView.updateGame();
            handler.postDelayed(this, 1000 / FRAME_RATE);
        }
    };

}
