package com.delarosa.huffmancode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HuffmanCode extends AppCompatActivity {
    private static double entropy;
    private static int total;
    private static List<String> fregArray = new ArrayList<>();
    private static List<String> probArray = new ArrayList<>();
    private static TextView entropyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huffman_code);

        String text;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            text = bundle.getString("text");
        } else {
            text = "this is an example for huffman encoding";
        }

        entropyTextView = findViewById(R.id.entropy_result);
        // we will assume that all our characters will have
        // code less than 256, for simplicity
        int[] charFreqs = new int[256];
        // read each character and record the frequencies
        for (char c : text.toCharArray())
            charFreqs[c]++;

        // build tree
        HuffmanTree tree = buildTree(charFreqs);

        // print out results
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(tree, new StringBuffer());
        System.out.println("total: " + total);
        System.out.println("freq: " + fregArray);
        for (String frecuency : fregArray) {
            probArray.add(Double.toString(Double.parseDouble(frecuency) / total));
        }
        System.out.println("probArray: " + probArray);
        for (String prob : probArray) {
            entropy += Double.parseDouble(prob) * Math.log10(1 / Double.parseDouble(prob) / Math.log10(2));
        }
        System.out.println("entropia: " + entropy);
        entropyTextView.setText("entropia: " + String.valueOf(entropy));
    }

    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree(int[] charFreqs) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        for (int i = 0; i < charFreqs.length; i++)
            if (charFreqs[i] > 0)
                trees.offer(new HuffmanLeaf(charFreqs[i], (char) i));

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }

    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            // print out character, frequency, and code for this leaf (which is just the prefix)
            System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
            String cadena = leaf.value + "\t" + leaf.frequency + "\t" + prefix;
            total += leaf.frequency;
            fregArray.add(Integer.toString(leaf.frequency));
            if (cadena != null) {
                System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
            }


        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public static void main(String[] args) {

    }


}
