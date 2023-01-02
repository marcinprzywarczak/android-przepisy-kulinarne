package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PrzepisySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Przepisy";
    private static final int DB_VERSION = 2;

    PrzepisySQLiteOpenHelper(Context context){ super(context, DB_NAME, null, DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void wstawPrzepis(SQLiteDatabase db, String tytul, String kategoria, String opis,
                                     String wykonanie, String skladniki, String zdjecie, String sciezka){
        ContentValues obiektValues = new ContentValues();
        obiektValues.put("TYTUL", tytul);
        obiektValues.put("KATEGORIA", kategoria);
        obiektValues.put("OPIS", opis);
        obiektValues.put("WYKONANIE", wykonanie);
        obiektValues.put("SKLADNIKI", skladniki);
        obiektValues.put("ZDJECIE", zdjecie);
        obiektValues.put("SCIEZKA", sciezka);
        db.insert("PRZEPISY", null, obiektValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion < 1) {
            db.execSQL("CREATE TABLE PRZEPISY (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "TYTUL TEXT, " +
                    "KATEGORIA TEXT, " + "OPIS TEXT, " + "WYKONANIE TEXT, " + "SKLADNIKI TEXT, " + "ZDJECIE TEXT, " + "SCIEZKA TEXT);");
            wstawPrzepis(db, "Przykładowy przepis 1", "Dania główne",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur nisi elit, ultricies quis lorem sed, convallis tempor arcu. Aenean tincidunt tincidunt dui, vel pulvinar magna elementum quis. Cras est ex, sollicitudin vitae tincidunt eu, fringilla et diam. Maecenas non feugiat nisi.",
                    "Phasellus eget ante feugiat, auctor orci ac, ultrices tortor.\nUt dignissim elit velit, id dictum sapien tincidunt et.\nUt laoreet, enim vitae faucibus consequat, velit quam tempor dui, non luctus dolor urna lacinia nisi.\n" +
                            "Nunc vel sollicitudin justo, ac fermentum nulla.\nDonec rutrum mattis velit eget blandit\n",
                    "Składnik nr 1, 3 sztuki.\nSkładnik nr 2, 4 sztuki.\nSkładnik nr 3, 200 gramów\nSkladnik nr 4, 100 gramów\nSkładnik nr 5, 2 sztuki",
                    "", "");
            wstawPrzepis(db, "Przykładowy przepis 2", "Makarony",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur nisi elit, ultricies quis lorem sed, convallis tempor arcu. Aenean tincidunt tincidunt dui, vel pulvinar magna elementum quis. Cras est ex, sollicitudin vitae tincidunt eu, fringilla et diam. Maecenas non feugiat nisi.",
                    "Phasellus eget ante feugiat, auctor orci ac, ultrices tortor.\nUt dignissim elit velit, id dictum sapien tincidunt et.\nUt laoreet, enim vitae faucibus consequat, velit quam tempor dui, non luctus dolor urna lacinia nisi.\n" +
                            "Nunc vel sollicitudin justo, ac fermentum nulla.\nDonec rutrum mattis velit eget blandit\n",
                    "Składnik nr 1, 3 sztuki.\nSkładnik nr 2, 4 sztuki.\nSkładnik nr 3, 200 gramów\nSkladnik nr 4, 100 gramów\nSkładnik nr 5, 2 sztuki",
                    "", "");
            wstawPrzepis(db, "Przykładowy przepis 3", "Zupy",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur nisi elit, ultricies quis lorem sed, convallis tempor arcu. Aenean tincidunt tincidunt dui, vel pulvinar magna elementum quis. Cras est ex, sollicitudin vitae tincidunt eu, fringilla et diam. Maecenas non feugiat nisi.",
                    "Phasellus eget ante feugiat, auctor orci ac, ultrices tortor.\nUt dignissim elit velit, id dictum sapien tincidunt et.\nUt laoreet, enim vitae faucibus consequat, velit quam tempor dui, non luctus dolor urna lacinia nisi.\n" +
                            "Nunc vel sollicitudin justo, ac fermentum nulla.\nDonec rutrum mattis velit eget blandit\n",
                    "Składnik nr 1, 3 sztuki.\nSkładnik nr 2, 4 sztuki.\nSkładnik nr 3, 200 gramów\nSkladnik nr 4, 100 gramów\nSkładnik nr 5, 2 sztuki",
                    "", "");
//            wstawPrzepis(db, "Przykładowy przepis 4", "Pizza",
//                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur nisi elit, ultricies quis lorem sed, convallis tempor arcu. Aenean tincidunt tincidunt dui, vel pulvinar magna elementum quis. Cras est ex, sollicitudin vitae tincidunt eu, fringilla et diam. Maecenas non feugiat nisi.",
//                    "Phasellus eget ante feugiat, auctor orci ac, ultrices tortor.\nUt dignissim elit velit, id dictum sapien tincidunt et.\nUt laoreet, enim vitae faucibus consequat, velit quam tempor dui, non luctus dolor urna lacinia nisi.\n" +
//                            "Nunc vel sollicitudin justo, ac fermentum nulla.\nDonec rutrum mattis velit eget blandit\n",
//                    "Składnik nr 1, 3 sztuki.\nSkładnik nr 2, 4 sztuki.\nSkładnik nr 3, 200 gramów\nSkladnik nr 4, 100 gramów\nSkładnik nr 5, 2 sztuki",
//                    "", "");
//            wstawPrzepis(db, "Przykładowy przepis 5", "Street food",
//                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur nisi elit, ultricies quis lorem sed, convallis tempor arcu. Aenean tincidunt tincidunt dui, vel pulvinar magna elementum quis. Cras est ex, sollicitudin vitae tincidunt eu, fringilla et diam. Maecenas non feugiat nisi.",
//                    "Phasellus eget ante feugiat, auctor orci ac, ultrices tortor.\nUt dignissim elit velit, id dictum sapien tincidunt et.\nUt laoreet, enim vitae faucibus consequat, velit quam tempor dui, non luctus dolor urna lacinia nisi.\n" +
//                            "Nunc vel sollicitudin justo, ac fermentum nulla.\nDonec rutrum mattis velit eget blandit\n",
//                    "Składnik nr 1, 3 sztuki.\nSkładnik nr 2, 4 sztuki.\nSkładnik nr 3, 200 gramów\nSkladnik nr 4, 100 gramów\nSkładnik nr 5, 2 sztuki",
//                    "", "");
//            wstawPrzepis(db, "Przykładowy przepis 6", "Naleśniki",
//                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur nisi elit, ultricies quis lorem sed, convallis tempor arcu. Aenean tincidunt tincidunt dui, vel pulvinar magna elementum quis. Cras est ex, sollicitudin vitae tincidunt eu, fringilla et diam. Maecenas non feugiat nisi.",
//                    "Phasellus eget ante feugiat, auctor orci ac, ultrices tortor.\nUt dignissim elit velit, id dictum sapien tincidunt et.\nUt laoreet, enim vitae faucibus consequat, velit quam tempor dui, non luctus dolor urna lacinia nisi.\n" +
//                            "Nunc vel sollicitudin justo, ac fermentum nulla.\nDonec rutrum mattis velit eget blandit\n",
//                    "Składnik nr 1, 3 sztuki.\nSkładnik nr 2, 4 sztuki.\nSkładnik nr 3, 200 gramów\nSkladnik nr 4, 100 gramów\nSkładnik nr 5, 2 sztuki",
//                    "", "");
        }
    }
}