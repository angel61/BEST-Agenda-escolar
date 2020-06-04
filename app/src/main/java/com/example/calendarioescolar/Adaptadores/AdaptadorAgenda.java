package com.example.calendarioescolar.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarioescolar.Modelo.AgendaBD;
import com.example.calendarioescolar.Modelo.agenda_object;
import com.example.calendarioescolar.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase para adaptar la información de los elementos de la agenda para poder mostrarla en el RecycleView
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 * @see androidx.recyclerview.widget.RecyclerView.Adapter
 */
public class AdaptadorAgenda extends RecyclerView.Adapter<AdaptadorAgenda.ViewHolder> {
    protected View.OnClickListener onClickListener;


    protected AgendaBD agenda;
    protected int viewId;

    /**
     * Constructor de la clase
     *
     * @param agenda clase para trabajar en la tabla de agenda
     * @author Angel Lopez Palacios
     * @version 1.0
     * @see AgendaBD
     */
    public AdaptadorAgenda(AgendaBD agenda, int view) {
        this.agenda = agenda;
        this.viewId = view;
    }


    /**
     * Instancia de elementos a mostrar en el RecycleView
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, comentario, fecha;
        public ImageView foto;


        public ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.elTitulo);
            comentario = itemView.findViewById(R.id.elNota_Asig);
            foto = itemView.findViewById(R.id.elFoto);
            fecha = itemView.findViewById(R.id.elFecha);
        }

        public void personaliza(agenda_object agendaOb) {
            titulo.setText(agendaOb.getTitulo());

            int id = R.drawable.recordatorio;
            switch (agendaOb.getTipoAg()) {
                case EJERCICIOS:
                    id = R.drawable.deberes;
                    break;
                case EXAMEN:
                    id = R.drawable.examen;
                    break;
                case TRABAJO:
                    id = R.drawable.trabajo;
                    break;
            }
            foto.setImageResource(id);
            foto.setScaleType(ImageView.ScaleType.FIT_END);

            if (viewId != R.layout.elemento_agenda_home) {
                String texto = agendaOb.getComentario();
                if (texto.length() > 35) {
                    texto = texto.substring(0, 32) + "...";
                }
                comentario.setText(texto);
                if (agendaOb.getAsig().length() > 0) {
                    comentario.setText(agendaOb.getAsig());
                }

                Date fec = new Date(agendaOb.getFecha());
                Date actual = new Date(System.currentTimeMillis());
                Calendar cal = Calendar.getInstance();
                cal.setTime(actual);
                int hoy = cal.get(Calendar.DAY_OF_MONTH);
                cal.add(Calendar.DAY_OF_YEAR, -1);
                int ayer = cal.get(Calendar.DAY_OF_MONTH);
                cal.add(Calendar.DAY_OF_YEAR, +2);
                int mannana = cal.get(Calendar.DAY_OF_MONTH);
                cal.setTime(fec);
                int fecInt = cal.get(Calendar.DAY_OF_MONTH);

                cal.setTime(fec);
                String textoFecha;
                SimpleDateFormat sdm = new SimpleDateFormat("HH:mm");
                if (fecInt == ayer) {
                    textoFecha = "Ayer a las " + sdm.format(cal.getTime());

                } else if (fecInt == mannana) {
                    textoFecha = "Mañana a las " + sdm.format(cal.getTime());

                } else if (fecInt == hoy) {
                    textoFecha = "Hoy a las " + sdm.format(cal.getTime());
                } else {
                    textoFecha = new SimpleDateFormat("dd-MM-yyyy").format(fec);
                }
                fecha.setText(textoFecha);
            }
        }
    }

    /**
     * Crea un ViewHolder e inicializa los campos siguiendo el diseño de elemento_agenda.xml
     *
     * @param parent
     * @param viewType
     * @return ViewHolder
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewId, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }


    /**
     * Método que actualiza los ViewHolder a partir de la posicion del elemento
     *
     * @param holder
     * @param posicion
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        agenda_object ag = agenda.elemento(posicion);
        holder.personaliza(ag);
    }


    /**
     * Devuelve el número total de elementos en el conjunto de datos
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    @Override
    public int getItemCount() {
        return agenda.tamanno();
    }
}
