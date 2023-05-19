package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UpdatePasswordActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private TextView textViewAuth;
    private String OldPassword;
    private Button buttonChangePassword,buttonReAuth;
    private EditText editTextOldPassword,editTextNewPassword,editTextConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Change your Password");

        editTextOldPassword=findViewById(R.id.editText_change_pwd_current);
        editTextNewPassword = findViewById(R.id.editText_change_pwd_new);
        editTextConfirmNewPassword = findViewById(R.id.editText_change_conf_pwd_new);
        textViewAuth = findViewById(R.id.textView_change_pwd_authenticated);
        progressBar = findViewById(R.id.progressbar);
        buttonChangePassword =findViewById(R.id.button_change_pwd);
        buttonReAuth = findViewById(R.id.button_change_pwd_authenticate);

        editTextNewPassword.setEnabled(false);
        editTextConfirmNewPassword.setEnabled(false);
        buttonChangePassword.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        assert firebaseUser != null;
        if(firebaseUser.equals("")){
            Toast.makeText(UpdatePasswordActivity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdatePasswordActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }else{
            reAuthPassword(firebaseUser);
        }
    }

    private void reAuthPassword(FirebaseUser firebaseUser) {
        buttonReAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OldPassword = editTextOldPassword.getText().toString();

                if(TextUtils.isEmpty(OldPassword)){
                    Toast.makeText(UpdatePasswordActivity.this, "Password is Needed", Toast.LENGTH_SHORT).show();
                    editTextOldPassword.setError("Please Enter Your Current Password");
                    editTextOldPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()),OldPassword);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                editTextOldPassword.setEnabled(false);
                                editTextNewPassword.setEnabled(true);
                                editTextConfirmNewPassword.setEnabled(true);

                                buttonReAuth.setEnabled(false);
                                buttonChangePassword.setEnabled(true);

                                textViewAuth.setText("Authentication Complete" +  "Change Password now");
                                Toast.makeText(UpdatePasswordActivity.this, "Password has Verified", Toast.LENGTH_SHORT).show();

                                buttonChangePassword.setBackgroundTintList(ContextCompat.getColorStateList(UpdatePasswordActivity.this,R.color.dark_green));

                                buttonChangePassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        changePassword(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                }catch (Exception e){
                                    Toast.makeText(UpdatePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePassword(FirebaseUser firebaseUser) {
        String newPassword = editTextNewPassword.getText().toString();
        String newConfPassword = editTextConfirmNewPassword.getText().toString();

        if(TextUtils.isEmpty(newPassword)){
            Toast.makeText(UpdatePasswordActivity.this, "New Password is Needed", Toast.LENGTH_SHORT).show();
            editTextNewPassword.setError("Enter new password");
            editTextNewPassword.requestFocus();
        } else if (TextUtils.isEmpty(newConfPassword)) {
            Toast.makeText(UpdatePasswordActivity.this, "Password Confirmation Needed", Toast.LENGTH_SHORT).show();
            editTextConfirmNewPassword.setError("Confirm Password");
            editTextConfirmNewPassword.requestFocus();
        } else if (!newPassword.matches(newConfPassword)) {
            Toast.makeText(UpdatePasswordActivity.this, "Password Don't Match", Toast.LENGTH_SHORT).show();
            editTextConfirmNewPassword.setError("Use Same Password");
            editTextConfirmNewPassword.requestFocus();
        } else if (OldPassword.matches(newPassword)) {
            Toast.makeText(UpdatePasswordActivity.this, "Don't use oldPassword", Toast.LENGTH_SHORT).show();
            editTextNewPassword.setError("Same old Password");
            editTextNewPassword.requestFocus();
        } else{
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UpdatePasswordActivity.this, "Password has Changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdatePasswordActivity.this, UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception e) {
                            Toast.makeText(UpdatePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
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
            Intent intent = new Intent(UpdatePasswordActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.update_mail){
            Intent intent = new Intent(UpdatePasswordActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_settings) {
            Toast.makeText(UpdatePasswordActivity.this, "Settings Under Construction", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_password) {
            Intent intent = new Intent(UpdatePasswordActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.delete_user) {
            Intent intent = new Intent(UpdatePasswordActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_logout) {
            authProfile.signOut();
            Toast.makeText(UpdatePasswordActivity.this, "Logged out" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdatePasswordActivity.this, PatientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(UpdatePasswordActivity.this, "Something is Wrong" , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}