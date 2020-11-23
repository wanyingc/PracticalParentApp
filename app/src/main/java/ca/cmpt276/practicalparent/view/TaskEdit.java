package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Task;
import ca.cmpt276.practicalparent.model.TaskManager;

import static ca.cmpt276.practicalparent.view.ChildList.decodeBase64;
import static ca.cmpt276.practicalparent.view.ChildList.encodeToBase64;

public class TaskEdit extends AppCompatActivity {
    private int taskIndex;

    public static Intent makeIntent(Context context, int position) {
        Intent intent = new Intent(context, TaskEdit.class);
        intent.putExtra("ca.cmpt276.practicalparent - selectedTask", position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        extractTaskIndex();
        inputText();
        setEditButton();
        setRemoveButton();
    }

    private void inputText() {
        Task task = TaskManager.getInstance().getTask(taskIndex);

        EditText name = (EditText) findViewById(R.id.textInputSelectedTask);
        name.setText(task.getTaskName());

    }

    private void extractTaskIndex() {
        Intent intent = getIntent();
        taskIndex = intent.getIntExtra("ca.cmpt276.practicalparent - selectedTask",0);
    }

    private void setRemoveButton() {
        Button delete = (Button) findViewById(R.id.removeTask);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = TaskManager.getInstance().getTask(taskIndex).getTaskName() + " deleted!";
                TaskManager.getInstance().deleteTask(taskIndex);

                Toast.makeText(TaskEdit.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setEditButton() {
        Button apply = (Button) findViewById(R.id.editTask);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editBox = (EditText) findViewById(R.id.textInputSelectedTask);
                String name = editBox.getText().toString();

                if (name.length() == 0) {
                    String message = "Failed to edit. Field is empty.";
                    Toast.makeText(TaskEdit.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    TaskManager.getInstance().getTask(taskIndex).setTaskName(name);


                    String message = "Changes successful!";
                    Toast.makeText(TaskEdit.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}