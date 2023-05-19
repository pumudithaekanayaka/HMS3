package ati.sn0w_w01f.hms3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UploadImageActivity extends AppCompatActivity {

    private ProgressBar progressBar1;
    private ImageView imageView;
    private FirebaseAuth authprofile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Upload Image");

        Button chooseimage = findViewById(R.id.upload_image_choose_button);
        Button uploadimage = findViewById(R.id.upload_image_button);
        progressBar1 = findViewById(R.id.image_progressBar);
        imageView = findViewById(R.id.imageView_image);

        authprofile = FirebaseAuth.getInstance();
        firebaseUser = authprofile.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Image Data");

        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.get().load(uri).into(imageView);

        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar1.setVisibility(View.VISIBLE);
                UploadImage();
            }
        });

    }

    private void UploadImage() {
        if(uriImage != null){
            StorageReference filereference = storageReference.child(authprofile.getCurrentUser().getUid() + "." + getFileExtention(uriImage));

            filereference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authprofile.getCurrentUser();

                            UserProfileChangeRequest profileupdates = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileupdates);
                        }
                    });

                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(UploadImageActivity.this, "Image Upload Complete", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UploadImageActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            progressBar1.setVisibility(View.GONE);
            Toast.makeText(UploadImageActivity.this, "No File selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getMimeTypeFromExtension(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            uriImage = data.getData();
            imageView.setImageURI(uriImage);
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
            Intent intent = new Intent(UploadImageActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.update_mail){
            Intent intent = new Intent(UploadImageActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_settings) {
            Toast.makeText(UploadImageActivity.this, "Settings Under Construction", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.update_password) {
            Intent intent = new Intent(UploadImageActivity.this, UpdatePasswordActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.delete_user) {
            Intent intent = new Intent(UploadImageActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.user_logout) {
            authprofile.signOut();
            Toast.makeText(UploadImageActivity.this, "Logged out" , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UploadImageActivity.this, PatientActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else{
            Toast.makeText(UploadImageActivity.this, "Something is Wrong" , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}