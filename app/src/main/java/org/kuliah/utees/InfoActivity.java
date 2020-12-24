package org.kuliah.utees;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    private TextView tnama, temail, ttelepon;
    private String sPid, sPnama, sPemail, sPtelepon, sPimg;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        sPid = getIntent().getStringExtra("id");
        sPnama = getIntent().getStringExtra("nama");
        sPemail = getIntent().getStringExtra("email");
        sPtelepon = getIntent().getStringExtra("telepon");
        sPimg = getIntent().getStringExtra("imageUrl");

        tnama = findViewById(R.id.nama);
        temail = findViewById(R.id.email);
        ttelepon = findViewById(R.id.telepon);
        imageView = findViewById(R.id.img);

        tnama.setText(sPnama);
        temail.setText(sPemail);
        ttelepon.setText(sPtelepon);
        Picasso.get().load(sPimg).into(imageView);
    }
}