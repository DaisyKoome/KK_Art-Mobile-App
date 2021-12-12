package com.example.mysqlimagesuploaderlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView splash_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_image = findViewById(R.id.splash_image);
        Animation splash_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);

        splash_image.startAnimation(splash_anim);

        final Intent i = new Intent(SplashActivity.this, NavigationActivity.class);

        Thread splash_timer = new Thread(){
            public void run (){
                try {
                    sleep(5000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        splash_timer.start();

    }
}
