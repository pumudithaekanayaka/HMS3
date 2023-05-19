package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Calendar;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtfirstname,txtlastname,txtemail,txtnatid,txtaddress,txtdob,txtnumber,txtblood,txtpassword,txtconfpwd;
    private ProgressBar prgbar;
    private RadioGroup rdgender;
    private RadioButton rbgender;
    private static final String TAG = "RegisterActivity";
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).hide();
        Toast.makeText(RegisterActivity.this, "Please Enter Data", Toast.LENGTH_LONG).show();

        txtfirstname = findViewById(R.id.txt_editfirstname);
        txtlastname = findViewById(R.id.txt_editlastname);
        txtemail = findViewById(R.id.txt_editmail);
        txtnatid = findViewById(R.id.txt_editnatid);
        txtaddress = findViewById(R.id.txt_editaddress);
        txtblood = findViewById(R.id.txt_editblood);
        txtpassword = findViewById(R.id.txt_editpassword);
        txtconfpwd = findViewById(R.id.txt_editconfpassword);
        txtdob = findViewById(R.id.txt_editdob);
        txtnumber = findViewById(R.id.txt_editcontact);

        prgbar = findViewById(R.id.progbar);

        rdgender = findViewById(R.id.rg_gender);
        rdgender.clearCheck();

        txtdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtdob.setText(dayOfMonth + "/"+ (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });



        Button savebtn = findViewById(R.id.btn_save);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int genderid = rdgender.getCheckedRadioButtonId();
                rbgender = findViewById(genderid);

                String textfirstname = txtfirstname.getText().toString();
                String textlastname = txtlastname.getText().toString();
                String textemail = txtemail.getText().toString();
                String textnatid = txtnatid.getText().toString();
                String textaddress = txtaddress.getText().toString();
                String textblood = txtblood.getText().toString();
                String textpassword = txtpassword.getText().toString();
                String textconfirmpassword = txtconfpwd.getText().toString();
                String textdob = txtdob.getText().toString();
                String textnumber = txtnumber.getText().toString();
                String textgender;

                if(TextUtils.isEmpty(textfirstname)){
                    Toast.makeText(RegisterActivity.this, "Please Enter First Name", Toast.LENGTH_LONG).show();
                    txtfirstname.setError("Full Name is Required");
                    txtfirstname.requestFocus();
                }else if (TextUtils.isEmpty(textlastname)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
                    txtlastname.setError("Last Name is Required");
                    txtlastname.requestFocus();
                } else if (TextUtils.isEmpty(textemail)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    txtemail.setError("E-mail Address is Required");
                    txtemail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter valid Email", Toast.LENGTH_LONG).show();
                    txtemail.setError("Valid E-mail Address is Required");
                    txtemail.requestFocus();
                } else if (TextUtils.isEmpty(textnatid)){
                    Toast.makeText(RegisterActivity.this, "Please Enter National ID Number", Toast.LENGTH_LONG).show();
                    txtnatid.setError("Full Name is Required");
                    txtnatid.requestFocus();
                } else if (TextUtils.isEmpty(textaddress)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Address", Toast.LENGTH_LONG).show();
                    txtaddress.setError("Address is Required");
                    txtaddress.requestFocus();
                } else if (TextUtils.isEmpty(textblood)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Blood Group", Toast.LENGTH_LONG).show();
                    txtblood.setError("Blood Group is Required");
                    txtblood.requestFocus();
                } else if (TextUtils.isEmpty(textpassword)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    txtpassword.setError("Password is Required");
                    txtpassword.requestFocus();
                } else if (txtpassword.length() < 4) {
                    Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_LONG).show();
                    txtpassword.setError("Password at least 5 characters");
                    txtpassword.requestFocus();
                } else if (TextUtils.isEmpty(textconfirmpassword)) {
                    Toast.makeText(RegisterActivity.this, "Please Confirm the Password", Toast.LENGTH_LONG).show();
                    txtconfpwd.setError("Password Confirmation is Required");
                    txtconfpwd.requestFocus();
                } else if (!textpassword.equals(textconfirmpassword)) {
                    Toast.makeText(RegisterActivity.this, "Please use the same Password", Toast.LENGTH_LONG).show();
                    txtconfpwd.setError("Password Confirmation is Required");
                    txtconfpwd.requestFocus();
                    txtpassword.clearComposingText();
                    txtconfpwd.clearComposingText();
                } else if (TextUtils.isEmpty(textdob)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter date", Toast.LENGTH_LONG).show();
                    txtdob.setError("Date of Birth is Required");
                    txtdob.requestFocus();
                } else if (rdgender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please Enter gender", Toast.LENGTH_LONG).show();
                    rbgender.setError("Gender is Required");
                    rbgender.requestFocus();
                } else if (TextUtils.isEmpty(textnumber)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter phone number", Toast.LENGTH_LONG).show();
                    txtnumber.setError("Phone Number is Required");
                    txtnumber.requestFocus();
                } else if (txtnumber.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please Re-Enter phone number", Toast.LENGTH_LONG).show();
                    txtnumber.setError("Phone Number should be 10 digits");
                    txtnumber.requestFocus();
                } else {
                    textgender = rbgender.getText().toString();
                    prgbar.setVisibility(View.VISIBLE);
                    registerUser(textfirstname,textlastname,textemail,textpassword,textnatid,textaddress,textnumber,textdob,textgender,textblood);
                }
            }
        });
    }

    private void registerUser(String textfirstname,String textlastname,String textemail,String textpassword,String textnatid,String textaddress,String textnumber,String textdob,String textgender,String textblood) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textemail,textpassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textfirstname).build();
                    assert firebaseUser != null;
                    firebaseUser.updateProfile(profileChangeRequest);
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textfirstname,textlastname,textnatid,textaddress,textnumber,textdob,textgender,textblood);
                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered users");

                    referenceprofile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, "User added successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Task Failed successfully", Toast.LENGTH_LONG).show();
                                prgbar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthWeakPasswordException e){
                        txtpassword.setError("Your Password is too weak");
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        txtpassword.setError("Task failed Successfully");
                        txtpassword.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e){
                        txtpassword.setError("Already registered user");
                        txtpassword.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    prgbar.setVisibility(View.GONE);
                }
            }
        });
    }
}