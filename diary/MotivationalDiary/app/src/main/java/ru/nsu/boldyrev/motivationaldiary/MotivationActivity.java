package ru.nsu.boldyrev.motivationaldiary;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class MotivationActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private AudioManager audioManager;
    private static final int MAX_STREAMS = 5;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean loaded;
    private float volume;

    int[] sound = new int[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(streamType);

        this.volume = currentVolumeIndex / maxVolumeIndex;

        this.setVolumeControlStream(streamType);

        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        else {
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        this.sound[0] = soundPool.load(this, R.raw.a, 1);
        this.sound[1] = soundPool.load(this, R.raw.b, 1);
        this.sound[2] = soundPool.load(this, R.raw.c, 1);
        this.sound[3] = soundPool.load(this, R.raw.d, 1);
        this.sound[4] = soundPool.load(this, R.raw.e, 1);
        this.sound[5] = soundPool.load(this, R.raw.f, 1);
        this.sound[6] = soundPool.load(this, R.raw.g, 1);
        this.sound[7] = soundPool.load(this, R.raw.h, 1);


    }

    public void playRandom(View view) {
        if(loaded)  {
            float leftVolumn = volume;
            float rightVolumn = volume;

            Random random = new Random();
            int soundToPlay = sound[random.nextInt(7)];

            int streamId = this.soundPool.play(soundToPlay ,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }
}
