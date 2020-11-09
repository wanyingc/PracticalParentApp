package ca.cmpt276.practicalparent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

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

public class TimeOut extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView timerCountDownText;
    private EditText editTimerInput;
    private Button setButton;
    private Button startPauseButton;
    private Button resetButton;
    private CountDownTimer timerCountDown;
    private boolean timerRunning;
    private long startTime;
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_out);

        setSpinner();

        updateCountDownText();
        setStartPauseButton();  //Call start button function when clicked
        setResetButton();       //Call reset button function when clicked

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
                startPauseButton.setText("Start");
                startPauseButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();
        timerRunning = true;
        startPauseButton.setText("pause");
        resetButton.setVisibility(View.VISIBLE);
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
        }
        else if (item.contentEquals("2 min")) {
            setTimer(120000);
        }
        else if (item.contentEquals("3 min")) {
            setTimer(180000);
        }
        else if (item.contentEquals("5 min")) {
            setTimer(300000);
        }
        else if (item.contentEquals("10 min")) {
            setTimer(600000);
        }
        else if (item.contentEquals("Custom")) {
            setCustomTimer();
            editTimerInput.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.VISIBLE);
        }

        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    private void setCustomTimer() {
        editTimerInput = findViewById(R.id.editCustomTimerInput);
        setButton = findViewById(R.id.setCustomTimerButton);

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