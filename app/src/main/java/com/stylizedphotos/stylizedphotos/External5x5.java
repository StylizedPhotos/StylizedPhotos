package com.stylizedphotos.stylizedphotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class External5x5 extends AppCompatActivity {
    Bitmap bitmap;
    ImageView image;
    Filter filter;
    boolean empty_flag= false;
    boolean perf = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external5x5);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        final Uri imageUri = Uri.parse(extras.getString("imageUri"));
        perf = extras.getBoolean("perf");
        bitmap = null;  //convert the uri to a bitmap
        try {
            bitmap = scaleBitmap(getBitmapFromUri(imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = findViewById(R.id.imagePreview);
        image.setImageBitmap(bitmap);
        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                empty_flag=false;
                EditText temp;
                float myNum; // for conversion from string in the text view
                EditText edit_name = findViewById(R.id.filtername);
                String name = edit_name.getText().toString();
                TableLayout table = findViewById(R.id.tableLayout);
                int num_of_rows =table.getChildCount();
                float[][] arr = new float[num_of_rows][num_of_rows];
                float temp_sum=0;
                for(int i = 0; i < num_of_rows; i++)
                {
                    View view = table.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        int num_of_cols = row.getChildCount();
                        for(int j=0;j<row.getChildCount();j++)
                        {
                            temp = (EditText)row.getChildAt(j);
                            try {
                                myNum = Float.parseFloat(temp.getText().toString());
                                arr[i][j]= myNum;
                                temp_sum += myNum;
                            } catch(NumberFormatException exp) {
                                System.out.println("Could not parse" + exp);
                                empty_flag = true;
                            }
                        }
                    }
                }
                CheckBox checkBox_div = findViewById(R.id.divide);
                boolean divide = checkBox_div.isChecked();
                EditText filtername = findViewById(R.id.filtername);
                if (filtername.getText().toString().equals(""))
                    empty_flag = true;
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
                        intent2.putExtra("perf", perf);
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
        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText temp;
                TableLayout table = findViewById(R.id.tableLayout);
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
                            temp.setText("0");
                        }
                    }
                }
                image.setImageBitmap(bitmap);
            }
        });
        final Button preivew = findViewById(R.id.preview);
        preivew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText temp;
                EditText edit_name = findViewById(R.id.filtername);
                String name = edit_name.getText().toString();
                float myNum;
                float temp_sum = 0;
                TableLayout table = findViewById(R.id.tableLayout);
                int num_of_rows = table.getChildCount();
                float[][] arr = new float[num_of_rows][num_of_rows];
                for (int i = 0; i < num_of_rows; i++) {
                    View view = table.getChildAt(i);
                    if (view instanceof TableRow) {
                        TableRow row = (TableRow) view;
                        int num_of_cols = row.getChildCount();
                        for (int j = 0; j < row.getChildCount(); j++) {
                            temp = (EditText) row.getChildAt(j);
                            try {
                                myNum = Float.parseFloat(temp.getText().toString());
                                arr[i][j] = myNum;
                                temp_sum += myNum;
                            } catch (NumberFormatException exp) {
                                System.out.println("Could not parse" + exp);
                                empty_flag = true;
                            }

                        }
                    }
                }
                CheckBox checkBox_div = findViewById(R.id.divide);
                boolean divide = checkBox_div.isChecked();
                if (!empty_flag) {
                    if (temp_sum != 0 && divide || !divide) {
                        filter = new Filter(arr, name, getBaseContext(), divide);
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
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int maxWidth = 1500;
        int maxHeight = 1000;
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
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }
}
