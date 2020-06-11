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
    private Preferences prefs;

    /*
        Sizes & Points (Initialized for main 1920W resolution)
     */

    private int deviceWidth;
    private int deviceHeight;

    private int tileSize = 256;
    private int ballSize = 128;
    private int treeSize = 1200;
    private int ballsToHeight = 5;
    private double ballPositionX;
    private double ballPositionY = (tileSize * 1.0) + (ballSize * ballsToHeight);
    private int sideDeathMargin = 48;
    private int upDeathMargin = 16;

    private int vineWidth = 160;
    private int vineHeight = 320;
    private int vineHWidth = 320;
    private int vineHHeight = 160;
    private int vineWidthEnemy = 256;
    private int vineHeightEnemy = 512;

    private int gameOverHeight = 600;
    private int gameOverWidth = 1000;
    private int gameOverDrawingX;
    private int gameOverDrawingY;

    private int creditsHeight = 900;
    private int creditsWidth = 1200;

    private int aboutGameHeight = 900;
    private int aboutGameWidth = 1200;

    private int menuWidth = 600;
    private int menuHeight = 750;
    private int menuDrawingY = -220;
    private int menuDrawingX;

    private int buttonWidth = 500;
    private int buttonHeight = 60;

    private int restartButtonXFromGameOver = 250;
    private int restartButtonYFromGameOver = 300;

    private int menuButtonXFromGameOver = 250;
    private int menuButtonYFromGameOver = 390;

    private int quitButtonXFromGameOver = 250;
    private int quitButtonYFromGameOver = 480;

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

    private float fontScale = 4;
    private int fontDrawX = 100;
    private int fontDrawY = 100;

    /*
        Game variables (Physics & States)
     */

    @SuppressWarnings("FieldCanBeLocal")
    private final int velocityXForceDivider = 75,
                        velocityForceDivider = 75,
                        maxVelocity = 10;

    @SuppressWarnings("FieldCanBeLocal")
    private final double velocityDivider = 0.3,
                        speedC = 0.5;

    @SuppressWarnings("FieldCanBeLocal")
    private int pointerXCurrent = 0,
            pointerYCurrent = 0,
            pointerDiffX = 0,
            pointerDiffY = 0;

    private final double gravity = 9.8;
    private final double velocityMultiplierConstant = 0.14;
    private final double velocityXMultiplierConstant = 0.01;
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
    private int treeSpeedMultiplier = 9;
    private int powerBallSpeedMultiplier = 9;
    private int vineSpeedMultiplier = 9;
    private int prizeTreeDistanceBetween = 128;
    private int treeRectangleOffset = 160;
    private int vineXOffset1 = 90;
    private int vineYOffset1 = 40;
    private int vineOffsetValue = 5;
    private int velocityXSpeedDivider = 10;
    private int menuOffsetValue = 4;
    private int treeOffsetValue = 16;
    private int menuOffset = 50;
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
        ballPositionY = (tileSize * 1.0) + (ballSize * ballsToHeight);

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

        deviceHeight = Gdx.graphics.getHeight();
        deviceWidth = Gdx.graphics.getWidth();

        /*
            Prize for finishing the game
         */

        prefs = Gdx.app.getPreferences("My Preferences");
        prefs.remove("ball");

        if (prefs.getString("ball", "D").equals("S")) {

            ballSkin = ballS;

        } else if (prefs.getString("ball", "D").equals("A")) {

            ballSkin = ballA;

        } else {

            ballSkin = ball;

        }

        /*
            Misc vars
         */

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(fontScale);

        float soundVolume = 0.2f;
        float ballVolume = 0.02f;

        forest = Gdx.audio.newMusic(Gdx.files.internal("forest.wav"));
        forest.setLooping(true);

        forest.setVolume(soundVolume);
        forest.play();

        currentPowerBall = ball;
        ballSound = Gdx.audio.newMusic(Gdx.files.internal("ball.wav"));
        ballSound.setVolume(ballVolume);

        timer = new Timer();

        /*
            Adaptive design
         */

        if (Gdx.graphics.getWidth() <= 800) {

            setupW800Graphics();

        } else if (Gdx.graphics.getWidth() <= 1280) {

            setupW1280Graphics();

        } else if (Gdx.graphics.getWidth() <= 1920) {

            setupW1920Graphics();

        } else if (Gdx.graphics.getWidth() <= 2560) {

            setupW2560Graphics();

        } else {

            System.exit(-1);

        }
    }

    private void setupW2560Graphics() {

        ballsToHeight = 4;
        ballPositionY = (tileSize * 1.0) + (ballSize * ballsToHeight);

        tileSize *= 1.5;
        ballSize *= 1.5;

        buttonWidth *= 1.5;
        buttonHeight *= 1.5;

        menuHeight *= 1.5;
        menuWidth *= 1.5;
        menuDrawingX = deviceWidth - menuWidth;
        menuDrawingY *= 1.5;

        startEasyGameButtonXFromMenu
                = startHardGameButtonXFromMenu
                = creditsButtonXFromMenu
                = aboutAppButtonXFromMenu
                = quitButtonXFromMenu
                = menuDrawingX + (int)(menuOffset * 1.5);

        startEasyGameButtonYFromMenu = 925;
        startHardGameButtonYFromMenu = 1030;
        creditsButtonYFromMenu = 1135;
        aboutAppButtonYFromMenu = 1240;
        quitButtonYFromMenu = 1345;

        creditsHeight *= 1.5;
        creditsWidth *= 1.5;

        aboutGameHeight *= 1.5;
        aboutGameWidth *= 1.5;

        gameOverHeight *= 1.5;
        gameOverWidth *= 1.5;
        gameOverDrawingX = (deviceWidth / 2) - (gameOverWidth / 2);
        gameOverDrawingY = (deviceHeight / 2) - (gameOverHeight / 2);

        restartButtonXFromGameOver = 370;
        restartButtonYFromGameOver = 460;

        menuButtonXFromGameOver = 370;
        menuButtonYFromGameOver = 590;

        quitButtonXFromGameOver = 370;
        quitButtonYFromGameOver = 730;

        treeSize *= 1.5;
        vineWidthEnemy *= 1.5;
        vineHeightEnemy *= 1.5;
        vineWidth *= 1.5;
        vineHeight *= 1.5;
        vineHWidth *= 1.5;
        vineHHeight *= 1.5;

        font.getData().setScale(fontScale *= 1.5);
        fontDrawX *= 1.5;
        fontDrawY *= 1.5;

        sideDeathMargin *= 1.5;
        upDeathMargin *= 1.5;

        prizeTreeDistanceBetween *= 1.5;
        treeRectangleOffset *= 1.5;

        vineYOffset1 *= 1.5;
        vineXOffset1 *= 1.5;
        vineOffsetValue *= 1.5;
        menuOffsetValue *= 1.5;
        treeOffsetValue *= 1.5;

        vineSpeedMultiplier = 8;
        treeSpeedMultiplier = 8;
        powerBallSpeedMultiplier = 8;

    }

    private void setupW1920Graphics() {

        /*
            Main resolution
         */

        startEasyGameButtonXFromMenu
                = startHardGameButtonXFromMenu
                = creditsButtonXFromMenu
                = aboutAppButtonXFromMenu
                = quitButtonXFromMenu
                = menuDrawingX + menuOffset;

        ballPositionX = Gdx.graphics.getWidth() * 1.0 / 2 - (int) (ballSize * 1.0 / 2);
        gameOverDrawingX = (int) ((Gdx.graphics.getWidth() * 1.0 / 2) - gameOverWidth / 2);
        gameOverDrawingY = (int) ((Gdx.graphics.getHeight() * 1.0 / 2) - gameOverHeight / 2);
        menuDrawingX = Gdx.graphics.getWidth() - menuWidth;

        startEasyGameButtonYFromMenu = Gdx.graphics.getHeight() - startEasyGameButtonYFromMenu;
        startHardGameButtonYFromMenu = Gdx.graphics.getHeight() - startHardGameButtonYFromMenu;
        creditsButtonYFromMenu = Gdx.graphics.getHeight() - creditsButtonYFromMenu;
        aboutAppButtonYFromMenu = Gdx.graphics.getHeight() - aboutAppButtonYFromMenu;
        quitButtonYFromMenu = Gdx.graphics.getHeight() - quitButtonYFromMenu;

    }

    private void setupW1280Graphics() {

        ballsToHeight = 4;
        ballPositionY = (tileSize * 1.0) + (ballSize * ballsToHeight);

        tileSize /= 1.5;
        ballSize /= 1.5;

        buttonWidth /= 1.5;
        buttonHeight /= 1.5;

        menuHeight /= 1.5;
        menuWidth /= 1.5;
        menuDrawingX = deviceWidth - menuWidth;
        menuDrawingY /= 1.5;

        startEasyGameButtonXFromMenu
                = startHardGameButtonXFromMenu
                = creditsButtonXFromMenu
                = aboutAppButtonXFromMenu
                = quitButtonXFromMenu
                = menuDrawingX + (int)((menuOffset / 2) * 1.5);

        startEasyGameButtonYFromMenu = 490;
        startHardGameButtonYFromMenu = 540;
        creditsButtonYFromMenu = 585;
        aboutAppButtonYFromMenu = 630;
        quitButtonYFromMenu = 680;

        creditsHeight /= 1.5;
        creditsWidth /= 1.5;

        aboutGameHeight /= 1.5;
        aboutGameWidth /= 1.5;

        gameOverHeight /= 1.5;
        gameOverWidth /= 1.5;
        gameOverDrawingX = (deviceWidth / 2) - (gameOverWidth / 2);
        gameOverDrawingY = (deviceHeight / 2) - (gameOverHeight / 2);

        restartButtonXFromGameOver = 165;
        restartButtonYFromGameOver = 200;

        menuButtonXFromGameOver = 165;
        menuButtonYFromGameOver = 260;

        quitButtonXFromGameOver = 165;
        quitButtonYFromGameOver = 325;

        treeSize /= 1.5;
        vineWidthEnemy /= 1.5;
        vineHeightEnemy /= 1.5;
        vineWidth /= 1.5;
        vineHeight /= 1.5;
        vineHWidth /= 1.5;
        vineHHeight /= 1.5;

        font.getData().setScale(fontScale /= 1.5);
        fontDrawX /= 1.5;
        fontDrawY /= 1.5;

        sideDeathMargin /= 1.5;
        upDeathMargin /= 1.5;

        prizeTreeDistanceBetween /= 1.5;
        treeRectangleOffset /= 1.5;

        vineYOffset1 /= 1.5;
        vineXOffset1 /= 1.5;
        vineOffsetValue /= 1.5;
        menuOffsetValue /= 1.5;
        treeOffsetValue /= 1.5;

        vineSpeedMultiplier = 8;
        treeSpeedMultiplier = 8;
        powerBallSpeedMultiplier = 8;
    }

    private void setupW800Graphics() {

        ballsToHeight = 4;
        ballPositionY = (tileSize * 1.0) + (ballSize * ballsToHeight);

        tileSize /= 2;
        ballSize /= 2;

        buttonWidth /= 2;
        buttonHeight /= 2;

        menuHeight /= 2;
        menuWidth /= 2;
        menuDrawingX = deviceWidth - menuWidth;
        menuDrawingY /= 2;

        startEasyGameButtonXFromMenu
                = startHardGameButtonXFromMenu
                = creditsButtonXFromMenu
                = aboutAppButtonXFromMenu
                = quitButtonXFromMenu
                = menuDrawingX + (menuOffset / 2);

        startEasyGameButtonYFromMenu = 310;
        startHardGameButtonYFromMenu = 345;
        creditsButtonYFromMenu = 380;
        aboutAppButtonYFromMenu = 415;
        quitButtonYFromMenu = 450;

        creditsHeight /= 2;
        creditsWidth /= 2;

        aboutGameHeight /= 2;
        aboutGameWidth /= 2;

        gameOverHeight /= 2;
        gameOverWidth /= 2;
        gameOverDrawingX = (deviceWidth / 2) - (gameOverWidth / 2);
        gameOverDrawingY = (deviceHeight / 2) - (gameOverHeight / 2);

        restartButtonXFromGameOver = 130;
        restartButtonYFromGameOver = 150;

        menuButtonXFromGameOver = 130;
        menuButtonYFromGameOver = 195;

        quitButtonXFromGameOver = 130;
        quitButtonYFromGameOver = 245;

        treeSize /= 2;
        vineWidthEnemy /= 2;
        vineHeightEnemy /= 2;
        vineWidth /= 2;
        vineHeight /= 2;
        vineHWidth /= 2;
        vineHHeight /= 2;

        font.getData().setScale(fontScale /= 2);
        fontDrawX /= 2;
        fontDrawY /= 2;

        sideDeathMargin /= 2;
        upDeathMargin /= 2;

        prizeTreeDistanceBetween /= 2;
        treeRectangleOffset /= 2;

        vineYOffset1 /= 2;
        vineXOffset1 /= 2;
        vineOffsetValue /= 2;
        menuOffsetValue /= 2;
        treeOffsetValue /= 2;

        vineSpeedMultiplier = 8;
        treeSpeedMultiplier = 8;
        powerBallSpeedMultiplier = 8;

    }

    private void renderEnvironment(SpriteBatch batch) {

        float winTimerDelay = 15f;
        int maxScoreToWinEasyMode = 20000;
        int maxScoreToWinHardMode = 30000;
        if (gameMode == 2 && score >= maxScoreToWinEasyMode) {

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
                }, winTimerDelay);

            }

        } else if (gameMode == 1 && score >= maxScoreToWinHardMode) {

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
            }, winTimerDelay);

        }

        if (ballMode != 3) {

            int scoreToSpeedDivider = 10000;
            speed = speedC + (score * 1.0 / scoreToSpeedDivider);

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

        batch.draw(background, 0, 0, deviceWidth, deviceHeight);

        if (ballMode == 3) {

            speed = speedC * 2;
            int currentXPrizeTreePosition = (int) (Gdx.graphics.getWidth() - drawableXPrizeTree);
            drawableXPrizeTree += (speed * treeSpeedMultiplier);

            for (int i = 0; i < 3; i++) {

                batch.draw(prizeTree, currentXPrizeTreePosition + (i * treeSize + prizeTreeDistanceBetween), (int) (tileSize * 1.0 / 2), treeSize, treeSize);

            }

        }

        if (drawableX >= tileSize) {

            drawableX = 0;
            vineGeneratorLockTilesCount++;
            int scoreC = 10;
            score += (scoreC * scoreMultiplier);

            if (!isPowerBallTileActive) {

                double powerBallRandom = Math.random();

                // Ball TP spawn - 2.5%
                // Ball Power spawn - 2.5%
                if (powerBallRandom <= 0.025) {

                    isPowerBallTileActive = true;
                    drawableXPowerBall = 0;
                    currentPowerBall = powerBallP;
                    powerBallRandY = (int) (Math.random() * (deviceHeight - tileSize)) + (tileSize / 2);

                } else if (powerBallRandom >= 0.975) {

                    isPowerBallTileActive = true;
                    drawableXPowerBall = 0;
                    currentPowerBall = powerBallT;
                    powerBallRandY = (int) (Math.random() * (deviceHeight - tileSize)) + (tileSize / 2);

                }
            }

            // Tree spawn - 20%
            if (!isEnvironmentLifeTileActive && Math.random() <= 0.2) {

                isEnvironmentLifeTileActive = true;
                drawableXEnvironment = 0;

            }

            if (!isVinePreviouslyGenerated) {

                if (vineEnemies.size() < 3 && ballMode != 3) {

                    double vineRandom = Math.random();

                    // Vine spawn bottom - 20%
                    // Vine spawn top - 20%
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

                treeRectangle = new Rectangle((int) (currentXEnvironmentPosition + (treeSize * 1.0 / 3) + treeRectangleOffset), 0, (int) (treeSize * 1.0 / 5), Gdx.graphics.getHeight());

                drawableXEnvironment += (speed * treeSpeedMultiplier);
                Texture currentTree = tree;

                int treeScoreReward = 200;
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
                    score += (treeScoreReward * scoreMultiplier);

                } else if (isTreeDamaged) {

                    currentTree = damagedTree;

                } else if (currentXEnvironmentPosition < -treeSize) {

                    isEnvironmentLifeTileActive = false;
                    drawableXEnvironment = 0;
                    isTreeDamaged = false;
                    treeDamage = 0;
                    score += (treeScoreReward * scoreMultiplier);

                }

                batch.draw(currentTree, currentXEnvironmentPosition, (int) (tileSize * 1.0 / 2), treeSize, treeSize);

            }

            if (isPowerBallTileActive && ballMode == 0) {

                int currentXPowerBallPosition = (int) (Gdx.graphics.getWidth() - drawableXPowerBall);

                powerBallRectangle = new Rectangle(currentXPowerBallPosition, powerBallRandY, (int) (ballSize * 1.0 / 2), (int) (ballSize * 1.0 / 2));

                drawableXPowerBall += (speed * powerBallSpeedMultiplier);
                batch.draw(currentPowerBall, currentXPowerBallPosition, powerBallRandY, (int) (ballSize * 1.0 / 2), (int) (ballSize * 1.0 / 2));

                if (currentXPowerBallPosition + (int) (ballSize * 1.0 / 2) < 0) {

                    isPowerBallTileActive = false;
                    drawableXPowerBall = 0;

                }

            } else if (ballMode != 0) {

                isPowerBallTileActive = true;
                drawableXPowerBall = -ballSize;

            }

            if (vineEnemies.size() > 0) {

                ArrayList<VineEnemy> tempVineEnemies = vineEnemies;
                int idxToRemove = -1;

                for (int i = 0; i < tempVineEnemies.size(); i++) {

                    int currentXVinePosition;

                    if (tempVineEnemies.get(i).isFloorVine) {

                        currentXVinePosition = (int) (Gdx.graphics.getWidth() - tempVineEnemies.get(i).drawableXEnvironment);
                        batch.draw(vineVertical, currentXVinePosition, (int) (tileSize * 1.0 / 2) - vineYOffset1, vineWidthEnemy, vineHeightEnemy);
                        vineEnemies.get(i).vineRectangle = new Rectangle(currentXVinePosition + vineXOffset1, (int) (tileSize * 1.0 / 2) - vineYOffset1, vineWidthEnemy - (vineXOffset1 * 2), vineHeightEnemy - (int)(vineXOffset1 * 1.0 / 3));

                    } else {

                        currentXVinePosition = (int) (Gdx.graphics.getWidth() - tempVineEnemies.get(i).drawableXEnvironment);
                        batch.draw(vineVerticalReversed, currentXVinePosition, Gdx.graphics.getHeight() - vineHeightEnemy + vineYOffset1, vineWidthEnemy, vineHeightEnemy);
                        vineEnemies.get(i).vineRectangle = new Rectangle(currentXVinePosition + vineXOffset1, Gdx.graphics.getHeight() - vineHeightEnemy + (vineYOffset1 * 2), vineWidthEnemy - (vineXOffset1 * 2), vineHeightEnemy - (int)(vineXOffset1 * 1.0 / 3));

                    }

                    tempVineEnemies.get(i).drawableXEnvironment += (speed * vineSpeedMultiplier);

                    if (currentXVinePosition < -vineWidthEnemy) {

                        idxToRemove = i;

                    }

                }

                if (idxToRemove != -1) {

                    vineEnemies.remove(idxToRemove);
                    int vineScoreReward = 75;
                    score += (vineScoreReward * scoreMultiplier);

                }

            }

        }

        if (gameMode != -1 && gameMode != 2 && gameMode != 3 && gameMode != 4 && lastGameMode != 2 && ballMode != -3) {

            int vineHorI = 0;
            do {

                batch.draw(vineHorizontal, vineHorI, Gdx.graphics.getHeight() - (int) (vineHHeight * 1.0 / 2) + vineOffsetValue, vineHWidth, vineHHeight);
                vineHorI += vineHWidth - (vineOffsetValue * 4);

            } while (vineHorI < Gdx.graphics.getWidth());

            int vineI = 0;
            do {

                batch.draw(vineVertical, 0 - (int) (vineWidth * 1.0 / 3) - (vineOffsetValue * 2), vineI, vineWidth, vineHeight);
                batch.draw(vineVerticalReversed, Gdx.graphics.getWidth() - (int) (vineWidth * 1.0 / 2) - (vineOffsetValue * 3), vineI, vineWidth, vineHeight);
                vineI += vineHeight - (vineOffsetValue * 6);

            } while (vineI < Gdx.graphics.getHeight());

        }

        if (gameMode == 1 || gameMode == 2 || gameMode == 0) {

            if (ballMode != 3) {

                font.draw(batch, "Score: " + score, fontDrawX, Gdx.graphics.getHeight() - fontDrawY);

            } else {

                font.draw(batch, "Score: ---", fontDrawX, Gdx.graphics.getHeight() - fontDrawY);

            }

            font.draw(batch, powerBallTip, fontDrawX, Gdx.graphics.getHeight() - (fontDrawY * 2));
        }

        int i = 0;
        do {

            batch.draw(defaultTile, (int) (i * tileSize - drawableX), 0, tileSize, tileSize);

            if (gameMode == 1 || gameMode == 2)
                drawableX += speed;

            if (gameMode != -1 && gameMode != 0 && gameMode != 3 && gameMode != 4) {

                if (velocity == 0) {

                    if (velocityX <= speed / velocityXSpeedDivider) {

                        gravityConstantX = 1;
                        velocityX = speed / velocityXSpeedDivider;
                        ballPositionX -= (gravity * velocityX * gravityConstantX);

                    }

                }

            }

            i++;

        } while (i * tileSize - tileSize < Gdx.graphics.getWidth());

        if (gameMode == 0) {

            batch.draw(gameOver, gameOverDrawingX, gameOverDrawingY, gameOverWidth, gameOverHeight);

        }

        /*  Function to test buttons position for adaptive design

            drawRectangleOverBatch(batch, gameOverDrawingX + menuButtonXFromGameOver, deviceHeight - (gameOverDrawingY + menuButtonYFromGameOver), buttonWidth, buttonHeight);

         */

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

                    float powerBallTimer = 15f;
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
                        }, powerBallTimer);

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
                        }, powerBallTimer);

                    }

                }

            }

            batch.draw(ball, (int) ballPositionX, (int) ballPositionY, ballSize, ballSize);

        }

        if (isBallForceSide) {

            if (velocity == 0) {

                velocityXMultiplier = speed / velocityXSpeedDivider;

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
            Rectangle upMenuCeil = new Rectangle(menuDrawingX + (menuOffsetValue * 4), menuDrawingY + menuHeight + menuOffsetValue, menuWidth, menuOffsetValue);

            if (Intersector.overlaps(upMenuCeil, ballRectangle)) {

                ballPositionY = menuDrawingY + menuHeight + (menuOffsetValue);
                gravityConstant = -1;
                isBallForceUp = true;
                velocity = velocity * (1.0 - velocityDivider);

            }

            if (Intersector.overlaps(leftMenuWall, ballRectangle)) {

                ballPositionX = menuDrawingX - ballSize - (menuOffsetValue);
                gravityConstantX = -1;

            }

        }

        if (ballPositionX > Gdx.graphics.getWidth() - ballSize) {

            ballPositionX = Gdx.graphics.getWidth() - ballSize;
            gravityConstantX = -1;

        }

        if (isEnvironmentLifeTileActive && ballMode != -3) {

            int treeDamageToKill = 10;
            if (!isTreeDamaged && Intersector.overlaps(ballRectangle, treeRectangle)) {

                if (ballMode == 2) {

                    isEnvironmentLifeTileActive = false;
                    drawableXEnvironment = 0;
                    gameMode = 0;
                    resetGameVariables();

                } else if (ballMode == 1) {

                    isTreeDamaged = true;
                    treeDamage = treeDamageToKill;

                } else {

                    ballPositionX = treeRectangle.x - ballSize - treeOffsetValue;
                    gravityConstantX = -1;
                    treeDamage++;

                }

            }

            if (treeDamage >= treeDamageToKill) {

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

            if (ballPositionX < sideDeathMargin
                    || (ballPositionX + ballSize > Gdx.graphics.getWidth() - sideDeathMargin)
                    || (ballPositionY + ballSize > Gdx.graphics.getHeight() - upDeathMargin)) {

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
