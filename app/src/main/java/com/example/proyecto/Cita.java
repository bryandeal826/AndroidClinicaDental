package com.example.proyecto;

import java.io.Serializable;

public class Cita implements Serializable {
    private int id_cita;
    private String nombre_pa;
    private String apellidos_pa;
    private String nombre_de;
    private String apellidos_de;
    private String fecha;
    private String dia;
    private String hora_inicio;
    private String hora_final;
    private String estado;

    public Cita() {
    }

    public Cita(int id_cita, String nombre_pa, String apellidos_pa, String nombre_de, String apellidos_de, String fecha, String dia, String hora_inicio, String hora_final, String estado) {
        this.id_cita = id_cita;
        this.nombre_pa = nombre_pa;
        this.apellidos_pa = apellidos_pa;
        this.nombre_de = nombre_de;
        this.apellidos_de = apellidos_de;
        this.fecha = fecha;
        this.dia = dia;
        this.hora_inicio = hora_inicio;
        this.hora_final = hora_final;
        this.estado = estado;
    }

    public int getId_cita() {
        return id_cita;
    }

    public void setId_cita(int id_cita) {
        this.id_cita = id_cita;
    }

    public String getNombre_pa() {
        return nombre_pa;
    }

    public void setNombre_pa(String nombre_pa) {
        this.nombre_pa = nombre_pa;
    }

    public String getApellidos_pa() {
        return apellidos_pa;
    }

    public void setApellidos_pa(String apellidos_pa) {
        this.apellidos_pa = apellidos_pa;
    }

    public String getNombre_de() {
        return nombre_de;
    }

    public void setNombre_de(String nombre_de) {
        this.nombre_de = nombre_de;
    }

    public String getApellidos_de() {
        return apellidos_de;
    }

    public void setApellidos_de(String apellidos_de) {
        this.apellidos_de = apellidos_de;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String DevolverFormatoFecha(){
        String formato="";
        String mes="";
        int dias = Integer.parseInt(fecha.substring(0,2));
        int meses = Integer.parseInt(fecha.substring(3,5));
        int anio = Integer.parseInt(fecha.substring(6,10));
        if(meses==1){mes="enero";}
        if(meses==2){mes="febrero";}
        if(meses==3){mes="marzo";}
        if(meses==4){mes="abril";}
        if(meses==5){mes="mayo";}
        if(meses==6){mes="junio";}
        if(meses==7){mes="julio";}
        if(meses==8){mes="agosto";}
        if(meses==9){mes="septiembre";}
        if(meses==10){mes="octubre";}
        if(meses==11){mes="noviembre";}
        if(meses==12){mes="diciembre";}
        formato = dia+", "+dias+" de "+mes+" de "+anio;
        return formato;
    }
}
