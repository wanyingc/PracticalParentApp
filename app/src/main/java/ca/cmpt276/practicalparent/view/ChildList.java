package ca.cmpt276.practicalparent.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;

/**
 * Used to display the UI for the list of children.
 */

public class ChildList extends AppCompatActivity {

    private static final String PREF_NAME = "Name List Storage";
    private static final String NUM_STORED_VALUES = "Number of Stored Values";
    private static final String STORED_NAME = "Stored Name";
    private static final String STORED_BITMAP = "Stored Bitmap";
    private ChildManager manager;
    private ArrayAdapter<Child> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Child List");

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
        childClickHandler();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildList.class);
    }

    private void populateChildrenList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.childrenList);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(ChildList.this,R.layout.child_config_item, manager.children());
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_config_item,parent,false);
            }

            // Current Child
            Child currentChild = manager.getChild(position);

            // Names
            TextView nameView = (TextView) itemView.findViewById(R.id.config_item_name);
            nameView.setText(currentChild.getName());

            // Images
            ImageView imageView = (ImageView) itemView.findViewById(R.id.config_item_image);
            if (currentChild.getBitmap() == null) {
                imageView.setImageResource(R.drawable.default_image);
            } else {
                Bitmap icon = decodeBase64(currentChild.getBitmap());
                imageView.setImageBitmap(icon); // User Inputted Image
            }

            return itemView;
        }
    }

    private void childClickHandler() {
        ListView list = (ListView) findViewById(R.id.childrenList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String message = "Editing " + manager.getChild(position).getName();
                Toast.makeText(ChildList.this, message, Toast.LENGTH_LONG).show();

                Intent intent = ChildEdit.makeIntent(ChildList.this, position);
                startActivity(intent);
            }
        });
    }

    public void storeNamesAndSizeToSP() {
        SharedPreferences prefs = this.getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(NUM_STORED_VALUES,manager.size());
        for(int i=0;i<manager.size();i++) {
            String name = STORED_NAME + i;
            editor.putString(name,manager.getChild(i).getName());

            String bitmap = STORED_BITMAP + i;
            editor.putString(bitmap,manager.getChild(i).getBitmap());
        }
        editor.apply();
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onResume() {
        super.onResume();
        storeNamesAndSizeToSP();
        populateChildrenList();
    }
}