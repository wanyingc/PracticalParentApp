package ca.cmpt276.practicalparent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.cmpt276.practicalparent.model.TimeOutNotificationReceiver;

import static ca.cmpt276.practicalparent.model.TimeOutNotification.CHANNEL_ID;

/**
 * Used to manage Time out Activities
 */
public class TimeOut extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private MediaPlayer mp;
    private Button stopAlarmButton;
    private TextView timerCountDownText;
    private EditText editTimerInput;
    private Button setButton;
    private Button startPauseButton;
    private Button resetButton;
    private CountDownTimer timerCountDown;
    private boolean timerRunning;
    private long startTime;
    private long timeLeft;

    private NotificationManagerCompat notifManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_out);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Timer");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        editTimerInput = findViewById(R.id.editCustomTimerInput);
        setButton = findViewById(R.id.setCustomTimerButton);

        setSpinner();

        updateCountDownText();
        setStartPauseButton();  //Call start button function when clicked
        notifManager = NotificationManagerCompat.from(this);
        setResetButton();       //Call reset button function when clicked
        stopAlarm();
    }

    public void notifChannel() {
        Intent openTimeOutIntent = new Intent(this, TimeOut.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, openTimeOutIntent, 0);

        Intent broadcastIntent = new Intent(this, TimeOutNotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", "Dismissed");
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Practical Parent")
                .setContentText("Times Up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher,"Dismiss",actionIntent)
                .setVibrate(new long[] { 1000, 1000})
                .build();

        notifManager.notify(1,notification);
    }

    private void stopAlarm() {
        stopAlarmButton = findViewById(R.id.stopAlarm);
        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                stopAlarmButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("1 min");
        categories.add("2 min");
        categories.add("3 min");
        categories.add("5 min");
        categories.add("10 min");
        categories.add("Custom");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void setTimer(long milliseconds) {
        startTime = milliseconds;
        resetTimer();
    }


    private void setResetButton() {
        resetButton = findViewById(R.id.resetTimer);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerCountDown.cancel();
                timerRunning = false;
                resetTimer();
            }
        });
        updateCountDownText();
    }

    private void setStartPauseButton() {
        startPauseButton = findViewById(R.id.startTimer);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
    }

    // Function to start the countdown timer
    private void startTimer() {
        timerCountDown = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                playAlarm();
                notifChannel();
                stopAlarmButton.setVisibility(View.VISIBLE);
                startPauseButton.setText("Start");
                startPauseButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
        startPauseButton.setText("pause");
        resetButton.setVisibility(View.VISIBLE);
    }

    private void playAlarm() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), alarmSound);
        mp.start();
    }

    // Function to pause the timer
    private void pauseTimer() {
        timerCountDown.cancel();
        timerRunning = false;
        startPauseButton.setText("Resume");
        resetButton.setVisibility(View.VISIBLE);
    }

    // Update the text of the timer when timer is running
    private void updateCountDownText() {
        timerCountDownText = findViewById(R.id.timerText);
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerCountDownText.setText(timeLeftFormatted);
    }

    // Function to reset the timer back to last selected starting time
    private void resetTimer() {
        timeLeft = startTime;
        updateCountDownText();
        resetButton.setVisibility(View.INVISIBLE);
        startPauseButton.setText("Start");
        startPauseButton.setVisibility(View.VISIBLE);

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeOut.class);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        if (item.contentEquals("1 min")) {
            setTimer(60000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);

        }
        else if (item.contentEquals("2 min")) {
            setTimer(120000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("3 min")) {
            setTimer(180000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("5 min")) {
            setTimer(300000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("10 min")) {
            setTimer(600000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("Custom")) {
            setCustomTimer();
            editTimerInput.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.VISIBLE);
        }

        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    private void setCustomTimer() {

        setButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String input = editTimerInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(TimeOut.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long textInput = Long.parseLong(input) * 60000;
                if (textInput == 0) {
                    Toast.makeText(TimeOut.this, "Please enter a valid number of minutes", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTimer(textInput);
                editTimerInput.setText("");
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}