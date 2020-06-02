package com.ambgen.naijahookup.animations;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.Image;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationClass {

    public static final void animateMainScreen(ImageView mainImage, TextView appNameView, Button signInButton,Button signUpRegularButton, Button signUpCommercialButton){
        ObjectAnimator imageViewAnimator=ObjectAnimator.ofFloat(mainImage,"translationY",-300,0);
        imageViewAnimator.setDuration(1000);
        ObjectAnimator textNameAnimator=ObjectAnimator.ofFloat(appNameView,"translationX",-200,0);
        textNameAnimator.setDuration(1000);

        ObjectAnimator signInButtonAnimator=ObjectAnimator.ofFloat(signInButton,"translationY",500,0);
        signInButtonAnimator.setDuration(600);

        ObjectAnimator signUpRegularAnimator=ObjectAnimator.ofFloat(signUpRegularButton,"translationY",500,0);
        signUpRegularAnimator.setDuration(800);


        ObjectAnimator signUpCommercialAnimator=ObjectAnimator.ofFloat(signUpCommercialButton,"translationY",500,0);
        signUpCommercialAnimator.setDuration(1000);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(imageViewAnimator,textNameAnimator,signInButtonAnimator,signUpRegularAnimator,signUpCommercialAnimator);
        animatorSet.start();

    }


    public static void animateLoginScreen(ImageView signinImage, TextView nameTextView, EditText emailEditText, EditText passwordEditText, Button signInButton){

        ObjectAnimator imageViewAnimator=ObjectAnimator.ofFloat(signinImage,"translationX",300,0);
        imageViewAnimator.setDuration(1000);
        ObjectAnimator textNameAnimator=ObjectAnimator.ofFloat(nameTextView,"translationX",-200,0);
        textNameAnimator.setDuration(1000);

        ObjectAnimator signInButtonAnimator=ObjectAnimator.ofFloat(emailEditText,"translationY",500,0);
        signInButtonAnimator.setDuration(600);

        ObjectAnimator signUpRegularAnimator=ObjectAnimator.ofFloat(passwordEditText,"translationY",500,0);
        signUpRegularAnimator.setDuration(800);


        ObjectAnimator signUpCommercialAnimator=ObjectAnimator.ofFloat(signInButton,"translationY",500,0);
        signUpCommercialAnimator.setDuration(1000);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(imageViewAnimator,textNameAnimator,signInButtonAnimator,signUpRegularAnimator,signUpCommercialAnimator);
        animatorSet.start();
    }

    public static void animateSignUpAsRegularSreen(ImageView mainImage,TextView naijaTextView, EditText nameEditText,EditText emailEditText,
                                                   EditText passwordEditText,Button signUpButton){
        ObjectAnimator mainImageAnimator=ObjectAnimator.ofFloat(mainImage,"scaleX",0,1);
        mainImageAnimator.setDuration(1000);

        ObjectAnimator scaleYImageAnimator=ObjectAnimator.ofFloat(mainImage,"scaleY",0,1);
        scaleYImageAnimator.setDuration(1000);

        ObjectAnimator naijaTextAnimator=ObjectAnimator.ofFloat(naijaTextView,"translationX",500,0);
        naijaTextAnimator.setDuration(1000);

        ObjectAnimator nameEditTextAnimator=ObjectAnimator.ofFloat(nameEditText,"translationX",-400,0);
        nameEditTextAnimator.setDuration(1000);

        ObjectAnimator emailEditTextAnimator=ObjectAnimator.ofFloat(emailEditText,"translationX",400,0);
        emailEditTextAnimator.setDuration(1000);

        ObjectAnimator passwordEditTextAnimator=ObjectAnimator.ofFloat(passwordEditText,"translationX",-400,0);
        passwordEditTextAnimator.setDuration(1000);


        ObjectAnimator signUpButtonAnimator=ObjectAnimator.ofFloat(signUpButton,"translationX",400,0);
        signUpButtonAnimator.setDuration(1000);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(mainImageAnimator,scaleYImageAnimator,naijaTextAnimator,nameEditTextAnimator,emailEditTextAnimator,passwordEditTextAnimator,signUpButtonAnimator);
        animatorSet.start();







    }
}
