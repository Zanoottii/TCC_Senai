package br.senai.tcc.nursecarework.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import br.senai.tcc.nursecarework.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
