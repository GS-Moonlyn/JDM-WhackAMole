package com.example.whackamole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class RankingActivity extends AppCompatActivity {

    TextView txtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        txtScore = findViewById(R.id.txtScore);

    }

    @Override
    protected void onStart() {
        super.onStart();

        int score = 0;

        try {
            FileInputStream fis = openFileInput("score.txt");

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fis));

            String line = br.readLine();
            score = Integer.parseInt(line);

            while (line != null) {
                score = Integer.parseInt(line);
                line = br.readLine();

            }

            br.close();
            fis.close();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        txtScore.setText(String.valueOf(score));
    }

    public void mainMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}