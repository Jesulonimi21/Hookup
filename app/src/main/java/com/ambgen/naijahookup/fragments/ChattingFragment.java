package com.ambgen.naijahookup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambgen.naijahookup.utilities.ConstantValues;
import com.ambgen.naijahookup.viewholders.ChatsViewHolder;
import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.utilities.StaticKeys;
import com.ambgen.naijahookup.commercial_fragments.chats.ChatsFragment;
import com.ambgen.naijahookup.commercial_fragments.model.RegularsModel;
import com.ambgen.naijahookup.models.ChatsModel;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.paperdb.Paper;


public class ChattingFragment extends Fragment {

    ImageView backArrow;
    CircleImageView chatDp;
    TextView chatNameTextView;
    RecyclerView chatRecyclerView;
    ImageView sendImage;
    EmojiconEditText chatEdittext;
    private String friendId;
    Query chatsQuery;
    final int PRESENTUSER=10;
    final int PRESENTUSERFRIEND=20;
    RecordButton recordButton;
    RecordView recordView;
    ImageView cameraImage;
    ImageView attachmentImage;
    String presentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();

    FirebaseRecyclerAdapter<ChatsModel, ChatsViewHolder> firebaseRecyclerAdapter;
    public ChattingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_chatting, container, false);
        presentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendId = getArguments().getString(StaticKeys.userId);
        String userType=  Paper.book().read(StaticKeys.userType);
        String friendType;
        if(userType.equals(StaticKeys.commercials)){
            friendType=StaticKeys.regulars;
        }else{
            friendType=StaticKeys.commercials;
        }
        recordButton=root.findViewById(R.id.record_button);
        recordView=root.findViewById(R.id.record_view);
        cameraImage=root.findViewById(R.id.camera);
        attachmentImage=root.findViewById(R.id.attachment);
        recordButton.setRecordView(recordView);
        chatDp=root.findViewById(R.id.chatting_img);
        chatNameTextView=root.findViewById(R.id.chat_name);
        loadProfile(chatDp, friendId,friendType,chatNameTextView);
        backArrow=root.findViewById(R.id.backArrow);
        chatRecyclerView=root.findViewById(R.id.chat_recycler);
        sendImage=root.findViewById(R.id.img_send);
        chatEdittext=root.findViewById(R.id.edittext_chat);

        markMyUnseenMessagesAsSeen();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setHasFixedSize(true);


        chatEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                  //  isAtext = true;
                    //   sendImage.setImageResource(R.drawable.ic_send_black_24dp);
                    sendImage.setVisibility(View.VISIBLE);
                    recordButton.setVisibility(View.GONE);
                    cameraImage.setVisibility(View.GONE);
                    attachmentImage.setVisibility(View.GONE);
                } else {
                 //   isAtext = false;
                    sendImage.setVisibility(View.GONE);
                    recordButton.setVisibility(View.VISIBLE);
                    cameraImage.setVisibility(View.VISIBLE);
                    attachmentImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        chatDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Navigation.findNavController(view).popBackStack();
            }
        });
        chatsQuery=FirebaseDatabase.getInstance().getReference()
                .child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(friendId).orderByChild(ChatsModel.TIMESTAMP);

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word=chatEdittext.getText().toString();
                if(word.equals("")){
                    return;
                }

                sendMessage(word,FirebaseAuth.getInstance().getCurrentUser().getUid(),friendId,"text");
                chatEdittext.setText("");
            }
        });

        FirebaseRecyclerOptions<ChatsModel> options=new FirebaseRecyclerOptions.
                Builder<ChatsModel>().setQuery(chatsQuery,ChatsModel.class).build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ChatsModel, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsViewHolder chatsViewHolder, int i, @NonNull ChatsModel chatsModel) {
                chatsViewHolder.textMessage.setText(chatsModel.getMessage());
                chatsViewHolder.timeMessage.setText(chatsModel.getTime());
            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=null;
                if(viewType==ConstantValues.SENDERSENTTEXTVIEW){
                    view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_sent_text_layout,parent,false);
                }else if(viewType==ConstantValues.SENDERSEENTEXTVIEW){
                    view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_seen_text_layout,parent,false);
                }else if(viewType==ConstantValues.SENDERUNSENTTEXTVIEW){
                    view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_unsent_text_layout,parent,false);
                }else if(viewType==ConstantValues.RECEIVERTEXTVIEW){
                    view=LayoutInflater.from(parent.getContext()).inflate(R.layout.received_text_messages,parent,false);

                }


                return new ChatsViewHolder(view);
            }

            @Override
            public int getItemViewType(int position) {
                ChatsModel chatsModel = getItem(position);
                if (chatsModel.getType().toLowerCase().equals("text")) {
                    if (chatsModel.getSenderid().equals(presentUserId)) {
                        if (chatsModel.getStatus().toLowerCase().equals(ChatsModel.UNSENT)) {
                        // return unsent sender textview
                            return ConstantValues.SENDERUNSENTTEXTVIEW;
                        } else if (chatsModel.getStatus().toLowerCase().equals(ChatsModel.SENT)) {
                        // return sent  sender textview
                            return  ConstantValues.SENDERSENTTEXTVIEW;
                        } else if (chatsModel.getStatus().toLowerCase().equals(ChatsModel.SEEN)) {
                        //return seen sender textview
                            return  ConstantValues.SENDERSEENTEXTVIEW;
                        }
                    } else {
                        if (chatsModel.getStatus().toLowerCase().equals("sent")) {
                            // make a seen request to db
                            String key=getRef(position).getKey();
                            Log.d(ChattingFragment.class.getSimpleName(),"updating key in itemViewType "+key);
                            DatabaseReference chatUpdateReference=FirebaseDatabase.getInstance().getReference().child("Chats");
                            Map<String,Object> updateChatsMap=new HashMap<>();
                            updateChatsMap.put(ChatsModel.STATUS,ChatsModel.SEEN);
                            chatUpdateReference.child(friendId).child(presentUserId).child(key).updateChildren(updateChatsMap);
                            chatUpdateReference.child(presentUserId).child(friendId).child(key).updateChildren(updateChatsMap);
                            return ConstantValues.RECEIVERTEXTVIEW;

                            // inflate normal receiver textview
                        } else {
                            return ConstantValues.RECEIVERTEXTVIEW;
                        }
                    }


                }
                return 0;

            }
        };

        firebaseRecyclerAdapter.startListening();
        chatRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.registerAdapterDataObserver(
                new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        chatRecyclerView.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount());

                    }

                }
        );
        return root;
    }

    public void loadProfile(final ImageView accountImageView, final String theId, final String userType, final TextView ladyNameView){
        DatabaseReference profileRef= FirebaseDatabase.getInstance().getReference();
        profileRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        RegularsModel users=dataSnapshot.child("Users").child(userType).child(theId).getValue(RegularsModel.class);
                        Log.d(ChatsFragment.class.getSimpleName(),theId);
                        ladyNameView.setText(users.getName());
                        Picasso.get().load(users.getImageurl()).placeholder(R.drawable.profile).fit().centerInside().into(accountImageView);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public void sendMessage(String message, final String presentUserId, final String friendsId, String type){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());
        String senderId=FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Map<String,Object> chatMap=new HashMap<>();
        chatMap.put(ChatsModel.MESSAGE, message);
        chatMap.put(ChatsModel.TIME, saveCurrentTime);
        chatMap.put(ChatsModel.TYPE, type);
        chatMap.put(ChatsModel.SENDERID,FirebaseAuth.getInstance().getCurrentUser().getUid());
        chatMap.put(ChatsModel.RECEIVERID,friendsId);
        chatMap.put(ChatsModel.ISSEEN, "false");
        chatMap.put(ChatsModel.EMAIL, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        chatMap.put(ChatsModel.REFERENCETEXT,"none");
        chatMap.put(ChatsModel.ADAPTERPOSITION,0);
        chatMap.put(ChatsModel.STATUS,"unsent");
        chatMap.put(ChatsModel.TIMESTAMP, ServerValue.TIMESTAMP);
        DatabaseReference senderChatsReference=FirebaseDatabase.getInstance().getReference().child("Chats");
        DatabaseReference sendersRef=senderChatsReference.child(presentUserId).child(friendsId).push();
        final String sendersKey=sendersRef.getKey();
                sendersRef.updateChildren(chatMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ChattingFragment.class.getSimpleName(),sendersKey);
                DatabaseReference chatUpdateReference=FirebaseDatabase.getInstance().getReference().child("Chats");
                Map<String,Object> updateChatsMap=new HashMap<>();
                updateChatsMap.put(ChatsModel.STATUS,ChatsModel.SENT);
                chatUpdateReference.child(presentUserId).child(friendsId).child(sendersKey).updateChildren(updateChatsMap);
            }
        });
        DatabaseReference receiverChatsReference=FirebaseDatabase.getInstance().getReference().child("Chats");

       DatabaseReference receiversRef= receiverChatsReference.child(friendsId).child(presentUserId).child(sendersKey);

       receiversRef.updateChildren(chatMap).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Log.d(ChattingFragment.class.getSimpleName(),sendersKey);
               DatabaseReference chatUpdateReference=FirebaseDatabase.getInstance().getReference().child("Chats");
               Map<String,Object> updateChatsMap=new HashMap<>();
               updateChatsMap.put(ChatsModel.STATUS,ChatsModel.SENT);
               chatUpdateReference.child(friendsId).child(presentUserId).child(sendersKey).updateChildren(updateChatsMap);
           }
       });

        final Map<String, Object> discussionHashMap = new HashMap<>();
        discussionHashMap.put("invertedtimestamp",0-System.currentTimeMillis());
        discussionHashMap.put("messageexists",true);


        final Map<String, Object> userDiscussionHashmap = new HashMap<>();
        userDiscussionHashmap.put("invertedtimestamp",0-System.currentTimeMillis());
        final DatabaseReference ownerRootRef = FirebaseDatabase.getInstance().getReference().child("Discussions");
        DatabaseReference friendsRootRef = FirebaseDatabase.getInstance().getReference().child("Discussions");
        ownerRootRef.child(presentUserId).child(friendsId).updateChildren(userDiscussionHashmap);
        friendsRootRef.child(friendsId).child(presentUserId).updateChildren(discussionHashMap);
    }

        public void markMyUnseenMessagesAsSeen(){
            final Map<String, Object> userDiscussionHashmap = new HashMap<>();
            userDiscussionHashmap.put("messageexists",false);
            final DatabaseReference ownerRootRef = FirebaseDatabase.getInstance().getReference().child("Discussions");
            ownerRootRef.child(presentUserId).child(friendId).updateChildren(userDiscussionHashmap);
        }
}
