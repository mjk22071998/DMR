package com.example.dmr.medicalrep.fcm;

import static com.example.dmr.medicalrep.utils.SessionManager.EMAIL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.dmr.medicalrep.R;
import com.example.dmr.medicalrep.activities.MainActivity;
import com.example.dmr.medicalrep.utils.SessionManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SessionManager.saveToken(getApplicationContext(),s);
        CollectionReference reference;
        reference = FirebaseFirestore.getInstance().collection("Users");
        reference.document(SessionManager.getUser(getApplicationContext()).get(EMAIL).toString()).update("token",s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);

    }
    private void sendNotification(RemoteMessage remoteMessage) {
        int m=1;
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, m,intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(m, notificationBuilder.build());
    }
}
