package com.example.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int SEARCH_REQUEST = 2;

    private ImageView mImageView;
    private ArrayList<String> imagePaths;
    private String searchInput, startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoToSearch = findViewById(R.id.buttonSearch);
        btnGoToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPhoto();
            }
        });

        //get the list of images paths
        imagePaths = new ArrayList<>();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] images = storageDir.listFiles();
        for (File image : images)
        {
            String imagePath = image.getPath();
            imagePaths.add(imagePath);
        }

      //  FileProvider.fi
        mImageView = findViewById(R.id.imageView);
        if(!imagePaths.isEmpty())
        {
            String path = imagePaths.get(0);
            File image = new File(path);
            mImageView.setImageURI(Uri.fromFile(image));

            //using bitmap, not working for some reason, myBitmap == null
            //Bitmap myBitmap = BitmapFactory.decodeFile(path);
            // mImageView.setImageBitmap(myBitmap);
        }

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void searchPhoto(){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST);
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
            setPic();
        }else if(resultCode == RESULT_OK && requestCode == SEARCH_REQUEST) {
            searchInput = data.getStringExtra("searchInput").trim();
            startDate = data.getStringExtra("startDate");
            endDate = data.getStringExtra("endDate");

            searchPhoto(searchInput, startDate, endDate);
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

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private void searchPhoto(String searchInput, String startDate, String endDate) {
        ArrayList<String> results = new ArrayList<>();

        if (searchInput != null){
            results = searchPhotoByCaption(searchInput);
        }else if (startDate != null || endDate != null){
            results = searchPhotoByDate(startDate, endDate);
        }

        if (!results.isEmpty()){
            currentPhotoPath = results.get(0);
            setPic();
        }
    }

    private ArrayList<String> searchPhotoByCaption(String searchInput){
        ArrayList<String> results = new ArrayList<>();

        for (String file : imagePaths) {
            if(file.contains(searchInput)){
                results.add(file);
            }
        }

        return results;
    }

    private ArrayList<String> searchPhotoByDate(String startDate, String endDate){
        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> imageDates = new ArrayList<>();

        Collections.sort(imagePaths);

        for (String path : imagePaths) {
            imageDates.add(path.substring(5, 13));
        }

        int firstIndex = imageDates.indexOf(startDate);
        int lastIndex = imageDates.lastIndexOf(endDate);

        if(firstIndex != -1 && lastIndex != -1){
            // TODO: need to add more scenarios
            if(lastIndex >= firstIndex){
                for(int i = firstIndex; i <= lastIndex; i++){
                    results.add(imagePaths.get(i));
                }
            }
        }

        return results;
    }

}
