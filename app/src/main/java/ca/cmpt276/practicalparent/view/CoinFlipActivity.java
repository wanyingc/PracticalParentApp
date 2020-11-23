package ca.cmpt276.practicalparent.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.ChildQueue;
import ca.cmpt276.practicalparent.model.Coin;
import ca.cmpt276.practicalparent.model.HistoryEntry;
import ca.cmpt276.practicalparent.model.HistoryManager;

/**
 * Used to display the UI of CoinFlipActivity
 */
public class CoinFlipActivity extends AppCompatActivity {
    public static final Child NO_PLAYER = new Child("");
    private boolean isPlayer;
    private Coin coin = Coin.getInstance();
    private ChildManager manager;
    private Child currentPlayer;
    private int currentPlayerChoice;
    private HistoryManager historyManager;
    private ChildQueue childQueue;

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
        childQueue = ChildQueue.getInstance();


        childQueue.update();
        setupFlipButtons();
        checkForPlayers();
        setPlayerLabel();
        setupSwitchPlayerButton();
    }



    @Override
    protected void onResume() {
        super.onResume();
        childQueue.update();
        checkForPlayers();
        setPlayerLabel();
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
                    Intent intent = HistoryActivity.makeIntent(this, manager.indexOfChild(currentPlayer));
                    startActivity(intent);
                } catch (Exception e) {
                    return false;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveQueueOrderToSP();
    }

    private void checkForPlayers() {
        if (manager.size() <= 0 || childQueue.peek().equals(ChildQueue.EMPTY_PLAYER)) {
            currentPlayer = ChildQueue.EMPTY_PLAYER;
            isPlayer = false;
        } else {
            currentPlayer = childQueue.peek();
            isPlayer = true;
        }
    }

    private void setPlayerLabel() {
        TextView text = findViewById(R.id.playerTurn);
        ImageView imageView = findViewById(R.id.coin_flip_player_image);

        if (isPlayer) {
            text.setText(currentPlayer.getName());
            Bitmap icon = ChildList.decodeBase64(currentPlayer.getBitmap());
            imageView.setImageBitmap(icon);
        }
        else {
            text.setText("");
        }
    }


    private void alternatePlayer() {
        if (isPlayer) {
            currentPlayer = childQueue.rotate();
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
                currentPlayerChoice = Coin.HEADS;
                text.setText("");
                coin.flip();
                flipAnimation();
                headsButton.setClickable(false);
                tailsButton.setClickable(false);
                if (isPlayer) {
                    addToHistory();
                } else {
                    childQueue.removeEmptyPlayer();
                    checkForPlayers();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alternatePlayer();
                        headsButton.setClickable(true);
                        tailsButton.setClickable(true);

                    }
                }, 2500);
            }
        });
        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlayerChoice = Coin.TAILS;
                text.setText("");
                coin.flip();
                flipAnimation();
                headsButton.setClickable(false);
                tailsButton.setClickable(false);
                if (isPlayer) {
                    addToHistory();
                } else {
                    childQueue.removeEmptyPlayer();
                    checkForPlayers();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alternatePlayer();
                        headsButton.setClickable(true);
                        tailsButton.setClickable(true);
                    }
                }, 2500);
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
        HistoryEntry entry = new HistoryEntry(currentPlayer.getName(), currentPlayerChoice, coin.getCoin());
        historyManager.addEntry(entry);
        saveHistoryToSP(entry);
    }

    private void saveHistoryToSP(HistoryEntry entry) {
        HistoryManager historyManager = HistoryManager.getInstance();
        SharedPreferences prefs = this.getSharedPreferences("HistPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int i = historyManager.size();
        editor.putString("player"+i, entry.getPlayer());
        editor.putInt("player_choice"+i, entry.getPlayerChoice());
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
            String player = prefs.getString("player"+i, "");
            int playerChoice = prefs.getInt("player_choice"+i, 0);
            String date = prefs.getString("date"+i, "");
            String time = prefs.getString("time"+i, "");
            int result = prefs.getInt("result"+i, 0);
            HistoryEntry entry = new HistoryEntry(player, playerChoice, result, date, time);
            historyManager.addEntry(entry);
        }
    }

    private void saveQueueOrderToSP() {
        SharedPreferences prefs = this.getSharedPreferences("QueuePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < manager.size(); i++) {
            editor.putInt(childQueue.getChild(i).getName(), i);
        }
        editor.apply();
    }

    private void fillQueue() {
        if (manager.size() > 0) {
            SharedPreferences prefs = getSharedPreferences("QueuePrefs", MODE_PRIVATE);
            childQueue.clearQueue();
            for (int i = 0; i < manager.size(); i++) {
                childQueue.enqueue(manager.getChild(i), prefs.getInt(manager.getChild(i).getName(), i));
            }
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

    private void setupSwitchPlayerButton() {
        ConstraintLayout layout = findViewById(R.id.current_child_bar);
        layout.setClickable(true);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PlayerChoice.makeIntent(CoinFlipActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipActivity.class);
    }
}