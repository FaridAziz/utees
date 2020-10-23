package org.kuliah.utees;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.kuliah.utees.model.Request;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database;

    private static final String TAG = "data";
    private EditText tnama, temail, ttelepon;
    private ProgressDialog loading;
    private Button btn_delete, btn_save;

    private String sPid, sPnama, sPemail, sPdesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();

        tnama = findViewById(R.id.nama);
        temail = findViewById(R.id.email);
        ttelepon = findViewById(R.id.telepon);

        tnama = findViewById(R.id.nama);
        temail = findViewById(R.id.email);
        ttelepon = findViewById(R.id.telepon);

        findViewById(R.id.simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String snama = tnama.getText().toString();
                String semail = temail.getText().toString();
                String stelepon = ttelepon.getText().toString();

                if (snama.equals("")) {
                    tnama.setError("Silahkan masukan nama");
                    tnama.requestFocus();
                } else if (semail.equals("")) {
                    temail.setError("Silahkan masukan email");
                    temail.requestFocus();
                } else if (ttelepon.equals("")) {
                    ttelepon.setError("Silahkan masukan telepon");
                    ttelepon.requestFocus();
                } else {
                    loading = ProgressDialog.show(MainActivity.this,
                            null,
                            "Please Wait",
                            true,
                            false);

                    MainActivity.this.submitUser(new Request(
                            snama.toLowerCase(),
                            semail.toLowerCase(),
                            stelepon.toLowerCase()));
                }
            }
        });

    }

    private void submitUser(Request request) {
        database.child("Request")
                .push()
                .setValue(request)
                .addOnSuccessListener(this, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object avoid) {
                        loading.dismiss();

                        tnama.setText("");
                        temail.setText("");
                        ttelepon.setText("");

                        Toast.makeText(MainActivity.this,
                                "Data berhasil ditambahkan",
                                Toast.LENGTH_SHORT).show();

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
                Toast.makeText(this, "List Page Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, List.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Exit..!");
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        alertDialogBuilder.setMessage("Beneran mau keluar??");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes" , (dialog, which) -> {
                finish();
        });

        alertDialogBuilder.setNegativeButton("No" , (dialog, which) -> {
            Toast.makeText(MainActivity.this, " gajadi keluar", Toast.LENGTH_LONG).show();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
