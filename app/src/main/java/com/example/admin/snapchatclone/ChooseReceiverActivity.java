package com.example.admin.snapchatclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.admin.snapchatclone.Helper.BitmapHelper;
import com.example.admin.snapchatclone.RecyclerViewFollow.FollowAdapter;
import com.example.admin.snapchatclone.RecyclerViewFollow.FollowObject;
import com.example.admin.snapchatclone.RecyclerViewReceiver.ReceiverAdapter;
import com.example.admin.snapchatclone.RecyclerViewReceiver.ReceiverObject;
import com.example.admin.snapchatclone.Start.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChooseReceiverActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    String Uid;
    Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_receiver);

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new ReceiverAdapter(getDataSet(),getApplication());
        mRecyclerView.setAdapter(adapter);
        Uid= FirebaseAuth.getInstance().getUid();

        FloatingActionButton mFab=findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStories();
            }
        });
    }



    private ArrayList<ReceiverObject> results=new ArrayList<>();
    private ArrayList<ReceiverObject> getDataSet() {
        listenForData();
        return results;
    }

    private void listenForData() {
        for (int i = 0; i < UserInformation.listFollowing.size(); i++) {
            DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("users").child(UserInformation.listFollowing.get(i));
            userDB.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String email = "";
                    String uid = dataSnapshot.getRef().getKey();
                    if (dataSnapshot.child("email").getValue() != null) {
                        email = dataSnapshot.child("email").getValue().toString();
                    }

                    if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        ReceiverObject obj = new ReceiverObject(email, uid, false);
                        if (!results.contains(obj)) {

                            results.add(obj);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void saveToStories(){
        rotateBitmap= BitmapHelper.getInstance().getBitmap();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference userStoryDB= database.getReference().child("users").child(Uid).child("stories");
        final String key= UUID.randomUUID().toString();
        assert key != null;
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("capture").child(key);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            rotateBitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
            byte[] dataToUpload=baos.toByteArray();
            storageReference.putBytes(dataToUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Long currentTimestamp = System.currentTimeMillis();
                            Long endTimestamp = currentTimestamp + (24 * 60 * 60 * 1000);

                            CheckBox mStory = findViewById(R.id.story);
                            if (mStory.isChecked()) {
                                Map<String, Object> mapToUpLoad = new HashMap<>();
                                mapToUpLoad.put("imageUrl", uri.toString());
                                mapToUpLoad.put("timestampBeg", currentTimestamp);
                                mapToUpLoad.put("timestampEnd", endTimestamp);
                                userStoryDB.child(key).updateChildren(mapToUpLoad);
                            }
                            for (int i = 0; i < results.size(); i++) {
                                if (results.get(i).isReceive()) {
                                    DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child("users").child(results.get(i).getUid()).child("received").child(Uid);
                                    Map<String, Object> mapToUpLoad = new HashMap<>();
                                    mapToUpLoad.put("imageUrl", uri.toString());
                                    mapToUpLoad.put("timestampBeg", currentTimestamp);
                                    mapToUpLoad.put("timestampEnd", endTimestamp);
                                    userDB.child(key).updateChildren(mapToUpLoad);

                                }
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                            return;
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChooseReceiverActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }
