package com.example.calendarioescolar.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calendarioescolar.R;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = root.findViewById(R.id.frase_txt);
        Calendar cal = Calendar.getInstance();
        String[] frasesSemanales = getResources().getStringArray(R.array.frases);
        int semanaDelAnno = cal.get(Calendar.WEEK_OF_YEAR) - 1;
        textView.setText(frasesSemanales[semanaDelAnno]);
        return root;
    }
}