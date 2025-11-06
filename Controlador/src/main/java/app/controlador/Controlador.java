/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app.controlador;

/**
 *
 * @author jimmy
 */

import app.interfazgrafica.Main;
import app.filtropluginapi.Filtro;
import app.filtropluginapi.FiltroManager;
import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Controlador {
    
    private Main mainFrame;
    private List<Filtro> filtros;  // Lista de filtros cargados
    private ButtonGroup grupoFiltros; // Grupo de botones de opción
    private JPanel panelFiltros; // Panel donde se agregan los radio buttons
    private File archivo;
    private Image imagenOriginal;
    
    public Controlador(Main mainFrame) {
        this.mainFrame = mainFrame;
        this.filtros = new ArrayList<>();
        this.grupoFiltros = new ButtonGroup();
        this.panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS)); // Layout vertical
        mainFrame.getjTextArea1().setEditable(false);  // Para que no se pueda escribir
        mainFrame.getjTextArea1().setLineWrap(true);  // Habilitar salto de línea
        mainFrame.getjTextArea1().setWrapStyleWord(true);
        mainFrame.getjTextArea1().setOpaque(false);  
        mainFrame.getjTextArea1().setBorder(null);
        mainFrame.getjScrollPane1().setViewportView(panelFiltros); // Agregar panel al scroll
        
        agregarEventos();
    }
    
    
    private void agregarEventos() {
        mainFrame.getjButton1().addActionListener(e -> seleccionarImagen());
        mainFrame.getjButton2().addActionListener(e -> cargarFiltro());  // Botón para cargar un filtro
        mainFrame.getjButton3().addActionListener(e -> ejecutarFiltro()); // Botón para aplicar filtro
    }
    
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes JPEG", "jpg", "jpeg"));

        int resultado = fileChooser.showOpenDialog(mainFrame);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivo = fileChooser.getSelectedFile();
            // Cargar y almacenar la imagen original
            imagenOriginal = new ImageIcon(archivo.getAbsolutePath()).getImage();
            // Mostrar la imagen escalada (sin modificar la lógica de escalado actual)
            mostrarImagen(archivo.getAbsolutePath());
        }
    }
    
    private void mostrarImagen(String ruta) {
    ImageIcon icono = new ImageIcon(ruta);
    imagenOriginal = icono.getImage();

    // Obtener dimensiones del JLabel
    int labelWidth = mainFrame.getjLabel4().getWidth();
    int labelHeight = mainFrame.getjLabel4().getHeight();

    // Obtener dimensiones originales de la imagen
    int imageWidth = imagenOriginal.getWidth(null);
    int imageHeight = imagenOriginal.getHeight(null);

    // Calcular la nueva escala manteniendo la relación de aspecto
    double escala = Math.min((double) labelWidth / imageWidth, (double) labelHeight / imageHeight);
    int newWidth = (int) (imageWidth * escala);
    int newHeight = (int) (imageHeight * escala);

    // Redimensionar la imagen manteniendo la proporción
    Image imagenEscalada = imagenOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

    // Mostrar la imagen escalada en el JLabel
    mainFrame.getjLabel4().setIcon(new ImageIcon(imagenEscalada));
    mainFrame.getjLabel4().setHorizontalAlignment(JLabel.CENTER);
    mainFrame.getjLabel4().setVerticalAlignment(JLabel.CENTER);
}

    
    private void cargarFiltro() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos JAR", "jar"));
        
        int resultado = fileChooser.showOpenDialog(mainFrame);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoJar = fileChooser.getSelectedFile();
            cargarFiltroDesdeJar(archivoJar);
        }
    }

    private void cargarFiltroDesdeJar(File archivoJar) {
        try {
            URL url = archivoJar.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, getClass().getClassLoader());

            // Leer el contenido del JAR
            try (JarFile jarFile = new JarFile(archivoJar)) {
                Enumeration<JarEntry> entradas = jarFile.entries();
                while (entradas.hasMoreElements()) {
                    JarEntry entrada = entradas.nextElement();
                    String nombreArchivo = entrada.getName();

                    // Verificar si es una clase
                    if (nombreArchivo.endsWith(".class")) {
                        String nombreClase = nombreArchivo.replace("/", ".").replace(".class", "");

                        try {
                            Class<?> clase = classLoader.loadClass(nombreClase);

                            // Verificar si la clase implementa la interfaz Filtro
                            if (Filtro.class.isAssignableFrom(clase) && !clase.isInterface()) {
                                Filtro filtro = (Filtro) clase.getDeclaredConstructor().newInstance();
                                
                                if (filtros.stream().anyMatch(f -> f.getNombre().equals(filtro.getNombre()))) {
                                mainFrame.getjTextArea1().setText("El filtro '" + filtro.getNombre() + "' ya está cargado.");
                                return;
                                }
                                
                                filtros.add(filtro);
                                mainFrame.getjTextArea1().setText("Filtro cargado: " + filtro.getNombre());
                                
                                // Agregarlo como opción en el JScrollPane
                                agregarFiltroALista(filtro);
                            }
                        } catch (ClassNotFoundException | NoClassDefFoundError e) {
                            mainFrame.getjTextArea1().setText("No se pudo cargar la clase: " + nombreClase);
                        }
                    }
                }
            }
        } catch (Exception e) {
        mainFrame.getjTextArea1().setText("Error al cargar el filtro: " + e.getMessage());
        }
    }

    private void agregarFiltroALista(Filtro filtro) {
    JRadioButton radioButton = new JRadioButton(filtro.getNombre());
    radioButton.putClientProperty("filtro", filtro);
    grupoFiltros.add(radioButton);
    panelFiltros.add(radioButton);
    panelFiltros.revalidate();
    panelFiltros.repaint();
}


private void ejecutarFiltro() {
        // Buscar qué radio button está seleccionado
        Image imagenFiltrada;
        for (int i = 0; i < panelFiltros.getComponentCount(); i++) {
            JRadioButton radioButton = (JRadioButton) panelFiltros.getComponent(i);
            if (radioButton.isSelected()) {
                Filtro filtroSeleccionado = filtros.get(i);
                ImageIcon imagenActual = (ImageIcon) mainFrame.getjLabel4().getIcon();
                if (imagenActual != null) {
                        //System.out.print(filtroSeleccionado.getNombre());
                        imagenFiltrada = filtroSeleccionado.aplicarFiltro(imagenActual.getImage());
                    if (imagenFiltrada==null) {
                        FiltroManager.setArchivoActual(archivo);
                        imagenFiltrada = filtroSeleccionado.aplicarFiltro(imagenOriginal);
                        mainFrame.getjTextArea1().setText("Filtro aplicado: " + filtroSeleccionado.getNombre() +"\n"+FiltroManager.getInfo());
                    }
                    else{
                        mainFrame.getjTextArea1().setText("Filtro aplicado: " + filtroSeleccionado.getNombre());
                        mainFrame.getjLabel5().setIcon(new ImageIcon(imagenFiltrada)); // Mostrar en jLabel5 
                    }
                } else {
                    mainFrame.getjTextArea1().setText("No hay imagen cargada.");
                }
                break;
            }
        }
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Main mainFrame = new Main();
            new Controlador(mainFrame);
            mainFrame.setVisible(true);
        });
    }
}
