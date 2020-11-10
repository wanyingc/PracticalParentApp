package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import ca.cmpt276.practicalparent.model.ChildManager;

public class PlayerChoice extends AppCompatActivity {
    private ChildManager manager;
    private int savedPlayer1, savedPlayer2;
    private RadioButton buttonSet1[];
    private RadioButton buttonSet2[];

    private int previousSelection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Children");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        manager = ChildManager.getInstance();
        previousSelection = -1;
        savedPlayer1 = savedPlayer2 = -1;
        buttonSet1 = new RadioButton[manager.size()];
        buttonSet2 = new RadioButton[manager.size()];

        setupRadioGroup1();
        setupRadioGroup2();
        setupPlayButton();

    }
    private void setupPlayButton() {
        Button button = findViewById(R.id.playButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CoinFlipActivity.makeIntent(PlayerChoice.this, savedPlayer1, savedPlayer2);
                startActivity(intent);
            }
        });
    }



    private void updateRadioButtons() {
        for (int i = 0; i < manager.size(); i++) {
            if (buttonSet1[i].isActivated()) {
                Log.e("TAG", "yes");
                buttonSet2[i].setClickable(false);
            } else {
                buttonSet2[i].setClickable(true);
            }

            if (buttonSet2[i].isActivated()) {
                buttonSet1[i].setClickable(false);
            } else {
                buttonSet1[i].setClickable(true);
            }
        }
    }


    private void setupRadioGroup1() {
        RadioGroup radio = findViewById(R.id.playerGroup1);
        for (int i = 0; i < manager.size(); i++) {
            final int childIndex = i;
            RadioButton b = new RadioButton(this);
            b.setText(manager.getChild(childIndex));

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedPlayer1 = childIndex;
                }
            });
            radio.addView(b);
        }
    }

    private void setupRadioGroup2() {
        RadioGroup radio = findViewById(R.id.playerGroup2);
        for (int i = 0; i < manager.size(); i++) {
            final int childIndex = i;
            RadioButton b = new RadioButton(this);
            b.setText(manager.getChild(childIndex));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedPlayer2 = childIndex;
                }
            });
            radio.addView(b);
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayerChoice.class);
    }
}