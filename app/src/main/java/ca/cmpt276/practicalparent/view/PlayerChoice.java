package ca.cmpt276.practicalparent.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.ChildQueue;

/**
 * Used to choose players if there are children stored
 */
public class PlayerChoice extends AppCompatActivity {
    private ChildManager childManager;
    private ChildQueue childQueue;
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
        childQueue = ChildQueue.getInstance();
        populatePlayerList();
        setNobodyButton();
        childClickHandler();

    }
    private void populatePlayerList() {
        ArrayAdapter<Child> adapter = new PlayerChoice.PlayerListAdapter();
        ListView listView = findViewById(R.id.player_queue_list);
        listView.setAdapter(adapter);
    }

    private class PlayerListAdapter extends ArrayAdapter<Child> {
        public PlayerListAdapter() {
            super(PlayerChoice.this, R.layout.child_config_item, childQueue.list());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_config_item, parent,false);
            }

            // Current Child
            Child currentChild = childQueue.getChild(position);

            // Names
            TextView nameView = (TextView) itemView.findViewById(R.id.config_item_name);
            nameView.setText(currentChild.getName());

            // Images
            ImageView imageView = (ImageView) itemView.findViewById(R.id.config_item_image);
            if (currentChild.getBitmap() == null) {
                imageView.setImageResource(R.drawable.default_image); // Default Image: tangi.co
            } else {
                Bitmap icon = ChildList.decodeBase64(currentChild.getBitmap());
                imageView.setImageBitmap(icon); // User Inputted Image
            }

            return itemView;
        }
    }

    public void childClickHandler() {
        ListView list = (ListView) findViewById(R.id.player_queue_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                childQueue.moveToFront(position);
                if (!childQueue.peek().equals(ChildQueue.EMPTY_PLAYER)) {
                    childQueue.removeEmptyPlayer();
                }
                finish();
            }
        });
    }

    public void setNobodyButton() {
        Button button = findViewById(R.id.nobody_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only add empty player if empty player does not already exist
                if(!childQueue.peek().equals(ChildQueue.EMPTY_PLAYER)) {
                    childQueue.addEmptyPlayer();
                }
                finish();
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PlayerChoice.class);
    }
}