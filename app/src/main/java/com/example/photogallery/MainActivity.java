package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.net.Uri.fromFile;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ArrayList<String> imagePaths;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currentPhotoPath;

        //get the list of images paths
        imagePaths = new ArrayList<>();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] images = storageDir.listFiles();
        for (File image : images)
        {
            String imagePath = image.getAbsolutePath();
            imagePaths.add(imagePath);
        }

      //  FileProvider.fi
        mImageView = (ImageView)findViewById(R.id.imageView);
        if(!imagePaths.isEmpty())
        {
            String path = imagePaths.get(0);
            setPic(path);
        }

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    public void ScrollPhotos(View v)
    {
        int buttonId = v.getId();
        if(buttonId== R.id.buttonLeft && index > 0) index--;
        else if (buttonId == R.id.buttonRight &&
            index < imagePaths.size())
            index++;
        setPic(imagePaths.get(index));
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), "Error while saving picture.", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic(currentPhotoPath);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic(String path) {
        Bitmap bitmap;
        if(path != null)
        {
            bitmap = BitmapFactory.decodeFile(path);
        }
        else
        {
             bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        }
        mImageView.setImageBitmap(bitmap);
    }
}
