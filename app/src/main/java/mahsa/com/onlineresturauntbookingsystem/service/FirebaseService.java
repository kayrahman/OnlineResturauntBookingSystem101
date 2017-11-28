package mahsa.com.onlineresturauntbookingsystem.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import mahsa.com.onlineresturauntbookingsystem.R;

import static mahsa.com.onlineresturauntbookingsystem.ui.NotificationDetailActivity.FROM_USER_KEY;


/**
 * Created by mahsa on 13/09/2017.
 */

public class FirebaseService extends com.google.firebase.messaging.FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        String from_user_id = remoteMessage.getData().get("fromUserId");

        Log.d("FROM_USER_ID",from_user_id);
        Log.d("MESSAGE_TITLE",messageTitle);
        Log.d("MESSAGE_BODY",messageBody);


        String click_action = remoteMessage.getNotification().getClickAction();


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_contact_phone_black_24dp)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra(FROM_USER_KEY,from_user_id);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);




        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
    }

}
