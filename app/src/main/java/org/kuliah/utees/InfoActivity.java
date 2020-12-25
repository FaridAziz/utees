package org.kuliah.utees;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    private TextView tnama, temail, ttelepon;
    private String sPid, sPnama, sPemail, sPtelepon, sPimg;
    private ImageView imageView;
    Button btn_telpon, btn_email;

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

        btn_telpon = (Button)findViewById(R.id.telepon_act);

        btn_telpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+sPtelepon));
                startActivity(intent);

            }
        });
    }
}