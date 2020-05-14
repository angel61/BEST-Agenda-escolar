package com.example.calendarioescolar.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.calendarioescolar.Modelo.RepositorioAgenda;
import com.example.calendarioescolar.Modelo.agenda_object;
import com.example.calendarioescolar.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AdaptadorAgenda extends RecyclerView.Adapter<AdaptadorAgenda.ViewHolder> {
    protected View.OnClickListener onClickListener;


    protected RepositorioAgenda agenda;
    protected int viewId;

    public AdaptadorAgenda(RepositorioAgenda agenda, int view) {
        this.agenda = agenda;
        this.viewId=view;
    }


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

            if(viewId!=R.layout.elemento_agenda_home) {
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
                if (fecInt == ayer) {
                    textoFecha = "Ayer a las " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);

                } else if (fecInt == mannana) {
                    textoFecha = "Mañana a las " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);

                } else if (fecInt == hoy) {
                    textoFecha = "Hoy a las " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
                } else {
                    textoFecha = new SimpleDateFormat("dd-MM-yyyy").format(fec);
                }
                fecha.setText(textoFecha);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewId, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        agenda_object ag = agenda.elemento(posicion);
        holder.personaliza(ag);
    }

    @Override
    public int getItemCount() {
        return agenda.tamanno();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


}
