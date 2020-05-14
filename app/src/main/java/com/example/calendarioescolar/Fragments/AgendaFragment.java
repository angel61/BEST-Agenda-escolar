package com.example.calendarioescolar.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Presentacion.EditarHorarioActivity;
import com.example.calendarioescolar.R;

public class AgendaFragment extends Fragment {

    public AgendaBD agendaBD;
    public AdaptadorAgendaBD adaptador;
    private CasosUsoAO casosUsoAO;
    private RecyclerView recycler;
    private SwipeRefreshLayout refresh;
    private RippleDrawable rippleDrawable;
    private Aplicacion aplicacion;

    private Activity actividad;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_agenda, container, false);

        setHasOptionsMenu(true);
        iniciar();

        return root;
    }

    private void iniciar() {
        actividad = getActivity();
        aplicacion=((Aplicacion) actividad.getApplication());

        adaptador = aplicacion.adaptador;

        agendaBD = aplicacion.agendaBD;

        casosUsoAO = new CasosUsoAO(actividad, agendaBD, adaptador);

        recycler = root.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(actividad));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(actividad.getDrawable(R.drawable.divider_agenda));
        recycler.addItemDecoration(itemDecorator);

        recycler.setAdapter(adaptador);

        refresh = root.findViewById(R.id.refreshl);

        inicializarListeners();
    }

    private void inicializarListeners() {

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarCursor();
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

    private void cargarCursor() {
        adaptador = new AdaptadorAgendaBD(agendaBD, R.layout.elemento_agenda, agendaBD.extraeCursor(aplicacion.agendaCursor));
        recycler.setAdapter(adaptador);
        aplicacion.adaptador = adaptador;
        refresh.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_ordenar:
                View v = actividad.findViewById(R.id.accion_ordenar);
                PopupMenu popup = new PopupMenu(actividad, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.pasadas:
                                aplicacion.agendaCursor=0;
                                cargarCursor();
                                return true;

                            case R.id.ayer:
                                aplicacion.agendaCursor=1;
                                cargarCursor();
                                return true;

                            case R.id.hoy:
                                aplicacion.agendaCursor=2;
                                cargarCursor();
                                return true;

                            case R.id.mannana:
                                aplicacion.agendaCursor=3;
                                cargarCursor();
                                return true;

                            case R.id.proximas:
                                aplicacion.agendaCursor=4;
                                cargarCursor();
                                return true;

                            case R.id.todo:
                                aplicacion.agendaCursor=5;
                                cargarCursor();
                                return true;
                        }
                        return false;
                    }
                });
                popup.inflate(R.menu.popup_filter);
                popup.show();
                return true;

        }
        return false;
    }
}