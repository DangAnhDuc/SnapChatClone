package com.example.admin.snapchatclone.RecyclerViewFollow;

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

public class FollowAdapter extends RecyclerView.Adapter<FollowViewHolders>{

    private List<FollowObject> userList;
    private Context context;


    public FollowAdapter(List<FollowObject> userList, Context context){
        this.userList=userList;
        this.context=context;
    }

    @NonNull
    @Override
    public FollowViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_follower_item,null);
        FollowViewHolders rcv=new FollowViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowViewHolders holder, int position) {
        holder.mEmail.setText(userList.get(position).getEmail());

        if(UserInformation.listFollowing.contains(userList.get(holder.getLayoutPosition()).getUid())){
            holder.mFollow.setText("following");
        }else{
            holder.mFollow.setText("follow");
        }

            holder.mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(!UserInformation.listFollowing.contains(userList.get(holder.getLayoutPosition()).getUid())){
                        holder.mFollow.setText("following");
                        FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("following").child(userList.get(holder.getLayoutPosition()).getUid()).setValue(true);
                    }else{
                        holder.mFollow.setText("follow");
                        FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("following").child(userList.get(holder.getLayoutPosition()).getUid()).removeValue();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }
}
