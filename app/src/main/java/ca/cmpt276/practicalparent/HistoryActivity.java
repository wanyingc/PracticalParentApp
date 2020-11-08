package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
        int i = 0;
        String[] myItems = new String[historyManager.size()];
        for (HistoryEntry entry : historyManager) {
            myItems[i] = "heads Player";
            i++;
            myItems[i] = "tails Player";
            i++;
            myItems[i] = "date";
            i++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item);
        GridView grid = (GridView) findViewById(R.id.historyGrid);
        grid.setAdapter(adapter);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, HistoryActivity.class);
    }
}