package com.example.proyecto;

import java.io.Serializable;

public class HorarioDentista implements Serializable {
    private int id_dentista;
    private String foto;
    private String nombres;
    private String apellidos;
    private String correo;
    private String dia;
    private String hora_inicio;
    private String hora_fin;
    private int id_dia;
    private int id_hora;

    public HorarioDentista() {
    }

    public HorarioDentista(int id_dentista, String foto, String nombres, String apellidos, String correo, int id_dia, int id_hora) {
        this.id_dentista = id_dentista;
        this.foto = foto;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.id_dia = id_dia;
        this.id_hora = id_hora;
    }

    public HorarioDentista(int id_dentista, String foto, String nombres, String apellidos, String correo, String dia, String hora_inicio, String hora_fin, int id_dia, int id_hora) {
        this.id_dentista = id_dentista;
        this.foto = foto;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.dia = dia;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.id_dia = id_dia;
        this.id_hora = id_hora;
    }

    public int getId_dentista() {
        return id_dentista;
    }

    public void setId_dentista(int id_dentista) {
        this.id_dentista = id_dentista;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getId_dia() {
        return id_dia;
    }

    public void setId_dia(int id_dia) {
        this.id_dia = id_dia;
    }

    public int getId_hora() {
        return id_hora;
    }

    public void setId_hora(int id_hora) {
        this.id_hora = id_hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }
}
