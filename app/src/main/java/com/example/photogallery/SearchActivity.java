package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView startDate;
    private TextView endDate;

    private static int START_DATE_FLAG = 0;
    private static int END_DATE_FLAG = 1;

    private int dateFlag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        startDate = findViewById(R.id.textStartDate);
        endDate = findViewById(R.id.textEndDate);

        findViewById(R.id.btnStartDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFlag = START_DATE_FLAG;
                showDateDialog();
            }
        });

        findViewById(R.id.btnEndDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFlag = END_DATE_FLAG;
                showDateDialog();
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
                startDate.setText(fmtOut.format(date));
            }else {
                endDate.setText(fmtOut.format(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}