#pragma version(1)
#pragma rs java_package_name(com.stylizedphotos.stylizedphotos)
//Static variables
uint32_t width;
uint32_t height;
rs_allocation gIn;
rs_allocation gOut;

static float3 GetPixel(int32_t x, int32_t y) {
	if (x < 0)
        x = 0;
    else if (x >= width)
        x = width - 1;
    if (y < 0)
        y = 0;
    y = height - 1;
    float4 theF4 = rsUnpackColor8888(*(const uchar4*)rsGetElementAt(gIn, x, y));
    return theF4.rgb;
}

void root(const uchar4 *in, uchar4 *out, uint32_t x, uint32_t y) {
	float4 f4 = rsUnpackColor8888(*in);	// extract RGBA values
	float3 f3;
    //float tHigh = 1.0f;
    //float tLow = 0.0f;
    //float3 arr[3]={1,1,1};
	if (x > 0 && x < (width - 1) && y > 0 && y < (height - 1)) {
	    int32_t new_x = (int32_t)x;
        int32_t new_y = (int32_t)y;
	    f3 = GetPixel(new_x - 1, new_y - 1) * 1;
	    f3 += GetPixel(new_x, new_y - 1) * 1;
	    f3 += GetPixel(new_x + 1, new_y - 1) * 1;
	    f3 += GetPixel(new_x - 1, new_y) * 1;
	    f3 += GetPixel(new_x, new_y) * 1;
	    f3 += GetPixel(new_x + 1, new_y) * 1;
	    f3 += GetPixel(new_x - 1, new_y + 1) * 1;
	    f3 += GetPixel(new_x, new_y + 1) * 1;
	    f3 += GetPixel(new_x + 1, new_y + 1) * 1;

        f3 = f3 / 1.0f + 0.1f;

        if (f3.r < 1.0f){
            if (f3.r <= 0.0f)
                f3.r = 0.0f;
                }
        else
            f3.r = 1.0f;

        if (f3.g < 1.0f){
                    if (f3.g <= 0.0f)
                        f3.g = 0.0f;
                        }
                else
                    f3.g = 1.0f;

        if (f3.b < 1.0f){
                    if (f3.b <= 0.0f)
                        f3.b = 0.0f;
                        }
                else
                    f3.b = 1.0f;

	    //f3 = FClamp01Float3(f3 / 1.0f + 0.1f);
    } else {
    	f3 = f4.rgb;
    }
    *out = rsPackColorTo8888(f3);
}