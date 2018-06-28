package com.stylizedphotos.stylizedphotos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FilterChooser extends AppCompatActivity
{
    //int number_of_filters=8;// a constant that change when the user add an external filter
    //private ImageButton mImageButton;
    ArrayList<String> filters_names = new ArrayList<>();
    ArrayList<Button> Buttons = new ArrayList<>();
    ArrayList<Filter> external_filters = new ArrayList<>();
    ImageView image;
    Bitmap bitmap;
    String mCurrentPhotoPath;
    Uri imageUri = null;
    Uri shareUri=null;
    boolean perf = false;
    private static final int RESULT_OPEN_FILTER_SCREEN = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chooser);

        Toolbar toolbar = findViewById(R.id.toolbar);//set toolbar to variable
        setSupportActionBar(toolbar);//enable toolbar
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity_filterchooser")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
                if(intent.getAction().equals("check_values"))
                {
                    Filter filter = (Filter)intent.getSerializableExtra("filter");
                    AddExternalFilter(filter);
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity_filterchooser"));
        registerReceiver(broadcastReceiver, new IntentFilter("check_values"));

        filters_names.add("Mean blur");
        filters_names.add("Gaussian blur");
        filters_names.add("Edge Detection");
        filters_names.add("Sharpening");
        filters_names.add("RGB");
        filters_names.add("HSV");
        filters_names.add("Color Removal");
        filters_names.add("Color Highlight");
        Bundle extras = getIntent().getExtras();
        imageUri = Uri.parse(extras.getString("imageUri"));
        perf = extras.getBoolean("perf");
        shareUri = Uri.parse(extras.getString("shareUri"));
        bitmap = null;  //convert the uri to a bitmap
        try {
            if(perf)
                bitmap = HelpMethods.scaleBitmap(getBitmapFromUri(imageUri));
            else
                bitmap = getBitmapFromUri(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image = (ImageView) findViewById(R.id.imageViewFilter);
        image.setImageBitmap(bitmap);
        for(int i=0;i<filters_names.size();i++) {
            final int opcode = i;
            LinearLayout filters = (LinearLayout) findViewById(R.id.filter_layout);
            Button but = new Button(this);
            but.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
            but.setText(filters_names.get(i));
            but.setShadowLayer(50,-1,-1,Color.BLACK);
            but.setId(i);
            Buttons.add(but);
            filters.addView(but);

            but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), FilterScreen.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("opcode", opcode);
                    intent.putExtra("perf" , perf);
                    startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
                }
            });
        }

            File file_filters [] = this.getFilesDir().listFiles();
            for(int i=0;i<file_filters.length;i++) {
                LinearLayout filters = (LinearLayout) findViewById(R.id.filter_layout);
                Button but = new Button(this);
                but.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
                but.setText(file_filters[i].getName());
                but.setId(i+filters_names.size());
                Buttons.add(but);
                filters.addView(but);
                StringBuilder text = new StringBuilder();
                String temp_line="";
                try {
                    File file = file_filters[i];
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line="";
                    if ((line = br.readLine()) != null)
                       temp_line += line;
                    text.append(temp_line);
                   /* while ((line = br.readLine()) != null) {
                        text.append(line);
                    }*/
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String input = text.toString();
                final String[] external_filter_data = input.split(";");
                //[0]=name [1]=size [2]= op [3]=ker [4]=divide(true or false)
                String[] kernel_values = external_filter_data[3].split(",");
                int size;
                size = Integer.parseInt(external_filter_data[1]);
                float [][] kernel = new float[size][size];

                for(int k=0;k<size;k++)
                    for(int j=0;j<size;j++)
                        kernel[k][j] = Float.parseFloat(kernel_values[k*size+j]);//get from 1d string array to 2d float
                final Matrix ker_mat = new Matrix(size,size,kernel);

                Bitmap preview = bitmap.createScaledBitmap(bitmap, 100,100,true);
                Bitmap temp_preview = preview;
                temp_preview = Matrix.convolution(ker_mat,preview,Boolean.parseBoolean(external_filter_data[4]));
                Drawable d = new BitmapDrawable(getResources(), temp_preview);
                but.setBackground(d);
                but.setShadowLayer(50,-1,-1,Color.BLACK);
                but.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), ExternalFilterScreen.class);
                        intent.putExtra("imageUri", imageUri.toString());
                        intent.putExtra("opcode", Integer.parseInt(external_filter_data[2]));
                        intent.putExtra("matrix", ker_mat);
                        intent.putExtra("divide", Boolean.parseBoolean(external_filter_data[4]));
                        intent.putExtra("perf", perf);
                        startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
                    }
                });
            }

        Bitmap preview = bitmap.createScaledBitmap(bitmap, 120,100,true);
        Bitmap temp_preview = preview;
        temp_preview = MeanBlur.Preview(preview);
        Drawable d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(0).setBackground(d);

        temp_preview = GaussianBlur.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(1).setBackground(d);

        temp_preview = EdgeDetection.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(2).setBackground(d);

        temp_preview = Sharpening.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(3).setBackground(d);

        RGB rgb = new RGB(this);
        temp_preview = rgb.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(4).setBackground(d);

        HSV hsv = new HSV(this);
        temp_preview = hsv.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(5).setBackground(d);

        ColorRemoval remove = new ColorRemoval(this);
        temp_preview = remove.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(6).setBackground(d);

        ColorHighlight highlight = new ColorHighlight(this);
        temp_preview = highlight.Preview(preview);
        d = new BitmapDrawable(getResources(), temp_preview);
        Buttons.get(7).setBackground(d);
        //but.setBackground(d);
        //Button blurbut = (Button)findViewById(R.1);
        //Button FilterButton = (Button) findViewById(R.id.button1);

    }

    private Bitmap getBitmapFromUri(Uri uri)throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public void RefreshImage (Bitmap bitmap)
    {
        image = (ImageView) findViewById(R.id.imageViewFilter);
        image.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void AddExternalFilter(final Filter filter)
    {
        //filter file: "filter name";"size";"index for array list (op code)";"matrix eg: 1,1,1,1,1,1,1,1,1 for 3x3"
        String external_filter_data="",temp;
        int size = filter.getKernel().getCols();//nxn so 1 size is enough

        external_filter_data += filter.getName() + ";" + size + ";" + filter.getOp_code() + ";";
        for(int i=0;i<size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(i==0 && j==0)
                {
                    temp = Float.toString(filter.getKernel().getVal(i,j));
                    external_filter_data +=temp;
                }
                else
                    external_filter_data +="," + filter.getKernel().getVal(i,j);
            }
        }
        external_filter_data += ";" + filter.isDivide();
        external_filter_data+='\n';
        //here we have full data string of the filter

//        File folder = new File(this.getFilesDir(), "External Filters");
//        if(!folder.exists())
//        {
//            folder.mkdirs();
//        }
        //folder created

        File filter_text = new File(this.getFilesDir(), filter.getName());//new file type with the name of the filter(not on disk)
        if(!filter_text.exists()) {
            try
            {
                filter_text.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error saving filter", Toast.LENGTH_SHORT).show();
            }
        }





        //String filename = "myfile";
       // String fileContents = "Hello world!";
        FileOutputStream outputStream;

//        try (PrintWriter p = new PrintWriter(new FileOutputStream(this.getFilesDir()+filter.getName()+".txt", true))) {
//            p.println(external_filter_data);
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }
        try {
            outputStream = openFileOutput(filter.getName(), Context.MODE_PRIVATE);
            outputStream.write(external_filter_data.getBytes());
            outputStream.close();
            Toast.makeText(this, "Filter was successfully created", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: Filter cannot be created", Toast.LENGTH_SHORT).show();
        }




        //external_filters.add(filter);
        /*LinearLayout filters = (LinearLayout) findViewById(R.id.filter_layout);
        Button but = new Button(this);
        // ImageButton but = new ImageButton(this);


        but.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
        but.setText(filter.getName());

        // preview.setDensity(1000);
        but.setId(filter.getOp_code());
        Buttons.add(but);
        filters.addView(but);

        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ExternalFilterScreen.class);
                intent.putExtra("imageUri", imageUri.toString());
                intent.putExtra("opcode", filter.getOp_code());
                intent.putExtra("matrix", filter.getKernel());
                startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
            }
        });*/
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_popup, menu);
        menu.add(Menu.NONE, 0, Menu.NONE, "Add filter");
        menu.add(Menu.NONE, 1, Menu.NONE, "Remove filter");
        menu.add(Menu.NONE, 2, Menu.NONE, "Share");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 0://add filter
                Intent intent = new Intent(getBaseContext(), ChooseExternalSize.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
                return true;
            case 1:
                Intent intent2 = new Intent(getBaseContext(), RemoveFilter.class);
                intent2.putExtra("imageUri", imageUri.toString());
                intent2.putExtra("perf", perf);
                startActivity(intent2);

                //to_delete.delete();
                return true;
            case 2://share image
                if(shareUri.toString().equals("") == false) {

                    Intent share = new Intent(Intent.ACTION_SEND);

                    // If you want to share a png image only, you can do:
                    // setType("image/png"); OR for jpeg: setType("image/jpeg");
                    share.setType("image/png");

                    // Make sure you put example png image named myImage.png in your
                    // directory
                    String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + File.separator +shareUri.toString();

                    File imageFileToShare = new File(imagePath);
                    Uri photoUri = FileProvider.getUriForFile(FilterChooser.this,
                            "com.stylizedphotos.stylizedphotos.provider", imageFileToShare);
                    share.putExtra(Intent.EXTRA_STREAM, photoUri);

                    startActivity(Intent.createChooser(share, "Share Image!"));
                }
                else
                    Toast.makeText(getApplicationContext(), "Default Image wasn't changed",
                            Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
}
}
