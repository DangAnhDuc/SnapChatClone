package com.example.admin.snapchatclone.RecyclerViewReceiver;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.snapchatclone.R;
import com.example.admin.snapchatclone.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverViewHolders>{

    private List<ReceiverObject> userList;
    private Context context;


    public ReceiverAdapter(List<ReceiverObject> userList, Context context){
        this.userList=userList;
        this.context=context;
    }

    @NonNull
    @Override
    public ReceiverViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_receiver_item,null);
        ReceiverViewHolders rcv=new ReceiverViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceiverViewHolders holder, int position) {
        holder.mEmail.setText(userList.get(position).getEmail());
        holder.mReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean receiveState=!userList.get(holder.getLayoutPosition()).isReceive();
                userList.get(holder.getLayoutPosition()).setReceive(receiveState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }
}
