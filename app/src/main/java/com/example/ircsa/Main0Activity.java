package com.example.ircsa;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Main0Activity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main0);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        image=findViewById(R.id.logo);
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);

        image.setAnimation(topAnim);
        t1.setAnimation(bottomAnim);
        t2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Main0Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN) ;
    }
}
