package com.example.thinkableproject.duckhunt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.example.thinkableproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_APPEND;

public class GameView extends View {

    Bitmap background;
    Rect rect;
    static int dWidth, dHeight;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS = 30;
    ArrayList<Duck1> duck1;
    ArrayList<Duck2> duck2;
    Bitmap ball, target;
    float ballX, ballY;
    float sX, sY;
    float fX, fY;
    float dX, dY;
    int x;
    FirebaseUser mUser;
    Long startTime, endTime;
    float tempY, tempX;
    Paint borderPaint;
    int score = 0;
    int life = 10;
    Context context;
    MediaPlayer duck1_hit, duck2_hit, duck_miss, ball_throw;
    public static boolean gameState = false;

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.backgrounddh);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        dWidth = size.x;
        dHeight = size.y;
        startTime = System.currentTimeMillis();
        rect = new Rect(0, 0, dWidth, dHeight);
        handler = new Handler();
        gameState = true;
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        duck1 = new ArrayList<>();
        duck2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Duck1 duck_1 = new Duck1(context);
            duck1.add(duck_1);
            Duck2 duck_2 = new Duck2(context);
            duck2.add(duck_2);
        }
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        target = BitmapFactory.decodeResource(getResources(), R.drawable.target);
        ballX = ballY = 0;
        sX = sY = fX = fY = 0;
        dX = dY = 0;
        tempX = tempY = 0;
        borderPaint = new Paint();
        borderPaint.setColor(Color.RED);
        borderPaint.setStrokeWidth(5);
        this.context = context;
        duck1_hit = MediaPlayer.create(context, R.raw.duck1_hit);
        duck2_hit = MediaPlayer.create(context, R.raw.duck2_hit);
        duck_miss = MediaPlayer.create(context, R.raw.duck_miss);
        ball_throw = MediaPlayer.create(context, R.raw.ball_throw);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (life < 1) {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Log.d("WEEK", String.valueOf(now.get(Calendar.WEEK_OF_MONTH)));
            Log.d("MONTH", String.valueOf(now.get(Calendar.MONTH)));
            Log.d("YEAR", String.valueOf(now.get(Calendar.YEAR)));
            Log.d("DAY", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
            Log.d("Month", String.valueOf(now.get(Calendar.MONTH)));

            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH) + 1;
            Format f = new SimpleDateFormat("EEEE");
            String str = f.format(new Date());
            gameState = false;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("score", score);
            context.startActivity(intent);
            ((Activity) context).finish();
            Log.d("Sending Score", String.valueOf(score));
            endTime = System.currentTimeMillis();
            Long seconds = (endTime - startTime) / 1000;


            SharedPreferences sh = context.getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

            x = sh.getInt("firstStartTimeConWH", 0);

// We can then use the data
            Log.d("A Count", String.valueOf(x));

            int y = x + 1;

            SharedPreferences prefsCount1 = context.getSharedPreferences("prefsTimeConWH", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefsCount1.edit();
            editor.putInt("firstStartTimeConWH", y);
            editor.apply();
            SharedPreferences sha = context.getSharedPreferences("prefsTimeConWH", MODE_APPEND);

// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show

            int x1 = sha.getInt("firstStartTimeConWH", 0);
            DatabaseReference referenceTime = FirebaseDatabase.getInstance().getReference("TimeSpentWHChart").child(mUser.getUid()).child("Concentration Games").child(String.valueOf(now.get(Calendar.YEAR))).child(String.valueOf(month)).child(String.valueOf(now.get(Calendar.WEEK_OF_MONTH))).child(str).child(String.valueOf(x));
            Log.d("ReferencePath", String.valueOf(referenceTime));
            referenceTime.setValue(seconds);

        }
        canvas.drawBitmap(background, null, rect, null);
        for (int i = 0; i < duck1.size(); i++) {
            canvas.drawBitmap(duck1.get(i).getBitMap(), duck1.get(i).duckX, duck1.get(i).duckY, null);
            duck1.get(i).duckFrame++;
            if (duck1.get(i).duckFrame > 8) {
                duck1.get(i).duckFrame = 0;
            }
            duck1.get(i).duckX -= duck1.get(i).velocity;
            if (duck1.get(i).duckX < -duck1.get(i).getWidth()) {
                duck1.get(i).resetPosition();
                life--;
                if (duck_miss != null)
                    duck_miss.start();
            }
            canvas.drawBitmap(duck2.get(i).getBitMap(), duck2.get(i).duckX, duck2.get(i).duckY, null);
            duck2.get(i).duckFrame++;
            if (duck2.get(i).duckFrame > 8) {
                duck2.get(i).duckFrame = 0;
            }
            duck2.get(i).duckX -= duck2.get(i).velocity;
            if (duck2.get(i).duckX < -duck2.get(i).getWidth()) {
                duck2.get(i).resetPosition();
                life--;
                if (duck_miss != null)
                    duck_miss.start();
            }
            if (ballX <= (duck1.get(i).duckX + duck1.get(i).getWidth())
                    && ballX + ball.getWidth() >= duck1.get(i).duckX
                    && ballY <= (duck1.get(i).duckY + duck1.get(i).getHeight())
                    && ballY >= duck1.get(i).duckY) {
                duck1.get(i).resetPosition();
                score++;
                if (duck1_hit != null)
                    duck1_hit.start();
            }
            if (ballX <= (duck2.get(i).duckX + duck2.get(i).getWidth())
                    && ballX + ball.getWidth() >= duck2.get(i).duckX
                    && ballY <= (duck2.get(i).duckY + duck2.get(i).getHeight())
                    && ballY >= duck2.get(i).duckY) {
                duck2.get(i).resetPosition();
                score++;
                if (duck2_hit != null)
                    duck2_hit.start();
            }
        }
        if (sX > 0 && sY > dHeight * .75f) {
            canvas.drawBitmap(target, sX - target.getWidth() / 2, sY - target.getHeight() / 2, null);
        }
        if ((Math.abs(fX - sX) > 0 || Math.abs(fY - sY) > 0) && fY > 0 && fY > dHeight * .75f) {
            canvas.drawBitmap(target, fX - target.getWidth() / 2, fY - target.getHeight() / 2, null);
        }
        if ((Math.abs(dX) > 10 || Math.abs(dY) > 10) && sY > dHeight * .75f && fY > dHeight * .75f) {
            ballX = fX - ball.getWidth() / 2 - tempX;
            ballY = fY - ball.getHeight() / 2 - tempY;
            canvas.drawBitmap(ball, ballX, ballY, null);
            tempX += dX;
            tempY += dY;
        }
        canvas.drawLine(0, dHeight * .75f, dWidth, dHeight * .75f, borderPaint);
        if (gameState)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = dY = fX = fY = tempX = tempY = 0;
                sX = event.getX();
                sY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                fX = event.getX();
                fY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                fX = event.getX();
                fY = event.getY();
                ballX = event.getX();
                ballY = event.getY();
                dX = fX - sX;
                dY = fY - sY;
                break;
        }
        return true;
    }

}
