package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextloginmail,editTextloginpassword;
    private ProgressBar progressBar;
    private TextView textView;
    private FirebaseAuth firebaselogin;
    private static final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView_login_head);
        Objects.requireNonNull(getSupportActionBar()).hide();

        editTextloginmail = findViewById(R.id.txt_loginmail);
        editTextloginpassword = findViewById(R.id.txt_loginpwd);
        progressBar = findViewById(R.id.progressBar);
        firebaselogin = FirebaseAuth.getInstance();

        Button forgotpwd = findViewById(R.id.button_forgot);
        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Reset Your Password",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        ImageView imageViewhidepwd = findViewById(R.id.hide_pwd);
        imageViewhidepwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewhidepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextloginpassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editTextloginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewhidepwd.setImageResource(R.drawable.ic_hide_pwd);
                } else{
                    editTextloginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewhidepwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        Button button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginmail = editTextloginmail.getText().toString();
                String loginpassword = editTextloginpassword.getText().toString();

                if(TextUtils.isEmpty(loginmail)){
                    Toast.makeText(LoginActivity.this,"Please Enter your E-mail",Toast.LENGTH_SHORT).show();
                    editTextloginmail.setError("E-Mail is Required");
                    editTextloginmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(loginmail).matches()){
                    Toast.makeText(LoginActivity.this,"Please Enter valid E-mail",Toast.LENGTH_SHORT).show();
                    editTextloginmail.setError("Valid E-Mail is Required");
                    editTextloginmail.requestFocus();
                } else if (TextUtils.isEmpty(loginpassword)) {
                    Toast.makeText(LoginActivity.this,"Please Enter your password",Toast.LENGTH_SHORT).show();
                    editTextloginpassword.setError("Password is Required");
                    editTextloginpassword.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginuser(loginmail,loginpassword);
                }
            }
        });
    }


    private void loginuser(String loginmail, String loginpassword) {
        firebaselogin.signInWithEmailAndPassword(loginmail,loginpassword).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser = firebaselogin.getCurrentUser();
                    assert firebaseUser != null;
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this,"Login Succeeded",Toast.LENGTH_SHORT).show();
                    }else{
                        firebaseUser.sendEmailVerification();
                        firebaselogin.signOut();
                        showAlertDialog();
                    }
                    Toast.makeText(LoginActivity.this,"Login Succeeded",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                    finish();
                } else {
                    try{
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidUserException e){
                        editTextloginmail.setError("User doesn't Exist yet");
                        editTextloginpassword.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextloginmail.setError("Invalid Credentials");
                        editTextloginpassword.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("E-Mail Not verified");
        builder.setMessage("Verify E-mail now, Can't Login without verification");

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

    @Override
    protected void onStart(){
        super.onStart();
        if(firebaselogin.getCurrentUser() !=null){
            Toast.makeText(LoginActivity.this,"Already Logged in", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
            finish();
        }else{
            Toast.makeText(LoginActivity.this,"You can Login now", Toast.LENGTH_LONG).show();
        }
    }
}