package com.example.calendarioescolar.Modelo;

/**
 * Interfaz RepositorioAgenda
 *
 * @author Angel Lopez Palacios
 * @version 1.0
 */
public interface RepositorioAgenda {

    agenda_object elemento(int id);

    int nuevo(int tipo);

    void borrar(int id);

    int tamanno();

    void actualiza(int id, agenda_object agendaobject);
}