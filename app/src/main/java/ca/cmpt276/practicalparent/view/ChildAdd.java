package ca.cmpt276.practicalparent.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_IMAGE_GALLERY = 1;

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
                if (name.matches("")) {
                    String message = "Name cannot be empty!";
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    // Image
                    ImageView selectedImage = (ImageView) findViewById(R.id.childAddImage);
                    String encoded = encodeToBase64(((BitmapDrawable) selectedImage.getDrawable()).getBitmap());

                    // Add
                    Child child = new Child(name, encoded);
                    ChildManager.getInstance().add(child);

                    String message = name + " added!";
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void selectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChildAdd.this);
        builder.setTitle("Make a Selection");

        String[] buttons = {"Open Gallery", "Open Camera", "Remove Photo", "Cancel"};
        builder.setItems(buttons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openGallery();
                        break;
                    case 1:
                        openCamera();
                        break;
                    case 2:
                        ImageView image = (ImageView) findViewById(R.id.childAddImage);
                        image.setImageResource(R.drawable.default_image);
                        break;
                    case 3:
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupChangeImage() {
        ImageView image = (ImageView) findViewById(R.id.childAddImage);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionDialog();
            }
        });
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            System.out.println("Unable to open Camera");
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ImageView selectedImage = (ImageView) findViewById(R.id.childAddImage);
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    selectedImage.setImageBitmap(imageBitmap);
                    break;
                case REQUEST_IMAGE_GALLERY:
                    // Retrieve Image from Gallery
                    Uri imageUri = data.getData();
                    selectedImage.setImageURI(imageUri);
                    break;
            }
            String message = "Image added!";
            Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}