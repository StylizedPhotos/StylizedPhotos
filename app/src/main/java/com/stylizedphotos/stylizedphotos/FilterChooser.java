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
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
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
    private static final int RESULT_OPEN_FILTER_SCREEN = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_chooser);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
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
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

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
        final Uri shareUri = Uri.parse(extras.getString("shareUri"));
        bitmap = null;  //convert the uri to a bitmap
        try {
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
           // ImageButton but = new ImageButton(this);


            but.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
            but.setText(filters_names.get(i));

           // preview.setDensity(1000);
            but.setId(i);
            Buttons.add(but);
            filters.addView(but);

            but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), FilterScreen.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("opcode", opcode);
                    startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
                }
            });
        }

        for(int i=0;i<external_filters.size();i++) {
            final int j = i;
            LinearLayout filters = (LinearLayout) findViewById(R.id.filter_layout);
            Button but = new Button(this);
            // ImageButton but = new ImageButton(this);


            but.setLayoutParams(new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT));
            but.setText(external_filters.get(i).getName());

            // preview.setDensity(1000);
            but.setId(i);
            Buttons.add(but);
            filters.addView(but);

            but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ExternalFilterScreen.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("opcode", external_filters.get(j).getOp_code());
                    intent.putExtra("matrix", external_filters.get(j).getKernel());
                    startActivityForResult(intent, RESULT_OPEN_FILTER_SCREEN);
                }
            });
        }

        ImageButton share = (ImageButton)findViewById(R.id.sharebutton);
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
            }
        });
        Button addfilter = (Button)findViewById(R.id.add_external);
        addfilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChooseExternalSize.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
            }
        });
        Bitmap preview = bitmap.createScaledBitmap(bitmap, 200,200,true);
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
        external_filters.add(filter);
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
}
