package com.ambgen.naijahookup.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.naijahookup.R;

public class ChatsViewHolder extends RecyclerView.ViewHolder {
   public TextView textMessage;
   public TextView timeMessage;

    public ChatsViewHolder(@NonNull View itemView) {
        super(itemView);
        textMessage=itemView.findViewById(R.id.text_message);
        timeMessage=itemView.findViewById(R.id.text_time);

    }
}
