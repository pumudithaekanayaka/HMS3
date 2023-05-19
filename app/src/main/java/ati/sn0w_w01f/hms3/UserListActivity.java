package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserListActivity extends AppCompatActivity implements ButtonSelectListner {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    UserAdapter userAdapter;
    ArrayList<UserItems> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Current Users");

        recyclerView =  findViewById(R.id.userlist_data);
        databaseReference = FirebaseDatabase.getInstance().getReference("Registered users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();
        userAdapter = new UserAdapter(this,arrayList);
        recyclerView.setAdapter(userAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserItems userItems = dataSnapshot.getValue(UserItems.class);
                    arrayList.add(userItems);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void ButtonSelected(UserItems button) {
        Intent intent = new Intent(this,UserProfileActivity.class);
        startActivity(intent);
    }
}