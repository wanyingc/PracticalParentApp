package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ca.cmpt276.practicalparent.R;
import ca.cmpt276.practicalparent.model.Child;
import ca.cmpt276.practicalparent.model.ChildManager;

import static ca.cmpt276.practicalparent.view.ChildList.encodeToBase64;

/**
 * Used to display the UI for the add child activity.
 */

public class ChildAdd extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

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
        setupChangeImage();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildAdd.class);
    }

    public void setupAdd() {
        Button save = (Button) findViewById(R.id.btnAddChild);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Name
                EditText textBox = (EditText) findViewById(R.id.editTextChildAdd);
                String name = textBox.getText().toString();

                // Image
                ImageView selectedImage = (ImageView) findViewById(R.id.childAddImage);
                String encoded = encodeToBase64( ((BitmapDrawable) selectedImage.getDrawable()).getBitmap() );

                // Add
                Child child = new Child(name, encoded);
                ChildManager.getInstance().add(child);

                String message = name + " added!";
                Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupChangeImage() {
        ImageView image = (ImageView) findViewById(R.id.childAddImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            // Retrieve Image from Gallery
            Uri imageUri = data.getData();
            ImageView selectedImage = (ImageView) findViewById(R.id.childAddImage);
            selectedImage.setImageURI(imageUri);

            String message = "Image added!";
            Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
        }
    }

}