/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app.filtropluginapi;

/**
 *
 * @author jimmy
 */
import java.awt.Image;

public interface Filtro {
    Image aplicarFiltro(Image imagen);
    String getNombre();
}
