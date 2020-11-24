package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Random;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;
import ca.cmpt276.practicalparent.model.HistoryManager;
import ca.cmpt276.practicalparent.model.Task;
import ca.cmpt276.practicalparent.model.TaskManager;

/**
 * Displays task list
 */
public class TasksList extends AppCompatActivity {
    private AlertDialog dialogBuilder;
    private TaskManager manager;
    private ChildManager childManager;
    private ArrayAdapter<Task> adapter;
    private Child currentChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_list);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Task List");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TaskAdd.makeIntent(TasksList.this);
                startActivity(intent);
            }
        });

        childManager = ChildManager.getInstance();
        manager = TaskManager.getInstance();
        loadTasksFromSP();
        taskItemClickHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePlayers();
        updateTasks();
        populateTaskList();
        saveTasksToSP();
    }
    // Display pop up screen
    public void createPopUpDialog(View viewClicked, final int position) {
        dialogBuilder = new AlertDialog.Builder(this).create();
        View contactPopupView = getLayoutInflater().inflate(R.layout.popupscreen,null);
        final Task task = manager.getTask(position);
        final TextView viewChildName = (TextView) findViewById(R.id.nextTurnChild);
        final Child childTurn = task.getChild();
        //Set image for popup
        ImageView childImageDialog = (ImageView) contactPopupView.findViewById(R.id.childImagePopUp);
        //childImageDialog.setImageResource(R.drawable.default_image);
        if (!childTurn.equals(ChildManager.NO_CHILD)) {
            Bitmap icon = ChildList.decodeBase64(childTurn.getBitmap());
            childImageDialog.setImageBitmap(icon); // User Inputted Image
        }


        dialogBuilder.setTitle(task.getTaskName());
        dialogBuilder.setMessage(childTurn.getName());
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
                        if (task.getChild().equals(childManager.NO_CHILD)) {
                            viewChildName.setText("No child configured.");
                        }
                        else {
                            task.setChild(childManager.getChild((childManager.indexOfChild(childTurn) + 1) % childManager.size()));
                            viewChildName.setText(childTurn.getName());
                            populateTaskList();
                        }
                        saveTasksToSP();
                    }
                });

        dialogBuilder.show();
    }

    private void saveTasksToSP() {
        SharedPreferences prefs = this.getSharedPreferences("taskPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < manager.size(); i++) {
            if (childManager.size() > 0) {
                editor.putInt("task_child_index" + i, childManager.indexOfChild(manager.getTask(i).getChild()));
            }
            editor.putString("task_name" + i, manager.getTask(i).getTaskName());
        }
        editor.putInt("task_size", manager.size());
        editor.apply();
    }

    private void loadTasksFromSP() {
        SharedPreferences prefs = getSharedPreferences("taskPrefs", MODE_PRIVATE);
        manager.clear();
        Child child;
        int playerIndex;
        String taskName;
        int size = prefs.getInt("task_size", 0);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                playerIndex = prefs.getInt("task_child_index" + i, 0);
                taskName = prefs.getString("task_name"+i, "");
                child = childManager.getChild(playerIndex);
                Task task = new Task(taskName, child);
                if (!manager.contains(taskName)) {
                    manager.addTask(task);
                }
            }
        }
    }

    private void updateTasks() {
        for (Task t : manager) {
            if (!childManager.list().contains(t.getChild())) {
                t.setChild(currentChild);
            }
        }
    }

    public void updatePlayers() {
        if (childManager.size() == 0){
            currentChild = childManager.NO_CHILD;
        }
        else {
            currentChild = childManager.getChild(0);
        }
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

                createPopUpDialog(viewClicked, position);
            }
        });

    }

    // Intent from Main menu to task list activity
    public static Intent makeIntent(Context context) {
        return new Intent(context, TasksList.class);
    }

    public void populateTaskList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        adapter = new TaskListAdapter();
        ListView list = (ListView) findViewById(R.id.taskList);
        list.setAdapter(adapter);
    }

    public class TaskListAdapter extends ArrayAdapter<Task> {
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
            TextView taskView = (TextView) itemView.findViewById(R.id.taskDescription);
            taskView.setText(currentTask.getTaskName());

            // Task is set to default first child added
            if (childManager.size() == 0){
                TextView childNameText = (TextView) itemView.findViewById(R.id.nextTurnChild);
                childNameText.setText("No child configured.");
            }
            else if (childManager.size() >= 0){
//                indexOfChild = 1;
//                Child defaultChildTurn = childManager.getChild(0);
//                TextView testChildCount = (TextView) itemView.findViewById(R.id.nextTurnChild);
//                testChildCount.setText(defaultChildTurn.getName());

                TextView childNext = (TextView) itemView.findViewById(R.id.nextTurnChild);
                childNext.setText(currentTask.getChild().getName());
            }


            return itemView;
        }
    }




}