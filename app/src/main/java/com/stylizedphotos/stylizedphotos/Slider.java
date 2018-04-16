package com.stylizedphotos.stylizedphotos;

public class Slider {
    String name;
    int max_value;
    int min_value;

    public int getMax_value() {
        return max_value;
    }

    public int getMin_value() {
        return min_value;
    }

    public String getName() {
        return name;
    }

    public void setMax_value(int max_value) {
        this.max_value = max_value;
    }

    public void setMin_value(int min_value) {
        this.min_value = min_value;
    }

    public void setName(String name) {
        this.name = name;
    }

    Slider(String name, int max, int min)
    {
        this.name = name;
        max_value=max;
        min_value=min;
    }
}
