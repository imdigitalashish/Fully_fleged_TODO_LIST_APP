package com.imdigitalashish.vacoder;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.imdigitalashish.vacoder.App.CHANNEL_ID;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyNotificationService extends IntentService {


    public MyNotificationService() {
        super("MyNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent OpenActivity = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, OpenActivity, 0);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        String username = intent.getStringExtra("nameview");
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_check_box_24)
                .setContentTitle("A reminder for you")
                .setContentText(username)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);


    }

}

