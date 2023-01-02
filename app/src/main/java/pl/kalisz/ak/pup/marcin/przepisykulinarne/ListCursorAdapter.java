package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ListCursorAdapter extends CursorAdapter {

    public ListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView item_tytul = (TextView) view.findViewById(R.id.item_tytul);
        TextView item_kategoria = (TextView) view.findViewById(R.id.item_kategoria);
        TextView item_opis = (TextView) view.findViewById(R.id.item_opis);
        ImageView przepisZdjecie = (ImageView) view.findViewById(R.id.image_item);
        // Extract properties from cursor
        String tytul = cursor.getString(cursor.getColumnIndexOrThrow("TYTUL"));
        String kategoria = cursor.getString(cursor.getColumnIndexOrThrow("KATEGORIA"));
        String sciezka = cursor.getString(cursor.getColumnIndexOrThrow("SCIEZKA"));
        String zdjecie = cursor.getString(cursor.getColumnIndexOrThrow("ZDJECIE"));
        String opis = cursor.getString(cursor.getColumnIndexOrThrow("OPIS"));
        // Populate fields with extracted properties
        if(!sciezka.equals("") || !zdjecie.equals(""))
        {
            try {
                File f=new File(sciezka, zdjecie);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
//                b = Bitmap.createScaledBitmap(b, b.getWidth()/2, b.getHeight()/2, true);
                przepisZdjecie.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        item_opis.setText(opis);
        item_tytul.setText(tytul);
        item_kategoria.setText(kategoria);
    }
}