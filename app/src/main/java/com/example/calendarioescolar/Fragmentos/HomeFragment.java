package com.example.calendarioescolar.Fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgenda;
import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Adaptadores.AdaptadorDivider;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    private ListView listview;
    private ArrayList<String> asignaturas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        iniciar();
        ((Aplicacion)actividad.getApplication()).home=this;
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

        nada = root.findViewById(R.id.noActividades);

        recycler = root.findViewById(R.id.rvActividadesHoy);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(actividad));

        RecyclerView.ItemDecoration dividerItemDecoration = new AdaptadorDivider(actividad.getDrawable(R.drawable.divider_home));
        recycler.addItemDecoration(dividerItemDecoration);

        recycler.setAdapter(adaptador);
        recycler.setClipToOutline(true);
        sinContenido();

        listview = root.findViewById(R.id.listAsignaturasHoy);
        incializarListView();

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

    public void sinContenido() {

            if (((AdaptadorAgendaBD)recycler.getAdapter()).getCursor().getCount() <= 0) {
            nada.setVisibility(View.VISIBLE);
        } else {
            nada.setVisibility(View.GONE);
        }
    }

    public void incializarListView() {
        asignaturas = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(actividad);
        String pruebaD = pref.getString("timetable_demo", "");
        int dia = (cal.get(Calendar.DAY_OF_WEEK) - 1);
        JSONObject result = null;
        try {
            result = new JSONObject(pruebaD);
            JSONArray stickers = result.getJSONArray("sticker");
            for (int i = 0; stickers.length() > i; i++) {
                JSONObject sticker = stickers.getJSONObject(i);
                JSONArray schedule = sticker.getJSONArray("schedule");
                JSONObject clase = schedule.getJSONObject(0);
                if (clase.getInt("day") == (dia - 1)) {
                    String nombre = clase.getString("classTitle");
                    asignaturas.add(nombre);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (asignaturas.size() <= 0) {
            root.findViewById(R.id.noAsignaturas).setVisibility(View.VISIBLE);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(actividad, android.R.layout.simple_list_item_1, asignaturas);
        listview.setAdapter(adapter);
        int dp = (int) (48 * actividad.getResources().getSystem().getDisplayMetrics().density);
        ViewGroup.LayoutParams params = listview.getLayoutParams();
        params.height = (dp) * asignaturas.size();
        listview.setLayoutParams(params);
    }


}