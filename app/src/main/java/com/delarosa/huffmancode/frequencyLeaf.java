package com.delarosa.huffmancode;

public class frequencyLeaf extends frequencyTree {
    public final char value;

    public frequencyLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}