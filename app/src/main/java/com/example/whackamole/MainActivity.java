package com.example.whackamole;

import
        androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private CountDownTimer timer;
    private final int quadro = 5;
    private int quadroAtual;
    private Random random = new Random();
    private final int MAX_SECOND = 25;
    private long tempoRestante = (MAX_SECOND + 1) * 1000;
    public static int PONTO;
    private TextView textScore;
    private int rng;
    private int lastRNG;
    private Context context;
    private MediaPlayer mediaPlayer;
    private final int MOLE_TIMER = 500;
    private final int GAME_TIMER = 30000;
    private final int MAX_MOLES = 9;
    private ImageView[] moles = new ImageView[MAX_MOLES];
    private SoundPool soundpool;
    private int hit, show;
    private boolean running = false;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo2);
        this.context = this;

        DatabaseReference playersNode = referencia.child("Jogadores");


        playersNode.orderByChild("pontos").limitToFirst(10);
        playersNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("Firebase", dataSnapshot.getKey());
                Log.i("Firebase", dataSnapshot.getValue().toString());
                Jogadores[] jogadores = dataSnapshot.getValue(Jogadores[].class);
                Log.i("Firebase", jogadores.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //public void

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundpool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        hit = soundpool.load(this, R.raw.bonk, 1);
        show = soundpool.load(this, R.raw.drip, 1);

        moles[0] = findViewById(R.id.hole1);
        moles[1] = findViewById(R.id.mole2);
        moles[2] = findViewById(R.id.mole3);
        moles[3] = findViewById(R.id.mole4);
        moles[4] = findViewById(R.id.mole5);
        moles[5] = findViewById(R.id.mole6);
        moles[6] = findViewById(R.id.mole7);
        moles[7] = findViewById(R.id.mole8);
        moles[8] = findViewById(R.id.mole9);

        textScore = findViewById(R.id.textScore);
        PONTO = 0;

        mediaPlayer = MediaPlayer.create(context, R.raw.bgm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        iniciarContagem();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                running = true;
                update();
                new Handler().postDelayed(this, 20);
            }
        },20);
    }

    private void iniciarContagem() {
        timer = new CountDownTimer(tempoRestante, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempoRestante = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                onEndGame();
            }
        }.start();
    }

    private void update() {
        quadroAtual = (quadroAtual + 1) % quadro;
        textScore.setText("Pontos: " + PONTO);

        if (!isMoleUp()) {
            while (rng == lastRNG) {
                rng = random.nextInt(moles.length);
            }

            lastRNG = rng;
            moles[rng].setVisibility(View.VISIBLE);
            soundpool.play(show, 0.25f, 0.25f, 0, 0, 1);

            timer = new CountDownTimer(MOLE_TIMER, 1000) {

                public void onTick(long millisUnitFinished) {

                }

                public void onFinish() {
                    moles[rng].setVisibility(View.INVISIBLE);
                }
            };
            timer.start();
        }
    }


    private boolean isMoleUp(){
        for(ImageView mole : moles){
            if(View.VISIBLE == mole.getVisibility()){
                return true;
            }
        }
        return false;
    }

    public void porrada(View view){
        PONTO++;
        soundpool.play(hit, 0.3f, 0.3f, 0, 0, 1);
        view.setVisibility(View.INVISIBLE);
        timer.cancel();
    }
    private void onEndGame() {
        mediaPlayer.stop();
        soundpool.autoPause();
        Intent intent = new Intent(this.context, Fim_Jogo.class);
        startActivity(intent);
        finish();
    }

}