package com.example.remindme2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SpalshScreen extends AppCompatActivity {
    private  static int SPLASH_TIME_OUT = 4000;
    Animation topAnim;
    Animation bottomAnim;
    ImageView splashScreenLogo;
    TextView welcomeTxt, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalsh_screen);

        topAnim =  AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim =  AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        splashScreenLogo = findViewById(R.id.splashScreenLogo);
        splashScreenLogo.setAnimation(topAnim);

        welcomeTxt = findViewById(R.id.welcomeTxt);
        desc = findViewById(R.id.splashScreenDesc); //slogan
        welcomeTxt.setAnimation(bottomAnim);
        desc.setAnimation( bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }}