package app.obtenermetadata;

import app.filtropluginapi.Filtro;
import app.filtropluginapi.FiltroManager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class ObtenerMetadata implements Filtro {
    
    @Override
    public String getNombre() {
        return "Obtener Metadata";
    }

    @Override
    public Image aplicarFiltro(Image imagenOriginal) {
        // Convertir la imagen a BufferedImage para extraer sus propiedades
        BufferedImage bufferedImage = toBufferedImage(imagenOriginal);

        // Obtener dimensiones de la imagen
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        
        /* Determinar el formato interno de la imagen
        int imageType = bufferedImage.getType();
        String tipoImagen = (imageType == BufferedImage.TYPE_INT_ARGB) ? "ARGB" : "Otro formato";
        */
        
        // Obtener información del archivo a partir del FiltroManager
        File archivo = FiltroManager.getArchivoActual(); // Se debe haber asignado previamente el archivo
        String nombreArchivo = "Desconocido";
        String extensionArchivo = "Desconocido";
        long fileSizeBytes = 0;
        if (archivo != null) {
            nombreArchivo = archivo.getName();
            if (nombreArchivo.contains(".")) {
                extensionArchivo = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);
            }
            // El tamaño real del archivo (en bytes) se obtiene del sistema de archivos
            fileSizeBytes = archivo.length();
        }
        
        double fileSizeKB = fileSizeBytes / 1024.0;
        String formattedFileSize = String.format("%.2f", fileSizeKB);
        
        // Construir el mensaje de metadata
        String metadata = "Metadata Obtenido:\n" +
                          "Nombre de archivo: " + nombreArchivo + "\n" +
                          "Extensión: " + extensionArchivo + "\n" +
                          "Tamaño real: " + formattedFileSize + " KB\n" +
                          "Ancho: " + width + " px\n" +
                          "Alto: " + height + " px\n";
        
        // Actualizar la interfaz y el gestor de filtros con la información obtenida
        FiltroManager.setUltimoFiltro("Obtener Metadata");
        FiltroManager.setInfo(metadata);
        
        // Retornar null para indicar que la imagen original no se modifica
        return null;
    }

    // Método auxiliar para convertir un objeto Image a BufferedImage
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return bi;
    }
}
