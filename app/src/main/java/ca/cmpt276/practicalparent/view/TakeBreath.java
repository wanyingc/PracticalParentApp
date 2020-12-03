package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.practicalparent.R;

/**
 * Displays and handles the UI for the Take Breath activity
 */

public class TakeBreath extends AppCompatActivity {

    /////////////////////////////////////////////////////////////////////
    // STATE PATTERN                                                   //
    /////////////////////////////////////////////////////////////////////

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
    private State currentState = idleState;

    private void setState(State newState) {
        currentState.handleExit();
        currentState = newState;
        currentState.handleEnter();
    }

    private class OutState extends State {
        @Override
        void handleEnter() {
            super.handleEnter();

            TextView helpText = (TextView) findViewById(R.id.breathText);
            helpText.setText("Release button and breathe out");

            Button breath = findViewById(R.id.btnBreath);
            breath.setText("OUT");
        }

        @Override
        void handlePress() {
            super.handlePress();
            Toast.makeText(TakeBreath.this, "Tapped from OUT state!", Toast.LENGTH_SHORT).show();
            setState(inState);
        }

        @Override
        void handleRelease() {
            super.handlePress();

            // Animation
            Button breath = (Button) findViewById(R.id.btnBreath);
            Animation scaleDown = AnimationUtils.loadAnimation(TakeBreath.this, R.anim.scale_down);
            scaleDown.setDuration(3000);
            scaleDown.setFillAfter(true);
            breath.startAnimation(scaleDown);
        }
    }

    private class InState extends State {
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                setState(outState);
            }
        };

        @Override
        void handleEnter() {
            super.handleEnter();

            TextView helpText = (TextView) findViewById(R.id.breathText);
            helpText.setText("Hold button and breathe in");

            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.setText("IN");
        }

        @Override
        void handlePress() {
            super.handlePress();

            // Timer
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.postDelayed(timerRunnable,3000);

            // Animation
            Button breath = (Button) findViewById(R.id.btnBreath);
            Animation scaleUp = AnimationUtils.loadAnimation(TakeBreath.this, R.anim.scale_up);
            scaleUp.setDuration(3000);
            scaleUp.setFillAfter(true);
            breath.startAnimation(scaleUp);

            Toast.makeText(TakeBreath.this, "Tapped from IN state!", Toast.LENGTH_SHORT).show();
        }

        @Override
        void handleRelease() {
            super.handleRelease();
            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.clearAnimation();
            timerHandler.removeCallbacks(timerRunnable);
        }

        @Override
        void handleExit() {
            super.handleExit();
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    private class IdleState extends State {
        @Override
        void handleEnter() {
            super.handleEnter();

            TextView helpText = (TextView) findViewById(R.id.breathText);
            helpText.setText("Let's take breaths together");

            Button breath = (Button) findViewById(R.id.btnBreath);
            breath.setText("Begin");

            Toast.makeText(TakeBreath.this, "Entered IDLE state", Toast.LENGTH_SHORT).show();
        }

        @Override
        void handlePress() {
            super.handlePress();
            Toast.makeText(TakeBreath.this, "Tapped from IDLE state!", Toast.LENGTH_SHORT).show();
            setState(inState);
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

        setupBreathButton();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TakeBreath.class);
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
}