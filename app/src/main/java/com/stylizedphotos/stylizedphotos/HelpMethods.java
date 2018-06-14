package com.stylizedphotos.stylizedphotos;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public abstract class HelpMethods {
    private Bitmap getGrayscale_ColorMatrixColorFilter(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap dest = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0); //value of 0 maps the color to gray-scale
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(src, 0, 0, paint);
        return dest;
    }

    public static Bitmap pad(Bitmap Src, int padding_x, int padding_y) {
        Bitmap outputimage = Bitmap.createBitmap(Src.getWidth() + padding_x, Src.getHeight() + padding_y, Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(outputimage);
        can.drawARGB(0, 0, 0, 0); //This represents White color
        can.drawBitmap(Src, padding_x / 2, padding_y / 2, null);
        return outputimage;
    }

    /**
     * Clamp a value to the range 0..255
     */
    public static int clamp(int c) {
        if (c < 0)
            return 0;
        if (c > 255)
            return 255;
        return c;
    }
}