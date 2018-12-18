package com.example.agrahame.flexitimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class DisplayMessageActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private TextView textView;
    public static final String EXTRA_MESSAGE = "com.example.agrahame.flexitimer.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string text as its text
        textView = findViewById(R.id.textView);

        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();
        textView.setText(message);
    }

    public void saveTime(View view) {
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();

        textView.setText("[T:" + hour + ":" + min + "] -- ");
    }
}

