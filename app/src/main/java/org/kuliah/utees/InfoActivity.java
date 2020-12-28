package org.kuliah.utees;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    private TextView tnama, temail, ttelepon, tig;
    private String sPid, sPnama, sPemail, sPtelepon, sPig, sPimg;
    private ImageView imageView;
    Button btn_telpon, btn_email, btn_ig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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
        imageView = findViewById(R.id.img);

        tnama.setText(sPnama);
        temail.setText(sPemail);
        ttelepon.setText(sPtelepon);
        tig.setText(sPig);
        Picasso.get().load(sPimg).into(imageView);

        btn_telpon = (Button)findViewById(R.id.telepon_act);
        btn_email = (Button)findViewById(R.id.email_act);
        btn_ig = (Button)findViewById(R.id.ig_act);

        btn_telpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+sPtelepon));
                startActivity(intent);

            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = temail.getText().toString().trim();

                sendMail(email);


            }
        });

        btn_ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("http://instagram.com/_u/"+sPig);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/"+sPig)));
                }


            }
        });
    }

    private void sendMail(String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

        try {
            startActivity(Intent.createChooser(intent, "pilih"));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}