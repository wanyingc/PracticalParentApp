package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ca.cmpt276.practicalparent.model.ChildManager;

public class PlayerChoice extends AppCompatActivity {
    private ChildManager manager;
    private int player1, player2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = ChildManager.getInstance();
        setupRadioGroup1();
        setupRadioGroup2();
        setupPlayButton();
    }

    private void setupPlayButton() {
        Button button = findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CoinFlipActivity.makeIntent(PlayerChoice.this, player1, player2);
                startActivity(intent);
            }
        });
    }


    private void setupRadioGroup1() {
        RadioGroup radio = findViewById(R.id.playerGroup1);
        for (int i = 0; i < manager.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(""+manager.getChild(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            radio.addView(button);
        }
    }

    private void setupRadioGroup2() {
        RadioGroup radio = findViewById(R.id.playerGroup2);
        for (int i = 0; i < manager.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setText(""+manager.getChild(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            radio.addView(button);
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayerChoice.class);
    }
}