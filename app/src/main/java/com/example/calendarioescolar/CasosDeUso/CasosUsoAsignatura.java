package com.example.calendarioescolar.CasosDeUso;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;

import com.example.calendarioescolar.Modelo.AsignaturasBD;
import com.example.calendarioescolar.Presentacion.EditarHorarioActivity;
import com.example.calendarioescolar.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CasosUsoAsignatura {
    private AsignaturasBD asBD;
    private Activity actividad;

    public CasosUsoAsignatura(Activity actividad, AsignaturasBD asBD) {
        this.actividad = actividad;
        this.asBD = asBD;
    }

    public ArrayList<String> arrayAsignaturas(ArrayList<String> lista) {
        Cursor cursor = asBD.extraeCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lista.add(cursor.getString(1)); //add the item
            cursor.moveToNext();
        }
        return lista;
    }

    public String nombreAsignatura(int pos) {
        return asBD.elemento(pos);
    }

    public void dialogoAnnadirAsignatura() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
        builder.setTitle("Introducir nombre de la asignatura");

        final EditText input = new EditText(actividad);
        TextInputLayout textInputLayout = new TextInputLayout(actividad);

        textInputLayout.addView(input);
        textInputLayout.setPadding(60, 10, 60, 10);
        builder.setView(textInputLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aux = input.getText().toString();
                if (aux.length() > 0) {
                    asBD.annade(aux);

                    Cursor cursAux = asBD.extraeCursor();
                    cursAux.moveToLast();
                    TextInputLayout asignatura = actividad.findViewById(R.id.asignaturae);
                    asignatura.getEditText().setText(cursAux.getString(1));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }
    public void borrar(int _id){
        Intent i = new Intent();
        i.putExtra("idx", _id);
        actividad.setResult(EditarHorarioActivity.RESULT_OK_DELETE, i);
        actividad.finish();
    }
}
