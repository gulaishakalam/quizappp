package com.example.quizappp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashActivity extends AppCompatActivity {
    private TextView appName;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appName=findViewById(R.id.app_name);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.emporp);
        appName.setTypeface(typeface);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        appName.setAnimation(anim);
        mAuth=FirebaseAuth.getInstance();

        DbQuery.g_Firestore = FirebaseFirestore.getInstance();



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

               if(mAuth.getCurrentUser() != null)
               {
                   DbQuery.loadCategories(new MyCompleteListener() {
                       @Override
                       public void onSuccess() {
                           Intent i=new Intent(splashActivity.this,LoginActivity.class);
                           startActivity(i);
                           splashActivity.this.finish();
                       }

                       @Override
                       public void onFailure() {
                           Toast.makeText(splashActivity.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                       }
                   });


               }
               else
               {
                   Intent intent=new Intent(splashActivity.this,LoginActivity.class);
                   startActivity(intent);
                   splashActivity.this.finish();
               }

           }
        }.start();
    }
}