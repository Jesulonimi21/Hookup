package com.ambgen.naijahookup.regular_fragment.regular_account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambgen.naijahookup.activities.MainActivity;
import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.activities.RegularHomeActivity;
import com.ambgen.naijahookup.utilities.StaticKeys;
import com.ambgen.naijahookup.commercial_fragments.account.AccountFragment;
import com.ambgen.naijahookup.commercial_fragments.model.CommercialModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import maes.tech.intentanim.CustomIntent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegularAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegularAccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Uri uri;
    String downloadedImageUri;
    Map<String,Object> theHashMap;
    private StorageReference dbImageRef;
    String theId;
    Boolean imageChosen=false;
    ImageView updateImageView;
    CardView update_image_btn;
    ImageView accountImageView;
    TextView accountName;
    EditText nameEditText;

    public RegularAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegularFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegularAccountFragment newInstance(String param1, String param2) {
        RegularAccountFragment fragment = new RegularAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_regular_account, container, false);
        ImageView addAPhoto=root.findViewById(R.id.add_a_photo);
        updateImageView=root.findViewById(R.id.update_image_view);
        TextView logOut=root.findViewById(R.id.log_out);
        accountImageView=root.findViewById(R.id.account_identifier);
        accountName=root.findViewById(R.id.account_name);
        nameEditText=root.findViewById(R.id.person_name);
        Button updateInfoButton=root.findViewById(R.id.update);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                Intent i = new Intent(getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                CustomIntent.customType(getContext(), "fadein-to-fadeout");
            }
        });
        updateInfoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateSingleInfo(view);
                    }
                }
        );
        addAPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickerListener.chooseImg(RegularAccountFragment.this);
            }
        });
        update_image_btn=root.findViewById(R.id.update_image_btn);
        update_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAllInfoToDb(); }});
        theId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbImageRef= FirebaseStorage.getInstance().getReference().child("profileImg");


        DatabaseReference profileRef= FirebaseDatabase.getInstance().getReference();
        profileRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        CommercialModel users=dataSnapshot.child("Users").child(StaticKeys.regulars).child(theId).getValue(CommercialModel.class);

                        accountName.setText("welcome "+users.getName());
//                        Toast.makeText(WorkerHomeActivity.this,users.getImageurl(),Toast.LENGTH_LONG).show();
                        Picasso.get().load(users.getImageurl()).fit().centerInside().into(accountImageView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    return root;
    }



    RegularAccountFragment.imagePickerListener imagePickerListener;
    public interface  imagePickerListener{
        void chooseImg(RegularAccountFragment fragment);
    }


    public  void imageChosen(Uri uri){
        Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        if(uri!=null){
            imageChosen=true;
            updateImageView.setImageURI(uri);
        }
        Log.d(AccountFragment.class.getSimpleName(),uri.toString());
        this.uri=uri;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        imagePickerListener= (RegularHomeActivity) context;
    }




    private void uploadAllInfoToDb() {
        if(!imageChosen){
            Toast.makeText(getContext(),"Please chose an image",Toast.LENGTH_LONG).show();
            return;
        }

        /************  Instantiate a progress dialog*********************/
        final ProgressDialog loading=new ProgressDialog(getContext(),R.style.AppCompatAlertDialogStyle);
        loading.setTitle("Posting image in process");
        loading.setMessage("please Be patient");
        loading.setCanceledOnTouchOutside(true);
        loading.show();

        /**********************  instantiate the Storage reference ******************/
        final StorageReference filePath=dbImageRef.child(uri.getLastPathSegment()+System.currentTimeMillis()+"");
        /*****************put the byte in the StorageReference and equate it to an uploadtask*******************/
        final UploadTask uploadTask=filePath.putFile(uri);
        /****************add a success listener on the the upload task************************/
        uploadTask.addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getContext(),"uploaded image",Toast.LENGTH_LONG).show();
                        /*********** if the task is successful, use a Tak to continue with the upload task*******/
                        Task<Uri> taskUri=uploadTask.continueWithTask(
                                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                                        if(!task.isSuccessful()){
                                            throw task.getException();
                                        }
                                        loading.dismiss();
                                        Toast.makeText(getContext(),"imageurl retrieved",Toast.LENGTH_LONG).show();
                                        downloadedImageUri=filePath.getDownloadUrl().toString();
                                        theHashMap=new HashMap<>();

                                        return filePath.getDownloadUrl();
                                    }
                                }

                        ).addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String userType= Paper.book().read(StaticKeys.userType);
                                        /**** if the task is completed, get the result, put in in the doewnloas=d urll and store it ina database***/
                                        downloadedImageUri=task.getResult().toString();
                                        theHashMap.put("imageurl",downloadedImageUri);
                                        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference().
                                                child("Users").
                                                child(userType).
                                                child(theId);
                                        Picasso.get().load(downloadedImageUri).fit().centerInside().into(accountImageView);
                                        userRef.updateChildren(theHashMap);
//                                        sendToActivity();
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
                        Toast.makeText(getContext(),"imagge uploading failed",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    public void updateSingleInfo(View v) {
        final ProgressDialog loading = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        loading.setTitle("Uploading Info");
        loading.setMessage("please be patient");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        final String name = nameEditText.getText().toString();


        if (name.trim().equals("")) {
            nameEditText.setError("This field is required");
            loading.dismiss();
            return;
        }

        DatabaseReference updateReference = FirebaseDatabase.getInstance().getReference();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, Object> signUpMap = new HashMap<>();
        signUpMap.put(StaticKeys.name, name.trim());



        String category = Paper.book().read(StaticKeys.userType);

        updateReference.child("Users").child(category).child(id).updateChildren(signUpMap).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Detail uploaded successfully", Toast.LENGTH_SHORT).show();
                        accountName.setText("Welcome "+name);
                        loading.dismiss();

                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}
