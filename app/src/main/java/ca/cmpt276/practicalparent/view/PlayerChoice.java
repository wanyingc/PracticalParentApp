package ca.cmpt276.practicalparent.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.Coin;
import ca.cmpt276.practicalparent.model.HistoryEntry;
import ca.cmpt276.practicalparent.model.PlayerQueue;

/**
 * Used to choose players if there are children stored
 */
public class PlayerChoice extends AppCompatActivity {
    private ChildManager childManager;
    private PlayerQueue playerQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_choice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Children");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        playerQueue = PlayerQueue.getInstance();
        populatePlayerList();

    }

    private void populatePlayerList() {
        ArrayAdapter<String> adapter = new PlayerChoice.PlayerListAdapter();
        ListView listView = findViewById(R.id.player_queue_list);
        listView.setAdapter(adapter);
    }

    private class PlayerListAdapter extends ArrayAdapter<String> {
        public PlayerListAdapter() {
            super(PlayerChoice.this, R.layout.player_item, playerQueue.list());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.player_item, parent,false);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("log", ""+playerQueue.getPlayer(position));
                }
            });
            String currentName = playerQueue.getPlayer(position);

            TextView playerText = itemView.findViewById(R.id.queue_player_name);
            playerText.setText(currentName);


            //ImageView image = itemView.findViewById(R.id.player_portrait);
            return itemView;
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayerChoice.class);
    }
}