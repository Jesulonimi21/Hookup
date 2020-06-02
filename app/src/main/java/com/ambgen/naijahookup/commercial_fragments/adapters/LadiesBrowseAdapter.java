package com.ambgen.naijahookup.commercial_fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.commercial_fragments.model.RegularsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LadiesBrowseAdapter extends  RecyclerView.Adapter<LadiesBrowseAdapter.RegularsViewModel> {

    List<RegularsModel> listOfLadies=new ArrayList<>();
    public LadiesBrowseAdapter(  List<RegularsModel> listOfLadies){
            this.listOfLadies=listOfLadies;
    }

    @NonNull
    @Override
    public RegularsViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ladies_item_layout,parent,false);
        return new RegularsViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegularsViewModel holder, int position) {
        RegularsModel regularsModel=listOfLadies.get(position);
        holder.ladyName.setText(regularsModel.getName());
        Picasso.get().load(regularsModel.getImageurl()).fit().into(holder.ladyImageView);
    }

    @Override
    public int getItemCount() {
        return listOfLadies.size();
    }

    public static class RegularsViewModel extends RecyclerView.ViewHolder{
      public  ImageView ladyImageView;
      public  TextView  ladyName;
      public TextView addToChatsTextView;
        public RegularsViewModel(@NonNull View itemView) {
            super(itemView);
            ladyImageView=itemView.findViewById(R.id.lady_image);
            ladyName=itemView.findViewById(R.id.lady_name);
            addToChatsTextView=itemView.findViewById(R.id.add_to_chats);
        }
    }

    public static class RegularsChatsViewHolder extends RecyclerView.ViewHolder{
        public  ImageView ladyImageView;
        public  TextView  ladyName;
        public RegularsChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            ladyImageView=itemView.findViewById(R.id.lady_image);
            ladyName=itemView.findViewById(R.id.lady_name);
        }
    }
}
