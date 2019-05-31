package com.example.admin.snapchatclone;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.admin.snapchatclone.RecyclerViewStory.StoryObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayImageActiviy extends AppCompatActivity {
    String userID, chatorstory;
    private ImageView mImage;
    private boolean started=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_activiy);
        Bundle bundle= getIntent().getExtras();
        userID= bundle.getString("userID");
        chatorstory=bundle.getString("chatorstory");
        mImage=findViewById(R.id.image);
        switch (chatorstory){
            case "chat":
                listenForChat();
                break;
            case "story":
                listenForStory();
                break;
        }


    }

    private void listenForChat() {
        final DatabaseReference chatDb=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("received").child(userID);
        chatDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageURL="";
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {

                    if(chatSnapshot.child("imageUrl").getValue()!=null){
                        imageURL= chatSnapshot.child("imageUrl").getValue().toString();
                    }
                        imageUrlList.add(imageURL);
                        if(!started){
                            started=true;
                            initializeDisplay();
                        }
                    chatDb.child(chatSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ArrayList<String> imageUrlList=new ArrayList<>();

    private void listenForStory() {
        for (int i=0;i< UserInformation.listFollowing.size();i++){
            DatabaseReference followingStoryDB= FirebaseDatabase.getInstance().getReference().child("users").child(userID);
            followingStoryDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String imageURL="";

                    long timestampBeg = 0;
                    long timestampEnd = 0;
                    for (DataSnapshot storySnapshot : dataSnapshot.child("stories").getChildren()) {
                        if (storySnapshot.child("timestampBeg").getValue()!=null) {
                            timestampBeg = Long.parseLong(storySnapshot.child("timestampBeg").getValue().toString());
                        }
                        if(storySnapshot.child("timestampEnd").getValue()!=null){
                            timestampEnd= Long.parseLong(storySnapshot.child("timestampEnd").getValue().toString());

                        }
                        if(storySnapshot.child("imageUrl").getValue()!=null){
                            imageURL= storySnapshot.child("imageUrl").getValue().toString();

                        }
                        long timestampCurrent= System.currentTimeMillis();
                        if(timestampCurrent>=timestampBeg&&timestampCurrent<=timestampEnd) {
                                imageUrlList.add(imageURL);
                                if(!started){
                                    started=true;
                                    initializeDisplay();
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
    private int imageIterator=0;
    private void initializeDisplay() {
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).into(mImage);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage();
            }
        });
        final Handler handler=new Handler();
        final int delay=5000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeImage();
                handler.postDelayed(this,delay);
            }
        },delay);
    }

    private void changeImage() {
        if(imageIterator==imageUrlList.size()-1){
            finish();
            return;
        }
        imageIterator++;
        Glide.with(getApplication()).load(imageUrlList.get(imageIterator)).into(mImage);

    }
}
