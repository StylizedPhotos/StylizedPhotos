#pragma version(1)
#pragma rs java_package_name(com.stylizedphotos.stylizedphotos)

float hue = 0;
float range = 0;
int width = 0;
rs_allocation inarray;
rs_allocation outarray;

void root(const uchar4 *in, uchar4 *out, uint32_t x, uint32_t y) {
    int index = (y*width+x)*3;

    float temp_hue =  rsGetElementAt_float(inarray, index);
    float temp_saturation =  rsGetElementAt_float(inarray, index+1);
    float temp_value =  rsGetElementAt_float(inarray, index+2);


   if(temp_hue<=hue+range && temp_hue>=hue-range)
       rsSetElementAt_float(outarray, 0, index+1);
    else
      rsSetElementAt_float(outarray, temp_saturation, index+1);
    rsSetElementAt_float(outarray, temp_hue, index);
    rsSetElementAt_float(outarray, temp_value/100, index+2);

}
