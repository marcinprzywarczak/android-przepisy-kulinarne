package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PrzepisDodaj extends AppCompatActivity {
    private EditText tytul, opis, skladniki, wykonanie;
    private Spinner kategorie;
    private SQLiteDatabase db;
    private final int GALLERY_REQ_CODE = 1000;
    ImageView img;
    OutputStream outputStream;
    boolean zdjecie = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_przepis_dodaj);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        img = findViewById(R.id.zdjeciePrzepis);
        Button btn = findViewById(R.id.zdjecie);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        try{
            SQLiteOpenHelper przepisSQLiteOpenHelper = new PrzepisySQLiteOpenHelper(this);
            db = przepisSQLiteOpenHelper.getWritableDatabase();
            tytul = (EditText) findViewById(R.id.tytul);
            opis = (EditText) findViewById(R.id.opis);
            skladniki = findViewById(R.id.skladniki);
            wykonanie = findViewById(R.id.wykonanie);
            kategorie = findViewById(R.id.kategorie);
        }
        catch (SQLException e){
            Toast toast = Toast.makeText(this,"Baza danycj jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }
    public void anuluj(View view){
        finish();
    }

    public void zapisz(View view){
        String filename = "";
        String sciezka = "";
        if(zdjecie){
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("przepisy-image", Context.MODE_PRIVATE);
            filename = System.currentTimeMillis()+".jpg";
            sciezka = directory.getAbsolutePath();
            File mypath = new File(directory,filename);
            FileOutputStream fos = null;
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        Log.d("IMAGE", "zapisz: " + directory.getAbsolutePath());
        ContentValues obiektValues = new ContentValues();
        obiektValues.put("TYTUL", tytul.getText().toString());
        obiektValues.put("KATEGORIA", String.valueOf(kategorie.getSelectedItem()));
        obiektValues.put("OPIS", opis.getText().toString());
        obiektValues.put("WYKONANIE", wykonanie.getText().toString());
        obiektValues.put("SKLADNIKI", skladniki.getText().toString());
        obiektValues.put("ZDJECIE", filename);
        obiektValues.put("SCIEZKA", sciezka);
        db.insert("PRZEPISY", null, obiektValues);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                img.setImageURI(data.getData());
                BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
                img.setImageBitmap(bitmap);

                this.zdjecie = true;
            }
        }
    }
}
