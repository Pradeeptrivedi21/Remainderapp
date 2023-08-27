package com.dev.remainderapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dev.remainderapp.R;
import com.dev.remainderapp.ui.theme.localdb.DatabaseClass;

import java.util.concurrent.Future;

public class NotificationViewActivity extends AppCompatActivity {

    DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        databaseClass = DatabaseClass.getDatabase(getApplicationContext());
        TextView txtvname=findViewById(R.id.txtvbdnname);
        TextView txtvnote=findViewById(R.id.txtvnotes);
        txtvname.setText("Hello today is :"+getIntent().getStringExtra("message") + " Birthday ");
        txtvnote.setText(getIntent().getStringExtra("notes"));
        Button btngonow=findViewById(R.id.btn_go);
        btngonow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseClass.EventDao().deleteById(getIntent().getStringExtra("message"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        // Do something after 5s = 5000ms


                    }
                }, 20);

            }
        });


    }
}