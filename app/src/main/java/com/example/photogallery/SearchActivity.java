package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText searchInput;
    private TextView startDate;
    private TextView endDate;
    private Button btnStartDate;
    private Button btnEndDate;
    private Button btnSearch;

    private String strStartDate;
    private String strEndDate;

    private static int START_DATE_FLAG = 1;
    private static int END_DATE_FLAG = 2;

    private int dateFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.etSearch);
        startDate = findViewById(R.id.tvStartDate);
        endDate = findViewById(R.id.tvEndDate);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnSearch = findViewById(R.id.btnSearch);

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFlag = START_DATE_FLAG;
                showDateDialog();
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFlag = END_DATE_FLAG;
                showDateDialog();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent  = new Intent(SearchActivity.this, MainActivity.class)
                    .putExtra("searchInput", searchInput.getText().toString())
                    .putExtra("startDate", strStartDate)
                    .putExtra("endDate", strEndDate);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void showDateDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateStr = year + "-" + (month + 1) + "-" + dayOfMonth;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");

        try {
            Date date = formatter.parse(dateStr);

            SimpleDateFormat fmtOut = new SimpleDateFormat("d MMM, yyyy");

            if(dateFlag == START_DATE_FLAG) {
                strStartDate = fmtOut.format(date);
                startDate.setText(strStartDate);
            }else {
                strEndDate = fmtOut.format(date);
                endDate.setText(strEndDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}