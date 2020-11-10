package ca.cmpt276.practicalparent.model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class TimeOutNotification extends Application {
    public static final String CHANNEL_ID = "TimeOutChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timeout Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Timeout Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            
        }
    }
}
