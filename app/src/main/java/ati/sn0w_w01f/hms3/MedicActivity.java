package ati.sn0w_w01f.hms3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MedicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button viewUsers = findViewById(R.id.View_Users);
        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicActivity.this,UserListActivity.class);
                startActivity(intent);
            }
        });

        Button medic = findViewById(R.id.Medic_login);
        medic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicActivity.this,DoctorDataActivity.class);
                startActivity(intent);
            }
        });

        Button doc = findViewById(R.id.Doc_login);
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicActivity.this,DiagnosticDataActivity.class);
                startActivity(intent);
            }
        });
    }
}