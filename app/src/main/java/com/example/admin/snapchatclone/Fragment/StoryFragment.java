package com.example.admin.snapchatclone.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin.snapchatclone.R;
import com.example.admin.snapchatclone.RecyclerViewStory.StoryAdapter;
import com.example.admin.snapchatclone.RecyclerViewStory.StoryViewHolders;
import com.example.admin.snapchatclone.RecyclerViewStory.StoryObject;
import com.example.admin.snapchatclone.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public static StoryFragment newInstance(){
        StoryFragment storyFragment=new StoryFragment();
        return storyFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story,container,false);


        mRecyclerView=view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new StoryAdapter(getDataSet(),getContext());
        mRecyclerView.setAdapter(adapter);

        Button refresh= view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                listenForData();
            }
        });
        return view;
    }

    private void clear() {
        int size= this.results.size();
        this.results.clear();
        adapter.notifyItemRangeRemoved(0,size);
    }

    private ArrayList<StoryObject> results=new ArrayList<>();
    private ArrayList<StoryObject> getDataSet() {
        listenForData();
        return results;
    }

    private void listenForData() {
        for (int i=0;i< UserInformation.listFollowing.size();i++){
            DatabaseReference followingStoryDB= FirebaseDatabase.getInstance().getReference().child("users").child(UserInformation.listFollowing.get(i));
            followingStoryDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.child("email").getValue().toString();
                    String uid = dataSnapshot.getRef().getKey();
                    long timestampBeg = 0;
                    long timestampEnd = 0;
                    for (DataSnapshot storySnapshot : dataSnapshot.child("stories").getChildren()) {
                        if (storySnapshot.child("timestampBeg").getValue()!=null) {
                            timestampBeg = Long.parseLong(storySnapshot.child("timestampBeg").getValue().toString());
                        }
                        if(storySnapshot.child("timestampEnd").getValue()!=null){
                            timestampEnd= Long.parseLong(storySnapshot.child("timestampEnd").getValue().toString());

                        }

                        long timestampCurrent= System.currentTimeMillis();
                        if(timestampCurrent>=timestampBeg&&timestampCurrent<=timestampEnd) {
                            StoryObject object = new StoryObject(email, uid,"story");
                            if (!results.contains(object)) {
                                results.add(object);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
