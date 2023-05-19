package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DoctorDataAddActivity extends AppCompatActivity {

    private EditText txtdocname,txtdocid,txtdocmail,txtdocpwd,txtconfpwd,txtcontact;
    private ProgressBar prgbar;
    private static final String TAG = "DoctorDataAddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_data_add);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Toast.makeText(DoctorDataAddActivity.this, "Please Enter Data", Toast.LENGTH_LONG).show();

        txtdocname = findViewById(R.id.txt_editdocname);
        txtdocid = findViewById(R.id.txt_editdocID);
        txtdocmail = findViewById(R.id.txt_docmail);
        txtdocpwd = findViewById(R.id.txt_editdocpwd);
        txtconfpwd = findViewById(R.id.txt_editconfpwd);
        txtcontact = findViewById(R.id.txt_editdoccontatct);

        prgbar = findViewById(R.id.progbar);



        Button savebtn = findViewById(R.id.btn_save);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textdocname = txtdocname.getText().toString();
                String textdocid = txtdocid.getText().toString();
                String textdocmail = txtdocmail.getText().toString();
                String textdocpwd = txtdocpwd.getText().toString();
                String textconfpwd = txtconfpwd.getText().toString();
                String texdocmobile = txtcontact.getText().toString();

                if(TextUtils.isEmpty(textdocname)){
                    Toast.makeText(DoctorDataAddActivity.this, "Please Enter First Name", Toast.LENGTH_LONG).show();
                    txtdocname.setError("Full Name is Required");
                    txtdocname.requestFocus();
                }else if (TextUtils.isEmpty(textdocid)) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
                    txtdocid.setError("Full Name is Required");
                    txtdocid.requestFocus();
                } else if (TextUtils.isEmpty(textdocmail)) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    txtdocmail.setError("E-mail Address is Required");
                    txtdocmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textdocmail).matches()) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Enter valid Email", Toast.LENGTH_LONG).show();
                    txtdocmail.setError("Valid E-mail Address is Required");
                    txtdocmail.requestFocus();
                } else if (TextUtils.isEmpty(textdocpwd)) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    txtdocpwd.setError("Password is Required");
                    txtdocpwd.requestFocus();
                } else if (textdocpwd.length() < 4) {
                    Toast.makeText(DoctorDataAddActivity.this, "Password is too short", Toast.LENGTH_LONG).show();
                    txtconfpwd.setError("Password at least 5 characters");
                    txtconfpwd.requestFocus();
                } else if (TextUtils.isEmpty(textconfpwd)) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Confirm the Password", Toast.LENGTH_LONG).show();
                    txtconfpwd.setError("Password Confirmation is Required");
                    txtconfpwd.requestFocus();
                } else if (!textdocpwd.equals(textconfpwd)) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please use the same Password", Toast.LENGTH_LONG).show();
                    txtconfpwd.setError("Password Confirmation is Required");
                    txtconfpwd.requestFocus();
                    txtdocpwd.clearComposingText();
                    txtconfpwd.clearComposingText();
                } else if (TextUtils.isEmpty(texdocmobile)) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Enter phone number", Toast.LENGTH_LONG).show();
                    txtcontact.setError("Phone Number is Required");
                    txtcontact.requestFocus();
                } else if (texdocmobile.length() != 10) {
                    Toast.makeText(DoctorDataAddActivity.this, "Please Re-Enter phone number", Toast.LENGTH_LONG).show();
                    txtcontact.setError("Phone Number should be 10 digits");
                    txtcontact.requestFocus();
                } else {
                    prgbar.setVisibility(View.VISIBLE);
                    DocData(textdocname,textdocid,textdocmail,textdocpwd,texdocmobile);
                }
            }
        });
    }

    private void DocData(String textdocname,String  textdocmail,String textdocpwd,String textdocid,String texdocmobile) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textdocmail,textdocpwd).addOnCompleteListener(DoctorDataAddActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textdocid).build();
                    assert firebaseUser != null;
                    firebaseUser.updateProfile(profileChangeRequest);
                    ReadWriteDocDetails writeUserDetails = new ReadWriteDocDetails(textdocname,textdocmail,textdocid,texdocmobile);
                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Doctor Data");

                    referenceprofile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(DoctorDataAddActivity.this, "Diagnostic Data added successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DoctorDataAddActivity.this, UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(DoctorDataAddActivity.this, "Task Failed successfully", Toast.LENGTH_LONG).show();
                                prgbar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    }catch (FirebaseAuthWeakPasswordException e){
                        txtdocpwd.setError("Your Password is too weak");
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        txtdocpwd.setError("Task failed Successfully");
                        txtdocpwd.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        txtdocpwd.setError("Already registered user");
                        txtdocpwd.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(DoctorDataAddActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    prgbar.setVisibility(View.GONE);
                }
            }
        });
    }
}