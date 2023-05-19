package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textViewAuth;
    private String oldemail, newemail, userpassword;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail,editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update your E-mail Address");

        progressBar = findViewById(R.id.progressbar);
        editTextNewEmail = findViewById(R.id.editText_update_email_new);
        editTextPassword = findViewById(R.id.editText_update_email_verify_password);
        textViewAuth = findViewById(R.id.textView_update_email_authenticated);
        buttonUpdateEmail = findViewById(R.id.button_update_email);

        buttonUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        assert firebaseUser != null;
        oldemail = firebaseUser.getEmail();
        TextView textoldemail = findViewById(R.id.textView_update_email_old);
        textoldemail.setText(oldemail);
        
        if(firebaseUser==null){
            Toast.makeText(UpdateEmailActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
        } else {
            reAuth(firebaseUser);
        }
    }

    private void reAuth(FirebaseUser firebaseUser) {
        Button buttonverify = findViewById(R.id.button_authenticate_user);
        buttonverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               userpassword = editTextPassword.getText().toString();

               if(TextUtils.isEmpty(userpassword)){
                   Toast.makeText(UpdateEmailActivity.this, "Password is need to continue", Toast.LENGTH_SHORT).show();
                   editTextPassword.setError("Please enter your Password");
                   editTextPassword.requestFocus();
               } else{
                   progressBar.setVisibility(View.VISIBLE);

                   AuthCredential credential = EmailAuthProvider.getCredential(oldemail,userpassword);

                   firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               progressBar.setVisibility(View.GONE);

                               Toast.makeText(UpdateEmailActivity.this, "User Verified", Toast.LENGTH_SHORT).show();

                               textViewAuth.setText("User Authenticaed, Please update your E-mail");

                               editTextNewEmail.setEnabled(true);
                               editTextPassword.setEnabled(false);
                               buttonverify.setEnabled(false);
                               buttonUpdateEmail.setEnabled(true);

                               buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this, R.color.dark_green));

                               buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       newemail = editTextNewEmail.getText().toString();
                                       if(TextUtils.isEmpty(newemail)){
                                           Toast.makeText(UpdateEmailActivity.this, "New E-mail is required", Toast.LENGTH_SHORT).show();
                                           editTextNewEmail.setError("Please enter new E-Mail");
                                           editTextNewEmail.requestFocus();
                                       } else if(!Patterns.EMAIL_ADDRESS.matcher(newemail).matches()){
                                           Toast.makeText(UpdateEmailActivity.this, "A valid E-mail is required", Toast.LENGTH_SHORT).show();
                                           editTextNewEmail.setError("Please enter a valid E-Mail");
                                           editTextNewEmail.requestFocus();
                                       } else if (oldemail.matches(newemail)) {
                                           Toast.makeText(UpdateEmailActivity.this, "New E-mail cannot be current E-mail", Toast.LENGTH_SHORT).show();
                                           editTextNewEmail.setError("Please enter new E-Mail");
                                           editTextNewEmail.requestFocus();
                                       } else {
                                           progressBar.setVisibility(View.VISIBLE);
                                           updateEmail(firebaseUser);
                                       }
                                   }
                               });
                           }else {
                               try{
                                   throw task.getException();
                               } catch (Exception e){
                                   Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       }
                   });
               }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(newemail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(UpdateEmailActivity.this, "E-mail has been updated, Please verify new E-Mail", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateEmailActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (Exception e){
                        Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
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
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.update_mail){
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_settings) {
            Toast.makeText(UpdateEmailActivity.this, "Settings Under Construction", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_password) {
            Intent intent = new Intent(UpdateEmailActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.delete_user) {
            Intent intent = new Intent(UpdateEmailActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_logout) {
            authProfile.signOut();
            Toast.makeText(UpdateEmailActivity.this, "Logged out" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this, PatientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(UpdateEmailActivity.this, "Something is Wrong" , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}