package com.matheusflausino.closetapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matheusflausino.closetapp.model.Clothes;
import com.matheusflausino.closetapp.model.Type;
import com.matheusflausino.closetapp.repo.ClothesDAO;
import com.matheusflausino.closetapp.repo.TypeDAO;
import com.matheusflausino.closetapp.util.PrefixTokenizer;
import com.matheusflausino.closetapp.util.UtilFoto;
import com.matheusflausino.closetapp.util.UtilString;

import java.io.IOException;
import java.util.ArrayList;

import static com.matheusflausino.closetapp.util.UtilString.ALTERAR;
import static com.matheusflausino.closetapp.util.UtilString.ID;
import static com.matheusflausino.closetapp.util.UtilString.MODO;

public class CaptureActivity extends AppCompatActivity {
    private static final int REQUEST_TODAS_PERMISSOES = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_IMAGE_CROP = 3;

    private int modo;
    private int idC;

    private ImageView imageFoto;
    private String    caminhoFoto = null;
    private Intent uri = null;
    private ArrayAdapter<String> adapter;

    private Spinner textType;
    private MultiAutoCompleteTextView textModel;
    private MultiAutoCompleteTextView textColor;
    private CheckBox textFavorite;

    private ImageButton buttonCamera;
    private ImageButton buttonGallery;

    private ClothesDAO dbClothes;
    private TypeDAO dbType;

    View.OnClickListener onClickGallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getPermissionsCamera())
                UtilFoto.galleryOpen(CaptureActivity.this, REQUEST_IMAGE_GALLERY);
        }
    };

    View.OnClickListener onClickCamera = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getPermissionsCamera())
                UtilFoto.cameraOpen(CaptureActivity.this, REQUEST_IMAGE_CAPTURE);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dbClothes = new ClothesDAO(this);
        dbType = new TypeDAO(this);
        String p; //String Preferences

        if(getPermissionsCamera());

        // FindElements
        textFavorite = findViewById(R.id.favoriteBox);
        imageFoto = (ImageView) findViewById(R.id.imageCapture);
        buttonCamera = (ImageButton) findViewById(R.id.buttonCapture);
        buttonGallery = (ImageButton) findViewById(R.id.buttonGallery);
        textType = findViewById(R.id.typeSpinner);
        textModel = findViewById(R.id.modelMultiAuto);
        textColor = findViewById(R.id.colorMultiAuto);

        //GET MODO
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO);

        //Imagem
        imageFoto.setImageResource(R.drawable.tshirt_crew);

        //BOTÃO DA CAMERA
        buttonCamera.setOnClickListener(onClickCamera);

        //BOTÃO DA GALERIA
        buttonGallery.setOnClickListener(onClickGallery);

        // Spinner TIPO
        ArrayList<String> list = new ArrayList<>();
        for (Type l : dbType.findAll()){
            list.add(l.getId()-1, l.getType());
        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                list);

        textType.setAdapter(adapter);

        //multiautocomplete MODELO
        p = UtilString.getPreference(this, UtilString.PREFERENCES_MODELS);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                new Gson().fromJson(p,String[].class));

        textModel.setAdapter(adapter);
        textModel.setTokenizer(new PrefixTokenizer('#'));

        //multiautocomplete COR
        p = UtilString.getPreference(this, UtilString.PREFERENCES_COLORS);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                new Gson().fromJson(p,String[].class));

        textColor.setAdapter(adapter);
        textColor.setTokenizer(new PrefixTokenizer('#'));

        if(modo == ALTERAR){
            idC = (int) bundle.get(ID);
            Clothes c = (Clothes) dbClothes.findById(idC);
            caminhoFoto = c.getImage();
            UtilFoto.setImage(caminhoFoto, imageFoto, 240, 320);
            textType.setSelection(c.getType().getId()-1);
            textModel.setText(c.getModel());
            textColor.setText(c.getColor());
            if(c.getFavorite() == 1)
                textFavorite.setChecked(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();

                return true;

            case R.id.cancelClothes:
                setResult(Activity.RESULT_CANCELED);
                finish();
                return true;

            case R.id.saveClothes:
                if(!saveClothes())
                    Toast.makeText(this, R.string.error_save, Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        setResult(Activity.RESULT_CANCELED);
        finish();
        return;
    }

    private boolean getPermissionsCamera() {

        if (    ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_TODAS_PERMISSOES);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_TODAS_PERMISSOES: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //getFoto();
                } else {
                    Toast.makeText(this, R.string.erro_permissoes, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (data == null && requestCode != REQUEST_IMAGE_CROP)
                return;
            switch (requestCode) {
                case REQUEST_IMAGE_GALLERY:

                case REQUEST_IMAGE_CAPTURE:
                    uri = data;
                    UtilFoto.cropImage(CaptureActivity.this, REQUEST_IMAGE_CROP, uri.getData());
                    break;
                case REQUEST_IMAGE_CROP:
                    if (data != null)
                        uri = data;
                    else
                        Log.d("URI", "onActivityResult: "+uri);

                    try {
                        caminhoFoto = UtilFoto.createImageFile(CaptureActivity.this, uri);
                        UtilFoto.setImage(caminhoFoto, imageFoto, 240,320);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

    }

    private boolean saveClothes(){
        int idT = (int) textType.getSelectedItemId();

        Clothes c;
        if(modo == ALTERAR)
            c = (Clothes) dbClothes.findById(idC);
        else
            c = new Clothes();


        Type t = (Type) dbType.findById(idT+1);
        c.setType(t);

        if(caminhoFoto == null)
            return false;
        c.setImage(caminhoFoto);

        if(UtilString.stringVazia(textModel.getText().toString()))
            return false;
        c.setModel(textModel.getText().toString());

        if(UtilString.stringVazia(textColor.getText().toString()))
            return false;
        c.setColor(textColor.getText().toString());

        if(textFavorite.isChecked()) {
            c.setFavorite(1);
        }else{
            c.setFavorite(0);
        }
        if(modo == ALTERAR){
            if (dbClothes.update(c) == -1)
                return false;
        }else {
            if (dbClothes.create(c) == -1)
                return false;
        }

        UtilString.userPreference(this, UtilString.PREFERENCES_MODELS, c.getModel());
        UtilString.userPreference(this, UtilString.PREFERENCES_COLORS, c.getColor());

        setResult(Activity.RESULT_OK);
        finish();
        return true;
    }

}
