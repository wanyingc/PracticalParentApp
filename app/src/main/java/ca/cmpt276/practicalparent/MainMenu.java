package ca.cmpt276.practicalparent;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    private Button timeOutButton;
    private Button configButton;
    private Button coinFlipButton;

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


    }

    public void openConfigChildActivity() {
        Intent intent = ChildList.makeIntent(MainMenu.this);
        startActivity(intent);
    }

    public void openCoinFlipActivity() {
        Intent intent = CoinFlipActivity.makeIntent(MainMenu.this);
        startActivity(intent);
    }

    public void openTimeOutActivity() {
        Intent intent = new Intent(this,TimeOut.class);
        startActivity(intent);
    }

}