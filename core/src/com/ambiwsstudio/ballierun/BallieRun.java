package com.ambiwsstudio.ballierun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
    private double velocityDivider = 0.2;
    private int gravityConstant = 1;

    private Texture ball;

    private boolean isBallOnGround = false;
    private boolean isBallFlyingUp = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        defaultTile = new Texture("Tile001.JPG");
        ball = new Texture("ball.png");
    }

    private void renderEnvironment(SpriteBatch batch) {

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

            isBallFlyingUp = true;
            ballPositionY = tileSize;
            velocity = velocity * (1.0 - velocityDivider);
            gravityConstant = -1;

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
