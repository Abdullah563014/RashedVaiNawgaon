package com.rashed.md.securityeye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    ImageView imageView;
    TextView welcomeTextView,appNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        imageView=findViewById(R.id.welcomeImageViewId);
        welcomeTextView=findViewById(R.id.welcomeTextViewId);
        appNameTextView=findViewById(R.id.welcomeAppNameTextViewId);


        WelcomeThread welcomeThread=new WelcomeThread();
        welcomeThread.start();

    }


    public class WelcomeThread extends Thread{
        @Override
        public void run() {
            runOnUiThread(new MyRunnable());
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public class MyRunnable implements Runnable{

        @Override
        public void run() {
            Animation imageAnimation= AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.zoomin);
            Animation welcomeAnimation= AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.mixed_anim);
            Animation appNameAnimation= AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.bounce);


            imageView.startAnimation(imageAnimation);
            welcomeTextView.startAnimation(welcomeAnimation);
            appNameTextView.startAnimation(appNameAnimation);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
