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

    final private double gravity = 9.8;
    private double velocity = 0;
    private double velocityMultiplier = 0.14;
    private final double velocityMultiplierConstant = 0.14;
    private double velocityDivider = 0.3;
    private int velocityForceDivider = 75;
    private int gravityConstant = 1;

    private int pointerXLast = 0;
    private int pointerYLast = 0;
    private int pointerXCurrent = 0;
    private int pointerYCurrent = 0;
    private int pointerDiffX = 0;
    private int pointerDiffY = 0;
    private boolean isTouchedOnce = false;
    private boolean isInjectedForce = true;

    private Texture ball;

    private boolean isBallFlyingUp = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        defaultTile = new Texture("Tile001.JPG");
        ball = new Texture("ball.png");
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

                if (pointerDiffY < 0) {

                    isBallFlyingUp = true;
                    gravityConstant = -1;
                    velocity = (-pointerDiffY * 1.0 / velocityForceDivider);
                    velocityMultiplier = velocityMultiplierConstant;

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

        batch.draw(ball, ballSize, (int) ballPositionY, ballSize, ballSize);

        if (!isBallFlyingUp) {

            ballPositionY -= (gravity * velocity * gravityConstant);
            velocity += velocityMultiplier;

        } else {

            ballPositionY -= (gravity * velocity * gravityConstant);
            velocity -= velocityMultiplier;

            if (velocity < 0) {

                isBallFlyingUp = false;
                gravityConstant = 1;

            }

        }

        if (ballPositionY < tileSize) {

            ballPositionY = tileSize;
            gravityConstant = -1;

            if (velocity < 1) {

                isBallFlyingUp = false;
                velocity = 0;
                velocityMultiplier = 0;

            } else {

                isBallFlyingUp = true;
                velocity = velocity * (1.0 - velocityDivider);

            }


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
