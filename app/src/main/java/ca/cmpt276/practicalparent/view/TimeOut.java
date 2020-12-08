package ca.cmpt276.practicalparent.view;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.TimeOutNotificationReceiver;

import static ca.cmpt276.practicalparent.model.TimeOutNotification.CHANNEL_ID;

/**
 * Used to manage Time out Activities.
 */
public class TimeOut extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static MediaPlayer mp;
    private Button stopAlarmButton;
    private TextView timerCountDownText;
    private EditText editTimerInput;
    private Button setButton;
    private Button startPauseButton;
    private Button resetButton;
    private CountDownTimer timerCountDown;
    private boolean timerRunning;
    private long startTime;
    private double timeLeft;
    private ProgressBar progressBarCircle;
    private long countDownInterval =1000;
    private Spinner timerRate;
    final private ReentrantLock lock = new ReentrantLock();
    private NotificationManagerCompat notifManager;
    private double currentTime;

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
        progressBarCircle = findViewById(R.id.progressBarCircle);
        setSpinner();
        setTimerRateSpinner();

        setStartPauseButton();  //Call start button function when clicked
        notifManager = NotificationManagerCompat.from(this);
        setResetButton();       //Call reset button function when clicked
        stopAlarm();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerCountDown.cancel();
    }


    private void setTimerRateSpinner() {
        timerRate = findViewById(R.id.timeRateSpinner);
        timerRate.setOnItemSelectedListener(this);
        List<String> timeRateOptions = new ArrayList<String>();
        timeRateOptions.add("25%");
        timeRateOptions.add("50%");
        timeRateOptions.add("75%");
        timeRateOptions.add("100%");
        timeRateOptions.add("200%");
        timeRateOptions.add("300%");
        timeRateOptions.add("400%");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeRateOptions);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timerRate.setAdapter(dataAdapter);
        timerRate.setSelection(3);
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

    // Set timer spinner
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
        currentTime = milliseconds;
        timeLeft = milliseconds;
        resetTimer();
        updateCountDownText();
        setProgressBarValues();
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
        if (!timerRunning) {
            timerCountDown = new CountDownTimer((long) timeLeft, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    currentTime -= 1000;
                    timeLeft = millisUntilFinished;
                    updateCountDownText();
                    progressBarCircle.setProgress((int) (currentTime));
                    Log.e("Time", "Current: " + currentTime + ",Left: " + timeLeft);
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    timerCountDown.cancel();
                    playAlarm();
                    notifChannel();
                    setProgressBarValues();
                    stopAlarmButton.setVisibility(View.VISIBLE);
                    startPauseButton.setText("Start");
                    startPauseButton.setVisibility(View.INVISIBLE);
                    resetButton.setVisibility(View.VISIBLE);
                }
            };
            timerCountDown.start();
            timerRunning = true;
            startPauseButton.setText("pause");
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    private void setProgressBarValues() {
        progressBarCircle.setMax((int) (currentTime));
        progressBarCircle.setProgress((int) (currentTime));
    }

    private void playAlarm() {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), alarmSound);
        mp.start();
    }

    // Function to pause the timer
    private void pauseTimer() {
        if (timerRunning) {
            timerCountDown.cancel();
            timerRunning = false;
            startPauseButton.setText("Resume");
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    // Update the text of the timer when timer is running
    private void updateCountDownText() {
        timerCountDownText = findViewById(R.id.timerText);
        int minutes = (int) ((currentTime / 1000)/60);
        int seconds = (int) ((currentTime / 1000)%60);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerCountDownText.setText(timeLeftFormatted);
    }

    // Function to reset the timer back to last selected starting time
    private void resetTimer() {
        timeLeft = startTime;
        currentTime = startTime;
        updateCountDownText();
        setProgressBarValues();
        resetButton.setVisibility(View.INVISIBLE);
        startPauseButton.setText("Start");
        startPauseButton.setVisibility(View.VISIBLE);

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeOut.class);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner setTimerSpinner = (Spinner) parent;
        Spinner setRateSpinner = (Spinner) parent;

        if (setTimerSpinner.getId() == R.id.spinner) {
            String item = parent.getItemAtPosition(position).toString();
            setUpSelectedTime(item);
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }

        if (setRateSpinner.getId() == R.id.timeRateSpinner) {
            String item = parent.getItemAtPosition(position).toString();
            setUpSelectedRate(item);
        }


    }

    public void setUpSelectedRate(String item) {
        final double tempTimeLeft;
        final boolean timerRunningState;
        TextView timeRateView;

        timeRateView = findViewById(R.id.timeRateView);
        if (item.contentEquals("25%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            tempTimeLeft = timeLeft * (0.75);
            timeLeft += tempTimeLeft;
            setNewTimer(timeLeft,1750);
            timeRateView.setText("Time @25%");
            if (timerRunningState) {
                startTimer();
            }
        }
        else if (item.contentEquals("50%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            tempTimeLeft = timeLeft * (0.5);
            timeLeft += tempTimeLeft;
            setNewTimer(timeLeft,1500);
            timeRateView.setText("Time @50%");
            if (timerRunningState) {
                startTimer();
            }
        }
        else if (item.contentEquals("75%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            tempTimeLeft = timeLeft * (0.25);
            timeLeft += tempTimeLeft;
            setNewTimer(timeLeft,1250);
            timeRateView.setText("Time @75%");
            if (timerRunningState) {
                startTimer();
            }
        }
        else if (item.contentEquals("100%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            tempTimeLeft = timeLeft;
            setNewTimer(tempTimeLeft,1000);
            timeRateView.setText("Time @100%");
            if (timerRunningState) {
                startTimer();
            }
        }
        else if (item.contentEquals("200%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            timeLeft = timeLeft /2;
            setNewTimer(timeLeft,500);
            timeRateView.setText("Time @200%");
            if (timerRunningState) {
                startTimer();
            }
        }
        else if (item.contentEquals("300%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            timeLeft = timeLeft/3;
            setNewTimer(timeLeft,1000/3);
            timeRateView.setText("Time @300%");
            if (timerRunningState) {
                startTimer();
            }
        }
        else if (item.contentEquals("400%")) {
            timerRunningState = timerRunning;
            if (timerRunningState) {
                pauseTimer();
            }
            timeLeft = currentTime;
            timeLeft = timeLeft/4;
            setNewTimer(timeLeft,250);
            timeRateView.setText("Time @400%");
            if (timerRunningState) {
                startTimer();
            }
        }
    }

    private void setNewTimer(double tempTimeLeft, int interval) {
        timeLeft = tempTimeLeft;
        countDownInterval = interval;
    }

    private void setUpSelectedTime(String item) {
        if (item.contentEquals("1 min")) {
            if (timerRunning) {
                timerCountDown.cancel();
                timerRunning = false;
            }
            setTimer(60000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("2 min")) {
            if (timerRunning) {
                timerCountDown.cancel();
                timerRunning = false;
            }
            setTimer(120000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("3 min")) {
            if (timerRunning) {
                timerCountDown.cancel();
                timerRunning = false;
            }
            setTimer(180000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("5 min")) {
            if (timerRunning) {
                timerCountDown.cancel();
                timerRunning = false;
            }
            setTimer(300000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("10 min")) {
            if (timerRunning) {
                timerCountDown.cancel();
                timerRunning = false;
            }
            setTimer(600000);
            editTimerInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
        }
        else if (item.contentEquals("Custom")) {
            if (timerRunning) {
                timerCountDown.cancel();
                timerRunning = false;
            }
            setCustomTimer();
            editTimerInput.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.VISIBLE);
        }
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