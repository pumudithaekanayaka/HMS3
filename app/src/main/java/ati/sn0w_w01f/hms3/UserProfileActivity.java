package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome,textViewFirstname,textViewLastname,textViewNatid,textViewBlood,textViewEmail,textViewDob,textViewAddress,textViewGender,textViewContact;
    private ProgressBar progressBar;
    private String Firstname,Lastname,Natid,Blood,Email,Dob,Address,Gender,Contact;
    private ImageView imageView;
    private FirebaseAuth authprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        Objects.requireNonNull(getSupportActionBar()).setTitle("User Profile");

        textViewWelcome = findViewById(R.id.txtview_welcome);
        textViewFirstname = findViewById(R.id.profile_txt_firstname);
        textViewLastname = findViewById(R.id.profile_txt_lastname);
        textViewNatid = findViewById(R.id.profile_txt_natid);
        textViewBlood = findViewById(R.id.profile_txt_blood);
        textViewEmail = findViewById(R.id.profile_txt_email);
        textViewDob = findViewById(R.id.profile_txt_dob);
        textViewAddress = findViewById(R.id.profile_txt_address);
        textViewGender = findViewById(R.id.profile_txt_gender);
        textViewContact = findViewById(R.id.profile_txt_contact);
        progressBar = findViewById(R.id.progressbar);

        imageView = findViewById(R.id.image_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UploadImageActivity.class);
                startActivity(intent);
            }
        });

        authprofile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authprofile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        } else {
            checkifEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    private void checkifEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("E-Mail Not verified");
        builder.setMessage("Verify E-mail now, Can't Login without verification next time");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser){
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails != null){
                    Firstname = firebaseUser.getDisplayName();
                    Lastname = readUserDetails.lastname;
                    Email = firebaseUser.getEmail();
                    Natid = readUserDetails.natid;
                    Blood = readUserDetails.blood;
                    Dob = readUserDetails.dob;
                    Address = readUserDetails.address;
                    Gender = readUserDetails.gender;
                    Contact = readUserDetails.contact;

                    textViewWelcome.setText("Welocme, " + Firstname +" "+ Lastname + "!");
                    textViewFirstname.setText(Firstname);
                    textViewLastname.setText(Lastname);
                    textViewEmail.setText(Email);
                    textViewNatid.setText(Natid);
                    textViewAddress.setText(Address);
                    textViewDob.setText(Dob);
                    textViewBlood.setText(Blood);
                    textViewGender.setText(Gender);
                    textViewContact.setText(Contact);

                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(imageView);

                } else{
                    Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_refresh){
        startActivity(getIntent());
        finish();
        } else if (id == R.id.update_user) {
            Intent intent = new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.update_mail){
            Intent intent = new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_settings) {
            Toast.makeText(UserProfileActivity.this, "Settings Under Construction", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_password) {
            Intent intent = new Intent(UserProfileActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.delete_user) {
            Intent intent = new Intent(UserProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_logout) {
            authprofile.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged out" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserProfileActivity.this, PatientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(UserProfileActivity.this, "Something is Wrong" , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}