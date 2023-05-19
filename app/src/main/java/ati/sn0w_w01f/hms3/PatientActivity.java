package ati.sn0w_w01f.hms3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class PatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Button btnlogin = findViewById(R.id.login_btn);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        Button btnreg = findViewById(R.id.reg_btn);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}