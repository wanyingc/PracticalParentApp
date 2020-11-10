package ca.cmpt276.practicalparent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.cmpt276.practicalparent.model.ChildManager;

public class ChildList extends AppCompatActivity {

    private static final String PREF_NAME = "Name List Storage";
    private static final String NUM_STORED_VALUES = "Number of Stored Values";
    private ChildManager manager;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChildAdd.makeIntent(ChildList.this);
                startActivity(intent);
            }
        });

        manager = ChildManager.getInstance();
        getNamesAndSizeFromSP();
        childClickHandler();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildList.class);
    }

    private void populateChildrenList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        int i = 0;
        String[] children = new String[manager.size()];
        for (String s: manager) {
            children[i] = s;
            i++;
        }
        adapter = new ArrayAdapter<String>(this, R.layout.item, children);
        ListView list = (ListView) findViewById(R.id.childrenList);
        list.setAdapter(adapter);
    }

    private void childClickHandler() {
        ListView list = (ListView) findViewById(R.id.childrenList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "Editing " + textView.getText().toString();
                Toast.makeText(ChildList.this, message, Toast.LENGTH_LONG).show();

                Intent intent = ChildEdit.makeIntent(ChildList.this, position);
                startActivity(intent);
            }
        });
    }

    public void getNamesAndSizeFromSP() {
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        final int childListSize = prefs.getInt(NUM_STORED_VALUES,0);
        manager.clear();
        for(int i=0;i<childListSize;i++) {
            String index = "Stored Name " + i;
            manager.add(prefs.getString(index,""));
        }
    }

    public void storeNamesAndSizeToSP() {
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_STORED_VALUES,manager.size());
        for(int i=0;i<manager.size();i++) {
            String index = "Stored Name " + i;
            editor.putString(index,manager.getChild(i));
        }
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        storeNamesAndSizeToSP();
        populateChildrenList();
    }
}