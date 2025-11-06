package app.filtropluginapi;

import java.io.File;

public class FiltroManager {
    private static String ultimoFiltroAplicado = null;
    private static String Info = "";
    private static File archivoActual=null;
    public static void setUltimoFiltro(String nombreFiltro) {
        ultimoFiltroAplicado = nombreFiltro;
    }

    public static String getUltimoFiltro() {
        return ultimoFiltroAplicado;
    }

    public static String getInfo() {
        return Info;
    }

    public static void setInfo(String Informacion) {
        Info = Informacion;
    }
    
    // Setter para asignar el archivo actual.
    public static void setArchivoActual(File archivo) {
        archivoActual = archivo;
    }
    
    // Getter para recuperar el archivo actual.
    public static File getArchivoActual() {
        return archivoActual;
    }
}
