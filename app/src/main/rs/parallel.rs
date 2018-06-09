#pragma version(1)
#pragma rs java_package_name(com.stylizedphotos.stylizedphotos)

int red = 0;
int green = 0;
int blue = 0;

/*uchar4 __attribute__((kernel)) parallel(uint32_t x, uint32_t y){
    //uchar4 ret =  rsPackColorTo8888(0,255,255);
    return ret;
}*/

void root(const uchar4 *in, uchar4 *out, uint32_t x, uint32_t y) {
    float redpix = 0;
    float greenpix = 0;
    float bluepix = 0;
    float3 inpixel = convert_float4(in[0]).rgb;
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
    float3 outpixel = {(float)(inpixel.x + red)/255,(float)(inpixel.y + green)/255,(float)(inpixel.z + blue)/255};
   *out = rsPackColorTo8888(outpixel);
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

void setRed(int k){
    red = k;
}
void setGreen(int k){
    green = k;
}
void setBlue(int k){
    blue = k;

}