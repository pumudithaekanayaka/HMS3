package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class DiagnosticDataEnterActivity extends AppCompatActivity {

    private EditText txtpatname,txtdiagid,txtpatid,txtsymptoms,txtdiag,txtmed,txtdocmail,txtnotes;
    private ProgressBar prgbar;
    private static final String TAG = "DiagnosticDataEnterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_data_enter);

        Objects.requireNonNull(getSupportActionBar()).hide();
        Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter Data", Toast.LENGTH_LONG).show();

        txtpatname = findViewById(R.id.txt_editpatientname);
        txtdiagid = findViewById(R.id.txt_editdiagID);
        txtpatid = findViewById(R.id.txt_patID);
        txtsymptoms = findViewById(R.id.txt_editsymptoms);
        txtdiag = findViewById(R.id.txt_editdiag);
        txtmed = findViewById(R.id.txt_editmed);
        txtdocmail = findViewById(R.id.txt_docmail);
        txtnotes = findViewById(R.id.txt_editnotes);

        prgbar = findViewById(R.id.progbar);



        Button savebtn = findViewById(R.id.btn_save);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textpatname = txtpatname.getText().toString();
                String textpatid = txtpatid.getText().toString();
                String textdiagid = txtdiagid.getText().toString();
                String textsymptoms = txtsymptoms.getText().toString();
                String textdiag = txtdiag.getText().toString();
                String textmed = txtmed.getText().toString();
                String textdocmail = txtdocmail.getText().toString();
                String textnotes = txtnotes.getText().toString();

                if(TextUtils.isEmpty(textpatname)){
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter First Name", Toast.LENGTH_LONG).show();
                    txtpatname.setError("Full Name is Required");
                    txtpatname.requestFocus();
                }else if (TextUtils.isEmpty(textpatid)) {
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
                    txtpatid.setError("Full Name is Required");
                    txtpatid.requestFocus();
                } else if (TextUtils.isEmpty(textdiagid)) {
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    txtdiagid.setError("E-mail Address is Required");
                    txtdiagid.requestFocus();
                } else if (TextUtils.isEmpty(textsymptoms)){
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter National ID Number", Toast.LENGTH_LONG).show();
                    txtsymptoms.setError("Full Name is Required");
                    txtsymptoms.requestFocus();
                } else if (TextUtils.isEmpty(textdiag)) {
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter Address", Toast.LENGTH_LONG).show();
                    txtdiag.setError("Address is Required");
                    txtdiag.requestFocus();
                } else if (TextUtils.isEmpty(textdocmail)) {
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    txtdocmail.setError("E-mail Address is Required");
                    txtdocmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textdocmail).matches()) {
                    Toast.makeText(DiagnosticDataEnterActivity.this, "Please Enter valid Email", Toast.LENGTH_LONG).show();
                    txtdocmail.setError("Valid E-mail Address is Required");
                    txtdocmail.requestFocus();
                }else {
                    prgbar.setVisibility(View.VISIBLE);
                    DiagData(textpatname,textpatid,textdiagid,textsymptoms,textdiag,textmed,textdocmail,textnotes);
                }
            }
        });
    }

    private void DiagData(String textpatname,String textpatid,String textdiagid,String textsymptoms,String textdiag,String textmed,String textdocmail,String textnotes) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textdocmail,textdiagid).addOnCompleteListener(DiagnosticDataEnterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textpatid).build();
                    assert firebaseUser != null;
                    firebaseUser.updateProfile(profileChangeRequest);
                    ReadWriteDiagDetails writeUserDetails = new ReadWriteDiagDetails(textpatname,textpatid,textdiagid,textsymptoms,textdiag,textmed,textdocmail,textnotes);
                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Diagnostic Data");

                    referenceprofile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(DiagnosticDataEnterActivity.this, "Diagnostic Data added successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DiagnosticDataEnterActivity.this, UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(DiagnosticDataEnterActivity.this, "Task Failed successfully", Toast.LENGTH_LONG).show();
                                prgbar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(DiagnosticDataEnterActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    prgbar.setVisibility(View.GONE);
                }
            }
        });
    }
}