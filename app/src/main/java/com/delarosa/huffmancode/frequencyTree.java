package com.delarosa.huffmancode;

/**
 * DTO (data transfer object) de frecuencia
 */
abstract class frequencyTree implements Comparable<frequencyTree> {
    //variable de frecuencia
    public final int frequency; 

    public frequencyTree(int freq) {
        frequency = freq;
    }

    // compara la frecuencia
    public int compareTo(frequencyTree tree) {
        return frequency - tree.frequency;
    }
}
