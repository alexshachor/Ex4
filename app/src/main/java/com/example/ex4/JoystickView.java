package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;
    private final int ratio = 5; //The smaller, the more shading will occur
    private float sin, cos;

    private void setupDimensions()
    {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight())*4/9;
        hatRadius = Math.min(getWidth(), getHeight()) / 6;
    }

    public JoystickView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context, AttributeSet attributes, int style)
    {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView (Context context, AttributeSet attributes)
    {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    private void drawJoystick(float newX, float newY, boolean bool)
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setColor(Color.rgb(0x19, 0x1d, 0x51));
            colors.setStyle(Paint.Style.FILL);
            myCanvas.drawPaint(colors);

            //First determine the sin and cos of the angle that the touched point is at relative to the center of the joystick
            float hypotenuse = (float) Math.sqrt(Math.pow(newX - centerX, 2) + Math.pow(newY - centerY, 2));
            sin = (newY - centerY) / hypotenuse; //sin = o/h
            cos = (newX - centerX) / hypotenuse; //cos = a/h

            //Draw the base first before shading
            colors.setColor(Color.rgb(0xf4,0x1a,0x48));
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);

            //Drawing the joystick hat
            for(int i = 0; i <= (int) (hatRadius / ratio); i++) {
                colors.setARGB(255, (int) (i * (255 * ratio / hatRadius)), (int) (i * (255 * ratio / hatRadius)), 255); //Change the joystick color for shading purposes
                myCanvas.drawCircle(newX, newY, hatRadius - (float) i * (ratio) / 2 , colors);
            }

            if (sin > 0.99) {
                sin = 1;
                cos = 0;
            } else if (sin < -0.99) {
                sin = -1;
                cos = 0;
            }
            if (cos > 0.99) {
                cos = 1;
                sin = 0;
            } else if (cos < -0.99) {
                cos = -1;
                sin = 0;
            }

            //draw the text
            colors.setColor(Color.rgb(255,0,0));
            colors.setTextSize(48);
            myCanvas.drawText("Aileron: " + Float.toString(cos), 20, 100, colors);
            myCanvas.drawText("Elevator: " + Float.toString(sin), 20, 40, colors);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        setupDimensions();
        drawJoystick(centerX, centerY, false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setupDimensions();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouch(View v, MotionEvent e)
    {
        if(v.equals(this))
        {
            if(e.getAction() != e.ACTION_UP)
            {
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));
                if(displacement < (baseRadius - hatRadius))
                {
                    drawJoystick(e.getX(), e.getY(), false);
                    //joystickCallback.onJoystickMoved((e.getX() - centerX)/baseRadius, (e.getY() - centerY)/baseRadius, getId());
                }
                else
                {
                    float ratio = (baseRadius - hatRadius) / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY, true);

                    //joystickCallback.onJoystickMoved((constrainedX-centerX)/baseRadius, (constrainedY-centerY)/baseRadius, getId());
                }
            }
            else
                drawJoystick(centerX, centerY, false);
            //joystickCallback.onJoystickMoved(0,0,getId());
        }
        return true;
    }

    public interface JoystickListener
    {
        //void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}