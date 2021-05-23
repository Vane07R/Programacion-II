package com.ugb.miprimercalculadora;

public class menu {
    String idmenu;
    String rev;
    String nombremenu;
    String descipcionmenu ;
    String espera;
    String precio;
    String mesa;
    String bebida;
    String postre;
    String urlfoto;
    String urlvideo;

    public menu(String idmenu, String rev, String nombremenu, String descipcionmenu, String espera, String precio,String mesa,String bebida, String postre, String urlfoto, String urlvideo) {
        this.idmenu = idmenu;
        this.rev = rev;
        this.nombremenu = nombremenu;
        this.descipcionmenu = descipcionmenu;
        this.espera = espera;
        this.precio = precio;
        this.mesa = mesa;
        this.bebida = bebida;
        this.postre = postre;
        this.urlfoto = urlfoto;
        this.urlvideo = urlvideo;
    }

    public String getIdmenu() {
        return idmenu;
    }

    public void setIdmenu(String idmenu) {
        this.idmenu = idmenu;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNombre() {
        return nombremenu;
    }

    public void setNombre(String nombre) {
        this.nombremenu = nombre;
    }

    public String getDescipcion() {
        return descipcionmenu;
    }

    public void setDescipcionmenu(String descipcionmenu) {
        this.descipcionmenu = descipcionmenu;
    }

    public String getEspera() {
        return espera;
    }

    public void setEspera(String espera) {
        this.espera = espera;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getBebida() {
        return bebida;
    }

    public void setBebida(String bebida) {
        this.bebida = bebida;
    }

    public String getPostre() {
        return postre;
    }

    public void setPostre(String postre) {
        this.postre = postre;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getUrlvideo() {
        return urlvideo;
    }

    public void setUrlvideo(String urlvideo) {
        this.urlvideo = urlvideo;
    }
}
