package com.ambgen.naijahookup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.utilities.StaticKeys;
import com.ambgen.naijahookup.animations.AnimationClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import io.paperdb.Paper;
import maes.tech.intentanim.CustomIntent;

public class SignInActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    ProgressDialog loading;
    FirebaseAuth mAuth;
    String userType;
    DatabaseReference   tokRef;
    DatabaseReference    TokenReference;
    ImageView signInImage;
    TextView naijaHookUpText;
    Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Paper.init(this);
        emailEditText=findViewById(R.id.signin_email);
        passwordEditText=findViewById(R.id.signin_password);
        loading=new ProgressDialog(SignInActivity.this,R.style.AppCompatAlertDialogStyle);
        mAuth=FirebaseAuth.getInstance();
        signInImage=findViewById(R.id.signin_img);
        naijaHookUpText=findViewById(R.id.name_identifier);
        signInButton=findViewById(R.id.signInButton);
        AnimationClass.animateLoginScreen(signInImage,naijaHookUpText,
                emailEditText,passwordEditText,signInButton);
    }

    public void signInUser(View v){
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        if(TextUtils.isEmpty(email)){
            emailEditText.setError("This field is required");
            return;
        }else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("This field is required");
            return;
        }
    validateAccount(email,password);
    }

    private void validateAccount(final String email, final String password) {



        //############################---  INSTANTIATE DATABASE REFERENCE AS WELL AS PROGRESS DIALOG    --############################################//

        loading.setTitle("Signing you in");
        loading.setMessage("please wait while you are being signed in");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        DatabaseReference signRef= FirebaseDatabase.getInstance().getReference();
                        signRef.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if((dataSnapshot.child("Users").child(StaticKeys.commercials).child(
                                                mAuth.getCurrentUser().getUid()
                                        ).exists())){
                                            userType=StaticKeys.commercials;
                                        }else{
                                            userType=StaticKeys.regulars;
                                        }

                                        if(dataSnapshot.child("Users").child(userType).child(
                                                mAuth.getCurrentUser().getUid()
                                        ).exists()){

                                            Paper.book().write(StaticKeys.userPasswordKey,password);
                                            Paper.book().write(StaticKeys.userEmailKey,email);
                                            Paper.book().write(StaticKeys.userType,userType);

//                                    Prevalent.currentOnlineUser=usersData;
                                            if(userType.equals(StaticKeys.commercials)){

                                                FirebaseInstanceId.getInstance().getInstanceId()
                                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                if (!task.isSuccessful()) {
                                                                    return;
                                                                }

                                                                // Get new Instance ID token
                                                                tokRef=FirebaseDatabase.getInstance().getReference().child("tokens").child(mAuth.getCurrentUser().getUid());

                                                                String token = task.getResult().getToken();
                                                                tokRef.child("tokenValue").setValue(token);

                                                                Log.d("deviceToken",token);
                                                                TokenReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userType);
                                                                TokenReference.child(
                                                                        mAuth.getCurrentUser().getUid()
                                                                ).child("tokenId").setValue(token).addOnSuccessListener(
                                                                        new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                loading.dismiss();
                                                                                Intent i=new Intent(SignInActivity.this, CommercialHomeActivity.class);
                                                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                i.putExtra(StaticKeys.userEmailKey,email);
                                                                                i.putExtra(StaticKeys.userType,userType);
                                                                                i.putExtra(StaticKeys.userPasswordKey,password);
                                                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                startActivity(i);
                                                                            }
                                                                        }
                                                                );
                                                                // Log and toast
                                                            }
                                                        });

                                            }
                                            else{
                                                TokenReference=FirebaseDatabase.getInstance().getReference().child("Users").child(userType);
                                                FirebaseInstanceId.getInstance().getInstanceId()
                                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                                if (!task.isSuccessful()) {
                                                                    return;
                                                                }

                                                                // Get new Instance ID token
                                                                String token = task.getResult().getToken();

                                                                tokRef=FirebaseDatabase.getInstance().getReference().child("tokens").child(mAuth.getCurrentUser().getUid());

                                                                tokRef.child("tokenValue").setValue(token);

                                                                Log.d("deviceToken",token);

                                                                TokenReference.child(mAuth.getCurrentUser().getUid()).child("tokenId")
                                                                        .setValue(token).addOnSuccessListener(
                                                                        new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                loading.dismiss();
                                                                                Intent i=new Intent(SignInActivity.this, RegularHomeActivity.class);
                                                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                i.putExtra(StaticKeys.userEmailKey,email);
                                                                                i.putExtra(StaticKeys.userType,userType);
                                                                                i.putExtra(StaticKeys.userPasswordKey,password);
                                                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                startActivity(i);
                                                                                CustomIntent.customType(SignInActivity.this,"fadein-to-fadeout");
                                                                            }
                                                                        }
                                                                );
                                                                // Log and toast

                                                            }
                                                        });


                                            }
                                        }else{
                                            loading.dismiss();
                                            Toast.makeText(SignInActivity.this,"check login details and try again",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }
                        );


                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading.dismiss();
                        Toast.makeText(SignInActivity.this,"Cjeck intrnet connection and email then try again",Toast.LENGTH_LONG).show();
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        );

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this, "right-to-left");
    }

}
