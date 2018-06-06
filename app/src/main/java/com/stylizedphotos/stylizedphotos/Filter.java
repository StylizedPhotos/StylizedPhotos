package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Filter {
    private Bitmap bitmap;
    private String name;
    private ArrayList<SeekBar> seekbar_array;
    private ArrayList<TextView> names;
    private int op_code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SeekBar> getSeekbar_array() {
        return seekbar_array;
    }

    public void setSeekbar_array(ArrayList<SeekBar> seekbar_array) {
        this.seekbar_array = seekbar_array;
    }

    public void addSeekbar(SeekBar seekbar) {
        this.seekbar_array.add(seekbar);
    }

    public ArrayList<TextView> getNames() {
        return names;
    }

    public void setNames(ArrayList<TextView> names) {
        this.names = names;
    }

    public void addName(TextView name) {
        this.names.add(name);
    }



    Filter(){

    }

    Filter(Bitmap bitmap, final FilterScreen filterScreen)
    {
        this.bitmap=bitmap;
        seekbar_array = new ArrayList<SeekBar>();
        names = new ArrayList<TextView>();
    }
}
