package org.kuliah.utees;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class DetailActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private static final String TAG = "data";
    private EditText tnama, temail, ttelepon, tig;
    private ProgressDialog loading;
    private Button btn_delete, btn_save, btnFoto, hapus;
    private ImageView imageView;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private String sPid, sPnama, sPemail, sPtelepon, sPig, sPimg;

    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        sPid = getIntent().getStringExtra("id");
        sPnama = getIntent().getStringExtra("nama");
        sPemail = getIntent().getStringExtra("email");
        sPtelepon = getIntent().getStringExtra("telepon");
        sPig = getIntent().getStringExtra("ig");
        sPimg = getIntent().getStringExtra("imageUrl");

        tnama = findViewById(R.id.nama);
        temail = findViewById(R.id.email);
        ttelepon = findViewById(R.id.telepon);
        tig = findViewById(R.id.ig);
        imageView = findViewById(R.id.gmb);
        btnFoto = (Button)findViewById(R.id.btnFoto);
        hapus = (Button)findViewById(R.id.hapus);

        tnama.setText(sPnama);
        temail.setText(sPemail);
        ttelepon.setText(sPtelepon);
        tig.setText(sPig);
        Picasso.get().load(sPimg).into(imageView);

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
                    loading = ProgressDialog.show(DetailActivity.this,
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

                                                    DetailActivity.this.editUser(new Request(
                                                            snama.toLowerCase(),
                                                            semail.toLowerCase(),
                                                            stelepon.toLowerCase(),
                                                            sig.toLowerCase(),
                                                            imageUrl.toLowerCase()),sPid);
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

        hapus.setOnClickListener((view) -> {
            mDatabaseRef.child("Request")
                    .child(sPid)
                    .removeValue();

            Toast.makeText(DetailActivity.this,
                    "Data berhasil dihapus",
                    Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void editUser(Request request, String id) {
        mDatabaseRef.child("Request")
                .child(id)
                .setValue(request)
                .addOnSuccessListener(this, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object avoid) {
                        loading.dismiss();

                        tnama.setText("");
                        temail.setText("");
                        ttelepon.setText("");
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_add_a_photo_24));

                        Toast.makeText(DetailActivity.this,
                                "Data berhasil diubah",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

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