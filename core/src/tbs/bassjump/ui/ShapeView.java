package tbs.bassjump.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.objects.Player;


/**
 * Created by root on 3/01/15.
 */
public class ShapeView extends View {
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static int thickness = 12;
    private int[] points;
    private Player.PlayerShape playerShape;
    private int w, h, cx, cy, l, angleOffSet, initRotation, rotationStep;
    private boolean isStarShape;

    public ShapeView(Context context, Player.PlayerShape playerShape) {
        super(context);

        init(playerShape);
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(Player.PlayerShape.HEXAGON);
    }

    public void drawCircle(Canvas canvas) {
        canvas.drawCircle(cx, cy, l - (thickness / 2), paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (playerShape) {
            case CIRCLE:
                drawCircle(canvas);
                break;
            default:
                drawPolygon(canvas);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        init(playerShape);
    }


    private void init(Player.PlayerShape playerShape) {
        this.playerShape = playerShape;
        cx = w / 2;
        cy = h / 2;
        thickness = Math.min(w, h) / 12;
        l = (Math.min(w, h) / 2) - thickness;
        switch (playerShape) {
            case RECT:
                initRectAngle();
                setShapeRotation(-90);
                break;
            case TRIANGLE:
                initTriangle();
                setShapeRotation(-90);
                break;
            case PENTAGON:
                initPentagon();
                setShapeRotation(-90);
                break;
            case HEXAGON:
                initHexagon();
                setShapeRotation(-90);
                break;
            case SHURIKEN_STAR:
                initTriangleStar();
                setShapeRotation(-90);
                break;
            case PENTAGON_STAR:
                initPentagonStar();
                setShapeRotation(-90);
                break;
        }

        paint.setColor(Game.color);
        paint.setStrokeWidth(thickness);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
    }

    private void initRectAngle() {
        points = new int[8];
        initRotation = 45;
        rotationStep = 90;
        angleOffSet = 0;
    }

    private void initTriangle() {
        points = new int[6];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
    }

    private void initPentagon() {
        points = new int[10];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
    }

    private void initHexagon() {
        points = new int[12];
        initRotation = 30;
        rotationStep = 60;
        angleOffSet = 0;
    }


    private void initTriangleStar() {
        isStarShape = true;
        points = new int[12];
        initRotation = 90;
        rotationStep = 120;
        angleOffSet = 30;
    }

    private void initPentagonStar() {
        isStarShape = true;
        points = new int[20];
        initRotation = 0;
        rotationStep = 72;
        angleOffSet = 72;
    }


    public void setShapeRotation(double rotation) {
        if (points == null || points.length <= 5)
            return;
        rotation += angleOffSet;

        if (isStarShape) {
            final double firstStep = initRotation + rotation;
            final double halfStep = (rotationStep / 2);
            for (int i = 0; i < points.length; i += 4) {
                points[i] = cx + (int) (l * Math.cos(Math.toRadians(firstStep + (halfStep * i))));
                points[i + 1] = cy + (int) (l * Math.sin(Math.toRadians(firstStep + (halfStep * i))));

                points[i + 2] = cx + (int) ((l / 2) * Math.cos(Math.toRadians(firstStep + (halfStep * (i + 1)))));
                points[i + 3] = cy + (int) ((l / 2) * Math.sin(Math.toRadians(firstStep + (halfStep * (i + 1)))));
            }
        } else {
            for (int i = 0; i < points.length; i += 2) {
                points[i] = cx + (int) (l * Math.cos(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
                points[i + 1] = cy + (int) (l * Math.sin(Math.toRadians(initRotation + (rotationStep * i / 2) + rotation)));
            }
        }
    }

    public void drawPolygon(Canvas canvas) {
        for (int i = 0; i < points.length; i += 2) {
            canvas.drawLine(points[i], points[i + 1], points[(i + 2) % points.length], points[(i + 3) % points.length], paint);
        }
    }
}
