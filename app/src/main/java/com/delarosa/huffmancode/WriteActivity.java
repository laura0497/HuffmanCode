package com.delarosa.huffmancode;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * esta clase lee el texto y pasa el array de char a la clase HuffmanCode
 */
public class WriteActivity extends AppCompatActivity {
    private TextInputEditText textToEvaluate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //agrego barra transparente en la vista
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // barra de notificacion transparente
        changeStatusBarColor();
        textToEvaluate = findViewById(R.id.textToEvaluate);
    }

    /**
     * este metodo cambia el color de la barra de notificacion a transparente
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * la app entra a este metodo cuando se toca el boton
     * @param view
     */
    public void evaluateText(View view) {
        if (!textToEvaluate.getText().equals("")) {
            Intent intent = new Intent(WriteActivity.this, HuffmanCode.class);
            intent.putExtra("text", String.valueOf(textToEvaluate.getText()));
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "ingresa una palabra", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(WriteActivity.this,SlideActivity.class);
        startActivity(intent);
    }
}
