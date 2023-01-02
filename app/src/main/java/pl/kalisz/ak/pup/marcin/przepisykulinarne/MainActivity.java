package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private CharSequence komunikat;
    private SQLiteOpenHelper przepisySQLiteOpenHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listViewPrzepisy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        this.setTitle("Wszystkie przepisy");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listViewPrzepisy = findViewById(R.id.listViewPrzepisyMain);
        try{
            przepisySQLiteOpenHelper = new PrzepisySQLiteOpenHelper(this);
            db = przepisySQLiteOpenHelper.getWritableDatabase();
        }
        catch(SQLException e){
            Toast toast = Toast.makeText(this,"Baza danycj jest niedostępna", Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listViewOrganizacje, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PrzepisSzczegoly.class);
                intent.putExtra(PrzepisSzczegoly.EXTRA_PRZEPIS_ID, (int)id);
                startActivity(intent);
            }
        };
        listViewPrzepisy.setOnItemClickListener(itemClickListener);

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent int1 = new Intent(MainActivity.this, PrzepisDodaj.class);
                startActivity(int1);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI", "ZDJECIE", "SCIEZKA"},
                null, null, null ,null, null);

        if(cursor.moveToFirst()){
            ListCursorAdapter adapter = new ListCursorAdapter(this, cursor);
            listViewPrzepisy.setAdapter(adapter);
            listViewPrzepisy.setTextFilterEnabled(true);

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {
                    return db.query("PRZEPISY", new String[]{"_id", "TYTUL", "KATEGORIA", "OPIS", "WYKONANIE", "SKLADNIKI", "ZDJECIE", "SCIEZKA"},
                            "TYTUL LIKE ?", new String[]{"%" + charSequence + "%"}, null ,null, null);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.nav_16:
                Intent intent1 = new Intent(this, Przepisy.class);
                intent1.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Dania główne");
                startActivity(intent1);
                break;

            case R.id.nav_17:
                Intent intent2 = new Intent(this, Przepisy.class);
                intent2.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Makarony");
                startActivity(intent2);
                break;

            case R.id.nav_18:
                Intent intent3 = new Intent(this, Przepisy.class);
                intent3.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Zupy");
                startActivity(intent3);
                break;

            case R.id.nav_19:
                Intent intent4 = new Intent(this, Przepisy.class);
                intent4.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Pizza");
                startActivity(intent4);
                break;

            case R.id.nav_110:
                Intent intent5 = new Intent(this, Przepisy.class);
                intent5.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Street food");
                startActivity(intent5);
                break;

            case R.id.nav_111:
                Intent intent6 = new Intent(this, Przepisy.class);
                intent6.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Naleśniki");
                startActivity(intent6);
                break;

            case R.id.nav_112:
                Intent intent7 = new Intent(this, Przepisy.class);
                intent7.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Sałatki");
                startActivity(intent7);
                break;

            case R.id.nav_113:
                Intent intent8 = new Intent(this, Przepisy.class);
                intent8.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Kanapki");
                startActivity(intent8);
                break;

            case R.id.nav_114:
                Intent intent9 = new Intent(this, Przepisy.class);
                intent9.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Przystawki");
                startActivity(intent9);
                break;

            case R.id.nav_115:
                Intent intent10 = new Intent(this, Przepisy.class);
                intent10.putExtra(Przepisy.EXTRA_PRZEPIS_KATEGORIA, "Domowe przetwory");
                startActivity(intent10);
                break;
            case R.id.navInfo:
                Intent intent11 = new Intent(this, Info.class);
                startActivity(intent11);
                break;
            default:
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}