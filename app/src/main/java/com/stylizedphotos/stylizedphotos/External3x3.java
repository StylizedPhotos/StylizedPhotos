package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class External3x3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external3x3);
        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        Button add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText temp;
                float[][] arr = new float[3][3];
                float myNum = 0; // for conversion from string in the text view
                EditText edit_name = (EditText)findViewById(R.id.filtername);
                String name = edit_name.toString();
                TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
                TableRow row1 = (TableRow)findViewById(R.id.row1);
                TableRow row2 = (TableRow)findViewById(R.id.row2);
                TableRow row3 = (TableRow)findViewById(R.id.row3);
                int num_of_rows =table.getChildCount();
                for(int i = 0; i < num_of_rows; i++)
                {
                    View view = table.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        int num_of_cols = row.getChildCount();
                        for(int j=0;j<row.getChildCount();j++)
                        {
                            temp = (EditText)row.getChildAt(j);
                            myNum = 0;
                            try {
                                myNum = Float.parseFloat(temp.getText().toString());
                            } catch(NumberFormatException exp) {
                                System.out.println("Could not parse" + exp);
                            }
                            arr[i][j]= myNum;
                        }
                    }
                }
                final Filter filter = new Filter(arr, name,getBaseContext());

                Intent intent =new Intent();
                intent.setAction("check_values");
                intent.putExtra("filter",filter);
                sendBroadcast(intent);
                Intent close = new Intent("finish_activity");
                sendBroadcast(close);
                intent = new Intent(getBaseContext(), FilterChooser.class);
                intent.putExtra("imageUri", imageUri.toString());
                intent.putExtra("shareUri", "");
                startActivity(intent);
                finish();
            }
        });
        Button reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        Button preivew = (Button)findViewById(R.id.preview);
        preivew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
}
