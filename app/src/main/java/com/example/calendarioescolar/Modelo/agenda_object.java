package com.example.calendarioescolar.Modelo;


public class agenda_object {
    private String titulo;
    private String comentario;
    private long fecha;
    private TipoAgenda tipoAg;
    private String Asig;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public agenda_object(String titulo, String comentario, long fecha, TipoAgenda tipoAg, String Asig) {
        this.titulo = titulo;
        this.comentario = comentario;
        this.fecha = fecha;
        this.tipoAg = tipoAg;
        this.Asig = Asig;
    }
    public agenda_object(String titulo, String comentario, long fecha, TipoAgenda tipoAg) {
        this.titulo = titulo;
        this.comentario = comentario;
        this.fecha = fecha;
        this.tipoAg = tipoAg;
    }
    public agenda_object() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public TipoAgenda getTipoAg() {
        return tipoAg;
    }

    public void setTipoAg(TipoAgenda tipoAg) {
        this.tipoAg = tipoAg;
    }

    public String getAsig() {
        return Asig;
    }

    public void setAsig(String asig) {
        Asig = asig;
    }
}
