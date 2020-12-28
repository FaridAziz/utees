package org.kuliah.utees;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.kuliah.utees.model.Request;

import static io.perfmark.PerfMark.setEnabled;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = "data";
    private EditText tnama, temail, ttelepon, tig;
    private ProgressDialog loading;
    private Button btn_delete, btn_save, btnFoto;
    private ImageView imageView;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

//    private String sPid, sPnama, sPemail, sPtelepon, sPimg;

    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

//        sPid = getIntent().getStringExtra("id");
//        sPnama = getIntent().getStringExtra("nama");
//        sPemail = getIntent().getStringExtra("email");
//        sPtelepon = getIntent().getStringExtra("telepon");
//        sPimg = getIntent().getStringExtra("imageUrl");

        tnama = findViewById(R.id.nama);
        temail = findViewById(R.id.email);
        ttelepon = findViewById(R.id.telepon);
        tig = findViewById(R.id.ig);
        imageView = findViewById(R.id.gmb);
        btnFoto = (Button)findViewById(R.id.btnFoto);

//        tnama.setText(sPnama);
//        temail.setText(sPemail);
//        ttelepon.setText(sPtelepon);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnabled(false);
                String snama = tnama.getText().toString();
                String semail = temail.getText().toString();
                String stelepon = ttelepon.getText().toString();
                String sig = tig.getText().toString();

                if (snama.equals("")) {
                    tnama.setError("Silahkan masukan nama");
                    tnama.requestFocus();
                } else if (semail.equals("")) {
                    temail.setError("Silahkan masukan email");
                    temail.requestFocus();
                } else if (ttelepon.equals("")) {
                    ttelepon.setError("Silahkan masukan telepon");
                    ttelepon.requestFocus();
                } else if (mImageUri != null){
                    loading = ProgressDialog.show(MainActivity.this,
                            null,
                            "Please Wait",
                            true,
                            false);
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));
                    mUploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {
                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUrl = uri.toString();
                                                    //createNewPost(imageUrl);

                                                        MainActivity.this.submitUser(new Request(
                                                                snama.toLowerCase(),
                                                                semail.toLowerCase(),
                                                                stelepon.toLowerCase(),
                                                                sig.toLowerCase(),
                                                                imageUrl.toLowerCase()));
                                                }
                                            });
                                        }
                                    }

                                }
                            });
                }
                setEnabled(false);
            }
        });
    }

    private void submitUser(Request request) {
        mDatabaseRef.child("Request")
                .push()
                .setValue(request)
                .addOnSuccessListener(this, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object avoid) {
                        loading.dismiss();

                        tnama.setText("");
                        temail.setText("");
                        ttelepon.setText("");
                        tig.setText("");
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_a_photo_24));

                        Toast.makeText(MainActivity.this,
                                "Data berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_list:
                Toast.makeText(this, "Daftar Kontak Page Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, List.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public void onBackPressed(){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Confirm Exit..!");
//        alertDialogBuilder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
//        alertDialogBuilder.setMessage("Beneran mau keluar??");
//        alertDialogBuilder.setCancelable(false);
//
//        alertDialogBuilder.setPositiveButton("Yes" , (dialog, which) -> {
//                finish();
//        });
//
//        alertDialogBuilder.setNegativeButton("No" , (dialog, which) -> {
//            Toast.makeText(MainActivity.this, " gajadi keluar", Toast.LENGTH_LONG).show();
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
