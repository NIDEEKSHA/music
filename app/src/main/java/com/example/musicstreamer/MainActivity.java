package com.example.musicstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicstreamer.services.OnClearFromRecentService;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements playable {

    ImageButton play;
    Track track;
    ImageButton share;
    String url="https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3";
    MediaPlayer mediaPlayer;
    boolean isPlaying=false;
    TextView artist,details,song;
    NotificationManager notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artist=(TextView) findViewById(R.id.artist_name);
        details=(TextView) findViewById(R.id.other_details);
        song=(TextView) findViewById(R.id.song_name);
        share=(ImageButton) findViewById(R.id.share);
        track=new Track(song.getText().toString(),artist.getText().toString(),R.drawable.p1);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createChannel();
            registerReceiver(broadcastReceiver,new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }
        play=(ImageButton) findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    onTrackPause();
                }else{
                    onTrackPlay();
                }
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(Intent.ACTION_SEND);
                myintent.setType("text/plain");
                String shareBody=url;
                String sharesub="your Subject here";
                myintent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                myintent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myintent,"share using"));
            }
        });

        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Toast.makeText(MainActivity.this,"Media Buffering Complete..",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void createChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel(CreateNotification.Channel_id,"Naada", NotificationManager.IMPORTANCE_LOW);

            notificationManager=getSystemService(NotificationManager.class);
            if(notificationManager!=null)
            {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getExtras().getString("actionname");

            switch(action)
            {
                case CreateNotification.action_play:
                if(isPlaying)
                {
                    onTrackPause();
                }
                else
                {
                    onTrackPlay();
                }
                break;
            }

        }
    };

    @Override
    public void onTrackPlay() {
        CreateNotification.createNotification(MainActivity.this, track,R.drawable.ic_pause_circle_outline_black_24dp,1);
        play.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        isPlaying=true;
        mediaPlayer.start();
    }

    @Override
    public void onTrackPause() {
        CreateNotification.createNotification(MainActivity.this, track,R.drawable.ic_play_circle_outline_black_24dp,1);
        play.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        isPlaying=false;
        mediaPlayer.pause();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);
    }


}
