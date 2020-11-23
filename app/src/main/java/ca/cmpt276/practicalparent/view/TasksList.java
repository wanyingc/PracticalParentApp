package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.Task;
import ca.cmpt276.practicalparent.model.TaskManager;

public class TasksList extends AppCompatActivity {
    private AlertDialog dialogBuilder;
    private AlertDialog dialog;
    private TaskManager manager;
    private ArrayAdapter<Task> adapter;
    private int popUpTaskIndex;
    public TextView testText;


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

        if (manager.emptyTask() == true){
            Task task1 = new Task("First bath");
            Task task2 = new Task("Put pop can into can cooler");

            TaskManager.getInstance().addTask(task1);
            TaskManager.getInstance().addTask(task2);
        }

        taskItemClickHandler();

    }

    // Display pop up screen
    public void createPopUpDialog() {
        dialogBuilder = new AlertDialog.Builder(this).create();
        final  View contactPopupView = getLayoutInflater().inflate(R.layout.popupscreen,null);

        Task task = TaskManager.getInstance().getTask(popUpTaskIndex);

        //Set image for popup
        ImageView childImageDialog = (ImageView) contactPopupView.findViewById(R.id.childImagePopUp);
        childImageDialog.setImageResource(R.drawable.default_image);

        dialogBuilder.setTitle(task.getTaskName());
        dialogBuilder.setMessage("Child Name");
        dialogBuilder.setView(contactPopupView);
        dialogBuilder.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogBuilder.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialogBuilder.show();
    }

    private void taskItemClickHandler() {
        ListView list = (ListView) findViewById(R.id.taskList);

        //Hold item to delete
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "Editing " + manager.getTask(position).getTaskName();
                Toast.makeText(TasksList.this, message, Toast.LENGTH_LONG).show();
                Intent intent = TaskEdit.makeIntent(TasksList.this, position);
                startActivity(intent);
                return true;
            }
        });

        //Click on item to show pop up message
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String message = "Showing " + manager.getTask(position).getTaskName();
                Toast.makeText(TasksList.this, message, Toast.LENGTH_LONG).show();

                popUpTaskIndex = position;
                createPopUpDialog();
            }
        });

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