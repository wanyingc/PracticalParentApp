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

import static ca.cmpt276.practicalparent.view.ChildList.decodeBase64;
import static ca.cmpt276.practicalparent.view.ChildList.encodeToBase64;

/**
 * Used to display the UI for the child edit activity.
 */

public class ChildEdit extends AppCompatActivity {
    private int childIndex;
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_IMAGE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Child");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        extractExtras();
        inputFields();
        setupChangeImage();
        setupApplyChange();
        setupDelete();
    }

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, ChildEdit.class);
        intent.putExtra("ca.cmpt276.practicalparent - selectedTask", index);
        return intent;
    }

    public void extractExtras() {
        Intent intent = getIntent();
        childIndex = intent.getIntExtra("ca.cmpt276.practicalparent - selectedChild",0);
    }

    private void inputFields() {
        Child child = ChildManager.getInstance().getChild(childIndex);

        EditText name = (EditText) findViewById(R.id.editTextSelectedChild);
        name.setText(child.getName());

        ImageView image = (ImageView) findViewById(R.id.childEditImage);
        if (child.getBitmap() == null) {
            image.setImageResource(R.drawable.default_image); // Default Image: tangi.co
        } else {
            Bitmap icon = decodeBase64(child.getBitmap());
            image.setImageBitmap(icon); // User Inputted Image
        }
    }

    private void selectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChildEdit.this);
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
                        ImageView image = (ImageView) findViewById(R.id.childEditImage);
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
        ImageView image = (ImageView) findViewById(R.id.childEditImage);
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
            ImageView selectedImage = (ImageView) findViewById(R.id.childEditImage);
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
            Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void setupApplyChange() {
        Button apply = (Button) findViewById(R.id.btnApplyChange);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Name
                EditText editBox = (EditText) findViewById(R.id.editTextSelectedChild);
                String name = editBox.getText().toString();
                if (name.matches("")) {
                    String message = "Name cannot be empty!";
                    Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    ChildManager.getInstance().getChild(childIndex).setName(name);

                    // Image
                    ImageView selectedImage = (ImageView) findViewById(R.id.childEditImage);
                    String encoded = encodeToBase64( ((BitmapDrawable) selectedImage.getDrawable()).getBitmap() );
                    ChildManager.getInstance().getChild(childIndex).setBitmap(encoded);

                    String message = "Changes successful!";
                    Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void setupDelete() {
        Button delete = (Button) findViewById(R.id.btnDeleteChild);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ChildManager.getInstance().getChild(childIndex).getName() + " deleted!";
                ChildManager.getInstance().deleteChild(childIndex);

                Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}