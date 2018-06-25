package com.stylizedphotos.stylizedphotos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class External3x3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external3x3);
        TextView temp;
        float[][] arr = new float[3][3];
        float myNum = 0; // for conversion from string in the text view
        String name="";
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
                    temp = (TextView)row.getChildAt(j);
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
        Filter filter = new Filter(arr, name,this);
    }
}
