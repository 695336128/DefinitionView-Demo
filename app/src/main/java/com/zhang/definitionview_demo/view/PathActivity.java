package com.zhang.definitionview_demo.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.ImageView;

import com.zhang.definitionview_demo.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PathActivity extends AppCompatActivity {

    private ImageView container_img;

    private BallMoveView ballMoveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        initView();
    }

    private void initView() {
        container_img = (ImageView) findViewById(R.id.container_img);
        ballMoveView = (BallMoveView) findViewById(R.id.ballMoveView);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ballMoveView.postInvalidate();
            }
        }, 200, 100);

        Bitmap bmpBuffer = Bitmap.createBitmap(500, 800, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpBuffer);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0x44000000);

        /* * * * * * * * * * * * * * * * * * * * * * */
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(10, 10, 220, 150), 10, 10, paint);
        paint.setStyle(Paint.Style.STROKE);
        Random random = new Random();
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 100; i++) {
            Path path = new Path();
            path.moveTo(random.nextInt(210) + 10, random.nextInt(140) + 10);
            path.lineTo(random.nextInt(210) + 10, random.nextInt(140) + 10);
            paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            paint.setAlpha(random.nextInt(150));
            canvas.drawPath(path, paint);
        }

        for (int i = 0; i < 4; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            paint.setColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            paint.setTextSkewX((random.nextInt(10) / 10) * (random.nextInt(10) > 5 ? 1 : -1));
            paint.setTextSize(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources().getDisplayMetrics()));
            canvas.drawText(String.valueOf(random.nextInt(9)), 40 * i + 20, 110, paint);
        }

        /* * * * * * * * * * * * * * * * * * * * * * */

        container_img.setImageBitmap(bmpBuffer);
    }
}
