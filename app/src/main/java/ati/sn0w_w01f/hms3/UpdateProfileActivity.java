package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateFirstName, editTextUpdateLastName, editTextUpdateNatID, editTextUpdateAddress, editTextUpdateBlood, editTextUpdateDoB, editTextUpdateContact;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGender;
    private String textFirstName,textLastName,textNatID,textAddress,textBlood, textDob, textGender, textContact;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update your profile");

        progressBar = findViewById(R.id.update_progressBar);
        editTextUpdateFirstName = findViewById(R.id.editText_update_first_name);
        editTextUpdateLastName = findViewById(R.id.editText_update_last_name);
        editTextUpdateNatID = findViewById(R.id.editText_update_natid);
        editTextUpdateDoB = findViewById(R.id.editText_update_dob);
        editTextUpdateContact = findViewById(R.id.editText_update_contact);
        editTextUpdateAddress = findViewById(R.id.editText_update_address);
        editTextUpdateBlood = findViewById(R.id.editText_update_blood);
        radioGroupUpdateGender = findViewById(R.id.radio_group_update_profile_gender);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        assert firebaseUser != null;
        showProfile(firebaseUser);

        TextView buttonupdateimage = findViewById(R.id.textView_profile_upload_pic);

        buttonupdateimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, UploadImageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView buttonupdatemail = findViewById(R.id.textView_profile_update_email);

        buttonupdatemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editTextUpdateDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textSADob[] = textDob.split("/");
                int day = Integer.parseInt(textSADob[0]);
                int month = Integer.parseInt(textSADob[1]) -1;
                int year = Integer.parseInt(textSADob[2]);

                DatePickerDialog picker;

                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextUpdateDoB.setText(dayOfMonth + "/"+ (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        Button buttonupdateprofile = findViewById(R.id.button_update_profile);
        buttonupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateprofile(firebaseUser);
            }
        });
    }

    private void updateprofile(FirebaseUser firebaseUser) {
        int selectedGenderID = radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGender = findViewById(selectedGenderID);

        if(TextUtils.isEmpty(textFirstName)){
            Toast.makeText(UpdateProfileActivity.this, "Please Enter First Name", Toast.LENGTH_LONG).show();
            editTextUpdateFirstName.setError("Full Name is Required");
            editTextUpdateFirstName.requestFocus();
        }else if (TextUtils.isEmpty(textLastName)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
            editTextUpdateLastName.setError("Full Name is Required");
            editTextUpdateLastName.requestFocus();
        } else if (TextUtils.isEmpty(textNatID)){
            Toast.makeText(UpdateProfileActivity.this, "Please Enter National ID Number", Toast.LENGTH_LONG).show();
            editTextUpdateNatID.setError("Full Name is Required");
            editTextUpdateNatID.requestFocus();
        } else if (TextUtils.isEmpty(textAddress)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter Address", Toast.LENGTH_LONG).show();
            editTextUpdateAddress.setError("Address is Required");
            editTextUpdateAddress.requestFocus();
        } else if (TextUtils.isEmpty(textBlood)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter Blood Group", Toast.LENGTH_LONG).show();
            editTextUpdateBlood.setError("Username is Required");
            editTextUpdateBlood.requestFocus();
        } else if (TextUtils.isEmpty(textDob)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter date", Toast.LENGTH_LONG).show();
            editTextUpdateDoB.setError("Date of Birth is Required");
            editTextUpdateDoB.requestFocus();
        } else if (TextUtils.isEmpty(radioButtonUpdateGender.getText())) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter gender", Toast.LENGTH_LONG).show();
            radioButtonUpdateGender.setError("Gender is Required");
            radioButtonUpdateGender.requestFocus();
        } else if (TextUtils.isEmpty(textContact)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter phone number", Toast.LENGTH_LONG).show();
            editTextUpdateContact.setError("Phone Number is Required");
            editTextUpdateContact.requestFocus();
        } else if (editTextUpdateContact.length() != 10) {
            Toast.makeText(UpdateProfileActivity.this, "Please Re-Enter phone number", Toast.LENGTH_LONG).show();
            editTextUpdateContact.setError("Phone Number should be 10 digits");
            editTextUpdateContact.requestFocus();
        } else {
            textFirstName = editTextUpdateFirstName.getText().toString();
            textLastName = editTextUpdateLastName.getText().toString();
            textNatID = editTextUpdateNatID.getText().toString();
            textAddress = editTextUpdateAddress.getText().toString();
            textBlood = editTextUpdateBlood.getText().toString();
            textDob = editTextUpdateDoB.getText().toString();
            textContact = editTextUpdateContact.getText().toString();
            textGender = radioButtonUpdateGender.getText().toString();

            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFirstName,textLastName,textNatID,textAddress,textBlood, textDob, textGender, textContact);

            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");

            String userID = firebaseUser.getUid();

            progressBar.setVisibility(View.VISIBLE);

            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(textFirstName).build();
                        firebaseUser.updateProfile(profileUpdate);

                        Toast.makeText(UpdateProfileActivity.this,"Update Successful", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(UpdateProfileActivity.this, UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else{
                        try{
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(UpdateProfileActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegd = firebaseUser.getUid();

        DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered users");

        progressBar.setVisibility(View.VISIBLE);

        referenceprofile.child(userIDofRegd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if(readUserDetails != null){
                    textFirstName = firebaseUser.getDisplayName();
                    textLastName = readUserDetails.lastname;
                    textNatID = readUserDetails.natid;
                    textDob = readUserDetails.dob;
                    textAddress = readUserDetails.address;
                    textContact = readUserDetails.contact;
                    textBlood = readUserDetails.blood;
                    textGender = readUserDetails.gender;

                    editTextUpdateFirstName.setText(textFirstName);
                    editTextUpdateLastName.setText(textLastName);
                    editTextUpdateNatID.setText(textNatID);
                    editTextUpdateContact.setText(textContact);
                    editTextUpdateDoB.setText(textDob);
                    editTextUpdateAddress.setText(textAddress);
                    editTextUpdateBlood.setText(textBlood);

                    if(textGender.equals("Male")){
                        radioButtonUpdateGender = findViewById(R.id.update_radio_male);
                    } else {
                        radioButtonUpdateGender = findViewById(R.id.update_radio_female);
                    }
                    radioButtonUpdateGender.setChecked(true);
                } else{
                    Toast.makeText(UpdateProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.update_mail){
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_settings) {
            Toast.makeText(UpdateProfileActivity.this, "Settings Under Construction", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_password) {
            Intent intent = new Intent(UpdateProfileActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.delete_user) {
            Intent intent = new Intent(UpdateProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_logout) {
            authProfile.signOut();
            Toast.makeText(UpdateProfileActivity.this, "Logged out" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateProfileActivity.this, PatientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(UpdateProfileActivity.this, "Something is Wrong" , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}