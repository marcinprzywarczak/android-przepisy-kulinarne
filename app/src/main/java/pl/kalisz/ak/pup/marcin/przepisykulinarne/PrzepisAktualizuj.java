package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrzepisAktualizuj extends AppCompatActivity {
    public static final String EXTRA_PRZEPIS_ID = "przepisId";
    private SQLiteDatabase db;
    private Cursor cursor;
    private int przepisId;
    private EditText tytul, opis, skladniki, wykonanie;
    ImageView przepisZdjecie;
    private Spinner kategorie;
    private final int GALLERY_REQ_CODE = 1000;
    String zdjecie, sciezka;
    boolean noweZdjecie = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_przepis_aktualizuj);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        przepisId = (Integer) getIntent().getExtras().get(EXTRA_PRZEPIS_ID);
        Button btn = findViewById(R.id.zdjecie_aktualizacja);
        przepisZdjecie = findViewById(R.id.zdjeciePrzepisAktualizuj);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        try{
            SQLiteOpenHelper przepisySQLiteOpenHelper = new PrzepisySQLiteOpenHelper(this);
            db = przepisySQLiteOpenHelper.getWritableDatabase();
            cursor = db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI",  "ZDJECIE", "SCIEZKA"},
                    "_id = ?", new String[]{Integer.toString(przepisId)}, null, null ,null);
            if(cursor.moveToFirst()){
                String tytul1 = cursor.getString(1);
                String kategoria1 = cursor.getString(2);
                String opis1 = cursor.getString(3);
                String wykonanie1 = cursor.getString(4);
                String skladniki1 = cursor.getString(5);
                zdjecie = cursor.getString(6);
                sciezka = cursor.getString(7);
                tytul = (EditText) findViewById(R.id.tytul_aktualizacja);
                opis = (EditText) findViewById(R.id.opis_aktualizacja);
                skladniki = findViewById(R.id.skladniki_aktualizacja);
                wykonanie = findViewById(R.id.wykonanie_aktualizacja);
                kategorie = findViewById(R.id.kategorie_aktualizacja);

                try {
                    File f=new File(sciezka, zdjecie);
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                    przepisZdjecie.setImageBitmap(b);
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                tytul.setText(tytul1);
                opis.setText(opis1);
                skladniki.setText(skladniki1);
                kategorie.setSelection(((ArrayAdapter)kategorie.getAdapter()).getPosition(kategoria1));
                wykonanie.setText(wykonanie1);
                skladniki.setText(skladniki1);
            }

        }
        catch (SQLException e){
            Toast toast = Toast.makeText(this,"Baza danych jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void anuluj(View view){
        finish();
    }

    public void zapisz(View view){
        if(noweZdjecie){
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("przepisy-image", Context.MODE_PRIVATE);
            String filename = System.currentTimeMillis()+".jpg";
            File mypath = new File(directory,filename);
            FileOutputStream fos = null;
            BitmapDrawable drawable = (BitmapDrawable) przepisZdjecie.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                File f = new File(sciezka, zdjecie);
                if(f.exists())
                    f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sciezka = directory.getAbsolutePath();
            zdjecie = filename;
        }
        ContentValues obiektValues = new ContentValues();
        obiektValues.put("TYTUL", tytul.getText().toString());
        obiektValues.put("KATEGORIA", String.valueOf(kategorie.getSelectedItem()));
        obiektValues.put("OPIS", opis.getText().toString());
        obiektValues.put("WYKONANIE", wykonanie.getText().toString());
        obiektValues.put("SKLADNIKI", skladniki.getText().toString());
        obiektValues.put("ZDJECIE", zdjecie);
        obiektValues.put("SCIEZKA", sciezka);
        db.update("PRZEPISY", obiektValues, "_id = ?", new String[]{Integer.toString(przepisId)});
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                przepisZdjecie.setImageURI(data.getData());
                BitmapDrawable drawable = (BitmapDrawable) przepisZdjecie.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
                przepisZdjecie.setImageBitmap(bitmap);
                noweZdjecie = true;
            }
        }
    }
}