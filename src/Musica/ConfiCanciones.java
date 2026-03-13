/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Musica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author croge
 */
public class ConfiCanciones {

    private ArrayList<Canciones> listaCanciones;

    private static final String CARPETA_DATOS     = "datos";
    private static final String CARPETA_CANCIONES = "canciones";
    private static final String CARPETA_PORTADAS  = "portadas";
    private static final String RUTA_CANCIONES = CARPETA_DATOS + File.separator + "canciones.dat";

    
    public ConfiCanciones() {
        listaCanciones = new ArrayList<>();
        crearCarpetaDatos(); 
        cargarArchivo();
    }

    private void crearCarpetaDatos() {
        File carpetaDatos = new File(CARPETA_DATOS);
        File carpetaCanciones = new File(CARPETA_CANCIONES);
        File carpetaPortadas  = new File(CARPETA_PORTADAS);
        
        if (!carpetaDatos.exists()) {
            carpetaDatos.mkdir();
        }
        if (!carpetaCanciones.exists()) {
            carpetaCanciones.mkdir();
        }
        if (!carpetaPortadas.exists()) {
            carpetaPortadas.mkdir();
        }
    }

    public void agregarCancion(Canciones c) {
        listaCanciones.add(c);
        guardarArchivo();
    }

    public void eliminarCancion(int index) {
        if (index >= 0 && index < listaCanciones.size()) {
            listaCanciones.remove(index);
            guardarArchivo();
        }
    }

    public ArrayList<Canciones> obtenerLista() {
        return listaCanciones;
    }

    public Canciones buscarPorNombre(String nombre, int indice) {
        if (indice >= listaCanciones.size()) {
            return null;
        }

        Canciones actual = listaCanciones.get(indice);

        if (actual.getNombreCancion().equalsIgnoreCase(nombre)) {
            return actual;
        }

        return buscarPorNombre(nombre, indice + 1);
    }

    public void guardarArchivo() {
        try {
            FileOutputStream fos = new FileOutputStream(RUTA_CANCIONES);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(listaCanciones);

            oos.close();
            fos.close();

        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void cargarArchivo() {
        File archivo = new File(RUTA_CANCIONES);

        if (!archivo.exists()) {
            return; 
        }

        try {
            FileInputStream fis = new FileInputStream(RUTA_CANCIONES);
            ObjectInputStream ois = new ObjectInputStream(fis);

            listaCanciones = (ArrayList<Canciones>) ois.readObject();

            ois.close();
            fis.close();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
            listaCanciones = new ArrayList<>();
        }
    }
}

