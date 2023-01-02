package pl.kalisz.ak.pup.marcin.przepisykulinarne;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class SkladnikiFragment extends Fragment {
    TextView textView;
    String tresc;
    String[]skladniki;

    public SkladnikiFragment(String test) {
        tresc = test;
        skladniki = tresc.split("\n");
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = getView().findViewById(R.id.skladniki_tabview);
        textView.setText("");
        for (String s : skladniki) {
            textView.append(Html.fromHtml("&#8226;") + s + "\n\n");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skladniki, container, false);
    }
}