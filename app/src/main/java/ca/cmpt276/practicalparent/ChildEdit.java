package ca.cmpt276.practicalparent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.practicalparent.model.ChildManager;

public class ChildEdit extends AppCompatActivity {
    private int childIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);

        extractExtras();
        updateEditBox();
        setupApplyChange();
        setupDelete();
    }

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, ChildEdit.class);
        intent.putExtra("ca.cmpt276.practicalparent - selectedChild", index);
        return intent;
    }

    public void extractExtras() {
        Intent intent = getIntent();
        childIndex = intent.getIntExtra("ca.cmpt276.practicalparent - selectedChild",0);

    }

    private void updateEditBox() {
        EditText name = (EditText) findViewById(R.id.editTextSelectedChild);
        String child = ChildManager.getInstance().getChild(childIndex);
        name.setText(child);
    }

    public void setupApplyChange() {
        Button apply = (Button) findViewById(R.id.btnApplyChange);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editBox = (EditText) findViewById(R.id.editTextSelectedChild);
                String name = editBox.getText().toString();

                ChildManager.getInstance().setChild(childIndex, name);
                String message = "Name changed successfully!";

                Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupDelete() {
        Button delete = (Button) findViewById(R.id.btnDeleteChild);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ChildManager.getInstance().getChild(childIndex) + " deleted!";
                ChildManager.getInstance().deleteChild(childIndex);

                Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}