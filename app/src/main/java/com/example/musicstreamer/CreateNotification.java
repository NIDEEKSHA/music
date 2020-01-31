package com.example.musicstreamer;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicstreamer.services.NotificationActionServices;

public class CreateNotification {

    public static final String Channel_id="channel1";
    public  static final String action_play= "actionplay";
    public static Notification noticication;

    public static void createNotification(Context context,Track track,int playbutton,int pos)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationManagerCompat notificationManagerCompat =NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat=new MediaSessionCompat(context,"tag");

            Bitmap icon= BitmapFactory.decodeResource(context.getResources(), track.getImage());
            
            Intent intentplay=new Intent(context, NotificationActionServices.class).setAction(action_play);
            
            PendingIntent pendingIntentPLay=PendingIntent.getBroadcast(context,0,intentplay,PendingIntent.FLAG_UPDATE_CURRENT);
            
            noticication=new NotificationCompat.Builder(context,Channel_id)
                    .setSmallIcon(R.drawable.ic_music_note_black_24dp)
//                    .setContentText(track.getCompany())
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(playbutton,"Play",pendingIntentPLay)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            //.setShowActionsInCompactView(1)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationManagerCompat.notify(1,noticication);
        }
    }
}
