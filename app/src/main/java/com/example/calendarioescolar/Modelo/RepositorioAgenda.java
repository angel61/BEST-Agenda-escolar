package com.example.calendarioescolar.Modelo;

public interface RepositorioAgenda {

    agenda_object elemento(int id);

    void annade(agenda_object agendaobject);

    int nuevo();

    void borrar(int id);

    int tamanno();

    void actualiza(int id, agenda_object agendaobject);
}