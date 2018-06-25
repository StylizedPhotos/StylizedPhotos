package com.stylizedphotos.stylizedphotos;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Filter {
    private String name;
    private int op_code;
    private Matrix kernel;


    public Filter(float[][] arr, String name, Context context)
    {
        this.name= name;
        kernel = new Matrix(arr.length,arr.length,arr);//nxn
        SharedPreferences pref = context.getSharedPreferences("save data", MODE_PRIVATE);
        SharedPreferences.Editor editor = context.getSharedPreferences("save data", MODE_PRIVATE).edit();
        int op_code = pref.getInt("opcode",8);
        this.op_code = op_code;
        editor.putInt("opcode", ++op_code);
        editor.apply();
    }



    public String getName() {
        return name;
    }

    public int getOp_code() {
        return op_code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOp_code(int op_code) {
        this.op_code = op_code;
    }

}
