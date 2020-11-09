package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.HistoryEntry;
import ca.cmpt276.practicalparent.model.HistoryManager;

public class HistoryActivity extends AppCompatActivity {
    HistoryManager historyManager;
    ChildManager childManager;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = findViewById(R.id.historyGrid);
        gridView.setClickable(false);
        historyManager = HistoryManager.getInstance();
        childManager = ChildManager.getInstance();
        fillHistory();
    }

    private void fillHistory() {
        int i = 0;
        String[] items = new String[historyManager.size()*3];
        for (HistoryEntry entry: historyManager) {
            Log.e("TAG", "1");
            items[i] = childManager.getChild(entry.getHeadsPlayer());
            i++;
            items[i] = childManager.getChild(entry.getTailsPlayer());
            i++;
            items[i] = entry.getTime() + " - " + entry.getDate().toString();
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.grid_item, items);
        gridView.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HistoryActivity.class);
    }
}