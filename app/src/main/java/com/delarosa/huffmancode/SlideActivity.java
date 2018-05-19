package com.delarosa.huffmancode;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SlideActivity extends AppCompatActivity {

    private LinearLayout dotsLayout;
    private int[] layouts;
    private PrefManager prefManager;
    private ImageButton write, file;
    private boolean firstTime;
    private static int PICK_FILE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //evalua que sea la primera vez que entra a la app
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            firstTime = false;
        } else {
            firstTime = true;
        }

        // se valida la version del sdk para soporte de barra transparente
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //se le asigna la vista
        setContentView(R.layout.activity_slide);

        ViewPager viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);


        // referencia de las vistas que van a estar dentro del slider
        layouts = new int[]{
                R.layout.slide_icon,
                R.layout.slide_members,
                R.layout.slide_choose};

        // se añaden los puntos en la pagina principal
        addBottomDots(0);

        // barra de notificacion transparente
        changeStatusBarColor();

        //se le asigna un adaptador al slider
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


    }

    /**
     * este metodo añade los puntos en la parte inferior de la vista
     *
     * @param currentPage
     */
    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int dotIndex = 0; dotIndex < dots.length; dotIndex++) {
            dots[dotIndex] = new TextView(this);
            dots[dotIndex].setText(Html.fromHtml("&#8226;"));
            dots[dotIndex].setTextSize(35);
            dots[dotIndex].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[dotIndex]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    /**
     * este metodo valida la version del sdk para validar el permiso de lectura del dispositivo
     */
    private void launchFileActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                intentFileActivity();
            } else {
                requestPermission();
            }
        } else {
            intentFileActivity();
        }

    }

    /**
     * este metodo me lleva a leer el documento de texto
     */
    private void intentFileActivity() {
        prefManager.setFirstTimeLaunch(false);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, PICK_FILE);
    }

    /**
     * este metodo me lleva  a la actividad que lee el texto que ingresa el usuario
     */
    private void launchWriteActivity() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SlideActivity.this, WriteActivity.class));
        finish();
    }

    /**
     * escucha cada vez que se haga un movimiento en el adaptador del slider
     */
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);
            if (position == 1) {
                ImageButton juanpa, laura, ivan;
                juanpa = findViewById(R.id.juanpaView);
                laura = findViewById(R.id.lauraView);
                ivan = findViewById(R.id.ivanView);
                animView(juanpa);
                animView(laura);
                animView(ivan);
            } else if (position == 2) {
                write = findViewById(R.id.writeButton);
                file = findViewById(R.id.fileButton);
                animView(write);
                animView(file);
                //se le asigna un escuchador a los botones
                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchFileActivity();
                    }
                });
                write.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchWriteActivity();
                    }
                });
            }


        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * este metodo coloca un efecto de animacion en las imagenes
     *
     * @param imageButton
     */
    private void animView(ImageButton imageButton) {
        imageButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
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
     * Adaptador del slider
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            // se valida si es la primera vez que entra a la app.. si es asi muestra el array
            //de layouts y sino solo muestra un fragmento en especifico
            if (firstTime) {
                layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(layouts[position], container, false);
                container.addView(view);
            } else {
                layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.slide_choose, container, false);
                container.addView(view);

                write = view.findViewById(R.id.writeButton);
                file = view.findViewById(R.id.fileButton);
                animView(write);
                animView(file);
                dotsLayout.setVisibility(View.INVISIBLE);
                //se le asigna un escuchador a los botones
                file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchFileActivity();
                    }
                });
                write.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchWriteActivity();
                    }
                });

            }


            return view;
        }

        @Override
        public int getCount() {
            if (firstTime)
                return layouts.length;
            return layouts[2];
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * este metodo me da respuesta del archivo de texto
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE) {
            if (resultCode == RESULT_OK) {
                // User pick the file
                Uri uri = data.getData();
                String fileContent = readTextFile(uri);
                Toast.makeText(this, fileContent, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SlideActivity.this, HuffmanCode.class);
                intent.putExtra("text", fileContent);
                startActivity(intent);

            } else {
                Log.i("-------", data.toString());
            }
        }
    }

    /**
     * este metodo lee el archivo de texto y lo convierte en texto
     *
     * @param uri
     * @return
     */
    private String readTextFile(Uri uri) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    /**
     * este metodo checkea si ya tengo el permiso activo
     *
     * @return
     */
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * este metodo hace la peticion para pedir el permiso
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    /**
     * aqui llega la respuesta del usuario al permiso de leer la informacion
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentFileActivity();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
