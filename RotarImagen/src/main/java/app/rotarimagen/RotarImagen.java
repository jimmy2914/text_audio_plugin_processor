package app.rotarimagen;

import app.filtropluginapi.Filtro;
import app.filtropluginapi.FiltroManager;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotarImagen implements Filtro {
    private static final int ANGULO_INCREMENTO = 15;
    private static BufferedImage ultimaImagenProcesada = null;
    private static int anguloAcumulado = 0;

    @Override
    public String getNombre() {
        return "Rotar Imagen";
    }

    @Override
    public Image aplicarFiltro(Image imagenOriginal) {
        BufferedImage imagenActual = convertirABufferedImage(imagenOriginal);

        String filtroPrevio = FiltroManager.getUltimoFiltro();
        //System.out.println("Último filtro aplicado: " + filtroPrevio);

        // Reiniciar si la imagen ha cambiado o si se aplicó otro filtro antes
        if (ultimaImagenProcesada == null || !imagenesIguales(ultimaImagenProcesada, imagenActual) || 
            (filtroPrevio != null && !filtroPrevio.equals("Rotar Imagen"))) {
            anguloAcumulado = 0;
            ultimaImagenProcesada = imagenActual;
        }

        anguloAcumulado += ANGULO_INCREMENTO;
        FiltroManager.setUltimoFiltro("Rotar Imagen");

        // Aplicar la rotación
        int ancho = imagenOriginal.getWidth(null);
        int alto = imagenOriginal.getHeight(null);
        BufferedImage imagenRotada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenRotada.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(anguloAcumulado), ancho / 2.0, alto / 2.0);
        g2d.setTransform(transform);
        g2d.drawImage(imagenOriginal, 0, 0, null);
        g2d.dispose();

        return imagenRotada;
    }

    private BufferedImage convertirABufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return bufferedImage;
    }

    private boolean imagenesIguales(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
