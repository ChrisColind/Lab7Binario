/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Musica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author croge
 */
public class Gui extends JFrame {
    private ConfiCanciones gestor;

    private DefaultListModel<Canciones> modeloLista;
    private JList<Canciones> listaCanciones;

    private JLabel labelImagen;
    private JLabel labelInfo;

    private JButton btnPlay, btnPause, btnStop, btnAdd, btnRemove;
    private Clip clipAudio;
    private boolean pausado = false;
    private long posicionPausa = 0;
    private File ultimaCarpeta = new File(System.getProperty("user.home"));

    public Gui() {
        gestor = new ConfiCanciones();
        configurarVentana();
        inicializarComponentes();
        cargarCancionesEnLista();
    }

    private void configurarVentana() {
        setTitle("Reproductor de Musica");
        setSize(720, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(18, 18, 18));
    }

    private void inicializarComponentes() {
        modeloLista = new DefaultListModel<>();
        listaCanciones = new JList<>(modeloLista);
        listaCanciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCanciones.setBackground(new Color(18, 18, 18));
        listaCanciones.setForeground(new Color(255, 255, 255));
        listaCanciones.setFont(new Font("Arial", Font.PLAIN, 13));
        listaCanciones.setSelectionForeground(new Color(185, 0, 0));

        listaCanciones.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                mostrarImagenSeleccionada();
                mostrarInfoSeleccionada();
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaCanciones);
        scrollLista.setPreferredSize(new Dimension(290, 0));
        scrollLista.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(40, 40, 40)),
            "Lista de Canciones",
            0, 0,
            new Font("Arial", Font.BOLD, 12),
            new Color(179, 179, 179)
        ));
        scrollLista.getViewport().setBackground(new Color(18, 18, 18));
        add(scrollLista, BorderLayout.WEST);

        labelImagen = new JLabel();
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        labelImagen.setVerticalAlignment(SwingConstants.CENTER);
        labelImagen.setOpaque(true);
        labelImagen.setBackground(new Color(40, 40, 40));
        labelImagen.setForeground(new Color(179, 179, 179));
        labelImagen.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        labelImagen.setText("Sin imagen");
        labelImagen.setForeground(new Color(100, 100, 100));
        labelImagen.setFont(new Font("Arial", Font.ITALIC, 13));

        labelInfo = new JLabel("Selecciona una cancion");
        labelInfo.setForeground(new Color(160, 160, 160));
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelInfo.setHorizontalAlignment(SwingConstants.CENTER);
        labelInfo.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBackground(new Color(18, 18, 18));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDerecho.add(labelImagen, BorderLayout.CENTER);
        panelDerecho.add(labelInfo, BorderLayout.SOUTH);
        add(panelDerecho, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        panelBotones.setBackground(new Color(9, 9, 9));

        btnPlay   = crearBoton("▶  Play",    new Color(20, 90, 20));
        btnPause  = crearBoton("⏸  Pause",   new Color(100, 75, 0));
        btnStop   = crearBoton("⏹  Stop",    new Color(110, 20, 20));
        btnAdd    = crearBoton("＋  Agregar", new Color(15, 60, 110));
        btnRemove = crearBoton("✕  Eliminar", new Color(80, 20, 20));

        btnPlay.addActionListener(e -> reproducirCancion());
        btnPause.addActionListener(e -> pausarCancion());
        btnStop.addActionListener(e -> detenerCancion());
        btnAdd.addActionListener(e -> agregarCancion());
        btnRemove.addActionListener(e -> eliminarCancion());

        panelBotones.add(btnPlay);
        panelBotones.add(btnPause);
        panelBotones.add(btnStop);
        panelBotones.add(btnAdd);
        panelBotones.add(btnRemove);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(185, 0, 0));
        btn.setForeground(new Color(255, 255, 255));
        btn.setFont(new Font("Monospaced", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void cargarCancionesEnLista() {
        modeloLista.clear();
        for (Canciones c : gestor.obtenerLista()) {
            modeloLista.addElement(c);
        }
    }

    private void mostrarImagenSeleccionada() {
        Canciones seleccionada = listaCanciones.getSelectedValue();

        if (seleccionada == null) {
            labelImagen.setIcon(null);
            labelImagen.setText("Sin imagen");
            return;
        }

        String ruta = seleccionada.getRutaImagen();
        System.out.println("Ruta imagen: " + ruta);
        if (ruta == null || ruta.isEmpty()) {
            labelImagen.setIcon(null);
            labelImagen.setText("Sin imagen");
            return;
        }

        File archivoImg = new File(ruta);
        if (!archivoImg.exists()) {
            labelImagen.setIcon(null);
            labelImagen.setText("Imagen no encontrada");
            return;
        }

        ImageIcon icono = new ImageIcon(ruta);
        Image escalada = icono.getImage().getScaledInstance(255, 255, Image.SCALE_SMOOTH);
        labelImagen.setIcon(new ImageIcon(escalada));
        labelImagen.setText("");
    }

    private void mostrarInfoSeleccionada() {
        ABS elemento = listaCanciones.getSelectedValue();

        if (elemento == null) {
            labelInfo.setText("Selecciona una cancion");
            return;
        }

        labelInfo.setText(elemento.getInfo());
    }

    //====================REPRODUCCION=========================================

    private void reproducirCancion() {
        Canciones seleccionada = listaCanciones.getSelectedValue();

        if (seleccionada == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una cancion primero.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ruta = seleccionada.getRutaArchivo();
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this,
                "No se encontro el archivo:\n" + ruta,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ruta.toLowerCase().endsWith(".wav")) {
            JOptionPane.showMessageDialog(this,
                "Solo se pueden reproducir archivos .WAV",
                "Formato no soportado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        detenerCancion();
        pausado = false;
        posicionPausa = 0;

        try {
            AudioInputStream streamAudio = AudioSystem.getAudioInputStream(archivo);
            clipAudio = AudioSystem.getClip();
            clipAudio.open(streamAudio);
            clipAudio.start();

            labelInfo.setText(seleccionada.getInfo());

        } catch (UnsupportedAudioFileException e) {
            JOptionPane.showMessageDialog(this, "Formato de audio no soportado.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (LineUnavailableException e) {
            JOptionPane.showMessageDialog(this, "Linea de audio no disponible.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de audio.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void pausarCancion() {
        if (clipAudio == null) return;

        if (!pausado) {
            posicionPausa = clipAudio.getMicrosecondPosition();
            clipAudio.stop();
            pausado = true;
            labelInfo.setText("Pausado");
        } else {
            clipAudio.setMicrosecondPosition(posicionPausa);
            clipAudio.start();
            pausado = false;
            labelInfo.setText("Reproduciendo");
        }
    }

    private void detenerCancion() {
        if (clipAudio != null) {
            clipAudio.stop();
            clipAudio.close();
            clipAudio = null;
        }
        pausado = false;
        posicionPausa = 0;
    }

    //====================GESTION DE CANCIONES==================================

    private void agregarCancion() {
        File carpetaDescargas = new File(System.getProperty("user.home") + File.separator + "Downloads");
        JTextField campoNombre   = new JTextField();
        JTextField campoArtista  = new JTextField();
        JComboBox<ENUM_Genero> comboGenero = new JComboBox<>(ENUM_Genero.values());

        JButton btnCancion = new JButton("Seleccionar cancion...");
        JButton btnImagen  = new JButton("Seleccionar imagen...");
        JLabel labelCancion = new JLabel("Ninguna cancion seleccionada");
        JLabel labelImagen  = new JLabel("Ninguna imagen seleccionada");

        final String[] rutaAudio  = {""};
        final String[] rutaImagen = {""};

        btnCancion.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(carpetaDescargas);
            fc.setFileFilter(new FileNameExtensionFilter("Archivos WAV", "wav"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaAudio[0] = fc.getSelectedFile().getAbsolutePath();
                labelCancion.setText(fc.getSelectedFile().getName());
            }
        });

        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser(carpetaDescargas);
            fc.setFileFilter(new FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                rutaImagen[0] = fc.getSelectedFile().getAbsolutePath();
                labelImagen.setText(fc.getSelectedFile().getName());
            }
        });

        JPanel panel = new JPanel(new GridLayout(10, 1, 5, 5));
        panel.add(new JLabel("Nombre de la cancion:"));
        panel.add(campoNombre);
        panel.add(new JLabel("Artista:"));
        panel.add(campoArtista);
        panel.add(new JLabel("Genero:"));
        panel.add(comboGenero);
        panel.add(btnCancion);
        panel.add(labelCancion);
        panel.add(btnImagen);
        panel.add(labelImagen);

        int resultado = JOptionPane.showConfirmDialog(this, panel,
            "Agregar cancion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado != JOptionPane.OK_OPTION) return;

        String nombre  = campoNombre.getText().trim();
        String artista = campoArtista.getText().trim();

        if (nombre.isEmpty() || artista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y el artista no pueden estar vacios.");
            return;
        }

        if (rutaAudio[0].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una cancion.");
            return;
        }

        String duracion      = calcularDuracion(rutaAudio[0]);
        ENUM_Genero generoElegido = (ENUM_Genero) comboGenero.getSelectedItem();

        Canciones nueva = new Canciones(nombre, artista, duracion, generoElegido, rutaAudio[0], rutaImagen[0]);
        gestor.agregarCancion(nueva);
        modeloLista.addElement(nueva);
    }
    
    private String calcularDuracion(String ruta) {
        try {
            File archivo = new File(ruta);
            AudioInputStream audio = AudioSystem.getAudioInputStream(archivo);
            AudioFormat formato = audio.getFormat();
            long frames = audio.getFrameLength();
            double segundosTotales = frames / formato.getFrameRate();

            int minutos = (int) (segundosTotales / 60);
            int segundos = (int) (segundosTotales % 60);

            audio.close();
            return minutos + ":" + String.format("%02d", segundos);

        } catch (Exception e) {
            return "Desconocida";
        }
    }
    private void eliminarCancion() {
        int indice = listaCanciones.getSelectedIndex();

        if (indice < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una canción para eliminar.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Seguro que deseas eliminar esta canción?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) return;

        detenerCancion();
        gestor.eliminarCancion(indice);
        modeloLista.remove(indice);
        labelImagen.setIcon(null);
        labelImagen.setText("Sin imagen");
        labelInfo.setText("Selecciona una canción");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("E   rror de look and feel del sistema.");
        }

        SwingUtilities.invokeLater(() -> {
            Gui ventana = new Gui();
            ventana.setVisible(true);
        });
    }
}
