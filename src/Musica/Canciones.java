/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Musica;

/**
 *
 * @author croge
 */
public class Canciones extends ABS {

    private static final long serialVersionUID = 1L;

    private String artista;
    private String duracion;
    private ENUM_Genero genero;   
    private String rutaImagen;

    public Canciones(String nombreCancion, String artista, String duracion, ENUM_Genero genero, String rutaArchivo, String rutaImagen) {
        super(nombreCancion, rutaArchivo); 
        this.artista    = artista;
        this.duracion   = duracion;
        this.genero     = genero;
        this.rutaImagen = rutaImagen;
    }
    public String getNombreCancion() {
        return titulo; 
    }

    public void setNombreCancion(String nombre) {
        this.titulo = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public ENUM_Genero getGenero() {
        return genero;
    }

    public void setGenero(ENUM_Genero genero) {
        this.genero = genero;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }
    @Override
    public String getInfo() {
        return titulo + " - " + artista + " [" + genero.getEtiqueta() + "] (" + duracion + ")";
    }

    @Override
    public void play() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void stop() {
    }

    @Override
    public String toString() {
        return artista + " - " + titulo;
    }
}
