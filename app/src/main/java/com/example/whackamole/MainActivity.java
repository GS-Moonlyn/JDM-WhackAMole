package com.example.whackamole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
    }

    private void onClick(View view){
        if(view == btnPlay){
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            finish();
        }
    }
}