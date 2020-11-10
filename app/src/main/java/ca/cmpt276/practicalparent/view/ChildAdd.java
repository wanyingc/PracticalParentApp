package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.ChildManager;

/**
 * Used to display the UI for the add child activity.
 */

public class ChildAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Child");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupAdd();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildAdd.class);
    }

    public void setupAdd() {
        Button save = (Button) findViewById(R.id.btnAddChild);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textBox = (EditText) findViewById(R.id.editTextChildAdd);
                String name = textBox.getText().toString();

                String message = name + " added!";
                ChildManager.getInstance().add(name);

                Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}