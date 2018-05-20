package com.delarosa.huffmancode;

public class frequencyNode extends frequencyTree {
    public final frequencyTree left, right;

    public frequencyNode(frequencyTree l, frequencyTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}