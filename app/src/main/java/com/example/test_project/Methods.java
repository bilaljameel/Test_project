package com.example.test_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Methods {

    Context context;

    long timestamp;
    String date_time;
    int mHour;
    int mMinute;


    public void datePicker(Context con){
        context =con;
        long fnf;

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        tiemPicker(year,monthOfYear,dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    private void tiemPicker(final int year, final int month, final int day){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        getTimeToTimestamp(year,month,day,hourOfDay,minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }



    private void getTimeToTimestamp(int year, int month, int day, int hour, int mint){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, mint);
        calendar.set(Calendar.SECOND, 0);

        timestamp = calendar.getTimeInMillis();
        timestamp = timestamp/1000;
        System.out.println(timestamp);
        MainActivity.timestamp = timestamp;

    }

    public String getTimestampToTime(long time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        java.util.Date currenTimeZone=new java.util.Date((long)time*1000);
        return sdf.format(currenTimeZone);
    }

}
