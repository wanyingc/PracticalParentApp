package ca.cmpt276.practicalparent.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;

public class MainMenu extends AppCompatActivity {
    private Button tasksButton;
    private Button timeOutButton;
    private Button configButton;
    private Button coinFlipButton;
    private Button helpButton;
    private ChildManager manager;
    private static final String PREF_NAME = "Name List Storage";
    private static final String NUM_STORED_VALUES = "Number of Stored Values";
    private static final String STORED_NAME = "Stored Name";
    private static final String STORED_BITMAP = "Stored Bitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // start Activity when coin flip button is clicked
        coinFlipButton = (Button) findViewById(R.id.flipCoin);
        coinFlipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoinFlipActivity();
            }
        });

        // start Activity when configuration button is clicked
        configButton = (Button) findViewById(R.id.configChild);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfigChildActivity();
            }
        });

        // start Activity when time out button is clicked
        timeOutButton = (Button) findViewById(R.id.timeOut);
        timeOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeOutActivity();
            }
        });

        // start Activity when help button is clicked
        helpButton = (Button) findViewById(R.id.help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHelpActivity();
            }
        });

        //start Activity when tasks button is clicked
        tasksButton = (Button) findViewById(R.id.tasks);
        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTasksActivity();            }
        });

        getNamesAndSizeFromSP();
    }

    public void openConfigChildActivity() {
        Intent intent = ChildList.makeIntent(MainMenu.this);
        startActivity(intent);
    }

    public void openCoinFlipActivity() {
        if (ChildManager.getInstance().size() == 0) {
            Intent intent = CoinFlipActivity.makeIntent(MainMenu.this);
            startActivity(intent);
        } else {
            Intent intent = PlayerChoice.makeIntent(MainMenu.this);
            startActivity(intent);
        }
    }
    public void openTimeOutActivity() {
        Intent intent = TimeOut.makeIntent(MainMenu.this);
        startActivity(intent);
    }

    public void openHelpActivity() {
        Intent intent = Help.makeIntent(MainMenu.this);
        startActivity(intent);
    }

    public void getNamesAndSizeFromSP() {
        manager = ChildManager.getInstance();
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        final int childListSize = prefs.getInt(NUM_STORED_VALUES,0);
        manager.clear();
        for(int i=0;i<childListSize;i++) {
            String name = STORED_NAME + i;
            String bitmap = STORED_BITMAP + i;
            Child child = new Child(prefs.getString(name,""),prefs.getString(bitmap,null));
            manager.add(child);
        }
    }

    public void openTasksActivity() {
        Intent intent = TasksList.makeIntent(MainMenu.this);
        startActivity(intent);
    }

}