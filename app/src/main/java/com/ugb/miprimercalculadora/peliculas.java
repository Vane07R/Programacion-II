package com.ugb.miprimercalculadora;

class peliculasd{
    String idpelicula;
    String rev;
    String titulo;
    String sinopsis;
    String duracion;
    String precio;
    String urlfoto;
    String urltrailer;

    public peliculasd(String idpelicula, String rev, String titulo, String sinopsis, String duracion, String precio, String urlfoto, String urltrailer) {
        this.idpelicula = idpelicula;
        this.rev = rev;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.precio = precio;
        this.urlfoto = urlfoto;
        this.urltrailer = urltrailer;
    }

    public String getIdpelicula() {
        return idpelicula;
    }

    public void setIdpelicula(String idpelicula) {
        this.idpelicula = idpelicula;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getUrltrailer() {
        return urltrailer;
    }

    public void setUrltriler(String urltrailer) {
        this.urltrailer = urltrailer;
    }
}