package com.delarosa.huffmancode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DecodeHuffman extends AppCompatActivity {

    private String text = "", textToShow = "";
    private static List<HuffmanDto> huffmanCode = new ArrayList<>();
    private TextView codigoHuffmanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setea la vista
        setContentView(R.layout.huffman_code);

        //valido si el intent tiene datos
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            huffmanCode = (List<HuffmanDto>) bundle.getSerializable("huffmanCodeList");
        }else{
            huffmanCode = new ArrayList<>();
        }

        //nocion de controles graficos
        TextView entropyTextView = findViewById(R.id.entropy_result);
        entropyTextView.setVisibility(View.INVISIBLE);
        codigoHuffmanText = findViewById(R.id.codigo_huffman_text);
        EditText textToView = findViewById(R.id.text_to_view);
        textToView.setInputType(InputType.TYPE_CLASS_NUMBER);

        //escuchador al texto.... cada vez que cambie lee el array y decodifica el huffman
        textToView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (s.length() != 0) {
                        String lastCharacter = String.valueOf(s.charAt(s.length() - 1));
                        if (lastCharacter.equals("0") || lastCharacter.equals("1")) {
                            text = text.concat(lastCharacter);
                            for (int i = 0; i < huffmanCode.size(); i++) {
                                if (huffmanCode.get(i).getCode().equals(text)) {
                                    if (count == 0)
                                        textToShow = textToShow.substring(0, textToShow.length() - 1);
                                    else
                                        textToShow = textToShow + huffmanCode.get(i).getSymbol();

                                    codigoHuffmanText.setText(textToShow);
                                    text = "";
                                }
                            }
                        }

                    } else {
                        codigoHuffmanText.setText("");
                        textToShow = "";
                        text = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

    }

}
