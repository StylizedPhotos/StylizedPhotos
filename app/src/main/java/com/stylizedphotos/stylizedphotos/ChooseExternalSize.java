package com.stylizedphotos.stylizedphotos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseExternalSize extends AppCompatActivity {
    final int RESULT_OPEN_3X3 = 3;
    final int RESULT_OPEN_5X5 = 5;
    final int RESULT_OPEN_7X7 = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_external_size);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity_chooseexternalsize")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity_chooseexternalsize"));

        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        Button size3 = (Button)findViewById(R.id.size3);
        Button size5 = (Button)findViewById(R.id.size5);
        Button size7 = (Button)findViewById(R.id.size7);



        size3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External3x3.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivityForResult(intent, RESULT_OPEN_3X3);
            }
        });
        size5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External5x5.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivityForResult(intent, RESULT_OPEN_5X5);
            }
        });
        size7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External7x7.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivityForResult(intent, RESULT_OPEN_7X7);
            }
        });

    }
}
