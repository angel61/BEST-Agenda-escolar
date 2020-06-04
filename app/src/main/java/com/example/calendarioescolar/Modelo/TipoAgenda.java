package com.example.calendarioescolar.Modelo;

import com.example.calendarioescolar.R;

/**
 * Listado de los tipos con su respectivo icono
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public enum TipoAgenda {
    RECORDATORIO("Recordatorio", R.drawable.recordatorio),
    EJERCICIOS("Ejercicios", R.drawable.deberes),
    TRABAJO("Trabajo", R.drawable.trabajo),
    EXAMEN("Examen", R.drawable.examen);

    private final String texto;
    private final int recurso;

    TipoAgenda(String texto, int recurso) {
        this.texto = texto;
        this.recurso = recurso;
    }

    public String getTexto() {
        return texto;
    }

    public int getRecurso() {
        return recurso;
    }

    public static String[] getNombres() {
        String[] resultado = new String[TipoAgenda.values().length];
        for (TipoAgenda tipo : TipoAgenda.values()) {
            resultado[tipo.ordinal()] = tipo.texto;
        }
        return resultado;
    }
}