package com.example.mysqlimagesuploaderlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class NavigationActivity extends AppCompatActivity {

    Button anArtist, aClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        anArtist = findViewById(R.id.anArtistBtn);
        aClient = findViewById(R.id.aClientBtn);

        anArtist.setOnClickListener(v -> {
            final Intent i = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivity(i);
        });

        aClient.setOnClickListener(v -> {
            final Intent i = new Intent(NavigationActivity.this, ClientLoginActivity.class);
            startActivity(i);
        });
    }
}