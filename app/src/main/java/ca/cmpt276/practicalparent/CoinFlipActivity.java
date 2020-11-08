package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.Coin;

public class CoinFlipActivity extends AppCompatActivity {
    private static final String EXTRA_PLAYER_ONE = "ca.cmpt276.practicalparent.CoinFlipActivity - player1";
    private static final String EXTRA_PLAYER_TWO = "ca.cmpt276.practicalparent.CoinFlipActivity - player2";
    private static final int PLAYER_UNSET = -1;
    private Coin coin = Coin.getInstance();
    private ChildManager manager;

    private int player1, player2, currentPlayer;
    private int player1Choice, player2Choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = ChildManager.getInstance();
        extractDataFromIntent();
        currentPlayer = player1;
        setupFlipButtons();
        if (player1 != PLAYER_UNSET || player2 != PLAYER_UNSET) {
            setPlayerLabel();
        }

    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        player1 = intent.getIntExtra(EXTRA_PLAYER_ONE, -1);
        player2 = intent.getIntExtra(EXTRA_PLAYER_TWO, -1);
    }

    private void setPlayerLabel() {
        TextView text = findViewById(R.id.playerTurn);
        text.setText(manager.getChild(currentPlayer) + "'s turn!");
    }

    private void alternatePlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        setPlayerLabel();
        Log.e("TAG", ""+manager.getChild(currentPlayer));
    }


    private void setupFlipButtons() {
        final Button headsButton = findViewById(R.id.headsButton);
        final Button tailsButton = findViewById(R.id.tailsButton);
        final TextView text = (TextView)findViewById(R.id.resultsLabel);
        headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                coin.flip();
                flipAnimation();
                headsButton.setClickable(false);
                tailsButton.setClickable(false);
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
                text.setText("");
                coin.flip();
                flipAnimation();
                headsButton.setClickable(false);
                tailsButton.setClickable(false);
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

    /**
     * Citation: https://ssaurel.medium.com/learn-to-create-a-flip-coin-application-on-android-7f2ba5c6dc64
     */
    private void flipAnimation() {
        final ImageView coinImage = (ImageView) findViewById(R.id.coinDisplay);
        Animation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(100);
        fadeOut.setFillAfter(true);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                coinImage.setImageResource(coin.getCoin() == Coin.HEADS ? R.drawable.coin_heads : R.drawable.coin_tails);
                Animation fadeIn = new AlphaAnimation(0,1);
                fadeIn.setInterpolator(new DecelerateInterpolator());
                fadeIn.setDuration(3000);
                fadeIn.setFillAfter(true);

                coinImage.startAnimation(fadeIn);
                coinImage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateResultLabel();
                    }
                }, 1000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        coinImage.startAnimation(fadeOut);
    }

    private void updateResultLabel() {
        TextView text = (TextView)findViewById(R.id.resultsLabel);
        if (coin.getCoin() == Coin.HEADS) {
            text.setText("Heads!");
        } else {
            text.setText("Tails!");
        }
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