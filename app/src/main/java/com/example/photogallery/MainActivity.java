package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int SEARCH_REQUEST = 1;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == SEARCH_REQUEST) {
            searchInput = data.getStringExtra("searchInput");
            startDate = data.getStringExtra("startDate");
            endDate = data.getStringExtra("endDate");

            String searchResult = searchInput +" " + startDate + " " + endDate;
            // TODO: search the folder where all photos are stored and display the first matched photo
        }
    }

    private void searchPhoto(){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST);
    }
}
