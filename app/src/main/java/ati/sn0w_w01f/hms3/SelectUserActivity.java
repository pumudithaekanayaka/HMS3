package ati.sn0w_w01f.hms3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class SelectUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button patbutton = findViewById(R.id.patient_btn);
        patbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this, PatientActivity.class);
                startActivity(intent);
            }
        });

        Button medicbutton = findViewById(R.id.medic_btn);
        medicbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this,MedicActivity.class);
                startActivity(intent);
            }
        });
    }
}