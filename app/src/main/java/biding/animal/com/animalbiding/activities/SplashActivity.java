package biding.animal.com.animalbiding.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.SharedPrefernceManger;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();

        screenHandler();
    }

    //...
    private void screenHandler() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent mainActivityIntent;
                if (SharedPrefernceManger.getUserTypeId().equalsIgnoreCase("2")) // Associate
                    mainActivityIntent = new Intent(SplashActivity.this, AssociateHomeActivity.class);
                else
                    mainActivityIntent = new Intent(SplashActivity.this, BiddingBaseActivity.class);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivityIntent);
                finish();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    //method to remove callback of handler
    private void removeHandlerCallback() {
        try {
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeHandlerCallback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeHandlerCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandlerCallback();
    }
}
