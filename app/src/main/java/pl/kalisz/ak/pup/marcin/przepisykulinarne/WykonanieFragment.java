package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WykonanieFragment extends Fragment {
    TextView textView;
    String tresc;
    String[] wykonanie;

    public WykonanieFragment(String test) {
        tresc = test;
        wykonanie = tresc.split("\n");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = getView().findViewById(R.id.wykonanie_tabview);
        textView.setText(tresc);
        textView.setText("");
        int i = 1;
        for (String s : wykonanie) {
            textView.append(i + ". " + s + "\n\n");
            i++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wykonanie, container, false);
    }
}