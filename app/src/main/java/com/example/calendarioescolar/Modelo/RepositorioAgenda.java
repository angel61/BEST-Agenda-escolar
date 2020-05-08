package com.example.calendarioescolar.Modelo;

public interface RepositorioAgenda {

    agenda_object elemento(int id);

    void annade(agenda_object agendaobject);

    int nuevo(int tipo);

    void borrar(int id);

    int tamanno();

    void actualiza(int id, agenda_object agendaobject);
}