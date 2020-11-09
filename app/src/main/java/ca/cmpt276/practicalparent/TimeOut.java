package ca.cmpt276.practicalparent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class TimeOut extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 60000;
    private TextView timerCountDownText;
    private Button startPauseButton;
    private Button resetButton;
    private CountDownTimer timerCountDown;
    private boolean timerRunning;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_out);

        timerCountDownText = findViewById(R.id.timerText);
        startPauseButton = findViewById(R.id.startTimer);
        resetButton = findViewById(R.id.resetTimer);
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
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }

    private void startTimer() {
        timerCountDown = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                startPauseButton.setText("Start");
                startPauseButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
        startPauseButton.setText("pause");
        resetButton.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        timerCountDown.cancel();
        timerRunning = false;
        startPauseButton.setText("Resume");
        resetButton.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerCountDownText.setText(timeLeftFormatted);
    }

    private void resetTimer() {
        timerCountDown.cancel();
        timerRunning = false;
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        resetButton.setVisibility(View.INVISIBLE);
        startPauseButton.setText("Start");
        startPauseButton.setVisibility(View.VISIBLE);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeOut.class);
    }
}