package com.ambiwsstudio.ballierun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BallieRun extends ApplicationAdapter {

    /*
        Gdx variables
     */

    private SpriteBatch batch;
    private Texture background;
    private Texture defaultTile;
    private Texture ball;
    private Texture vineHorizontal;
    private Texture vineVertical;
    private Texture vineVerticalReversed;
    private Texture gameOver;

    /*
        Sizes & Points
     */

    private int tileSize = 256;
    private int ballSize = 128;
    private double ballPositionY = (tileSize * 1.0) + (ballSize * 5);
    private double ballPositionX;

    private static final int vineWidth = 160;
    private static final int vineHeight = 320;
    private static final int vineHWidth = 320;
    private static final int vineHHeight = 160;

    private int gameOverHeight = 600;
    private int gameOverWidth = 1000;
    private int gameOverDrawingX;
    private int gameOverDrawingY;

    private static final int buttonWidth = 500;
    private static final int buttonHeight = 60;

    private static final int restartButtonXFromGameOver = 250;
    private static final int restartButtonYFromGameOver = 300;

    private static final int menuButtonXFromGameOver = 250;
    private static final int menuButtonYFromGameOver = 390;

    private static final int quitButtonXFromGameOver = 250;
    private static final int quitButtonYFromGameOver = 480;

    /*
        Game variables (Physics & States)
     */

    private final double gravity = 9.8;
    private final double velocityMultiplierConstant = 0.14;
    private static final double velocityDivider = 0.3;
    private final double velocityXMultiplierConstant = 0.01;
    private static final int velocityXForceDivider = 75;
    private static final int velocityForceDivider = 75;
    private static final int maxVelocity = 10;
    private double velocity = 0;
    private double velocityMultiplier = 0.14;
    private int gravityConstant = 1;
    private double velocityX = 0;
    private double velocityXMultiplier = 0.01;
    private int gravityConstantX = 0;
    private double drawableX = 0;
    private static final double speed = 0.2;
    private int gameMode = 1;

    /*
        User-input variables & Booleans
     */

    private int pointerXLast = 0;
    private int pointerYLast = 0;

    @SuppressWarnings("FieldCanBeLocal")
    private static int pointerXCurrent = 0,
            pointerYCurrent = 0,
            pointerDiffX = 0,
            pointerDiffY = 0;

    private boolean isTouchedOnce = false;
    private boolean isInjectedForce = true;
    private boolean isBallForceUp = false;
    private boolean isBallForceSide = false;

    private void resetGameVariables() {

        ballPositionX = Gdx.graphics.getWidth() * 1.0 / 2 - (int)(ballSize * 1.0 / 2);
        ballPositionY = (tileSize * 1.0) + (ballSize * 5);

        velocity = 0;
        velocityMultiplier = velocityMultiplierConstant;
        gravityConstant = 1;
        velocityX = 0;
        velocityXMultiplier = velocityXMultiplierConstant;
        gravityConstantX = 0;

    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("Background.png");
        defaultTile = new Texture("Tile_2.png");
        ball = new Texture("ball.png");
        vineHorizontal = new Texture("vineh.png");
        vineVertical = new Texture("vinev.png");
        vineVerticalReversed = new Texture("vinevr.png");
        gameOver = new Texture("gameover.png");

        ballPositionX = Gdx.graphics.getWidth() * 1.0 / 2 - (int)(ballSize * 1.0 / 2);
        gameOverDrawingX = (int) ((Gdx.graphics.getWidth() * 1.0 / 2) - gameOverWidth / 2);
        gameOverDrawingY = (int) ((Gdx.graphics.getHeight() * 1.0 / 2) - gameOverHeight / 2);
    }

    private void renderEnvironment(SpriteBatch batch) {

        if (Gdx.input.isTouched()) {

            if (!isTouchedOnce) {

                isInjectedForce = false;
                pointerXLast = Gdx.input.getX();
                pointerYLast = Gdx.input.getY();

            }

            isTouchedOnce = true;

        } else {

            isTouchedOnce = false;

            if (!isInjectedForce) {

                isInjectedForce = true;

                pointerXCurrent = Gdx.input.getX();
                pointerYCurrent = Gdx.input.getY();

                pointerDiffX = pointerXLast - pointerXCurrent;
                pointerDiffY = pointerYLast - pointerYCurrent;

                if (pointerDiffX == 0 && pointerDiffY == 0)
                    return;

                velocityMultiplier = velocityMultiplierConstant;

                if (pointerDiffY < 0) {

                    isBallForceUp = true;
                    gravityConstant = -1;
                    velocity = (-pointerDiffY * 1.0 / velocityForceDivider);

                } else if (pointerDiffY > 0) {

                    isBallForceUp = false;
                    gravityConstant = 1;
                    velocity = velocity + (pointerDiffY * 1.0 / velocityForceDivider);

                } else {

                    isBallForceUp = false;
                    gravityConstant = 1;
                    velocity = 0;

                }

                if (pointerDiffX > 0) {

                    velocityX = (pointerDiffX * 1.0 / velocityXForceDivider);
                    gravityConstantX = 1;
                    velocityXMultiplier = velocityXMultiplierConstant;
                    isBallForceSide = true;

                }

                if (pointerDiffX < 0) {

                    velocityX = (-pointerDiffX * 1.0 / velocityXForceDivider);
                    gravityConstantX = -1;
                    velocityXMultiplier = velocityXMultiplierConstant;
                    isBallForceSide = true;

                }

                if (velocity > maxVelocity) {

                    velocity = maxVelocity;

                }

                if (velocityX > maxVelocity) {

                    velocityX = maxVelocity;

                }

            }

        }

        batch.draw(background, 0, 0);

        int vineHorI = 0;
        do {

            batch.draw(vineHorizontal, vineHorI, Gdx.graphics.getHeight() - (int)(vineHHeight * 1.0 / 2) + 5, vineHWidth, vineHHeight);
            vineHorI += vineHWidth - 20;

        } while (vineHorI < Gdx.graphics.getWidth());

        int vineI = 0;
        do {

            batch.draw(vineVertical, 0 - (int)(vineWidth * 1.0 / 3) - 10, vineI, vineWidth, vineHeight);
            batch.draw(vineVerticalReversed, Gdx.graphics.getWidth() - (int)(vineWidth * 1.0 / 2) - 15, vineI, vineWidth, vineHeight);
            vineI += vineHeight - 30;

        } while (vineI < Gdx.graphics.getHeight());


        if (drawableX >= tileSize)
            drawableX = 0;

        int i = 0;
        do {

            batch.draw(defaultTile, (int) (i * tileSize - drawableX), 0, tileSize, tileSize);

            if (gameMode == 1)
                drawableX += speed;

            if (velocity == 0) {

                if (velocityX <= 0.02) {

                    gravityConstantX = 1;
                    velocityX = 0.02;
                    ballPositionX -= (gravity * velocityX * gravityConstantX);

                }

            }

            i++;

        } while (i * tileSize - tileSize < Gdx.graphics.getWidth());

        if (gameMode == 0) {

            batch.draw(gameOver, gameOverDrawingX, gameOverDrawingY, gameOverWidth, gameOverHeight);

        }

        if (Gdx.input.justTouched()) {

            if (gameMode == 0) {

                if (isBtnClicked(restartButtonXFromGameOver, restartButtonYFromGameOver)) {

                    gameMode = 1;
                    resetGameVariables();

                }

                if (isBtnClicked(menuButtonXFromGameOver, menuButtonYFromGameOver)) {

                    gameMode = -1;
                    goToMenu();

                }

                if (isBtnClicked(quitButtonXFromGameOver, quitButtonYFromGameOver)) {

                    System.exit(0);
                    Gdx.app.exit();

                }

            }

        }

    }

    private void goToMenu() {



    }

    @SuppressWarnings("unused")
    private void drawRectangleOverBatch(SpriteBatch batch, int x, int y, int width, int height) {

        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(x, y, width, height);
        renderer.end();

        batch.begin();

    }

    private boolean isBtnClicked(int btnXFromGameOver, int btnYFromGameOver) {

        int btnXFrom = gameOverDrawingX + btnXFromGameOver;
        int btnXTo = gameOverDrawingX + btnXFromGameOver + buttonWidth;

        int btnYFrom = gameOverDrawingY + btnYFromGameOver;
        int btnYTo = gameOverDrawingY + btnYFromGameOver - buttonHeight;

        return ((Gdx.input.getX() >= btnXFrom)
                && (Gdx.input.getX() <= btnXTo))
                && ((Gdx.input.getY() <= btnYFrom)
                && (Gdx.input.getY() >= btnYTo));

    }

    private void renderBall(SpriteBatch batch) {

        if (gameMode == 1)
            batch.draw(ball, (int) ballPositionX, (int) ballPositionY, ballSize, ballSize);

        if (isBallForceSide) {

            if (velocity == 0) {

                velocityXMultiplier = 0.03;

            } else {

                velocityXMultiplier = velocityXMultiplierConstant;

            }

            ballPositionX += (gravity * velocityX * gravityConstantX);
            velocityX -= velocityXMultiplier;

            if (velocityX < 0) {

                isBallForceSide = false;
                velocityX = 0;
                velocityXMultiplier = 0;
                gravityConstantX = 0;

            }

        }

        if (ballPositionX > Gdx.graphics.getWidth() - ballSize) {

            ballPositionX = Gdx.graphics.getWidth() - ballSize;
            gravityConstantX = -1;

        }

        if (ballPositionX < 0) {

            ballPositionX = 0;
            gravityConstantX = 1;

        }


        if (!isBallForceUp) {

            ballPositionY -= (gravity * velocity * gravityConstant);
            velocity += velocityMultiplier;

        } else {

            ballPositionY -= (gravity * velocity * gravityConstant);
            velocity -= velocityMultiplier;

            if (velocity < 0) {

                isBallForceUp = false;
                gravityConstant = 1;

            }

        }

        if (ballPositionY < (tileSize * 1.0 / 2)) {

            ballPositionY = (tileSize * 1.0 / 2);
            gravityConstant = -1;

            if (velocity < 1.1) {

                isBallForceUp = false;
                velocity = 0;
                velocityMultiplier = 0;

            } else {

                isBallForceUp = true;
                velocity = velocity * (1.0 - velocityDivider);

            }

        }

        if (ballPositionY > Gdx.graphics.getHeight() - ballSize) {

            ballPositionY = Gdx.graphics.getHeight() - ballSize;
            gravityConstant = 1;
            isBallForceUp = false;
            velocity = velocity * (1.0 - velocityDivider);

        }

        if (ballPositionX < 48
                || (ballPositionX + ballSize > Gdx.graphics.getWidth() - 48)
                || (ballPositionY + ballSize > Gdx.graphics.getHeight() - 16)) {

            gameMode = 0;

        }

    }

    @Override
    public void render() {

        batch.begin();

        renderEnvironment(batch);
        renderBall(batch);

        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        ball.dispose();
        background.dispose();
    }
}
