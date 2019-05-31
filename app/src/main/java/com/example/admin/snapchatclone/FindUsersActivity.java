package com.example.admin.snapchatclone;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.snapchatclone.RecyclerViewFollow.FollowAdapter;
import com.example.admin.snapchatclone.RecyclerViewFollow.FollowObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class FindUsersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private EditText inputUserEmail;
    private Button searchUserEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        inputUserEmail=(EditText) findViewById(R.id.inputUserEmail);
        searchUserEmail=(Button) findViewById(R.id.searchUserEmail);

        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new FollowAdapter(getDataSet(),getApplication());
        mRecyclerView.setAdapter(adapter);
        searchUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                listenForData();
            }
        });
    }

    private void clear() {
        int size= this.results.size();
        this.results.clear();
        adapter.notifyItemRangeRemoved(0,size);
    }

    private void listenForData() {
        DatabaseReference userDB= FirebaseDatabase.getInstance().getReference().child("users");
        Query query=userDB.orderByChild("email").startAt(inputUserEmail.getText().toString()).endAt(inputUserEmail.getText().toString()+"\uf8ff");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email="";
                String uid = dataSnapshot.getRef().getKey();
                if(dataSnapshot.child("email").getValue()!=null){
                    email=dataSnapshot.child("email").getValue().toString();
                }

                if(!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    FollowObject obj=new FollowObject(email,uid);
                    results.add(obj);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<FollowObject> results=new ArrayList<>();
    private ArrayList<FollowObject> getDataSet() {
        listenForData();
        return results;
    }
}
