package com.ambiwsstudio.ballierun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

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
    private Texture menu;
    private Texture credits;
    private Texture aboutGame;
    private Texture tree;
    private Texture damagedTree;
    private Texture powerBallP;
    private Texture powerBallT;
    private Texture currentPowerBall;
    private Texture ballA;
    private Texture ballS;
    private Texture ballSkin;
    private Texture prizeTree;
    private BitmapFont font;
    private Rectangle ballRectangle = new Rectangle();
    private Rectangle treeRectangle = new Rectangle();
    private Rectangle powerBallRectangle = new Rectangle();
    private ArrayList<VineEnemy> vineEnemies = new ArrayList<>();
    private Music forest;
    private Music ballSound;
    private Timer timer;
    Preferences prefs;

    /*
        Sizes & Points
     */

    private int tileSize = 256;
    private int ballSize = 128;
    private double ballPositionY = (tileSize * 1.0) + (ballSize * 5);
    private double ballPositionX;

    private int treeSize = 1200;

    private final int vineWidth = 160;
    private final int vineHeight = 320;
    private final int vineHWidth = 320;
    private final int vineHHeight = 160;
    private final int vineWidthEnemy = 256;
    private final int vineHeightEnemy = 512;

    private int gameOverHeight = 600;
    private int gameOverWidth = 1000;
    private int gameOverDrawingX;
    private int gameOverDrawingY;

    private int creditsHeight = 900;
    private int creditsWidth = 1200;

    private int aboutGameHeight = 900;
    private int aboutGameWidth = 1200;

    private final int menuWidth = 600;
    private final int menuHeight = 750;
    private final int menuDrawingY = -220;
    private int menuDrawingX;

    private final int buttonWidth = 500;
    private final int buttonHeight = 60;

    private final int restartButtonXFromGameOver = 250;
    private final int restartButtonYFromGameOver = 300;

    private final int menuButtonXFromGameOver = 250;
    private final int menuButtonYFromGameOver = 390;

    private final int quitButtonXFromGameOver = 250;
    private final int quitButtonYFromGameOver = 480;

    private int startEasyGameButtonYFromMenu = menuDrawingY + menuHeight - 190;
    private int startEasyGameButtonXFromMenu;

    private int startHardGameButtonYFromMenu = menuDrawingY + menuHeight - 255;
    private int startHardGameButtonXFromMenu;

    private int creditsButtonYFromMenu = menuDrawingY + menuHeight - 325;
    private int creditsButtonXFromMenu;

    private int aboutAppButtonYFromMenu = menuDrawingY + menuHeight - 395;
    private int aboutAppButtonXFromMenu;

    private int quitButtonYFromMenu = menuDrawingY + menuHeight - 470;
    private int quitButtonXFromMenu;

    /*
        Game variables (Physics & States)
     */

    private final double gravity = 9.8;
    private final double velocityMultiplierConstant = 0.14;
    private final double velocityDivider = 0.3;
    private final double velocityXMultiplierConstant = 0.01;
    private final int velocityXForceDivider = 75;
    private final int velocityForceDivider = 75;
    private final int maxVelocity = 10;
    private double velocity = 0;
    private double velocityMultiplier = 0.14;
    private int gravityConstant = 1;
    private double velocityX = 0;
    private double velocityXMultiplier = 0.01;
    private int gravityConstantX = 0;
    private double drawableX = 0;
    private double drawableXEnvironment = 0;
    private double drawableXPrizeTree = 0;
    private double drawableXPowerBall = 0;
    private double speed = 0.5;
    private int gameMode = -1;
    private int lastGameMode = 0;
    private int treeDamage = 0;
    private int vineGeneratorLockTilesCount = 0;
    private int score = 0;
    private int scoreMultiplier = 1;
    private int ballMode = 0;
    private int powerBallRandY = 0;
    private String powerBallTip = "";

    private static class VineEnemy {

        Rectangle vineRectangle;
        double drawableXEnvironment = 0;
        boolean isFloorVine = true;

    }

    /*
        User-input variables & Booleans
     */

    private int pointerXLast = 0;
    private int pointerYLast = 0;

    @SuppressWarnings("FieldCanBeLocal")
    private int pointerXCurrent = 0,
            pointerYCurrent = 0,
            pointerDiffX = 0,
            pointerDiffY = 0;

    private boolean isTouchedOnce = false;
    private boolean isInjectedForce = true;
    private boolean isBallForceUp = false;
    private boolean isBallForceSide = false;
    private boolean isEnvironmentLifeTileActive = false;
    private boolean isTreeDamaged = false;
    private boolean isVinePreviouslyGenerated = false;
    private boolean isPowerBallTileActive = false;

    private void resetGameCommonVariables() {

        ball = ballSkin;

        ballMode = 0;
        drawableXPowerBall = 0;
        velocity = 0;
        velocityMultiplier = velocityMultiplierConstant;
        gravityConstant = 1;
        velocityX = 0;
        velocityXMultiplier = velocityXMultiplierConstant;
        gravityConstantX = 0;
        powerBallRandY = 0;

        pointerXLast = 0;
        pointerYLast = 0;
        pointerXCurrent = 0;
        pointerYCurrent = 0;
        pointerDiffX = 0;
        pointerDiffY = 0;

        powerBallTip = "";

        isPowerBallTileActive = false;
        isTouchedOnce = false;
        isInjectedForce = true;
        isBallForceSide = false;
        isBallForceUp = false;

    }

    private void resetGameVariables() {

        ballPositionX = Gdx.graphics.getWidth() * 1.0 / 2 - (int) (ballSize * 1.0 / 2);
        ballPositionY = (tileSize * 1.0) + (ballSize * 5);

        resetGameCommonVariables();

        drawableXEnvironment = 0;
        treeDamage = 0;
        score = 0;
        drawableX = 0;
        speed = 0.5;
        vineGeneratorLockTilesCount = 0;

        vineEnemies.clear();

        isEnvironmentLifeTileActive = false;
        isTreeDamaged = false;
        isVinePreviouslyGenerated = false;

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
        menu = new Texture("menu.png");
        credits = new Texture("credits.png");
        aboutGame = new Texture("aboutgame.png");
        tree = new Texture("Object_17.png");
        damagedTree = new Texture("Object_17d.png");
        powerBallP = new Texture("ballP.png");
        powerBallT = new Texture("ballT.png");
        ballA = new Texture("ballA.png");
        ballS = new Texture("ballS.png");
        prizeTree = new Texture("Object_16.png");

        prefs = Gdx.app.getPreferences("My Preferences");
        prefs.remove("ball");

        if (prefs.getString("ball", "D").equals("S")) {

            ballSkin = ballS;

        } else if (prefs.getString("ball", "D").equals("A")) {

            ballSkin = ballA;

        } else {

            ballSkin = ball;

        }

        ballPositionX = Gdx.graphics.getWidth() * 1.0 / 2 - (int) (ballSize * 1.0 / 2);
        gameOverDrawingX = (int) ((Gdx.graphics.getWidth() * 1.0 / 2) - gameOverWidth / 2);
        gameOverDrawingY = (int) ((Gdx.graphics.getHeight() * 1.0 / 2) - gameOverHeight / 2);
        menuDrawingX = Gdx.graphics.getWidth() - menuWidth;

        startEasyGameButtonYFromMenu = Gdx.graphics.getHeight() - startEasyGameButtonYFromMenu;
        startHardGameButtonYFromMenu = Gdx.graphics.getHeight() - startHardGameButtonYFromMenu;
        creditsButtonYFromMenu = Gdx.graphics.getHeight() - creditsButtonYFromMenu;
        aboutAppButtonYFromMenu = Gdx.graphics.getHeight() - aboutAppButtonYFromMenu;
        quitButtonYFromMenu = Gdx.graphics.getHeight() - quitButtonYFromMenu;

        startEasyGameButtonXFromMenu
                = startHardGameButtonXFromMenu
                = creditsButtonXFromMenu
                = aboutAppButtonXFromMenu
                = quitButtonXFromMenu
                = menuDrawingX + 50;

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);

        forest = Gdx.audio.newMusic(Gdx.files.internal("forest.wav"));
        forest.setLooping(true);
        forest.setVolume(0.2f);
        forest.play();

        ballSound = Gdx.audio.newMusic(Gdx.files.internal("ball.wav"));
        ballSound.setVolume(0.02f);

        timer = new Timer();

        currentPowerBall = ball;
    }

    private void renderEnvironment(SpriteBatch batch) {

        if (gameMode == 2 && score >= 20000) {

            if (!prefs.getString("ball", "D").equals("S")) {

                prefs.putString("ball", "A");
                prefs.flush();

                ball = ballA;
                ballSkin = ballA;

                isBallForceUp = true;
                gravityConstant = 0;
                gravityConstantX = 1;

                powerBallTip = "Thank You ;)";
                ballMode = 3;

                timer.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {

                        gameMode = -1;
                        resetGameVariables();

                        timer.stop();
                        timer = new Timer();

                    }
                }, 15f);

            }

        } else if (gameMode == 1 && score >= 30000) {

            prefs.putString("ball", "S");
            prefs.flush();

            ball = ballS;
            ballSkin = ballS;

            isBallForceUp = true;
            gravityConstant = 0;
            gravityConstantX = 1;

            powerBallTip = "Thank You ;)";
            ballMode = 3;

            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {

                    gameMode = -1;
                    resetGameVariables();

                    timer.stop();
                    timer = new Timer();

                }
            }, 15f);

        }

        if (ballMode != 3) {

            speed = 0.5 + (score * 1.0 / 10000);

        }

        if (Gdx.input.justTouched() && ballMode != 3) {

            if (ballMode == 2) {

                ballPositionX = Gdx.input.getX() - (int) (ballSize * 1.0 / 2);
                ballPositionY = Gdx.graphics.getHeight() - (Gdx.input.getY() + (int) (ballSize * 1.0 / 2));

                ballRectangle = new Rectangle((int) ballPositionX, (int) ballPositionY, ballSize, ballSize);

                velocityX = 0;
                velocityXMultiplier = velocityXMultiplierConstant;
                gravityConstantX = 0;

                velocity = 0;
                velocityMultiplier = velocityMultiplierConstant;
                isBallForceUp = false;
                gravityConstant = 1;

            }

        }

        if (ballMode != 2 && ballMode != 3) {

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

                    if (pointerDiffX == 0 && pointerDiffY == 0) {

                        return;

                    }

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

                    if (gameMode == 1 || gameMode == 2 || gameMode == -1)
                        ballSound.play();

                }

            }

        }

        batch.draw(background, 0, 0);

        if (ballMode == 3) {

            speed = 1;
            int currentXPrizeTreePosition = (int) (Gdx.graphics.getWidth() - drawableXPrizeTree);
            drawableXPrizeTree += (speed * 9);

            for (int i = 0; i < 3; i++) {

                batch.draw(prizeTree, currentXPrizeTreePosition + (i * treeSize + 128), (int) (tileSize * 1.0 / 2), treeSize, treeSize);

            }

        }

        if (drawableX >= tileSize) {

            drawableX = 0;
            vineGeneratorLockTilesCount++;
            score += (10 * scoreMultiplier);

            if (!isPowerBallTileActive) {

                double powerBallRandom = Math.random();

                if (powerBallRandom <= 0.025) {

                    isPowerBallTileActive = true;
                    drawableXPowerBall = 0;
                    currentPowerBall = powerBallP;
                    powerBallRandY = (int) (Math.random() * 900) + (tileSize / 2);

                } else if (powerBallRandom >= 0.975) {

                    isPowerBallTileActive = true;
                    drawableXPowerBall = 0;
                    currentPowerBall = powerBallT;
                    powerBallRandY = (int) (Math.random() * 900) + (tileSize / 2);

                }
            }

            if (!isEnvironmentLifeTileActive && Math.random() <= 0.2) {

                isEnvironmentLifeTileActive = true;
                drawableXEnvironment = 0;

            }

            if (!isVinePreviouslyGenerated) {

                if (vineEnemies.size() < 3 && ballMode != 3) {

                    double vineRandom = Math.random();

                    if (vineRandom <= 0.2) {

                        VineEnemy vineEnemy = new VineEnemy();
                        vineEnemy.isFloorVine = true;
                        vineEnemies.add(vineEnemy);
                        isVinePreviouslyGenerated = true;
                        vineGeneratorLockTilesCount = 0;

                    } else if (vineRandom >= 0.8) {

                        VineEnemy vineEnemy = new VineEnemy();
                        vineEnemy.isFloorVine = false;
                        vineEnemies.add(vineEnemy);
                        isVinePreviouslyGenerated = true;
                        vineGeneratorLockTilesCount = 0;

                    }

                }

            }

            if (vineGeneratorLockTilesCount >= 3 && ballMode != 3) {

                vineGeneratorLockTilesCount = 0;
                isVinePreviouslyGenerated = false;

            }

        }

        if (gameMode != 0 && ballMode != 3) {

            if (isEnvironmentLifeTileActive) {

                int currentXEnvironmentPosition = (int) (Gdx.graphics.getWidth() - drawableXEnvironment);

                treeRectangle = new Rectangle((int) (currentXEnvironmentPosition + (treeSize * 1.0 / 3) + 160), 0, (int) (treeSize * 1.0 / 5), Gdx.graphics.getHeight());

                drawableXEnvironment += (speed * 9);
                Texture currentTree = tree;

                if (currentXEnvironmentPosition < -(treeSize / 3) && !isTreeDamaged && ballPositionX > currentXEnvironmentPosition
                        && ballPositionX < (currentXEnvironmentPosition + treeSize - (treeSize * 1.0 / 3))) {

                    isEnvironmentLifeTileActive = false;
                    drawableXEnvironment = 0;
                    gameMode = 0;
                    resetGameVariables();

                } else if (isTreeDamaged && currentXEnvironmentPosition < -treeSize) {

                    isEnvironmentLifeTileActive = false;
                    drawableXEnvironment = 0;
                    isTreeDamaged = false;
                    treeDamage = 0;
                    score += (200 * scoreMultiplier);

                } else if (isTreeDamaged) {

                    currentTree = damagedTree;

                } else if (currentXEnvironmentPosition < -treeSize) {

                    isEnvironmentLifeTileActive = false;
                    drawableXEnvironment = 0;
                    isTreeDamaged = false;
                    treeDamage = 0;
                    score += (200 * scoreMultiplier);

                }

                batch.draw(currentTree, currentXEnvironmentPosition, (int) (tileSize * 1.0 / 2), treeSize, treeSize);

            }

            if (isPowerBallTileActive && ballMode == 0) {

                int currentXPowerBallPosition = (int) (Gdx.graphics.getWidth() - drawableXPowerBall);

                powerBallRectangle = new Rectangle(currentXPowerBallPosition, powerBallRandY, (int) (ballSize * 1.0 / 2), (int) (ballSize * 1.0 / 2));

                drawableXPowerBall += (speed * 9);
                batch.draw(currentPowerBall, currentXPowerBallPosition, powerBallRandY, (int) (ballSize * 1.0 / 2), (int) (ballSize * 1.0 / 2));

                if (currentXPowerBallPosition + (int) (ballSize * 1.0 / 2) < 0) {

                    isPowerBallTileActive = false;
                    drawableXPowerBall = 0;

                }

            } else if (ballMode != 0) {

                isPowerBallTileActive = true;
                drawableXPowerBall = -ball.getWidth();

            }

            if (vineEnemies.size() > 0) {

                ArrayList<VineEnemy> tempVineEnemies = vineEnemies;
                int idxToRemove = -1;

                for (int i = 0; i < tempVineEnemies.size(); i++) {

                    int currentXVinePosition;

                    if (tempVineEnemies.get(i).isFloorVine) {

                        currentXVinePosition = (int) (Gdx.graphics.getWidth() - tempVineEnemies.get(i).drawableXEnvironment);
                        batch.draw(vineVertical, currentXVinePosition, (int) (tileSize * 1.0 / 2) - 40, vineWidthEnemy, vineHeightEnemy);
                        vineEnemies.get(i).vineRectangle = new Rectangle(currentXVinePosition + 90, (int) (tileSize * 1.0 / 2) - 40, vineWidthEnemy - 180, vineHeightEnemy - 30);

                    } else {

                        currentXVinePosition = (int) (Gdx.graphics.getWidth() - tempVineEnemies.get(i).drawableXEnvironment);
                        batch.draw(vineVerticalReversed, currentXVinePosition, Gdx.graphics.getHeight() - vineHeightEnemy + 40, vineWidthEnemy, vineHeightEnemy);
                        vineEnemies.get(i).vineRectangle = new Rectangle(currentXVinePosition + 90, Gdx.graphics.getHeight() - vineHeightEnemy + 80, vineWidthEnemy - 180, vineHeightEnemy - 30);

                    }

                    tempVineEnemies.get(i).drawableXEnvironment += (speed * 9);

                    if (currentXVinePosition < -vineWidthEnemy) {

                        idxToRemove = i;

                    }

                }

                if (idxToRemove != -1) {

                    vineEnemies.remove(idxToRemove);
                    score += (75 * scoreMultiplier);

                }

            }

        }

        if (gameMode != -1 && gameMode != 2 && gameMode != 3 && gameMode != 4 && lastGameMode != 2 && ballMode != -3) {

            int vineHorI = 0;
            do {

                batch.draw(vineHorizontal, vineHorI, Gdx.graphics.getHeight() - (int) (vineHHeight * 1.0 / 2) + 5, vineHWidth, vineHHeight);
                vineHorI += vineHWidth - 20;

            } while (vineHorI < Gdx.graphics.getWidth());

            int vineI = 0;
            do {

                batch.draw(vineVertical, 0 - (int) (vineWidth * 1.0 / 3) - 10, vineI, vineWidth, vineHeight);
                batch.draw(vineVerticalReversed, Gdx.graphics.getWidth() - (int) (vineWidth * 1.0 / 2) - 15, vineI, vineWidth, vineHeight);
                vineI += vineHeight - 30;

            } while (vineI < Gdx.graphics.getHeight());

        }

        if (gameMode == 1 || gameMode == 2 || gameMode == 0) {

            if (ballMode != 3) {

                font.draw(batch, "Score: " + score, 100, Gdx.graphics.getHeight() - 100);

            } else {

                font.draw(batch, "Score: ---", 100, Gdx.graphics.getHeight() - 100);

            }

            font.draw(batch, powerBallTip, 100, Gdx.graphics.getHeight() - 200);
        }

        int i = 0;
        do {

            batch.draw(defaultTile, (int) (i * tileSize - drawableX), 0, tileSize, tileSize);

            if (gameMode == 1 || gameMode == 2)
                drawableX += speed;

            if (gameMode != -1 && gameMode != 0 && gameMode != 3 && gameMode != 4) {

                if (velocity == 0) {

                    if (velocityX <= speed / 10) {

                        gravityConstantX = 1;
                        velocityX = speed / 10;
                        ballPositionX -= (gravity * velocityX * gravityConstantX);

                    }

                }

            }

            i++;

        } while (i * tileSize - tileSize < Gdx.graphics.getWidth());

        if (gameMode == 0) {

            batch.draw(gameOver, gameOverDrawingX, gameOverDrawingY, gameOverWidth, gameOverHeight);

        }

        if (Gdx.input.justTouched()) {

            if (gameMode == 0) {

                if (isBtnClicked(restartButtonXFromGameOver, restartButtonYFromGameOver, gameOverDrawingX, gameOverDrawingY)) {

                    gameMode = lastGameMode;
                    resetGameVariables();

                }

                if (isBtnClicked(menuButtonXFromGameOver, menuButtonYFromGameOver, gameOverDrawingX, gameOverDrawingY)) {

                    gameMode = -1;
                    resetGameVariables();

                }

                if (isBtnClicked(quitButtonXFromGameOver, quitButtonYFromGameOver, gameOverDrawingX, gameOverDrawingY)) {

                    Gdx.app.exit();

                }

            } else if (gameMode == -1) {

                if (isBtnClicked(startEasyGameButtonXFromMenu, startEasyGameButtonYFromMenu)) {

                    gameMode = 2;
                    lastGameMode = 2;
                    scoreMultiplier = 2;
                    resetGameVariables();

                }

                if (isBtnClicked(startHardGameButtonXFromMenu, startHardGameButtonYFromMenu)) {

                    gameMode = 1;
                    lastGameMode = 1;
                    scoreMultiplier = 3;
                    resetGameVariables();

                }

                if (isBtnClicked(creditsButtonXFromMenu, creditsButtonYFromMenu)) {

                    gameMode = 3;

                }

                if (isBtnClicked(aboutAppButtonXFromMenu, aboutAppButtonYFromMenu)) {

                    gameMode = 4;

                }

                if (isBtnClicked(quitButtonXFromMenu, quitButtonYFromMenu)) {

                    Gdx.app.exit();

                }

            } else if (gameMode == 3 || gameMode == 4) {

                gameMode = -1;
                resetGameVariables();

            }

        }

        if (gameMode == -1) {

            batch.draw(menu, menuDrawingX, menuDrawingY, menuWidth, menuHeight);

        }

        if (gameMode == 3) {

            batch.draw(credits, (int) (Gdx.graphics.getWidth() * 1.0 / 2 - creditsWidth / 2), (int) (Gdx.graphics.getHeight() * 1.0 / 2 - creditsHeight / 2),
                    creditsWidth, creditsHeight);

        }

        if (gameMode == 4) {

            batch.draw(aboutGame, (int) (Gdx.graphics.getWidth() * 1.0 / 2 - aboutGameWidth / 2), (int) (Gdx.graphics.getHeight() * 1.0 / 2 - aboutGameHeight / 2),
                    aboutGameWidth, aboutGameHeight);

        }
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

    private boolean isBtnClicked(int btnXFromGameOver, int btnYFromGameOver, int sourceX, int sourceY) {

        int btnXFrom = sourceX + btnXFromGameOver;
        int btnXTo = sourceX + btnXFromGameOver + buttonWidth;

        int btnYFrom = sourceY + btnYFromGameOver;
        int btnYTo = sourceY + btnYFromGameOver - buttonHeight;

        return ((Gdx.input.getX() >= btnXFrom)
                && (Gdx.input.getX() <= btnXTo))
                && ((Gdx.input.getY() <= btnYFrom)
                && (Gdx.input.getY() >= btnYTo));

    }

    private boolean isBtnClicked(int btnXFromGameOver, int btnYFromGameOver) {

        int btnXTo = btnXFromGameOver + buttonWidth;

        int btnYTo = btnYFromGameOver - buttonHeight;

        return ((Gdx.input.getX() >= btnXFromGameOver)
                && (Gdx.input.getX() <= btnXTo))
                && ((Gdx.input.getY() <= btnYFromGameOver)
                && (Gdx.input.getY() >= btnYTo));

    }

    private void renderBall(SpriteBatch batch) {

        if (gameMode == 1
                || gameMode == -1
                || gameMode == 2) {

            ballRectangle = new Rectangle((int) ballPositionX, (int) ballPositionY, ballSize, ballSize);

            if (isPowerBallTileActive) {

                if (Intersector.overlaps(ballRectangle, powerBallRectangle)) {

                    ball = currentPowerBall;

                    if (currentPowerBall.equals(powerBallP)) {

                        ballMode = 1;
                        isPowerBallTileActive = true;
                        powerBallTip = "[Invincibility]";
                        timer.scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {

                                resetGameCommonVariables();

                                timer.stop();
                                timer = new Timer();

                            }
                        }, 15f);

                    } else if (currentPowerBall.equals(powerBallT)) {

                        ballMode = 2;
                        powerBallTip = "[Tap to teleport]";
                        timer.scheduleTask(new Timer.Task() {
                            @Override
                            public void run() {

                                resetGameCommonVariables();

                                timer.stop();
                                timer = new Timer();

                            }
                        }, 15f);

                    }

                }

            }

            batch.draw(ball, (int) ballPositionX, (int) ballPositionY, ballSize, ballSize);

        }

        if (isBallForceSide) {

            if (velocity == 0) {

                velocityXMultiplier = speed / 10;

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

        if (gameMode == -1) {

            Rectangle leftMenuWall = new Rectangle(menuDrawingX, menuDrawingY, 1, menuHeight);
            Rectangle upMenuCeil = new Rectangle(menuDrawingX + 16, menuDrawingY + menuHeight + 4, menuWidth, 4);

            if (Intersector.overlaps(upMenuCeil, ballRectangle)) {

                ballPositionY = menuDrawingY + menuHeight + 4;
                gravityConstant = -1;
                isBallForceUp = true;
                velocity = velocity * (1.0 - velocityDivider);

            }

            if (Intersector.overlaps(leftMenuWall, ballRectangle)) {

                ballPositionX = menuDrawingX - ballSize - 4;
                gravityConstantX = -1;

            }

        }

        if (ballPositionX > Gdx.graphics.getWidth() - ballSize) {

            ballPositionX = Gdx.graphics.getWidth() - ballSize;
            gravityConstantX = -1;

        }

        if (isEnvironmentLifeTileActive && ballMode != -3) {

            if (!isTreeDamaged && Intersector.overlaps(ballRectangle, treeRectangle)) {

                if (ballMode == 2) {

                    isEnvironmentLifeTileActive = false;
                    drawableXEnvironment = 0;
                    gameMode = 0;
                    resetGameVariables();

                } else if (ballMode == 1) {

                    isTreeDamaged = true;
                    treeDamage = 10;

                } else {

                    ballPositionX = treeRectangle.x - ballSize - 16;
                    gravityConstantX = -1;
                    //velocityX += (speed * 2);
                    treeDamage++;

                }

            }

            if (treeDamage >= 10) {

                isTreeDamaged = true;

            }

        }

        if (vineEnemies.size() > 0 && ballMode != -3) {

            for (VineEnemy enemy : vineEnemies) {

                if (Intersector.overlaps(ballRectangle, enemy.vineRectangle)) {

                    if (ballMode != 1) {

                        gameMode = 0;
                        resetGameVariables();
                        break;

                    }

                }

            }

            if (gameMode == 0)
                vineEnemies.clear();

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

        if (gameMode != -1 && gameMode != 2 && gameMode != 3 && gameMode != 4 && ballMode != -3) {

            if (ballPositionX < 48
                    || (ballPositionX + ballSize > Gdx.graphics.getWidth() - 48)
                    || (ballPositionY + ballSize > Gdx.graphics.getHeight() - 16)) {

                gameMode = 0;
                resetGameVariables();

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
        ball.dispose();
        defaultTile.dispose();
        ball.dispose();
        vineHorizontal.dispose();
        vineVertical.dispose();
        vineVerticalReversed.dispose();
        gameOver.dispose();
        menu.dispose();
        credits.dispose();
        background.dispose();
        aboutGame.dispose();
        tree.dispose();
        damagedTree.dispose();
        font.dispose();
        forest.dispose();
        powerBallP.dispose();
        powerBallT.dispose();
        currentPowerBall.dispose();
        ballS.dispose();
        ballA.dispose();
        ballSkin.dispose();
        prizeTree.dispose();

        System.exit(0);

    }

}
