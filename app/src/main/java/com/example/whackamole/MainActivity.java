package com.example.whackamole;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnRanking = findViewById(R.id.btnRanking);
    }

    public void startGame(View view){
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            finish();
    }

    public void rankingActivity(View view) {
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
        finish();
    }
}