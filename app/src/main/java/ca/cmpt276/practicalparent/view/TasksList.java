package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.Task;
import ca.cmpt276.practicalparent.model.TaskManager;

public class TasksList extends AppCompatActivity {
    private TaskManager manager;
    private ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Task List");
//
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TaskAdd.makeIntent(TasksList.this);
                startActivity(intent);
            }
        });

        manager = TaskManager.getInstance();

        // Populate the List with some test lenses
        manager.addTask(new Task("First bath"));
        manager.addTask(new Task("Put pop can into can cooler"));

    }

    // Intent from Main menu to task list activity
    public static Intent makeIntent(Context context) {
        return new Intent(context, TasksList.class);
    }

    private void populateTaskList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        adapter = new TaskListAdapter();
        ListView list = (ListView) findViewById(R.id.taskList);
        list.setAdapter(adapter);
    }

    private class TaskListAdapter extends ArrayAdapter<Task> {
        public TaskListAdapter() {
            super(TasksList.this,R.layout.activity_tasks_list, manager.TaskList());
        }
        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_items,parent,false);
            }

            // Current Task
            Task currentTask = manager.getTask(index);

            // Names
            TextView taskView = (TextView) itemView.findViewById(R.id.taskDescription);
            taskView.setText(currentTask.getTaskName());


            return itemView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTaskList();
    }
}