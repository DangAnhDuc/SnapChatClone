package com.example.admin.snapchatclone.RecyclerViewStory;

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

public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolders>{

    private List<StoryObject> storyList;
    private Context context;


    public StoryAdapter(List<StoryObject> userList, Context context){
        this.storyList=userList;
        this.context=context;
    }

    @NonNull
    @Override
    public StoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_story_item,null);
        StoryViewHolders rcv=new StoryViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryViewHolders holder, int position) {
        holder.mEmail.setText(storyList.get(position).getEmail());
        holder.mEmail.setTag(storyList.get(position).getUid());
        holder.mLayout.setTag(storyList.get(position).getChatorstory());
    }

    @Override
    public int getItemCount() {
        return this.storyList.size();
    }
}
