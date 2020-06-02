package com.ambgen.naijahookup.regular_fragment.regular_chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.utilities.StaticKeys;
import com.ambgen.naijahookup.commercial_fragments.adapters.LadiesBrowseAdapter;
import com.ambgen.naijahookup.commercial_fragments.model.CommercialModel;
import com.ambgen.naijahookup.commercial_fragments.model.FriendsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RegularChatsFragment extends Fragment {

    private RegularChatsViewModel regularChatsViewModel;
    private RecyclerView ladiesRecycler;
    Query workersViewQuery;
    FirebaseRecyclerAdapter<FriendsModel, LadiesBrowseAdapter.RegularsChatsViewHolder> firebaseRecyclerAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        regularChatsViewModel =
                ViewModelProviders.of(this).get(RegularChatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_regular_chats, container, false);

        FirebaseAuth auth= FirebaseAuth.getInstance();
        ladiesRecycler=root.findViewById(R.id.ladiesRecycler);
        ladiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        ladiesRecycler.setHasFixedSize(true);
        workersViewQuery= FirebaseDatabase.getInstance().getReference().child(StaticKeys.chats).child(auth.getCurrentUser().getUid()).orderByChild("invertedtimestamp");
        FirebaseRecyclerOptions<FriendsModel> options=new FirebaseRecyclerOptions.Builder<FriendsModel>().setQuery(workersViewQuery,FriendsModel.class)
                .build();
        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<FriendsModel, LadiesBrowseAdapter.RegularsChatsViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull LadiesBrowseAdapter.RegularsChatsViewHolder holder, int position, @NonNull final FriendsModel model) {

               loadProfile(holder.ladyImageView,model.getUser_id(),StaticKeys.commercials,holder.ladyName);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        bundle.putString(StaticKeys.userId,model.getUser_id());
                        Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.chattingFragment,bundle);
                    }
                });
            }

            @Override
            public int getItemViewType(int position) {
                FriendsModel friendsModel=getItem(position);
                if(friendsModel.isMessageexists()){
                    return 1;
                }else{
                    return 2;
                }
            }

            @NonNull
            @Override
            public LadiesBrowseAdapter.RegularsChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= null;
                if(viewType==1){
                 v=   LayoutInflater.from(parent.getContext()).inflate(R.layout.ladies_chats_item_message_layout,parent,false);
                }  else{
                  v=  LayoutInflater.from(parent.getContext()).inflate(R.layout.ladies_chats_item_nomessage_layout,parent,false);
                }

                return  new LadiesBrowseAdapter.RegularsChatsViewHolder(v);

            }
        };

        firebaseRecyclerAdapter.startListening();
        ladiesRecycler.setAdapter(firebaseRecyclerAdapter);


        return root;
    }

    public void loadProfile(final ImageView accountImageView, final String theId, final String userType, final TextView ladyNameView){
        DatabaseReference profileRef=FirebaseDatabase.getInstance().getReference();
        profileRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        CommercialModel users=dataSnapshot.child("Users").child(userType).child(theId).getValue(CommercialModel.class);

                        ladyNameView.setText(users.getName());
                        Picasso.get().load(users.getImageurl()).placeholder(R.drawable.profile).fit().centerInside().into(accountImageView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

}
