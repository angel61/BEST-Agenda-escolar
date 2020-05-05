package com.example.calendarioescolar;

import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentPast extends Fragment {
    View v;
    public AdaptadorAgendaBD adaptador;
    private RecyclerView recyclerPasado;
    private CasosUsoAO usoAO;

    private RippleDrawable rippleDrawable;
    public FragmentPast(){


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        v=inflater.inflate(R.layout.fragment_past,container,false);

        adaptador = ((Aplicacion) getActivity().getApplication()).adaptadorPa;
        recyclerPasado = v.findViewById(R.id.recycler_viewPa);
        recyclerPasado.setHasFixedSize(true);
        recyclerPasado.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPasado.addItemDecoration(new DividerItemDecoration(recyclerPasado.getContext(),DividerItemDecoration.VERTICAL));
        recyclerPasado.setAdapter(adaptador);
        usoAO=new CasosUsoAO(getActivity(),((Aplicacion) getActivity().getApplication()).agendaBD,adaptador,((Aplicacion) getActivity().getApplication()));
        inicializarListeners();
        return v;
    }
    private void inicializarListeners() {


        final GestureDetector GestureDetector =
                new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTapEvent(MotionEvent e) {
                        return false;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });

        recyclerPasado.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
                            usoAO.mostrar(pos, 1,1);
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
