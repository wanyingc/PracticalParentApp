package ca.cmpt276.practicalparent.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.Coin;
import ca.cmpt276.practicalparent.model.HistoryEntry;
import ca.cmpt276.practicalparent.model.HistoryManager;

/**
 * Used to display the history of coin flips
 */
public class HistoryActivity extends AppCompatActivity {
    public static final String CURRENT_PLAYER = "ca.cmpt276.practicalparent.view.HistoryActivity - currentPlayer";
    HistoryManager historyManager;
    ChildManager childManager;
    int currentChild;
    List<HistoryEntry> currentPlayerGameList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



        CoinFlipActivity.loadHistoryEntryFromSP(this);
        extractDataFromIntent();
        historyManager = HistoryManager.getInstance();
        childManager = ChildManager.getInstance();

        currentPlayerGameList = updateCurrentPlayerList(currentChild);
        fillHistory();
        setupCurrentPlayerModeSwitch();
        setupCurrentPlayerLabel();

        setupClearHistoryButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCurrentPlayerLabel();
    }

    private void setupClearHistoryButton() {
        Button button = findViewById(R.id.clearHistoryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistoryFromSP();
            }
        });
        fillHistory();
    }

    private void clearHistoryFromSP() {
        SharedPreferences prefs = this.getSharedPreferences("HistPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        historyManager.clear();
        this.recreate();
    }

    private void setupCurrentPlayerLabel() {
        if (currentChild != CoinFlipActivity.NO_PLAYER) {
            TextView text = findViewById(R.id.current_player_label);
            text.setText("Current Player: " + childManager.getChild(currentChild));
        }
    }

    private void setupCurrentPlayerModeSwitch() {
        final Switch s = (Switch)findViewById(R.id.current_player_mode_switch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(s.isChecked()) {
                    // display win mode
                    updateCurrentPlayerList(currentChild);
                    fillCurrentPlayerHistory();
                } else {
                    // display regular mode
                    fillHistory();
                }
            }
        });
    }

    private List<HistoryEntry> updateCurrentPlayerList(int player) {
        List<HistoryEntry> list = new ArrayList<HistoryEntry>();
        for (HistoryEntry entry : historyManager) {
            if (entry.getHeadsPlayer() == player || entry.getTailsPlayer() == player) {
                list.add(entry);
            }
        }
        return list;
    }



    private void fillHistory() {
        ArrayAdapter<HistoryEntry> adapter = new MyListAdapter();
        ListView listView = findViewById(R.id.history_list);
        listView.setAdapter(adapter);
    }

    private void fillCurrentPlayerHistory() {
        ArrayAdapter<HistoryEntry> adapter = new MyListAdapterWinner();
        ListView listView = findViewById(R.id.history_list);
        listView.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<HistoryEntry> {
        public MyListAdapter() {
            super(HistoryActivity.this, R.layout.grid_item, historyManager.list());
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.grid_item, parent,false);
            }
            HistoryEntry currentEntry = historyManager.getEntry(position);
            TextView headText = itemView.findViewById(R.id.head_name_text);
            headText.setText(childManager.getChild(currentEntry.getHeadsPlayer()));

            TextView tailsText = itemView.findViewById(R.id.tails_name_text);
            tailsText.setText(childManager.getChild(currentEntry.getTailsPlayer()));

            TextView dateText = itemView.findViewById(R.id.date_text);
            dateText.setText(currentEntry.getTime() + "  " + currentEntry.getDate());

            ImageView image = itemView.findViewById(R.id.checkMark);
            ImageView image2 = itemView.findViewById(R.id.checkMark2);
            image.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);

            if (currentEntry.getCoinResult() == Coin.HEADS) {
                image.setImageResource(R.drawable.ic_baseline_check_24);
                image2.setImageResource(R.drawable.ic_baseline_clear_24);
            } else {
                image2.setImageResource(R.drawable.ic_baseline_check_24);
                image.setImageResource(R.drawable.ic_baseline_clear_24);
            }

            return itemView;
        }
    }

    private class MyListAdapterWinner extends ArrayAdapter<HistoryEntry> {
        public MyListAdapterWinner() {
            super(HistoryActivity.this, R.layout.grid_item, currentPlayerGameList);
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.grid_item, parent,false);
            }
            HistoryEntry currentEntry = currentPlayerGameList.get(position);
            TextView headText = itemView.findViewById(R.id.head_name_text);
            headText.setText(childManager.getChild(currentEntry.getHeadsPlayer()));

            TextView tailsText = itemView.findViewById(R.id.tails_name_text);
            tailsText.setText(childManager.getChild(currentEntry.getTailsPlayer()));

            TextView dateText = itemView.findViewById(R.id.date_text);
            dateText.setText(currentEntry.getTime() + "  " + currentEntry.getDate());

            ImageView image = itemView.findViewById(R.id.checkMark);
            ImageView image2 = itemView.findViewById(R.id.checkMark2);
            image.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);

            if (currentEntry.getCoinResult() == Coin.HEADS) {
                image.setImageResource(R.drawable.ic_baseline_check_24);
                image2.setImageResource(R.drawable.ic_baseline_clear_24);
            } else {
                image2.setImageResource(R.drawable.ic_baseline_check_24);
                image.setImageResource(R.drawable.ic_baseline_clear_24);
            }

            return itemView;
        }
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        currentChild = intent.getIntExtra(CURRENT_PLAYER, -1);
    }

    public static Intent makeIntent(Context context, int child) {
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putExtra(CURRENT_PLAYER, child);
        return intent;
    }
}