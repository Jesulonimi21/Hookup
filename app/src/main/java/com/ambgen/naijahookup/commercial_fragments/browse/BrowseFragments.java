package com.ambgen.naijahookup.commercial_fragments.browse;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.utilities.StaticKeys;
import com.ambgen.naijahookup.commercial_fragments.adapters.LadiesBrowseAdapter.RegularsViewModel;
import com.ambgen.naijahookup.commercial_fragments.model.RegularsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BrowseFragments extends Fragment {

    private BrowseViewModel browseViewModel;

    private RecyclerView ladiesRecycler;
    Query workersViewQuery;
    FirebaseRecyclerAdapter<RegularsModel, RegularsViewModel> firebaseRecyclerAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        browseViewModel =
                ViewModelProviders.of(this).get(BrowseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_commercial_browse, container, false);
        ladiesRecycler=root.findViewById(R.id.ladiesRecycler);
        ladiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ladiesRecycler.setHasFixedSize(true);
        workersViewQuery= FirebaseDatabase.getInstance().getReference().child("Users").child(StaticKeys.regulars);






        FirebaseRecyclerOptions<RegularsModel> options=new FirebaseRecyclerOptions.Builder<RegularsModel>().setQuery(workersViewQuery,RegularsModel.class)
                .build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<RegularsModel, RegularsViewModel>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull RegularsViewModel holder, int position, @NonNull final RegularsModel model) {
                holder.ladyName.setText(model.getName());
                Picasso.get().load(model.getImageurl()).placeholder(R.drawable.profile).into(holder.ladyImageView);
                holder.addToChatsTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startDiscuss(model.getUserid(),model.getName());
                    }
                });
            }

            @NonNull
            @Override
            public RegularsViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.ladies_item_layout,parent,false);
                return  new RegularsViewModel(v);

            }
        };

        firebaseRecyclerAdapter.startListening();
        ladiesRecycler.setAdapter(firebaseRecyclerAdapter);

        return root;
    }

    public void startDiscuss(final String regularsId, final String regularsName){

        final ProgressDialog progressDialog=new ProgressDialog(getContext(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please be patient");
        progressDialog.show();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final DatabaseReference hirerNetworkReference = FirebaseDatabase.getInstance().getReference().child("Discussions");
        DatabaseReference workerNetworkReference = FirebaseDatabase.getInstance().getReference().child("Discussions");
        final Map<String, Object> discussionHashMap = new HashMap<>();
        discussionHashMap.put(StaticKeys.userId, regularsId);
        discussionHashMap.put(StaticKeys.name, regularsName);
        discussionHashMap.put("invertedtimestamp",0-System.currentTimeMillis());
        discussionHashMap.put("messageexists",false);
        workerNetworkReference.child(mAuth.getCurrentUser().getUid()).child(regularsId).updateChildren(discussionHashMap).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String,Object> otherUserHashMap=new HashMap<>();
                        otherUserHashMap.put(StaticKeys.userId, mAuth.getCurrentUser().getUid());
                        otherUserHashMap.put(StaticKeys.name, regularsName);
                        otherUserHashMap.put("invertedtimestamp",0-System.currentTimeMillis());
                        otherUserHashMap.put("messageexists",false);
                        hirerNetworkReference.child(regularsId).child(mAuth.getCurrentUser().getUid()).updateChildren(otherUserHashMap).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                    showDialog(regularsName +" has been added to your chats");
                                    }
                                }
                        ).addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
    public void showDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
