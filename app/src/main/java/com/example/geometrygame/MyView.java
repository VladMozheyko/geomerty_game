package com.example.geometrygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    private Paint paint;

    // Игрок
    private final float playerX = 200;
    private float playerY;
    private final float playerSize = 100;
    private float velocityY = 0;
    private final float gravity = 2;
    private boolean isJumping = false;

    // Прыжок
    private int jumpCount = 0;
    private final int maxJumpCount = 2;

    // Пол
    private float groundY;

    // Препятствия
    private final int obstacleCount = 4;
    private final float[] obstacleX = new float[obstacleCount];
    private final float[] obstacleY = new float[obstacleCount];
    private final float[] obstacleWidth = new float[obstacleCount];
    private final float[] obstacleHeight = new float[obstacleCount];
    private final float obstacleSpeed = 15;

    // Состояние игры
    private boolean isGameOver = false;

    public MyView(Context context) {
        super(context);
        paint = new Paint();
        startGameLoop();
    }

    private void initObstacles() {
        for (int i = 0; i < obstacleCount; i++) {
            obstacleX[i] = 1200 + i * 600;
            generateObstacle(i);
        }
    }

    private void generateObstacle(int i) {
        obstacleWidth[i] = 80 + (float) (Math.random() * 120);   // 80–200
        obstacleHeight[i] = 80 + (float) (Math.random() * 300);  // 80–380
        obstacleY[i] = 200 + (float) (Math.random() * (groundY - 300));
    }

    private void startGameLoop() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                update();
                invalidate();
                startGameLoop();
            }
        }, 16);
    }

    private void update() {
        if (isGameOver) return;

        // Гравитация и прыжки
        if (isJumping) {
            velocityY += gravity;
            playerY += velocityY;

            if (playerY >= groundY - playerSize) {
                playerY = groundY - playerSize;
                velocityY = 0;
                isJumping = false;
                jumpCount = 0; // сброс прыжков
            }
        }

        // Препятствия
        for (int i = 0; i < obstacleCount; i++) {
            obstacleX[i] -= obstacleSpeed;

            if (obstacleX[i] + obstacleWidth[i] < 0) {
                obstacleX[i] = getWidth() + (float) (Math.random() * 500);
                generateObstacle(i);
            }

            if (checkCollision(obstacleX[i], obstacleY[i], obstacleWidth[i], obstacleHeight[i])) {
                isGameOver = true;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Фон — градиент
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, getHeight(),
                Color.rgb(0, 0, 30), Color.rgb(0, 30, 70),
                Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setShader(null);

        // Пол
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(0, groundY, getWidth(), getHeight(), paint);

        // Игрок
        paint.setColor(Color.GREEN);
        canvas.drawRect(playerX, playerY, playerX + playerSize, playerY + playerSize, paint);

        // Препятствия
        paint.setColor(Color.RED);
        for (int i = 0; i < obstacleCount; i++) {
            canvas.drawRect(obstacleX[i], obstacleY[i],
                    obstacleX[i] + obstacleWidth[i], obstacleY[i] + obstacleHeight[i], paint);
        }

        // Game Over
        if (isGameOver) {
            paint.setColor(Color.WHITE);
            paint.setTextSize(80);
            canvas.drawText("Game Over", getWidth() / 2f - 200, getHeight() / 2f, paint);
            paint.setTextSize(40);
            canvas.drawText("Нажми для рестарта", getWidth() / 2f - 150, getHeight() / 2f + 80, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                resetGame();
            } else if (jumpCount < maxJumpCount) {
                velocityY = -30;
                isJumping = true;
                jumpCount++;
            }
        }
        return true;
    }

    private boolean checkCollision(float obsX, float obsY, float obsW, float obsH) {
        return playerX < obsX + obsW &&
                playerX + playerSize > obsX &&
                playerY < obsY + obsH &&
                playerY + playerSize > obsY;
    }

    private void resetGame() {
        playerY = groundY - playerSize;
        velocityY = 0;
        isJumping = false;
        jumpCount = 0;
        isGameOver = false;

        for (int i = 0; i < obstacleCount; i++) {
            obstacleX[i] = getWidth() + i * 500;
            generateObstacle(i);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        groundY = getHeight() - 200;
        playerY = groundY - playerSize;
        initObstacles();
    }
}
