package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photogallery.search.SearchPhoto;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int SEARCH_REQUEST = 2;

    private TextView tvTimestamp;
    private ArrayList<String> imagePaths;
    private int index = 0;

    private String searchInput, startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimestamp = findViewById(R.id.textViewTimeStamp);

        Button btnGoToSearch = findViewById(R.id.buttonSearch);
        btnGoToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPhoto();
            }
        });

        Button shareBtn = findViewById(R.id.btn_Share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(currentPhotoPath);
            }
        });

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
            //setPic(path);
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
        tvTimestamp.setText(getPhotoFullTimestamp(imagePaths.get(index)));
        getGeoLocation(imagePaths.get(index));
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
            setPic(currentPhotoPath);
        }else if(resultCode == RESULT_OK && requestCode == SEARCH_REQUEST) {
            searchInput = data.getStringExtra("searchInput").trim();
            startDate = data.getStringExtra("startDate");
            endDate = data.getStringExtra("endDate");

            String result = SearchPhoto.search(imagePaths, searchInput, startDate, endDate);

            if(result != null){
                currentPhotoPath = result;
            }else{
                currentPhotoPath = imagePaths.get(0);
            }

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
        tvTimestamp.setText(getPhotoFullTimestamp(currentPhotoPath));
    }

    public static String getPhotoDate(String photoPath){
        String[] fileNameElements = getFileName(photoPath).split("_");
        String timestamp = fileNameElements[1];
        return timestamp;
    }

    public static String getPhotoFullTimestamp(String photoPath){
        String[] fileNameElements = getFileName(photoPath).split("_");
        String timestampStr = fileNameElements[1] + fileNameElements[2];
        String formattedTime;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date timestamp;

        try {
            timestamp = formatter.parse(timestampStr);

            SimpleDateFormat fmtOut = new SimpleDateFormat(("d MMM, yyyy, HH:mm:ss"));
            formattedTime = fmtOut.format(timestamp);

        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return formattedTime;
    }

    public static String getFileName(String photoPath){
        int startIndex = photoPath.lastIndexOf("/") + 1;
        return photoPath.substring(startIndex);
    }

    private void getGeoLocation(String photoPath){
        try {
            ExifInterface exif = new ExifInterface(photoPath);
            String lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String lng = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

            TextView tvLocation = findViewById(R.id.tvLocation);
            tvLocation.setText(lat + " " + lng);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void share(String uriToImage){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }
}
