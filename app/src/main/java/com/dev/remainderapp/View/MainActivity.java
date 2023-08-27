package com.dev.remainderapp.View;

import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_DATE;
import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_EVENT_NOTES;
import static com.dev.remainderapp.apputils.appconfig.BIRTHDAY_EVENT_TIME;
import static com.dev.remainderapp.apputils.appconfig.FRIEND_NAME;
import static com.dev.remainderapp.apputils.appconfig.PERMISSION_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dev.remainderapp.R;
import com.dev.remainderapp.View.adapter.BirthdayEventAdapter;
import com.dev.remainderapp.View.adapter.onDeleteListner;
import com.dev.remainderapp.broadcast.NotificationReceiver;
import com.dev.remainderapp.ui.theme.localdb.DatabaseClass;
import com.dev.remainderapp.ui.theme.localdb.EntityClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, onDeleteListner {
    FloatingActionButton birthdayeventbtn;
    BirthdayEventAdapter eventAdapter;
    RecyclerView recyclerview;
    DatabaseClass databaseClass;

    TextView  txtv_notfound;

    String timeTonotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         initv();
    }

    private void initv() {
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("110")){
                getNotificationPermission();
            }
        }
        birthdayeventbtn = findViewById(R.id.add_alarm_fab);
        txtv_notfound = findViewById(R.id.txtv_notfound);
        recyclerview = findViewById(R.id.recyclerview);
        birthdayeventbtn.setOnClickListener(this);
        databaseClass = DatabaseClass.getDatabase(getApplicationContext());

    }

    private void setAdapter() {
        List<EntityClass> classList = databaseClass.EventDao().getAllData();

        if(classList.size()>0)txtv_notfound.setVisibility(View.GONE);else txtv_notfound.setVisibility(View.VISIBLE);
        eventAdapter = new BirthdayEventAdapter(getApplicationContext(), classList);
        recyclerview.setAdapter(eventAdapter);
        eventAdapter.setDeleteListner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();

    }


    @Override
    public void onClick(View view) {
        showBottomDialog();
//        if (view == birthdayeventbtn) {
//            Intent intent = new Intent(getApplicationContext(), BirthdayEventActivity.class);
//            startActivity(intent);
//        }
    }



    public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                }  else {
                    //deny
                }
                return;

        }
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        EditText personname=dialog.findViewById(R.id.edt_pname);
        EditText personbd_date=dialog.findViewById(R.id.edt_pbdate);
        EditText personbd_time=dialog.findViewById(R.id.edt_pbtime);
        EditText personbdnotes=dialog.findViewById(R.id.edt_pbnotes);
        Button btn_submit=dialog.findViewById(R.id.btn_submit);



        personbd_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(personbd_date);
            }
        });
        personbd_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime(personbd_time);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personname.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter friend Name Please", Toast.LENGTH_SHORT).show();
                }else  if(personbd_date.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter friend upcoming birthday date", Toast.LENGTH_SHORT).show();
                }else  if(personbd_time.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter event time", Toast.LENGTH_SHORT).show();
                }else{

                    EntityClass entityClass = new EntityClass();
                    String pname = (personname.getText().toString().trim());
                    String date = (personbd_date.getText().toString().trim());
                    String time = (personbd_time.getText().toString().trim());
                    String notes = (personbdnotes.getText().toString().trim());

                    entityClass.setFriendname(pname);
                    entityClass.setBirthdaydate(date);
                    entityClass.setEventtime(time);
                    entityClass.setBirthday_notes(notes);
                    databaseClass.EventDao().insertAll(entityClass);
                    dialog.dismiss();
                    initv();
                    setAlarm(pname, date, time,notes);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void setAlarm(String pname, String date, String time,String notes) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra(FRIEND_NAME, pname);
        intent.putExtra(BIRTHDAY_DATE, date);
        intent.putExtra(BIRTHDAY_EVENT_TIME, time);
        intent.putExtra(BIRTHDAY_EVENT_NOTES, notes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");


        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        startActivity(getIntent());
    }

    private void selectDate(EditText personbd_date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                personbd_date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void selectTime(EditText personbd_time) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                personbd_time.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();

    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    @Override
    public void ondelete(String name) {
        databaseClass.EventDao().deleteById(name);
        setAdapter();
    }
}