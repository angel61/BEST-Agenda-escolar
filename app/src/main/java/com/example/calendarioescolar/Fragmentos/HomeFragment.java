package com.example.calendarioescolar.Fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.R;

import java.util.Calendar;

public class HomeFragment extends Fragment {


    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    private CasosUsoAO casosUsoAO;
    private RecyclerView recycler;
    private RippleDrawable rippleDrawable;

    private TextView nada;

    private Activity actividad;

    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        iniciar();

        return root;
    }

    private void iniciar() {

        actividad = getActivity();
        TextView textView = root.findViewById(R.id.frase_txt);
        Calendar cal = Calendar.getInstance();
        String[] frasesSemanales = getResources().getStringArray(R.array.frases);
        int semanaDelAnno = cal.get(Calendar.WEEK_OF_YEAR) - 1;
        textView.setText(frasesSemanales[semanaDelAnno]);

        adaptador = ((Aplicacion) actividad.getApplication()).adaptadorHome;

        agendaBD = ((Aplicacion) actividad.getApplication()).agendaBD;

        casosUsoAO = new CasosUsoAO(actividad, agendaBD, adaptador);

        nada=root.findViewById(R.id.noActividades);

        recycler = root.findViewById(R.id.rvActividadesHoy);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(actividad));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(actividad.getDrawable(R.drawable.divider_home));
        recycler.addItemDecoration(itemDecorator);

        recycler.setAdapter(adaptador);
        recycler.setClipToOutline(true);
        recycler.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sinContenido();
            }
        });
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(actividad);
        String[] pruebaD=pref.getString("timetable_demo", "").split("\"day\":");
        for(int i=0;i<pruebaD.length;i++) {
            String[] aux2=pruebaD[i].split(",");
                System.out.println(aux2[0] + " " + pruebaD[i].split("classTitle\":\"")[0]);
        }
        inicializarListeners();
    }

    private void inicializarListeners() {
        final GestureDetector GestureDetector =
                new GestureDetector(actividad, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTapEvent(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

        recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                try {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null) {


                        if (GestureDetector.onTouchEvent(e)) {

                            rippleDrawable = (RippleDrawable) child.getBackground();
                            rippleDrawable.setHotspot(e.getX(), e.getY());
                            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});

                            int pos = rv.getChildAdapterPosition(child);
                            casosUsoAO.mostrar(pos, 2);
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sinContenido();
    }

    private void sinContenido() {
        if(((AdaptadorAgendaBD)recycler.getAdapter()).getCursor().getCount()<=0){
            nada.setVisibility(View.VISIBLE);
        }else{
            nada.setVisibility(View.GONE);
        }
    }
}