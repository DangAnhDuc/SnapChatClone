package com.example.admin.snapchatclone.RecyclerViewReceiver;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.snapchatclone.R;

public class ReceiverViewHolders extends RecyclerView.ViewHolder{
    public TextView mEmail;
    public CheckBox mReceiver;


    public ReceiverViewHolders(View itemView){
        super(itemView);
        mEmail=itemView.findViewById(R.id.email);
        mReceiver=itemView.findViewById(R.id.receive);

    }
}
