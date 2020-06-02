package com.ambgen.naijahookup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.utilities.StaticKeys;

import io.paperdb.Paper;
import maes.tech.intentanim.CustomIntent;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Paper.init(this);

//        Handler handler = new Handler(Looper.getMainLooper())
//        handler.postDelayed({
//                val intent = Intent(this@SplashActivity, OnboardScreenActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent)
//        CustomIntent.customType(this@SplashActivity, "fadein-to-fadeout")
//        }, 990)
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String userType= Paper.book().read(StaticKeys.userType);
                if(userType!=null){
                    if(userType.equals("")){
                        Intent i = new Intent(StartActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        CustomIntent.customType(StartActivity.this, "fadein-to-fadeout");
                        return;
                    }else if(userType.equals(StaticKeys.commercials)){
                        Intent i = new Intent(StartActivity.this, CommercialHomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        CustomIntent.customType(StartActivity.this, "fadein-to-fadeout");
                        return;
                    }else if(userType.equals(StaticKeys.regulars)){
                        Intent i = new Intent(StartActivity.this, RegularHomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        CustomIntent.customType(StartActivity.this, "fadein-to-fadeout");
                        return;
                    }
                }
                Intent i = new Intent(StartActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                CustomIntent.customType(StartActivity.this, "fadein-to-fadeout");
            }
        },990);
    }
}
