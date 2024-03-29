package com.example.calendarioescolar.Fragmentos;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.calendarioescolar.Actividades.AgendaObjectActivity;
import com.example.calendarioescolar.Adaptadores.AdaptadorAgendaBD;
import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.CasosDeUso.CasosUsoAO;
import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.R;

/**
 * Clase que extiende de Fragment utilizada para mostrar la interfaz de la pantalla principal de agenda
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see Fragment
 */
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


    /**
     * Se encarga de cargar la interfaz que se va ha mostrar
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see AgendaFragment#iniciar()
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_agenda, container, false);

        setHasOptionsMenu(true);
        iniciar();

        return root;
    }


    /**
     * Se encarga de inicializar los componentes del fragmento
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see AgendaFragment#inicializarListeners()
     */
    private void iniciar() {
        actividad = getActivity();
        aplicacion = ((Aplicacion) actividad.getApplication());

        adaptador = aplicacion.adaptador;

        agendaBD = aplicacion.agendaBD;

        casosUsoAO = new CasosUsoAO(actividad, agendaBD, adaptador);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(actividad, R.anim.layout_animation_rv);

        recycler = root.findViewById(R.id.recycler_view);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(actividad));

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(actividad.getDrawable(R.drawable.divider_agenda));
        recycler.addItemDecoration(itemDecorator);

        recycler.setAdapter(adaptador);

        recycler.setLayoutAnimation(controller);
        recycler.scheduleLayoutAnimation();
        refresh = root.findViewById(R.id.refreshl);

        inicializarListeners();
    }


    /**
     * Se encarga de inicializar los listeners de los componentes del fragmento
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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
        final GestureDetector gd2 =
                new GestureDetector(actividad, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {

                        final View child = recycler.findChildViewUnder(e.getX(), e.getY());
                        if (child != null) {

                            final int pos = recycler.getChildAdapterPosition(child);
                            PopupMenu popup = new PopupMenu(actividad, child);
                            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.accion_editar:
                                            casosUsoAO.editar(pos, AgendaObjectActivity.RESULTADO_EDITAR);
                                            return true;
                                        case R.id.accion_borrar:

                                            AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
                                            builder.setMessage("¿Deseas eliminar la actividad?")
                                                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            int _id = adaptador.idPosicion(pos);
                                                            casosUsoAO.borrar(_id);
                                                        }
                                                    })
                                                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();

                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });

                            popup.show();
                        }
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

                        gd2.onTouchEvent(motionEvent);

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


    /**
     * Su funcion es cargar el cursor en el recyclerView
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    private void cargarCursor() {
        adaptador = new AdaptadorAgendaBD(agendaBD, R.layout.elemento_agenda, agendaBD.extraeCursor(aplicacion.agendaCursor));
        recycler.setAdapter(adaptador);
        aplicacion.adaptador = adaptador;
        recycler.scheduleLayoutAnimation();
        refresh.setRefreshing(false);
    }


    /**
     * Evento que es lanzado cuando se selecciona alguna opcion del menu
     *
     * @param item objeto item del menu
     * @return boolean
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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
                                aplicacion.agendaCursor = 0;
                                cargarCursor();
                                return true;

                            case R.id.ayer:
                                aplicacion.agendaCursor = 1;
                                cargarCursor();
                                return true;

                            case R.id.hoy:
                                aplicacion.agendaCursor = 2;
                                cargarCursor();
                                return true;

                            case R.id.mannana:
                                aplicacion.agendaCursor = 3;
                                cargarCursor();
                                return true;

                            case R.id.proximas:
                                aplicacion.agendaCursor = 4;
                                cargarCursor();
                                return true;

                            case R.id.todo:
                                aplicacion.agendaCursor = 5;
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