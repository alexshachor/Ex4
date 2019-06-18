package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private final int ratio = 5;
    private ClientHandler clientHandler;

    private void setupDimensions() {
        this.centerX = getWidth() / 2;
        this.centerY = getHeight() / 2;
        this.baseRadius = Math.min(getWidth(), getHeight()) * 4 / 9;
        this.hatRadius = Math.min(getWidth(), getHeight()) / 5;
    }

    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        this.clientHandler = new ClientHandler();
    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setColor(Color.rgb(0x19, 0x1d, 0x51));
            colors.setStyle(Paint.Style.FILL);
            myCanvas.drawPaint(colors);

            //First determine the sin and cos of the angle that the touched point is at relative to the center of the joystick
            float hypotenuse = (float) Math.sqrt(Math.pow(newX - centerX, 2) + Math.pow(newY - centerY, 2));

            //Draw the base first before shading
            colors.setColor(Color.rgb(0xf4, 0x1a, 0x48));
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);

            //Drawing the joystick hat
            for (int i = 0; i <= (int) (hatRadius / ratio); i++) {
                colors.setARGB(255, (int) (i * (255 * ratio / hatRadius)), (int) (i * (255 * ratio / hatRadius)), 255); //Change the joystick color for shading purposes
                myCanvas.drawCircle(newX, newY, hatRadius - (float) i * (ratio) / 2, colors);
            }

            float sin;
            if ((sin = newY - centerY) != 0) {
                sin *= -1;
            }
            float cos = newX - centerX;
            if (hypotenuse != 0) {
                sin /= hypotenuse;
                cos /= hypotenuse;
            }

            double limit = 0.995;
            if (Math.abs(sin) > limit) {
                sin = Math.round(sin);
                cos = 0;
            }
            if (Math.abs(cos) > limit) {
                sin = 0;
                cos = Math.round(cos);
            }

            //draw the text
            colors.setColor(Color.rgb(255, 0, 0));
            colors.setTextSize(48);
            myCanvas.drawText("Aileron: " + Float.toString(cos), 20, 100, colors);
            myCanvas.drawText("Elevator: " + Float.toString(sin), 20, 40, colors);
            clientHandler.updateAileron(cos);
            clientHandler.updateElevator(sin);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    public boolean onTouch(View v, MotionEvent e) {
        if (v.equals(this)) {
            if (e.getAction() != e.ACTION_UP) {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if (displacement < (baseRadius - hatRadius)) {
                    drawJoystick(e.getX(), e.getY());
                    //joystickCallback.onJoystickMoved((e.getX() - centerX)/baseRadius, (e.getY() - centerY)/baseRadius, getId());
                } else {
                    float ratio = (baseRadius - hatRadius) / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                }
            } else {
                drawJoystick(centerX, centerY);
            }
        }
        return true;
    }
}