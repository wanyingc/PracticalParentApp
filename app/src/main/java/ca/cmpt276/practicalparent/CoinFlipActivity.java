package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

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

import ca.cmpt276.practicalparent.model.Coin;

public class CoinFlipActivity extends AppCompatActivity {
    public static final String EXTRA_PLAYER_ONE = "ca.cmpt276.practicalparent.CoinFlipActivity - player1";
    public static final String EXTRA_PLAYER_TWO = "ca.cmpt276.practicalparent.CoinFlipActivity - player2";
    private Coin coin = Coin.getInstance();
    private int player1, player2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extractDataFromIntent();
        setupHeadsButton();
        setupTailsButton();
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        player1 = intent.getIntExtra(EXTRA_PLAYER_ONE, 0);
        player2 = intent.getIntExtra(EXTRA_PLAYER_TWO, 0);
    }

    private void setPlayerLabel() {

    }

    private void setupHeadsButton() {
        Button button = findViewById(R.id.headsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coin.flip();
                //TODO add animation here:
                // flipAnimation();
                flipAnimation();

            }
        });
    }

    private void setupTailsButton() {
        Button button = findViewById(R.id.tailsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coin.flip();
                //TODO add animation here:
                // flipAnimation();
                flipAnimation();

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
        fadeOut.setDuration(500);
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
                fadeIn.setDuration(2000);
                fadeIn.setFillAfter(true);

                coinImage.startAnimation(fadeIn);
                updateResultLabel();
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