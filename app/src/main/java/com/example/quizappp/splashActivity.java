package com.example.quizappp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class splashActivity extends AppCompatActivity {
    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appName=findViewById(R.id.app_name);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.emporp);
        appName.setTypeface(typeface);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        appName.setAnimation(anim);
        new Thread()
        {
            @Override
           public void run()
           {
               try{
                   sleep(3000);
               }catch(InterruptedException e){
                   e.printStackTrace();
               }
               Intent intent=new Intent(splashActivity.this,MainActivity.class);
               startActivity(intent);
               splashActivity.this.finish();
           }
        }.start();
    }
}