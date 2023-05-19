package ati.sn0w_w01f.hms3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class DoctorDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_data);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button medic = findViewById(R.id.button_add_doc_data);
        medic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDataActivity.this,DoctorDataAddActivity.class);
                startActivity(intent);
            }
        });
    }
}