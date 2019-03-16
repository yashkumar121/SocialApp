package bluetooth_demo.kct.com.socialdemobyfirebase.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

import bluetooth_demo.kct.com.socialdemobyfirebase.R;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AVLoadingIndicatorView;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AppPrefernce;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final AVLoadingIndicatorView loader = (AVLoadingIndicatorView) findViewById(R.id.loader);


        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                loader.smoothToShow();
            }
        }, 500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppPrefernce prefernce=new AppPrefernce(SplashActivity.this);

                if(prefernce.getString(AppPrefernce.TOKENKEY)==null)
                    startActivity(new Intent(SplashActivity.this,LogInActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this,HomePageActivity.class));
                finish();
            }
        }, 3000);
    }
}
