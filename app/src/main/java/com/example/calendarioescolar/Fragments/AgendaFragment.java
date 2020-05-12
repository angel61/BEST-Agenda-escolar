package com.example.calendarioescolar.Fragments;

import android.app.Activity;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.R;

public class AgendaFragment extends Fragment {

    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    private CasosUsoAO casosUsoAO;
    private RecyclerView recycler;
    private SwipeRefreshLayout refresh;
    private RippleDrawable rippleDrawable;

    private Activity actividad;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_agenda, container, false);

        actividad = getActivity();
        iniciar();

        return root;
    }

    private void iniciar() {


        adaptador = ((Aplicacion) actividad.getApplication()).adaptador;

        agendaBD = ((Aplicacion) actividad.getApplication()).agendaBD;

        casosUsoAO = new CasosUsoAO(actividad, agendaBD, adaptador);

        recycler = root.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(actividad));
        recycler.addItemDecoration(new DividerItemDecoration(actividad, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(adaptador);

        refresh = root.findViewById(R.id.refreshl);

        inicializarListeners();
    }

    private void inicializarListeners() {

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adaptador = new AdaptadorAgendaBD(agendaBD, agendaBD.extraeCursor());
                recycler.setAdapter(adaptador);
                ((Aplicacion) actividad.getApplication()).adaptador = adaptador;
                refresh.setRefreshing(false);
            }
        });

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
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                    if (child != null) {


                        if (GestureDetector.onTouchEvent(motionEvent)) {

                            rippleDrawable = (RippleDrawable) child.getBackground();
                            rippleDrawable.setHotspot(motionEvent.getX(), motionEvent.getY());
                            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});

                            int pos = recyclerView.getChildAdapterPosition(child);
                            casosUsoAO.mostrar(pos, 1);
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });
    }
}