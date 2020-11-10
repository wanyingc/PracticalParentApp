package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.Coin;
import ca.cmpt276.practicalparent.model.HistoryEntry;
import ca.cmpt276.practicalparent.model.HistoryManager;

public class HistoryActivity extends AppCompatActivity {
    HistoryManager historyManager;
    ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        historyManager = HistoryManager.getInstance();
        childManager = ChildManager.getInstance();

        fillHistory();
    }

    private void fillHistory() {
//        int i = 0;
//        String[] items = new String[historyManager.size()*3];
//        for (HistoryEntry entry: historyManager) {
//            if (entry.getResult() == Coin.HEADS) {
//                items[i] = childManager.getChild(entry.getHeadsPlayer())+"(winner)";
//            } else {
//                items[i] = childManager.getChild(entry.getHeadsPlayer());
//            }
//            i++;
//            if (entry.getResult() == Coin.TAILS) {
//                items[i] = childManager.getChild(entry.getTailsPlayer())+"(winner)";
//            } else {
//                items[i] = childManager.getChild(entry.getTailsPlayer());
//            }
//            i++;
//            items[i] = entry.getTime() + " - " + entry.getDate();
//            i++;
//        }

        ArrayAdapter<HistoryEntry> adapter = new MyListAdapter();
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
            dateText.setText(currentEntry.getTime() + " - " + currentEntry.getDate());

            ImageView image = itemView.findViewById(R.id.checkMark);
            ImageView image2 = itemView.findViewById(R.id.checkMark2);
            image.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);

            if (currentEntry.getResult() == Coin.HEADS) {
                image.setImageResource(R.drawable.ic_baseline_check_24);
                image2.setVisibility(View.INVISIBLE);
            } else {
                image2.setImageResource(R.drawable.ic_baseline_check_24);
                image.setVisibility(View.INVISIBLE);
            }

            return itemView;
        }
    }



    public static Intent makeIntent(Context context) {
        return new Intent(context, HistoryActivity.class);
    }
}