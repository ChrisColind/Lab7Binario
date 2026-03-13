/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Musica;

/**
 *
 * @author croge
 */
public enum ENUM_Genero {

    POP("Pop"),
    ROCK("Rock"),
    JAZZ("Jazz"),
    CLASICA("Clásica"),
    ELECTRONICA("Electrónica"),
    REGGAETON("Reggaetón"),
    HIPHOP("Hip-Hop"),
    METAL("Metal"),
    CUMBIA("Cumbia"),
    OTRO("Otro");

    private final String etiqueta;

    ENUM_Genero(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
