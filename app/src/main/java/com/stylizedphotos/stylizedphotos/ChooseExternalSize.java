package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseExternalSize extends AppCompatActivity {
    final int RESULT_OPEN_3X3 = 3;
    final int RESULT_OPEN_5X5 = 5;
    final int RESULT_OPEN_7X7 = 7;
    final int RESULT_OPEN_9X9 = 9;
    final int RESULT_OPEN_11X11 = 11;
    final int RESULT_OPEN_13X13 = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_external_size);

        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        Button size3 = (Button)findViewById(R.id.size3);
        Button size5 = (Button)findViewById(R.id.size5);
        Button size7 = (Button)findViewById(R.id.size7);
        Button size9 = (Button)findViewById(R.id.size9);
        Button size11 = (Button)findViewById(R.id.size11);
        Button size13 = (Button)findViewById(R.id.size13);


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
                startActivityForResult(intent, RESULT_OPEN_5X5);
            }
        });
        size7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External7x7.class);
                startActivityForResult(intent, RESULT_OPEN_7X7);
            }
        });
        size9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External9x9.class);
                startActivityForResult(intent, RESULT_OPEN_9X9);
            }
        });
        size11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External11x11.class);
                startActivityForResult(intent, RESULT_OPEN_11X11);
            }
        });
        size13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), External13x13.class);
                startActivityForResult(intent, RESULT_OPEN_13X13);
            }
        });
    }
}
