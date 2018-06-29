#pragma version(1)
#pragma rs java_package_name(com.stylizedphotos.stylizedphotos)

int red = 0;
int green = 0;
int blue = 0;

void root(const uchar4 *in, uchar4 *out, uint32_t x, uint32_t y) {
    float redpix = 0;
    float greenpix = 0;
    float bluepix = 0;
    float3 inpixel = convert_float4(in[0]).rgb;

    float3 outpixel = {(float)(inpixel.x + red)/255,(float)(inpixel.y + green)/255,(float)(inpixel.z + blue)/255};
   *out = rsPackColorTo8888(outpixel);
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