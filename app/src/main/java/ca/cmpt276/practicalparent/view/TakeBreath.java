package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ca.cmpt276.practicalparent.R;

/**
 * Displays and handles the UI for the Take Breath activity
 */

public class TakeBreath extends AppCompatActivity {
    private static final String PREF_NAME = "Breaths Storage";
    private static final String NUM_BREATHS = "Number of Breaths";
    private static final String[] breaths = {"1 breath","2 breaths","3 breaths","4 breaths","5 breaths","6 breaths","7 breaths","8 breaths","9 breaths","10 breaths"};
    private int breathCount = 3;
    private Spinner dropDown;
    private MediaPlayer inhale;
    private MediaPlayer exhale;

    /////////////////////////////////////////////////////////////////////
    // STATE PATTERN                                                   //
    /////////////////////////////////////////////////////////////////////

    /**
     * Displays and handles the UI for each individual state
     */

    private abstract class State {
        // Empty implementations, so the derived class doesn't
        // need to override methods they don't care about.
        void handleEnter() {}
        void handlePress() {}
        void handleRelease() {}
        void handleExit() {}
    }

    private final State inState = new InState();
    private final State outState = new OutState();
    private final State idleState = new IdleState();
    private final State endState = new EndState();
    private State currentState = idleState;

    private void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private class OutState extends State {
        Handler timerHandler = new Handler();
        Runnable changeState = new Runnable() {
            @Override
            public void run() {
                breathCount--;
                if (breathCount > 0) {
                    setState(inState);

                    // Animate 7s more
                    Button breath = (Button) findViewById(R.id.btnBreath);
                    Animation pulse = AnimationUtils.loadAnimation(TakeBreath.this, R.anim.pulse_down);
                    pulse.setDuration(1000);
                    pulse.setFillAfter(true);
                    breath.startAnimation(pulse);
                } else {
                    setState(endState);
                }
            }
        };

        @Override
        void handleEnter() {
            super.handleEnter();

            // Help Text
            TextView helpText = (TextView) findViewById(R.id.helpText);
            helpText.setText("Release button and breathe out");

            // Button
            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.setText("Out");
            breath.setBackgroundResource(R.drawable.breathe_out_button);
        }

        @Override
        void handleRelease() {
            super.handleRelease();

            // Timer
            timerHandler.removeCallbacks(changeState);
            timerHandler.postDelayed(changeState,3000);

            // Sound
            exhale = MediaPlayer.create(TakeBreath.this,R.raw.exhale_audio);
            exhale.start();

            // Animation
            Button breath = (Button) findViewById(R.id.btnBreath);
            Animation scaleDown = AnimationUtils.loadAnimation(TakeBreath.this, R.anim.scale_down);
            scaleDown.setDuration(3000);
            scaleDown.setFillAfter(true);
            breath.startAnimation(scaleDown);
        }

        @Override
        void handlePress() {
            super.handlePress();

            // Button
            Button breath = (Button) findViewById(R.id.btnBreath);
            exhale.stop();
            timerHandler.removeCallbacks(changeState);

        }

        @Override
        void handleExit() {
            super.handleExit();

            // Breath Count
            TextView breathsRemaining = (TextView) findViewById(R.id.helpTextSupplementary);
            breathsRemaining.setText("Breaths remaining: " + breathCount);
        }
    }

    private class InState extends State {
        Handler timerHandler = new Handler();
        Runnable changeState = new Runnable() {
            @Override
            public void run() {
                setState(outState);

                // Animate 7s more
                Button breath = (Button) findViewById(R.id.btnBreath);
                Animation pulse = AnimationUtils.loadAnimation(TakeBreath.this, R.anim.pulse_up);
                pulse.setDuration(1000);
                pulse.setFillAfter(true);
                breath.startAnimation(pulse);
            }
        };

        @Override
        void handleEnter() {
            super.handleEnter();

            // Help Text
            TextView helpText = (TextView) findViewById(R.id.helpText);
            helpText.setText("Hold button and breathe in");

            // Show Breath Count
            TextView breathsRemaining = (TextView) findViewById(R.id.helpTextSupplementary);
            breathsRemaining.setVisibility(View.VISIBLE);

            // Button
            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.setText("In");
            breath.setBackgroundResource(R.drawable.breathe_in_button);
        }

        @Override
        void handlePress() {
            super.handlePress();

            // Timer
            timerHandler.removeCallbacks(changeState);
            timerHandler.postDelayed(changeState,3000);

            // Sound
            inhale = MediaPlayer.create(TakeBreath.this,R.raw.inhale_audio);
            inhale.start();

            // Animation
            Button breath = (Button) findViewById(R.id.btnBreath);
            Animation scaleUp = AnimationUtils.loadAnimation(TakeBreath.this, R.anim.scale_up);
            scaleUp.setDuration(3000);
            scaleUp.setFillAfter(true);
            breath.startAnimation(scaleUp);
        }

        @Override
        void handleRelease() {
            super.handleRelease();

            // Button
            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.clearAnimation();
            inhale.stop();
            timerHandler.removeCallbacks(changeState);
        }

        @Override
        void handleExit() {
            super.handleExit();
            timerHandler.removeCallbacks(changeState);
        }
    }

    private class IdleState extends State {
        @Override
        void handleEnter() {
            super.handleEnter();

            // Breath Count
            getBreathsFromSP();
            TextView helpText = (TextView) findViewById(R.id.helpText);
            if (breathCount == 1) {
                helpText.setText("Let's take " + breathCount + " breath together");
            } else {
                helpText.setText("Let's take " + breathCount + " breaths together");
            }

            // Show Drop Down
            dropDown.setVisibility(View.VISIBLE);
            dropDown.setSelection(breathCount-1);

            // Hide Breath Count
            TextView breathsRemaining = (TextView) findViewById(R.id.helpTextSupplementary);
            breathsRemaining.setVisibility(View.GONE);

            // Button
            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.setText("Begin");
            breath.setBackgroundResource(R.drawable.breathe_idle_button);
        }

        @Override
        void handlePress() {
            super.handlePress();
            setState(inState);
            inState.handlePress();
        }

        @Override
        void handleExit() {
            super.handleExit();

            // Hide Drop Down
            dropDown.setVisibility(View.GONE);

            // Breath Count
            TextView breathsRemaining = (TextView) findViewById(R.id.helpTextSupplementary);
            breathsRemaining.setText("Breaths remaining: " + breathCount);
        }
    }

    private class EndState extends State {
        @Override
        void handleEnter() {
            super.handleEnter();

            // Help Text
            TextView helpText = (TextView) findViewById(R.id.helpText);
            helpText.setText("Press button to reset breaths");

            // Breath Count
            TextView breathsRemaining = (TextView) findViewById(R.id.helpTextSupplementary);
            breathsRemaining.setText("There are no breaths remaining");

            // Button
            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.setText("Good job");
            breath.setBackgroundResource(R.drawable.breathe_end_button);
        }

        @Override
        void handlePress() {
            super.handlePress();
            inhale.stop();
            exhale.stop();
            setState(idleState);
        }
    }

    /////////////////////////////////////////////////////////////////////
    // TAKE BREATH UI                                                  //
    /////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Take Breath");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupDropDown();
        currentState.handleEnter();
        setupBreathButton();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TakeBreath.class);
    }

    private void setupDropDown() {
        dropDown = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, breaths);
        dropDown.setAdapter(adapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView helpText = (TextView) findViewById(R.id.helpText);
                breathCount = position+1;
                if (position == 0) {
                    helpText.setText("Let's take " + breathCount + " breath together");
                } else {
                    helpText.setText("Let's take " + breathCount + " breaths together");
                }
                storeBreathsToSP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupBreathButton() {
        final Button breath = (Button) findViewById(R.id.btnBreath);
        breath.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {

                    case android.view.MotionEvent.ACTION_DOWN:
                        currentState.handlePress();
                        break;

                    case android.view.MotionEvent.ACTION_UP:
                        currentState.handleRelease();
                        break;
                }
                return true;
            }
        });
    }

    public void storeBreathsToSP() {
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_BREATHS,breathCount);
        editor.apply();
    }

    public void getBreathsFromSP() {
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        breathCount = prefs.getInt(NUM_BREATHS,3);
    }
}