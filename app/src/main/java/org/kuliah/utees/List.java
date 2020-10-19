package org.kuliah.utees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.kuliah.utees.adapter.RequestAdapterRecycleview;
import org.kuliah.utees.model.Request;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    DatabaseReference database;

    ArrayList<Request>daftarReq;
    RequestAdapterRecycleview requestAdapterRecycleview;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;
    private FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = findViewById(R.id.rv_request);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(List.this,
                null,
                "Please wait...",
                true,
                false);

        database.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    Request requests = noteDataSnapshot.getValue(Request.class);
                    requests.setKey(noteDataSnapshot.getKey());

                    daftarReq.add(requests);
                }

                requestAdapterRecycleview = new RequestAdapterRecycleview(daftarReq);
                rc_list_request.setAdapter(requestAdapterRecycleview);
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
                loading.dismiss();
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
            case R.id.nav_tambah:
                Toast.makeText(this, "List Page Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(List.this, MainActivity.class);
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
//            finish();
//        });
//
//        alertDialogBuilder.setNegativeButton("No" , (dialog, which) -> {
//            Toast.makeText(List.this, " gajadi keluar", Toast.LENGTH_LONG).show();
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
}