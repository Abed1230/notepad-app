package com.abed.notepad;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateAndTimePickerActivity extends AppCompatActivity {

    private Button btnDate;
    private Button btnTime;

    private Calendar calendar;

    private boolean dateIsSet;
    private boolean timeIsSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_time_picker);

        btnDate = findViewById(R.id.btn_date);
        btnTime = findViewById(R.id.btn_time);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);

        calendar = Calendar.getInstance();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(DateAndTimePickerActivity.this, onDateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(DateAndTimePickerActivity.this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(DateAndTimePickerActivity.this)).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dateIsSet && timeIsSet) {
                    // Check if time has passed
                    if (!(calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())) {
                        Intent result = new Intent();
                        result.putExtra("time", calendar.getTimeInMillis());
                        setResult(RESULT_OK, result);
                        finish();
                    } else {
                        Toast.makeText(DateAndTimePickerActivity.this, "Time has passed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DateAndTimePickerActivity.this, "Set date and time first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String date = sdf.format(calendar.getTime());
            btnDate.setText(date);
            dateIsSet = true;
        }
    };

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String pattern;
            if (DateFormat.is24HourFormat(DateAndTimePickerActivity.this)) {
                pattern = "HH:mm";
            } else {
                pattern = "hh:mm aaa";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            String time = sdf.format(calendar.getTime());
            btnTime.setText(time);
            timeIsSet = true;
        }
    };
}