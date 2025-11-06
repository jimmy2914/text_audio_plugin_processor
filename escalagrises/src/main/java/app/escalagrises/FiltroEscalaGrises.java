/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app.escalagrises;

/**
 *
 * @author jimmy
 */

import app.filtropluginapi.Filtro;
import app.filtropluginapi.FiltroManager;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class FiltroEscalaGrises implements Filtro {

    @Override
    public String getNombre() {
        return "Escala de Grises";
    }

    @Override
    public Image aplicarFiltro(Image imagenOriginal) {
        // Convertir la imagen a BufferedImage
        BufferedImage bufferedImage = toBufferedImage(imagenOriginal);
 
        FiltroManager.setUltimoFiltro("Escala Grises");
        
        
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        // Aplicar el filtro de escala de grises
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(bufferedImage.getRGB(x, y));

                // Calcular el valor promedio de los canales RGB
                int promedio = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                // Crear un nuevo color en escala de grises
                Color colorGris = new Color(promedio, promedio, promedio);

                // Establecer el nuevo color en la imagen
                bufferedImage.setRGB(x, y, colorGris.getRGB());
            }
        }

        return bufferedImage;
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Crear un BufferedImage con formato ARGB
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(img, 0, 0, null);
        return bi;
    }

}
