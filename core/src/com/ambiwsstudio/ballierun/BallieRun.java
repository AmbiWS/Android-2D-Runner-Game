package com.ambiwsstudio.ballierun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BallieRun extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture background;
    private Texture defaultTile;
    private int tileSize = 256;
    private int ballSize = 128;
    private double ballPositionY = (tileSize * 1.0) + (ballSize * 5);
    private double ballPositionX = ballSize;

    final private double gravity = 9.8;
    private double velocity = 0;
    private double velocityMultiplier = 0.14;
    private final double velocityMultiplierConstant = 0.14;
    private double velocityDivider = 0.3;
    private int velocityForceDivider = 75;
    private int gravityConstant = 1;

    private double velocityX = 0;
    private double velocityXMultiplier = 0.01;
    private final double velocityXMultiplierConstant = 0.01;
    private int gravityConstantX = 0;
    private int velocityXForceDivider = 75;

    private int pointerXLast = 0;
    private int pointerYLast = 0;
    private int pointerXCurrent = 0;
    private int pointerYCurrent = 0;
    private int pointerDiffX = 0;
    private int pointerDiffY = 0;
    final private int maxVelocity = 10;
    private boolean isTouchedOnce = false;
    private boolean isInjectedForce = true;

    private Texture ball;
    private Texture ball2;

    private boolean isBallForceUp = false;
    private boolean isBallForceSide = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("Background.png");
        defaultTile = new Texture("Tile_2.png");
        ball = new Texture("ball.png");
        ball2 = new Texture("ball2.png");
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

        int i = 0;
        do {

            batch.draw(defaultTile, i * tileSize, 0, tileSize, tileSize);
            i++;

        } while (i * tileSize < Gdx.graphics.getWidth());

    }

    private void renderBall(SpriteBatch batch) {

        batch.draw(ball, (int) ballPositionX, (int) ballPositionY, ballSize, ballSize);

        if (Gdx.input.isTouched()) {

            batch.draw(ball2, (int)(pointerXLast - (ballSize * 1.0 / 2)), (int)(Math.abs(Gdx.graphics.getHeight() - pointerYLast) - (ballSize * 1.0 / 2)), ballSize, ballSize);

        }

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

            if (velocityX < 0.3)
                velocityX = 0.3;

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
        background.dispose();
    }
}
