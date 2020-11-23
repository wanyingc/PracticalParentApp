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
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.practicalparent.R;

import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.Task;
import ca.cmpt276.practicalparent.model.TaskManager;


public class TaskAdd extends AppCompatActivity {
    private TaskManager manager;
    private ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Task");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        setupAdd();

    }

    private void setupAdd() {
        Button save = (Button) findViewById(R.id.AddTask);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Name
                EditText textBox = (EditText) findViewById(R.id.textInputEditTask);
                String taskName = textBox.getText().toString();


                if (taskName.length() == 0) {
                    String message = "Failed to add task. Field is empty.";
                    Toast.makeText(TaskAdd.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    // Add
 //                   String childTurnNext = childManager.getChild(0).getName();
                    Task task = new Task(taskName,"childTurnNext");
                    TaskManager.getInstance().addTask(task);

                    String message = "New task added!";
                    Toast.makeText(TaskAdd.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskAdd.class);
    }
}