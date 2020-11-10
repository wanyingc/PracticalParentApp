package ca.cmpt276.practicalparent.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.Coin;
import ca.cmpt276.practicalparent.model.HistoryEntry;
import ca.cmpt276.practicalparent.model.HistoryManager;

/**
 * Used to display the UI of CoinFlipActivity
 */
public class CoinFlipActivity extends AppCompatActivity {
    private static final String EXTRA_PLAYER_ONE = "ca.cmpt276.practicalparent.view.CoinFlipActivity - player1";
    private static final String EXTRA_PLAYER_TWO = "ca.cmpt276.practicalparent.view.CoinFlipActivity - player2";
    public static final int NO_PLAYER = -1;
    private boolean isPlayers;
    private Coin coin = Coin.getInstance();
    private ChildManager manager;
    private int player1, player2, currentPlayer;
    private int player1Choice;
    private HistoryManager historyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Coin Flip");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        manager = ChildManager.getInstance();
        historyManager = HistoryManager.getInstance();
        //loadHistoryEntryFromSP(this);

        extractDataFromIntent();
        setupFlipButtons();
        if (player1 != NO_PLAYER && player2 != NO_PLAYER) {
            currentPlayer = player1;
            setPlayerLabel();
            isPlayers = true;
        } else {
            currentPlayer = NO_PLAYER;
            isPlayers = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        extractDataFromIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coinflip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.go_to_history:
                try {
                    Intent intent = HistoryActivity.makeIntent(this, currentPlayer);
                    startActivity(intent);
                } catch (Exception e) {
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setPlayerLabel() {
        TextView text = findViewById(R.id.playerTurn);
        text.setText(manager.getChild(currentPlayer) + "'s turn!");
    }

    private void alternatePlayer() {
        if (isPlayers) {
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            setPlayerLabel();
        } else {
            return;
        }
    }


    private void setupFlipButtons() {
        final Button headsButton = findViewById(R.id.headsButton);
        final Button tailsButton = findViewById(R.id.tailsButton);
        final TextView text = (TextView)findViewById(R.id.resultsLabel);
        headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayer == player1) {
                    player1Choice = Coin.HEADS;
                } else {
                    player1Choice = Coin.TAILS;
                }
                text.setText("");
                coin.flip();
                flipAnimation();
                headsButton.setClickable(false);
                tailsButton.setClickable(false);
                if (isPlayers) {
                    addToHistory();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alternatePlayer();
                        headsButton.setClickable(true);
                        tailsButton.setClickable(true);

                    }
                }, 1800);
            }
        });
        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlayer == player1) {
                    player1Choice = Coin.TAILS;
                } else {
                    player1Choice = Coin.HEADS;
                }
                text.setText("");
                coin.flip();
                flipAnimation();
                headsButton.setClickable(false);
                tailsButton.setClickable(false);
                if (isPlayers) {
                    addToHistory();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alternatePlayer();
                        headsButton.setClickable(true);
                        tailsButton.setClickable(true);
                    }
                }, 1800);
            }
        });

    }

    private void flipAnimation() {
        /*
          Sound: https://www.storyblocks.com/audio/stock/coin-flip-whirl-high-pitched-land-solid-surface-bounce-bgnx4za2ldbk0wxw9fq.html
         */
        MediaPlayer sound = MediaPlayer.create(CoinFlipActivity.this,R.raw.coin_flip_audio);
        sound.start();

        final ImageView coinImage = (ImageView) findViewById(R.id.coinDisplay);
        Animation coinFlip = AnimationUtils.loadAnimation(this, R.anim.coin_flip_anim);
        coinImage.startAnimation(coinFlip);

        coinFlip.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Animation coinResult = AnimationUtils.loadAnimation(CoinFlipActivity.this, R.anim.coin_flip_result);
                coinImage.startAnimation(coinResult);

                coinImage.setImageResource(coin.getCoin() == Coin.HEADS ? R.drawable.coin_heads : R.drawable.coin_tails);
                coinImage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateResultLabel();
                    }
                }, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void addToHistory() {
        int headsPlayer = (player1Choice == Coin.HEADS) ? player1 : player2;
        int tailsPlayer = (player1Choice == Coin.TAILS) ? player1 : player2;
        HistoryEntry entry = new HistoryEntry(headsPlayer, tailsPlayer, coin.getCoin());
        historyManager.addEntry(entry);
        saveHistoryToSP(entry);

    }

    private void saveHistoryToSP(HistoryEntry entry) {
        HistoryManager historyManager = HistoryManager.getInstance();
        SharedPreferences prefs = this.getSharedPreferences("HistPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int i = historyManager.size();
        editor.putInt("heads_player"+i, entry.getHeadsPlayer());
        editor.putInt("tails_player"+i, entry.getTailsPlayer());
        editor.putString("date"+i, entry.getDate());
        editor.putString("time"+i, entry.getTime());
        editor.putInt("result"+i, entry.getCoinResult());
        editor.putInt("size", historyManager.size());
        editor.apply();
    }

    static public void loadHistoryEntryFromSP(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("HistPrefs", MODE_PRIVATE);
        HistoryManager historyManager = HistoryManager.getInstance();
        historyManager.clear();
        int size = prefs.getInt("size", 0);
        for (int i = 1; i <= size; i++) {
            int playerHeads = prefs.getInt("heads_player"+i, 0);
            int playerTails = prefs.getInt("tails_player"+i, 0);
            String date = prefs.getString("date"+i, "");
            String time = prefs.getString("time"+i, "");
            int result = prefs.getInt("result"+i, 0);
            HistoryEntry entry = new HistoryEntry(playerHeads, playerTails, result, date, time);
            historyManager.addEntry(entry);
        }
    }

    private void updateResultLabel() {
        TextView text = (TextView)findViewById(R.id.resultsLabel);
        if (coin.getCoin() == Coin.HEADS) {
            text.setText("Heads!");
        } else {
            text.setText("Tails!");
        }
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        player1 = intent.getIntExtra(EXTRA_PLAYER_ONE, -1);
        player2 = intent.getIntExtra(EXTRA_PLAYER_TWO, -1);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipActivity.class);
    }

    public static Intent makeIntent(Context context, int player1, int player2) {
        Intent intent = new Intent(context, CoinFlipActivity.class);
        intent.putExtra(EXTRA_PLAYER_ONE, player1);
        intent.putExtra(EXTRA_PLAYER_TWO, player2);
        return intent;
    }

}