package com.example.sony.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback {



    private DrawThread drawThread; //new

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override   //new
    public void surfaceCreated(SurfaceHolder holder) {

        drawThread = new DrawThread(getContext(),getHolder());
        drawThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {



    }

    @Override //new
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }



    @Override //new
    public boolean onTouchEvent(MotionEvent event) {
        drawThread.set(event.getX(), event.getY(),0);
        return super.onTouchEvent(event);
    }
}








   class DrawThread extends Thread {  //new all class

    private SurfaceHolder surfaceHolder;

    private volatile boolean running = true;


float x = -10000, y = -10000, r = 0;
       void set(float x, float y, float r){
           this.x = x;
           this.y = y;
           this.r = r;

       }

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {  //paint here
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                           canvas.drawColor(Color.BLUE);

                    canvas.drawCircle(x, y, r, paint);

                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            r +=5;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}