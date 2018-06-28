package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.File;

public class RemoveFilter extends AppCompatActivity {
    File file_filters [];
    Uri imageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_filter);

        Bundle extras = getIntent().getExtras();
        imageUri = Uri.parse(extras.getString("imageUri"));
        final boolean perf = extras.getBoolean("perf");
        LinearLayout view = (LinearLayout)findViewById(R.id.linearLayout);
        file_filters = this.getFilesDir().listFiles();
        String temp_filter_name="";
        for(int i=0;i<file_filters.length;i++)
        {
            temp_filter_name = file_filters[i].getName();
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(temp_filter_name);
            checkBox.setId(i);
            view.addView(checkBox);
        }
        ImageButton deleteButton = (ImageButton)findViewById(R.id.delBut);
        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                for(int i=0;i<file_filters.length;i++)
                {
                    CheckBox checkBox = findViewById(i);
                    if(checkBox.isChecked())
                    {
                        file_filters[i].delete();
                    }
                }
                Intent closechooser = new Intent("finish_activity_filterchooser");
                sendBroadcast(closechooser);
                Intent intent = new Intent(getBaseContext(), FilterChooser.class);
                intent.putExtra("imageUri", imageUri.toString());
                intent.putExtra("perf", perf);
                intent.putExtra("shareUri", "");
                startActivity(intent);
                finish();
            }
        });
    }
}
