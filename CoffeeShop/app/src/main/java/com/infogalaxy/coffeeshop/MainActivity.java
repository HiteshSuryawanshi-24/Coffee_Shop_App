package com.infogalaxy.coffeeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread T = new Thread(){
            public void run()
            {
                try
                {
                    sleep(3000);
                    startActivity(new Intent(MainActivity.this, Login_Activity.class));
                    finish();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        T.start();

    }
}