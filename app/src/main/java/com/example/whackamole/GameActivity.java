package com.example.whackamole;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private final int frames = 4;
    private int currentFrame;
    private int rng;
    private int lastRNG;
    private Random random = new Random();
    public static int SCORE;
    private List<ImageView> moles = new ArrayList<>();
    private TextView textScore;
    private final String TEXT_SCORE_BASE = "Score: ";
    private CountDownTimer timer;
    private final int MOLE_TIMER = 550; //Tempo de Spawn entre toupeiras
    private final int GAME_TIMER = 30000; //Tempo máximo de partida
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.activity_game);
        SCORE = 0;
        textScore = findViewById(R.id.textScore);
        moles.add(findViewById(R.id.mole0));
        moles.add(findViewById(R.id.mole1));
        moles.add(findViewById(R.id.mole2));
        moles.add(findViewById(R.id.mole3));
        moles.add(findViewById(R.id.mole4));
        moles.add(findViewById(R.id.mole5));

        new CountDownTimer(GAME_TIMER, 1000){
                public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish() {
                Intent intent = new Intent(context, ResultActivity.class);
                startActivity(intent);
                finish();
            }

        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Update();
                new Handler().postDelayed(this, 30);
            }
        }, 30);
    }

    public void Update(){
        currentFrame = (currentFrame + 1) % frames;
        textScore.setText(TEXT_SCORE_BASE + SCORE);

        //lógica de spawn das toupeiras
        if(!isMoleUp()){
            while (rng == lastRNG) { rng = random.nextInt(moles.size()); }
            lastRNG = rng;
            moles.get(rng).setVisibility(View.VISIBLE);

            timer = new CountDownTimer(MOLE_TIMER, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() { moles.get(rng).setVisibility(View.INVISIBLE); }
            };
            timer.start();
        }
    }

    private void saveScore() {

        try {
            FileOutputStream fos = this.openFileOutput("score.txt", Context.MODE_APPEND);
            String strScore = String.valueOf(SCORE).concat("\n");
            fos.write(strScore.getBytes());
            fos.close();
        } catch (FileNotFoundException fnf) {

            fnf.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

    }

    private boolean isMoleUp(){
        for(ImageView mole : moles) {
            if (View.VISIBLE == mole.getVisibility()) { return true; }
        } return false;
    }

    public void moleHit(View view){
        SCORE++;
        View moleHit;
        if(view.getId() == R.id.mole0){ moleHit = findViewById(R.id.mole0_hit); }
        else if(view.getId() == R.id.mole1){ moleHit = findViewById(R.id.mole1_hit); }
        else if(view.getId() == R.id.mole2){ moleHit = findViewById(R.id.mole2_hit); }
        else if(view.getId() == R.id.mole3){ moleHit = findViewById(R.id.mole3_hit); }
        else if(view.getId() == R.id.mole4){ moleHit = findViewById(R.id.mole4_hit); }
        else { moleHit = findViewById(R.id.mole5_hit); }
        moleHit.setVisibility(View.VISIBLE);

        new CountDownTimer(40, 500) {
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {moleHit.setVisibility(View.INVISIBLE);}
        }.start();

        view.setVisibility(View.INVISIBLE);
        timer.cancel();
    }
}
