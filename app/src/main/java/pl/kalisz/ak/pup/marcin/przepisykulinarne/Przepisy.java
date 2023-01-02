package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Przepisy extends AppCompatActivity {

    public static final String EXTRA_PRZEPIS_KATEGORIA = "przepisKategoria";
    private static final String TAG = "test";
    private SQLiteOpenHelper przepisySQLiteOpenHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listViewPrzepisy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_przepisy);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listViewPrzepisy = findViewById(R.id.listViewPrzepisy);
        try{
            przepisySQLiteOpenHelper = new PrzepisySQLiteOpenHelper(this);
            db = przepisySQLiteOpenHelper.getWritableDatabase();
        }
        catch(SQLException e){
            Toast toast = Toast.makeText(this,"Baza danych jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listViewOrganizacje, View view, int position, long id) {
                Intent intent = new Intent(Przepisy.this, PrzepisSzczegoly.class);
                intent.putExtra(PrzepisSzczegoly.EXTRA_PRZEPIS_ID, (int)id);
                startActivity(intent);
            }
        };
        listViewPrzepisy.setOnItemClickListener(itemClickListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent int1 = new Intent(Przepisy.this, PrzepisDodaj.class);
                startActivity(int1);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        String przepisKategoria = (String) getIntent().getExtras().get(EXTRA_PRZEPIS_KATEGORIA);
        Log.d(TAG, "onResume: " + przepisKategoria);
        this.setTitle(przepisKategoria);
        Log.d(TAG, "onResume:" + przepisKategoria);
        cursor = db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI", "ZDJECIE", "SCIEZKA"},
                "KATEGORIA = ?",new String[]{przepisKategoria}, null ,null, null);

        if(cursor.moveToFirst()){
            ListCursorAdapter adapter = new ListCursorAdapter(this, cursor);
            listViewPrzepisy.setAdapter(adapter);

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {
                    return db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI", "ZDJECIE", "SCIEZKA"},
                            "TYTUL LIKE ? AND KATEGORIA = ?", new String[]{"%" + charSequence + "%", przepisKategoria}, null ,null, null);
                }
            });
            SearchView wyszukaj = (SearchView) findViewById(R.id.wyszukaj);
            wyszukaj.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
    }

}