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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;
import maes.tech.intentanim.CustomIntent;

public class CommercialRegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText nameEditText;
    EditText nationalIdEditText;
    EditText emailEditText;
    EditText passwordEditText;
    ImageView signUpImageView;
    TextView naijaHookUpTextView;
    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial_register);
        Paper.init(this);
        nameEditText=findViewById(R.id.person_name);
        nationalIdEditText=findViewById(R.id.person_id);
        emailEditText=findViewById(R.id.person_email);
        passwordEditText=findViewById(R.id.person_password);
        signUpImageView=findViewById(R.id.signup_img);
        naijaHookUpTextView=findViewById(R.id.name_identifier);
        signUpButton=findViewById(R.id.signUpButton);

        AnimationClass.animateSignUpAsRegularSreen(signUpImageView,naijaHookUpTextView,nameEditText,emailEditText,passwordEditText,signUpButton);



        mAuth=FirebaseAuth.getInstance();
    }

    public void signUpCommercial(View v){
        String name=nameEditText.getText().toString();
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String nationalId=nationalIdEditText.getText().toString();
        if(TextUtils.isEmpty(name)){
            nameEditText.setError("this field is required");
            return;
        }else if(TextUtils.isEmpty(email)){
            emailEditText.setError("This field is required");
            return;
        }else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("This field is required");
            return;
        }else if(TextUtils.isEmpty(nationalId)){
            nationalIdEditText.setError("This field is required");
            return;
        }



        sendAllInfoToDb(name,email,password,nationalId);
    }


    private void sendAllInfoToDb(final String name, final String email, final String passWord,final String nationalId) {

        //############################---  INSTANTIATE DATABASE REFERENCE AS WELL AS PROGRESS DIALOG    --############################################//

        final DatabaseReference hirerRootRef = FirebaseDatabase.getInstance().getReference();
        final ProgressDialog loading = new ProgressDialog(CommercialRegisterActivity.this, R.style.AppCompatAlertDialogStyle);
        loading.setTitle("Signing You Up");
        loading.setMessage("please be patient");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        //############################---   ADD A LISTENER ON THE DATABASE REFERENCE SO AS TO AVOID PEOPLE USING THE SAME PHONENUMBER   --############################################//

//


//############################--- STORE ALL USERS INFORRMATION IN A HASKMAP     --############################################//

        mAuth.createUserWithEmailAndPassword(email, passWord).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                        final String id = mAuth.getCurrentUser().getUid();
                        HashMap<String, Object> signUpMap = new HashMap<>();
                        signUpMap.put("name", name);
                        signUpMap.put("email", email);
                        signUpMap.put("password", passWord);
                        signUpMap.put("nationalid",nationalId);
                        signUpMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/flutterblogapp-7e511.appspot.com/o/profile.png?alt=media&token=34aa4337-126a-46f1-b649-2006416eb1aa");
                        signUpMap.put("userid", id);
                        //############################---  FINALLY USE THE DATABASE REFERENCE TO SEND USERS DATA TO THE DATABASE  --############################################//

                        hirerRootRef.child("Users").child(StaticKeys.commercials).child(id).updateChildren(signUpMap).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        //############################---STORE DATA IN A STATIC CLASS   --############################################//

//                                        StaticProfile.firstname = firstname;
//                                        StaticProfile.lastname = lastName;
//                                        StaticProfile.email = email;
//                                        StaticProfile.password = passWord;
//                                        StaticProfile.phonenumber = phoneNumber;
                                        Paper.book().write(StaticKeys.userPasswordKey, passWord);
                                        Paper.book().write(StaticKeys.userEmailKey, email);
                                        Paper.book().write(StaticKeys.userId, id);
                                        Paper.book().write(StaticKeys.userType,StaticKeys.commercials);



                                        //############################--- MAKE THE EDITTEXTS EMPTY  --############################################//


                                        Intent i = new Intent(CommercialRegisterActivity.this, CommercialHomeActivity.class);
                                        i.putExtra(StaticKeys.userId,id);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        CustomIntent.customType(CommercialRegisterActivity.this, "fadein-to-fadeout");
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CommercialRegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        loading.dismiss();
                                    }
                                }
                        );


                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Log.d(CommercialRegisterActivity.class.getSimpleName(), e.getMessage());
                        Log.d("signUpError", passWord);
                        Toast.makeText(CommercialRegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        Log.d("imageDebug", e.getMessage());
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
