package com.example.pingpong;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PongGameView extends View {
    // Game variables
    private int screenWidth, screenHeight;
    private int paddle1Y, paddle2Y, ballX, ballY, ballVelX, ballVelY;
    private int paddleWidth, paddleHeight, ballSize;
    private int player1Score, player2Score;
    private Paint paint;
    private boolean isAnimationRunning = true;


    public PongGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paddleWidth = 20;
        paddleHeight = 300;
        ballSize = 20;
        // Initialize starting positions and velocities here
        player1Score = -10;
        player2Score = -10;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenWidth = w;
        screenHeight = h;
        resetGame();
    }

    private void resetGame() {
        isAnimationRunning = false;
        paddle1Y = (screenHeight - paddleHeight) / 2;
        paddle2Y = (screenHeight - paddleHeight) / 2;
        ballX = screenWidth / 2;
        ballY = screenHeight / 2;
        ballVelX = -25; // Increase this value for faster horizontal speed
        ballVelY = -25; // Increase this value for faster vertical speed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        // Draw the middle line
        canvas.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight, paint);

        // Draw paddles and ball using the canvas and paint objects
        canvas.drawRect(0, paddle1Y, paddleWidth, paddle1Y + paddleHeight, paint);
        canvas.drawRect(screenWidth - paddleWidth, paddle2Y, screenWidth, paddle2Y + paddleHeight, paint);
        canvas.drawCircle(ballX, ballY, ballSize / 2, paint);

        // Draw scores at the center of the screen
        Paint textPaint = new Paint(paint);
        textPaint.setTextSize(60);

        String player1ScoreText = "Player 1: " + player1Score;
        String player2ScoreText = "Player 2: " + player2Score;

        float player1ScoreX = (screenWidth / 2) - textPaint.measureText(player1ScoreText) - 50;
        float player1ScoreY = 100;
        canvas.drawText(player1ScoreText, player1ScoreX, player1ScoreY, textPaint);

        float player2ScoreX = (screenWidth / 2) + 50;
        float player2ScoreY = 100;
        canvas.drawText(player2ScoreText, player2ScoreX, player2ScoreY, textPaint);

        if (isAnimationRunning) {
            invalidate(); // Keep redrawing the view while animation is running
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startAnimation();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // Update paddle1Y or paddle2Y based on touch position
            int touchedY = (int) event.getY();
            if (event.getX() < screenWidth / 2) {
                // Control the left paddle (paddle1Y)
                paddle1Y = touchedY - paddleHeight / 2;
            } else {
                // Control the right paddle (paddle2Y)
                paddle2Y = touchedY - paddleHeight / 2;
            }
            invalidate(); // Force redrawing the view
        }
        return true;
    }
    public void startAnimation() {
        isAnimationRunning = true;
        invalidate(); // Force redrawing the view
    }

    public void updateGame() {
        if (!isAnimationRunning) {
            return;
        }

        // Update ball position based on velocity
        ballX += ballVelX;
        ballY += ballVelY;

        // Handle collisions with the walls (vertical boundaries)
        if (ballY <= 0 || ballY >= screenHeight - ballSize) {
            ballVelY *= -1;
        }

        // Handle collisions with paddles
        if (ballX <= paddleWidth && ballY + ballSize >= paddle1Y && ballY <= paddle1Y + paddleHeight) {
            ballVelX *= -1;
            player1Score += 10;
        }
        if (ballX >= screenWidth - paddleWidth - ballSize && ballY + ballSize >= paddle2Y && ballY <= paddle2Y + paddleHeight) {
            ballVelX *= -1;
            player2Score += 10;
        }

        // Check for ball out of bounds (score point)
        if (ballX < 0) {
            // Player 2 scores
            player2Score = 0;
            player1Score = 0;
            if (player1Score == player2Score) {
                Toast.makeText(getContext(), "Match Draw!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Player 2 wins!", Toast.LENGTH_SHORT).show();
            }
            resetGame();
        } else if (ballX > screenWidth) {
            // Player 1 scores
            player1Score = 0;
            player2Score = 0;
            if (player1Score == player2Score) {
                Toast.makeText(getContext(), "Match Draw!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Player 1 wins!", Toast.LENGTH_SHORT).show();
            }            resetGame();
        }

        invalidate(); // Force redrawing the view
    }

}
