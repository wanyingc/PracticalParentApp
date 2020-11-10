package ca.cmpt276.practicalparent.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import ca.cmpt276.practicalparent.view.TimeOut;

/**
 * Used to stop time out notification
 */
public class TimeOutNotificationReceiver extends BroadcastReceiver {
    private MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        mp = TimeOut.mp;
        String message = intent.getStringExtra("toastMessage");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        mp.stop();
        mp.release();
    }
}
