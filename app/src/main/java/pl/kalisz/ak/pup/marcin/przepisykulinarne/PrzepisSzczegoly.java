package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PrzepisSzczegoly extends AppCompatActivity {
    public static final String EXTRA_PRZEPIS_ID = "przepisId";
    private static final String TAG = "test";
    private SQLiteDatabase db;
    private Cursor cursor;
    private int przepisId;
    TextView tytul_przepisu, kategoria_przepisu, opis_przepisu;
    ImageView przepisZdjecie;
    private String PRZEPIS_ID;
    FrameLayout frameLayout;
    TabLayout tabLayout;
    String zdjecie, sciezka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_przepis_szczegoly);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        if(getIntent().getExtras() != null)
            przepisId = (Integer) getIntent().getExtras().get(EXTRA_PRZEPIS_ID);

        String wykonanie = "";
        String skladniki = "";

        Log.d(TAG, "onCreate: " + przepisId);
        try{
            SQLiteOpenHelper przepisySQLiteOpenHelper = new PrzepisySQLiteOpenHelper(this);
            db = przepisySQLiteOpenHelper.getWritableDatabase();
            cursor = db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI", "ZDJECIE", "SCIEZKA"},
                    "_id = ?", new String[]{Integer.toString(przepisId)}, null, null ,null);
            if(cursor.moveToFirst()){
                String tytul = cursor.getString(1);
                String kategoria = cursor.getString(2);
                String opis = cursor.getString(3);
                wykonanie = cursor.getString(4);
                skladniki = cursor.getString(5);
                zdjecie = cursor.getString(6);
                sciezka = cursor.getString(7);
                tytul_przepisu = findViewById(R.id.tytul);
                kategoria_przepisu = findViewById(R.id.kategoria_przepisu);
                opis_przepisu = findViewById(R.id.opis);
                przepisZdjecie = findViewById(R.id.przepis_zdjecie);
                tytul_przepisu.setText(tytul);
                kategoria_przepisu.setText(kategoria);
                opis_przepisu.setText(opis);
                if(!sciezka.equals("") || !zdjecie.equals(""))
                {
                    try {
                        File f=new File(sciezka, zdjecie);
                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                        przepisZdjecie.setImageBitmap(b);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            cursor.close();
        }
        catch(SQLException e){
            Toast toast = Toast.makeText(this,"Baza danycj jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        frameLayout = findViewById(R.id.frameLayout);
        tabLayout = findViewById(R.id.tabs);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft;
        SkladnikiFragment fragment1 = new SkladnikiFragment(skladniki);
        ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment1);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        String finalWykonanie = wykonanie;
        String finalSkladniki = skladniki;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft;
                switch (tab.getPosition()){
                    case 0:
                        SkladnikiFragment fragment1 = new SkladnikiFragment(finalSkladniki);
                        ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout, fragment1);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;

                    case 1:
                        WykonanieFragment fragment2 = new WykonanieFragment(finalWykonanie);
                        ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout, fragment2);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public void usun(View view){
        if(!sciezka.equals("") || !zdjecie.equals(""))
        {
            File f = new File(sciezka, zdjecie);
            if(f.exists()) {
                f.delete();
            }
        }
        db.delete("PRZEPISY", "_id = ?", new String[]{Integer.toString(przepisId)});
        db.close();
        finish();
    }

    public void aktualizuj(View view){
        Intent intent = new Intent(PrzepisSzczegoly.this, PrzepisAktualizuj.class);
        intent.putExtra(PrzepisSzczegoly.EXTRA_PRZEPIS_ID, (int)przepisId);
        startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        String wykonanie = "";
        String skladniki = "";

        try{
            SQLiteOpenHelper przepisySQLiteOpenHelper = new PrzepisySQLiteOpenHelper(this);
            db = przepisySQLiteOpenHelper.getWritableDatabase();
            cursor = db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI", "ZDJECIE", "SCIEZKA"},
                    "_id = ?", new String[]{Integer.toString(przepisId)}, null, null ,null);
            if(cursor.moveToFirst()){
                String tytul = cursor.getString(1);
                String kategoria = cursor.getString(2);
                String opis = cursor.getString(3);
                wykonanie = cursor.getString(4);
                skladniki = cursor.getString(5);
                zdjecie = cursor.getString(6);
                sciezka = cursor.getString(7);
                tytul_przepisu = findViewById(R.id.tytul);
                kategoria_przepisu = findViewById(R.id.kategoria_przepisu);
                opis_przepisu = findViewById(R.id.opis);
                przepisZdjecie = findViewById(R.id.przepis_zdjecie);
                tytul_przepisu.setText(tytul);
                kategoria_przepisu.setText(kategoria);
                opis_przepisu.setText(opis);
                if(!sciezka.equals("") || !zdjecie.equals(""))
                {
                    try {
                        File f=new File(sciezka, zdjecie);
                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

//                        b = Bitmap.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, true);
                        przepisZdjecie.setImageBitmap(b);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
            cursor.close();
        }
        catch(SQLException e){
            Toast toast = Toast.makeText(this,"Baza danycj jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        frameLayout = findViewById(R.id.frameLayout);
        tabLayout = findViewById(R.id.tabs);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft;
        SkladnikiFragment fragment1 = new SkladnikiFragment(skladniki);
        ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment1);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        String finalWykonanie = wykonanie;
        String finalSkladniki = skladniki;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft;
                switch (tab.getPosition()){
                    case 0:
                        SkladnikiFragment fragment1 = new SkladnikiFragment(finalSkladniki);
                        ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout, fragment1);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;

                    case 1:
                        WykonanieFragment fragment2 = new WykonanieFragment(finalWykonanie);
                        ft = fm.beginTransaction();
                        ft.replace(R.id.frameLayout, fragment2);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
