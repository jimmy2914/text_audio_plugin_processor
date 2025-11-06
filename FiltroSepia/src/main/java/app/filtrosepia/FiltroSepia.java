/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app.filtrosepia;

import app.filtropluginapi.Filtro;
import app.filtropluginapi.FiltroManager;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author kevin
 */


public class FiltroSepia implements Filtro {

    @Override
    public String getNombre() {
        return "Filtro Sepia";
    }

    @Override
    public Image aplicarFiltro(Image imagenOriginal) {
        BufferedImage bufferedImage = toBufferedImage(imagenOriginal);
        FiltroManager.setUltimoFiltro("Filtro Sepia");
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = new Color(bufferedImage.getRGB(x, y));
                
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                
                int tr = (int)(0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int)(0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int)(0.272 * r + 0.534 * g + 0.131 * b);
                
                tr = Math.min(255, tr);
                tg = Math.min(255, tg);
                tb = Math.min(255, tb);
                
                Color sepiaColor = new Color(tr, tg, tb);
                bufferedImage.setRGB(x, y, sepiaColor.getRGB());
            }
        }
        
        return bufferedImage;
    }
    
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bimage.getGraphics().drawImage(img, 0, 0, null);
        return bimage;
    }
}
