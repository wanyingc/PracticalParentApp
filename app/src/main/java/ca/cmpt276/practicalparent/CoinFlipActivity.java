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
    Coin coin = Coin.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupFlipButton();
        createPlayerFragment();

    }

    private void createPlayerFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ChooseCoinFragment dialog = new ChooseCoinFragment();
        dialog.show(manager, "Message Dialog");
    }



    private void setupFlipButton() {
        Button button = findViewById(R.id.flipButton);
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

}