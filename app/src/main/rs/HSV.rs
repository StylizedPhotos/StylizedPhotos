#pragma version(1)
#pragma rs java_package_name(com.stylizedphotos.stylizedphotos)

float hue = 0;
float saturation = 0;
float value = 0;
int width = 0;
rs_allocation inarray;
rs_allocation outarray;

void root(const uchar4 *in, uchar4 *out, uint32_t x, uint32_t y) {
    int index = y*width+x;
    //float3 inpixel = rsGetElementAt_float3(inarray, index*3);
    index*=3;
    float temp_hue =  rsGetElementAt_float(inarray, index);
    float temp_saturation =  rsGetElementAt_float(inarray, index+1);
    float temp_value =  rsGetElementAt_float(inarray, index+2);
    float3 inpixel = {temp_hue,temp_saturation,temp_value};
    //float res = 360/(inpixel.x + hue);
    int temp = (int)(inpixel.x+hue)%360;
    float3 outpixel = {(float)temp,(float)(inpixel.y + saturation),(float)(inpixel.z + value)/100};
    temp_hue = outpixel.x;
    temp_saturation = outpixel.y;
    temp_value = outpixel.z;
    rsSetElementAt_float(outarray, temp_hue, index);
    rsSetElementAt_float(outarray, temp_saturation, index+1);
    rsSetElementAt_float(outarray, temp_value, index+2);



   /* if ((inpixel.x + red) > 255)
        redpix=255;
    else
        redpix = inpixel.x + red;
    if ((inpixel.x + green) > 255)
            greenpix=255;
        else
            greenpix = (float)(inpixel.y + green);
    if ((inpixel.x + blue) > 255)
            bluepix=255;
        else
            bluepix = inpixel.z + blue;*/
   // float3 outpixel = {(float)(inpixel.x + hue)/360,(float)(inpixel.y + saturation),(float)(inpixel.z + value)/100};
   //*out = rsPackColorTo8888(outpixel);
   //out[3] = 255;
    /*pixel = rsMatrixMultiply(&colorMat, pixel);
    pixel = clamp(pixel, 0.f, 255.f);
    pixel = (pixel - inBlack) * overInWMinInB;
    if (gamma != 1.0f)
        pixel = pow(pixel, (float3)gamma);
    pixel = pixel * outWMinOutB + outBlack;
    pixel = clamp(pixel, 0.f, 255.f);
    out->xyz = convert_uchar3(pixel);*/
}