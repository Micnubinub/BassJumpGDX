package tbs.bassjump.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import tbs.bassjump.Game;
import tbs.bassjump.GameMode;
import tbs.bassjump.GameValues;
import tbs.bassjump.Utility;
import tbs.bassjump.levels.Level;
import tbs.bassjump.levels.Platform;
import tbs.bassjump.utility.GameObject;
import tbs.bassjump.view_lib.ViewPager;


public class Player extends GameObject {
    private static final Color c = new Color();
    // Color
    public static int paintIndex;
    public static PlayerShape playerShape;
    public static int paintTrailOffset;
    private static int[] points;
    // Michael's quick fix
    private static int cx, cy, l, angleOffSet, initRotation, rotationStep;
    private static double playerJumpDistance, playerJumpPercentage;
    // TMP till final fix
    private static int xOffset;
    private static boolean isStarShape;
    // PARTICLES
    public final ArrayList<Particle> splashParticles1;
    public final ArrayList<Particle> splashParticles2;
    // MOVEMENT
    public boolean moving;
    public boolean canMove; // MAKE LEVEL MOVE INSTEAD
    public boolean goingRight; // 0 = LEFT, 1 = RIGHT
    // STATE
    public PlayerState state;
    // SCORE
    public int score;
    public int highScoreA; // ARCADE
    public int highScoreR; // RECRUIT
    public int highScoreU; // ULTRA
    public int highScoreS; // SINGUL
    public int highScoreS2; // SPEED
    public int gamesPlayed; // GAMES PLAYED
    public int tmpCoins; // COINS WHEN PLAYING ONLY
    // SPECIAL ACHIEVEMENTS
    public int gamesHund; // Games over 100;
    // PAINT TRAIL
    public ArrayList<PaintParticle> paintTrail;
    // OTHER:
    int speed;
    int right1;
    int right2;
    int bottom1;
    int bottom2;

    public Player() {
        Utility.print("Player Initialized");
        splashParticles1 = new ArrayList<Particle>();
        for (int i = 0; i < 12; i++) {
            splashParticles1.add(new Particle());
        }
        splashParticles2 = new ArrayList<Particle>();
        for (int i = 0; i < 12; i++) {
            splashParticles2.add(new Particle());
        }
        playerJumpDistance = Game.w - (GameValues.PLATFORM_WIDTH * 2)
                - GameValues.PLAYER_SCALE;
        Utility.equipShape(Utility.getEquippedShape());
    }

    public static void setPlayerShape(PlayerShape playerShape) {
        Player.playerShape = playerShape;
        l = Math.round((GameValues.PLAYER_SCALE / 2) / 0.7071f)
                - ((GameValues.PAINT_THICKNESS + 16) / 2);
        xOffset = 0;
        switch (playerShape) {
            case RECT:
                initRectAngle();
                break;
            case TRIANGLE:
                initTriangle();
                break;
            case PENTAGON:
                initPentagon();
                break;
            case HEXAGON:
                initHexagon();
                break;
            case CIRCLE:
                // l = (GameValues.PLAYER_SCALE / 2) - GameValues.PAINT_THICKNESS;
                break;
            case SHURIKEN_STAR:
                initShurikenStar();
                break;
            case PENTAGON_STAR:
                initPentagonStar();
                break;
        }
        //todo paint.setStrokeWidth((GameValues.PAINT_THICKNESS + 16));
    }

    private static void initRectAngle() {
        isStarShape = false;
        points = new int[8];
        initRotation = 45;
        rotationStep = 90;
        angleOffSet = 0;
        // calcPolyL();
    }

    private static void initTriangle() {
        isStarShape = false;
        points = new int[6];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
        // calcPolyL();
        xOffset = (int) (GameValues.PLAYER_SCALE * 0.1f);
    }

    private static void initPentagon() {
        isStarShape = false;
        points = new int[10];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
        // calcPolyL();
    }

    // private static void calcPolyL() {
    // final int n = points.length / 2;
    // final int cosAngle = ((n - 2) * 180) / (n * 2);
    // l = (int) ((GameValues.PLAYER_SCALE * 0.5f) /
    // Math.cos(Math.toRadians(cosAngle)));
    // }

    private static void initShurikenStar() {
        isStarShape = true;
        points = new int[12];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
        l = Math.round((GameValues.PLAYER_SCALE / 2));
        xOffset = (int) (GameValues.PLAYER_SCALE * 0.1f);
    }

    private static void initPentagonStar() {
        isStarShape = true;
        points = new int[20];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
    }

    private static void initHexagon() {
        isStarShape = false;
        points = new int[12];
        initRotation = 30;
        rotationStep = 60;
        angleOffSet = 0;
        // calcPolyL();
    }

    public static void setShapeRotation(double rotation) {
        if (points == null || points.length <= 5)
            return;
        rotation += angleOffSet;

        if (isStarShape) {
            for (int i = 0; i < points.length; i += 4) {
                points[(i) % points.length] = cx
                        + (int) (l * Math.cos(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation)));
                points[(i + 1) % points.length] = cy
                        + (int) (l * Math.sin(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation)));

                points[(i + 2) % points.length] = cx
                        + (int) ((l / 3) * Math.cos(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation
                        + (rotationStep / 2))));
                points[(i + 3) % points.length] = cy
                        + (int) ((l / 3) * Math.sin(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation
                        + (rotationStep / 2))));
            }
        } else {
            for (int i = 0; i < points.length; i += 2) {
                points[(i) % points.length] = cx
                        + (int) (l * Math.cos(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation)));
                points[(i + 1) % points.length] = cy
                        + (int) (l * Math.sin(Math.toRadians(initRotation
                        + (rotationStep * i / 2) + rotation)));
            }
        }
    }

    public static void circle(ShapeRenderer canvas) {
        canvas.circle(cx, cy, l - ((GameValues.PAINT_THICKNESS + 16) / 2));
    }

    @Override
    public void setup() {
        super.setup();
        // MOVEMENT
        moving = false; // Initiated by Controller to make true
        canMove = true;
        goingRight = true; // GOING RIGHT

        // LOCATION & SCALE
        scale = GameValues.PLAYER_SCALE;
        yPos = Game.h; // START OFF SCREEN
        xPos = GameValues.PLATFORM_WIDTH;

        // OTHER
        state = PlayerState.ON_GROUND;
        score = 0;

        // PAINT:
        paintIndex = 0;
        paintTrail = new ArrayList<PaintParticle>();
        for (int i = 0; i < 8; ++i) {
            paintTrail.add(new PaintParticle());
        }
        activatePaint(true);

        Utility.equipShape(Utility.getEquippedShape());
        tmpCoins = 0;
    }

    public void update() {
        getXCenter();
        getYCenter();

        // STARTING ANIMATION
        if (yPos > GameValues.PLAYER_POSITION) {
            yPos -= GameValues.SPEED_FACTOR / 3;
            if (yPos < GameValues.PLAYER_POSITION)
                yPos = GameValues.PLAYER_POSITION;
        }

        // PAINT
        for (int i = 0; i < paintTrail.size(); ++i) {
            if (paintTrail.get(i).active) {
                speed = GameValues.SPEED_FACTOR;
                if (Game.mode == GameMode.Recruit) { // SLOW
                    speed /= 1.5f;
                    if (speed < 1)
                        speed = 1;
                } else if (Game.mode == GameMode.Ultra) { // ULTRA FAST
                    speed *= 1.35f;
                }
                paintTrail.get(i).yPos += speed;
            }
        }

        // PARTICLES:
        for (int i = 0; i < splashParticles1.size(); i++) {
            splashParticles1.get(i).update();
        }
        for (int i = 0; i < splashParticles2.size(); i++) {
            splashParticles2.get(i).update();
        }

        switch (state) {
            case ON_GROUND:
                if (!isAlive(false)) {
                    startDying();
                } else if (canPaint()) {
                    speed = GameValues.SPEED_FACTOR;
                    if (Game.mode == GameMode.Recruit) { // SLOW
                        speed /= 1.5f;
                        if (speed < 1)
                            speed = 1;
                    } else if (Game.mode == GameMode.Ultra) { // ULTRA FAST
                        speed *= 1.35f;
                    }
                    paintTrail.get(paintIndex).height += speed;
                    paintTrail.get(paintIndex).yPos = (yPos + (scale / 11));
                }

                break;
            case DYING:
                if (goingRight) {
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos + scale < -GameValues.DEATH_GAP) {
                        deathActual();
                        Game.setupGame();
                    }
                } else {
                    xPos += GameValues.PLAYER_JUMP_SPEED;
                    if (xPos > Game.w + GameValues.DEATH_GAP) {
                        deathActual();
                        Game.setupGame();
                    }
                }

                break;
            case JUMPING:
                if (goingRight) { // RIGHT
                    xPos += GameValues.PLAYER_JUMP_SPEED;
                    if ((xPos + scale) >= (Game.w - GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Game.w / 2));
                        if (isAlive(true))
                            land(true);
                        else
                            startDying();
                    }
                } else { // LEFT
                    xPos -= GameValues.PLAYER_JUMP_SPEED;
                    if (xPos <= (GameValues.PLATFORM_WIDTH)) {
                        goingRight = (xPos < (Game.w / 2));
                        if (isAlive(true))
                            land(false);
                        else
                            startDying();
                    }
                }
                break;
        }
        playerJumpPercentage = (xPos - GameValues.PLATFORM_WIDTH)
                / playerJumpDistance;
    }

    public void startDying() {
        state = PlayerState.DYING;
    }

    public void deathActual() {

        // SCORE
        if (Game.mode == GameMode.Arcade) {
            if (highScoreA < score) {
                Utility.saveInt("hScore", score);
                highScoreA = score;
            }
        } else if (Game.mode == GameMode.Recruit) {
            if (highScoreR < score) {
                Utility.saveInt("hScoreR", score);
                highScoreR = score;
            }
        } else if (Game.mode == GameMode.Ultra) {
            if (highScoreU < score) {
                Utility.saveInt("hScoreU", score);
                highScoreU = score;
            }
        } else if (Game.mode == GameMode.Singularity) {
            if (highScoreS < score) {
                Utility.saveInt("hScoreS", score);
                highScoreS = score;
            }
        } else if (Game.mode == GameMode.SpeedRunner) {
            if (highScoreS2 < score) {
                Utility.saveInt("hScoreS2", score);
                highScoreS2 = score;
            }
        }

        // PLAYED AND DEATHS
        if (Utility.getString("gPlayed") != null) {
            gamesPlayed = Utility.getInt("gPlayed") + 1;
        } else {
            gamesPlayed = 0;
        }
        Utility.saveInt("gPlayed", gamesPlayed);

        if (score > 100 && Game.mode == GameMode.Arcade) {
            gamesHund += 1;
            if (gamesHund >= 10) {
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDg");
            }
        } else if (Game.mode == GameMode.Arcade) {
            gamesHund = 0;
        }

        // SAVE COINS
        Utility.saveCoins(Utility.getCoins()
                + tmpCoins);
        ViewPager.setNumCoins(Utility.getCoins());
    }

    public boolean isAlive(boolean j) {
        boolean fine = false;
        if (goingRight) {
            for (Platform p : Game.level.platformsRight) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
            }
        } else {
            for (Platform p : Game.level.platformsLeft) {
                if (yPos + scale >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
                if (yPos >= p.yPos
                        && yPos <= p.yPos + GameValues.PLATFORM_HEIGHT * 1.15f) {
                    fine = true;
                    p.landedOn = true;
                }
            }
        }
        return fine;
    }

    public boolean canPaint() {
        boolean fine = false;
        if (goingRight) {
            for (Platform p : Game.level.platformsRight) {
                if (yPos >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                }
            }
        } else {
            for (Platform p : Game.level.platformsLeft) {
                if (yPos >= p.yPos
                        && yPos + scale <= p.yPos + GameValues.PLATFORM_HEIGHT
                        * 1.15f) {
                    fine = true;
                }
            }
        }
        return fine;
    }

    public void jump() {
        if (state == PlayerState.ON_GROUND) {
            state = PlayerState.JUMPING;
        }

        activatePaint(false);
    }

    public void activatePaint(boolean start) {
        // PAINT ACTIVATE:
        int moveIndex = 0;
        if (!(paintIndex == paintTrail.size() - 1)) {
            moveIndex = paintIndex + 1;
        }
        paintTrail.get(moveIndex).active = true;
        paintTrail.get(moveIndex).height = 0;
        paintTrail.get(moveIndex).yPos = yPos;
        if (!start) {
            if (!goingRight) {
                paintTrail.get(moveIndex).xPos = GameValues.PLATFORM_WIDTH
                        - GameValues.PAINT_THICKNESS;
            } else {
                paintTrail.get(moveIndex).xPos = (Game.w - GameValues.PLATFORM_WIDTH);
            }
        } else {
            paintTrail.get(moveIndex).xPos = GameValues.PLATFORM_WIDTH
                    - GameValues.PAINT_THICKNESS;
        }
        paintIndex = moveIndex;
    }

    public void land(boolean right) {
        int coinPluser = 1;
        Game.scoreTextMult = 1.5f;
        Game.alphaM = 255; // BLITZ
        score += 1;

        // COIN BONUS:
        if (score >= 50) {
            coinPluser += 1;
        }
        if (score >= 100) {
            coinPluser += 2;
        }
        if (score >= 200) {
            coinPluser += 4;
        }
        tmpCoins += coinPluser;

        // if (score % 5 == 0)
        Game.color = Game.colors[Utility.randInt(0, Game.colors.length - 1)];
        state = PlayerState.ON_GROUND;
        if (right) {
            xPos = ((Game.w - GameValues.PLATFORM_WIDTH) - scale);
        } else {
            xPos = (GameValues.PLATFORM_WIDTH);
        }
        if (score % 2 == 0)
            GameValues.SPEED_BONUS += .005f;

        if (score % 18 == 0) {
            Level.powerCountdown = 4;
        }
        // / PARTICLES:
        showParticles();
        Game.showCircle(scale, getXCenter(), getYCenter(), 160, false);

        // TEXT
        if (right)
            Game.showAnimatedText("+" + coinPluser, xPos, getYCenter(),
                    (Game.h / 100), 12, 255, 0);
        else
            Game.showAnimatedText("+" + coinPluser, xPos
                            + GameValues.PLAYER_SCALE, getYCenter(),
                    (Game.h / 100), 12, 255, 0);
    }

    public int getXCenter() {
        cx = xPos + (scale / 2);
        return cx;
    }

    public int getYCenter() {
        cy = yPos + (scale / 2);
        return cy;
    }

    public boolean IsInBox(int x1, int y1, int width1, int height1, int x2,
                           int y2, int width2, int height2) {
        right1 = x1 + width1;
        right2 = x2 + width2;
        bottom1 = y1 + height1;
        bottom2 = y2 + height2;

        // Check if top-left point is in box chexk && y2 >= y2
        if (x2 >= x1 && x2 <= right1 && y2 <= bottom1)
            return true;
        // Check if bottom-right point is in box
        return (right2 >= x1 && right2 <= right1 && bottom2 >= y2 && bottom2 <= bottom1);
    }

    public void showParticles() {
        if (goingRight) {
            for (int i = 0; i < splashParticles1.size(); i++) {
                splashParticles1.get(i).setup(xPos, getYCenter(), true);
            }
        } else {
            for (int i = 0; i < splashParticles2.size(); i++) {
                splashParticles2.get(i)
                        .setup(xPos + scale, getYCenter(), false);
            }
        }
    }

    public void drawPolygon(ShapeRenderer canvas) {
        if (points == null || points.length <= 5)
            return;
        for (int i = 0; i < points.length; i += 2) {
            // canvas.drawLine(points[i], points[i + 1], points[(i + 2)
            // % points.length], points[(i + 3) % points.length], paint);
            canvas.line(goingRight ? points[i] - xOffset : points[i]
                            + xOffset, points[i + 1], goingRight ? points[(i + 2)
                            % points.length]
                            - xOffset : points[(i + 2) % points.length] + xOffset,
                    Game.h - points[(i + 3) % points.length]);

        }
    }

    public void draw(ShapeRenderer canvas) {
        setShapeRotation(playerJumpPercentage * 180);
        c.set(Game.color);
//        canvas.rect(xPos, yPos, scale, scale);
        switch (playerShape) {
            case CIRCLE:
                circle(canvas);
                break;
            default:
                drawPolygon(canvas);
                break;
        }

        // DRAW GLOW:
        if (Game.alphaM > 0) {
            c.set(0xffe5e4a0);
            c.a = (Game.alphaM / 255f);
            switch (playerShape) {
                case CIRCLE:
                    circle(canvas);
                    break;
                default:
                    drawPolygon(canvas);
                    break;
            }
        }
    }

    public enum PlayerShape {
        CIRCLE, RECT, TRIANGLE, HEXAGON, PENTAGON, SHURIKEN_STAR, PENTAGON_STAR
    }
}
