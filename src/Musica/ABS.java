/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Musica;

import java.io.Serializable;

/**
 *
 * @author croge
 */
public abstract class ABS implements INTERFACE_Repro, Serializable {

    private static final long serialVersionUID = 2L;

    protected String titulo;
    protected String rutaArchivo;

    public ABS(String titulo, String rutaArchivo) {
        this.titulo = titulo;
        this.rutaArchivo = rutaArchivo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public abstract String getInfo();

    @Override
    public abstract void play();

    @Override
    public abstract void pause();

    @Override
    public abstract void stop();
}