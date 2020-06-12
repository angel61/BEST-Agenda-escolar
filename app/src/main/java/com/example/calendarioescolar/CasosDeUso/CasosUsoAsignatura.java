package com.example.calendarioescolar.CasosDeUso;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.calendarioescolar.Aplicacion;
import com.example.calendarioescolar.Modelo.AsignaturasBD;
import com.example.calendarioescolar.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

/**
 * Clase encargada de cumplir los casos de uso del modulo de asignatura
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public class CasosUsoAsignatura {
    private AsignaturasBD asBD;
    private Activity actividad;
    private Aplicacion aplicacion;

    /**
     * Constructor de la clase que obtiene como parametros la actividad y las clase que controla la base de datos
     *
     * @param actividad Actividad que va a ser manejada
     * @param asBD      Clase que controla la base de datos
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public CasosUsoAsignatura(Activity actividad, AsignaturasBD asBD) {
        this.actividad = actividad;
        this.asBD = asBD;
        aplicacion = (Aplicacion) actividad.getApplication();
    }


    /**
     * Se encarga de rellenar el ArrayList pasado como parametro
     *
     * @param lista ArrayList que contiene el nombre de cada asignatura
     * @return ArrayList<String>
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public ArrayList<String> arrayAsignaturas(ArrayList<String> lista) {
        Cursor cursor = asBD.extraeCursor();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lista.add(cursor.getString(1)); //add the item
            cursor.moveToNext();
        }
        return lista;
    }


    /**
     * Se encarga de obtener como parametro la posicion en la cual se va a obtener el nombre de la asignatura
     *
     * @param pos Posicion de la asignatura de la cual se quiere obtener el nombre
     * @return String
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public String nombreAsignatura(int pos) {
        return asBD.elemento(pos);
    }


    /**
     * Muestra un dialogo para añadir una asignatura
     *
     * @author Angel Lopez Palacios
     * @version 1.0
     */
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
                String aux = input.getText().toString().trim();
                if (aux.length() > 0) {
                    int i = asBD.annade(aux);

                    if (i == -1) {
                        new AlertDialog.Builder(actividad)
                                .setTitle("Ya existe una asignatura con ese nombre.")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    } else {
                        Cursor cursAux = asBD.extraeCursor();
                        cursAux.moveToLast();
                        TextInputLayout asignatura = actividad.findViewById(R.id.asignaturae);
                        asignatura.getEditText().setText(cursAux.getString(1));
                    }
                } else {
                    new AlertDialog.Builder(actividad)
                            .setTitle("El nombre de la asignatura no puede quedarse en blanco.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
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


    /**
     * Se encarga de eliminar la asignatura que se encuentra en la posicion que se consiguio como parametro
     *
     * @param pos Posicion de la asignatura de la cual se quiere eliminar
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void eliminarAsig(int pos) {
        int id = idPosicion(pos);
        asBD.borrar(id);
    }


    /**
     * Se encarga de obtener el id de la asignatura que corresponde con la posicion
     *
     * @param posicion Posicion de la asignatura de la cual se quiere obtener el ID
     * @return int
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public int idPosicion(int posicion) {
        Cursor cursor = asBD.extraeCursor();
        cursor.moveToPosition(posicion);
        return cursor.getInt(0);
    }


    /**
     * Se encarga de mostrar un dialogo capaz de gestionar la asignaturas
     *
     * @param asignatura El campo de texto que va a ser rellenado
     * @author Angel Lopez Palacios
     * @version 1.0
     */
    public void dialogoAsignatura(final TextInputLayout asignatura) {
        ArrayList<String> array = new ArrayList<String>();

        array = arrayAsignaturas(array);

        final String[] a = array.toArray(new String[0]);
        final int[] aux = new int[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
        builder.setTitle("Seleccionar una asignatura")
                .setSingleChoiceItems(a, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        aux[0] = which;
                    }
                });
        builder.setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (a.length > 0) {
                    asignatura.getEditText().setText(a[aux[0]]);
                }
            }
        });
        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (a.length > 0) {
                    eliminarAsig(aux[0]);
                    asignatura.getEditText().setText("");
                }
            }
        });
        builder.setNeutralButton("Añadir", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAnnadirAsignatura();
            }
        });
        builder.show();
    }
}
