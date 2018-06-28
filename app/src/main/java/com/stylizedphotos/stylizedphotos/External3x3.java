package com.stylizedphotos.stylizedphotos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class External3x3 extends AppCompatActivity {
    Bitmap bitmap;
    ImageView image;
    Filter filter;
    boolean empty_flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external3x3);
        Bundle extras = getIntent().getExtras();
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));

        bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = scaleBitmap(getBitmapFromUri(imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        image = (ImageView)findViewById(R.id.imagePreview);
        image.setImageBitmap(bitmap);
        Button add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float temp_sum=0;
                EditText temp;
                empty_flag = false;
                float myNum = 0; // for conversion from string in the text view
                EditText edit_name = (EditText)findViewById(R.id.filtername);
                String name = edit_name.getText().toString();
                TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
                int num_of_rows =table.getChildCount();
                float[][] arr = new float[num_of_rows][num_of_rows];
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
                                arr[i][j]= myNum;
                                temp_sum+=myNum;
                            } catch(NumberFormatException exp) {
                                System.out.println("Could not parse" + exp);
                                empty_flag= true;
                            }
                        }
                    }
                }
                CheckBox checkBox_div = (CheckBox)findViewById(R.id.divide);
                boolean divide = checkBox_div.isChecked();

                if (!empty_flag) {
                    if (temp_sum != 0 && divide || !divide) {
                        filter = new Filter(arr, name, getBaseContext(), divide);
                        Intent intent = new Intent("check_values");
                        intent.putExtra("filter", filter);
                        sendBroadcast(intent);
                        Intent closechooser = new Intent("finish_activity_filterchooser");
                        sendBroadcast(closechooser);
                        Intent closeexternal = new Intent("finish_activity_chooseexternalsize");
                        sendBroadcast(closeexternal);
                        Intent intent2 = new Intent(getBaseContext(), FilterChooser.class);
                        intent2.putExtra("imageUri", imageUri.toString());
                        //intent2.putExtra("perf", perf);
                        intent2.putExtra("shareUri", "");
                        startActivity(intent2);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "The sum of all elements is zero.\ntry again!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Please fill all cells",
                            Toast.LENGTH_LONG).show();
            }
        });
        Button reset = (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText temp;
                TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
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
                            temp.setText("");
                        }
                    }
                }
                image.setImageBitmap(bitmap);
            }
        });
        final Button preivew = (Button)findViewById(R.id.preview);
        preivew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText temp;
                empty_flag = false;
                float temp_sum=0;
                EditText edit_name = (EditText)findViewById(R.id.filtername);
                String name = edit_name.getText().toString();
                float myNum = 0;
                TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
                int num_of_rows =table.getChildCount();
                float[][] arr = new float[num_of_rows][num_of_rows];
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
                                arr[i][j]= myNum;
                                temp_sum += myNum;
                            } catch(NumberFormatException exp) {
                                System.out.println("Could not parse" + exp);
                                empty_flag= true;
                            }

                        }
                    }
                }
                CheckBox checkBox_div = (CheckBox)findViewById(R.id.divide);
                boolean divide = checkBox_div.isChecked();
                if (!empty_flag) {
                    if (temp_sum != 0 && divide || !divide) {
                        filter = new Filter(arr, name, getBaseContext(), divide);
                        //Bitmap preview = bitmap.createScaledBitmap(bitmap, 600,600,true);
                        Bitmap temp_preview;
                        temp_preview = Matrix.convolution(filter.getKernel(), bitmap, filter.isDivide());
                        Drawable d = new BitmapDrawable(getResources(), temp_preview);
                        image.setImageBitmap(temp_preview);
                    } else
                        Toast.makeText(getApplicationContext(), "The sum of all elements is zero.\ntry again!",
                                Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please fill all cells",
                            Toast.LENGTH_LONG).show();
            }

        });
    }

    private Bitmap getBitmapFromUri(Uri uri)throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int maxWidth = 1500;
        int maxHeight = 1200;
        //Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        //Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }
}
