package com.ambgen.naijahookup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.animations.AnimationClass;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    ImageView mainImage;
    TextView appNameTextView;
    Button signInButton;
    Button signUpRegularButton;
    Button signUpCommercial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainImage=findViewById(R.id.home_img);
        signInButton=findViewById(R.id.signin_btn);
        signUpRegularButton=findViewById(R.id.sign_up_merchant);
        signUpCommercial=findViewById(R.id.sign_up_lady);
        appNameTextView=findViewById(R.id.name_identifier);

        AnimationClass.animateMainScreen(mainImage,appNameTextView,
                signInButton,signUpRegularButton,signUpCommercial);
    }
    public void signUpAsRegular(View v){
        Intent intent=new Intent(this, RegularRegisterActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
    }

    public void signUpAsMerchant(View v){
        Intent intent=new Intent(this, CommercialRegisterActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
    }
    public void signInAsUser(View view){
        Intent intent=new Intent(this, SignInActivity.class);
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
    }
}
