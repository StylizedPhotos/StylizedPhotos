#pragma version(1)
#pragma rs java_package_name(com.stylizedphotos.stylizedphotos)

uchar4 __attribute__((kernel)) parallel(uint32_t x, uint32_t y){
    uchar4 ret =  rsPackColorTo8888(0,255,255);
    return ret;
}

uchar4 __attribute__((kernel)) convolution_parellel(uint32_t x, uint32_t y){
    uchar4 ret =  rsPackColorTo8888(0,255,255);
    return ret;
}