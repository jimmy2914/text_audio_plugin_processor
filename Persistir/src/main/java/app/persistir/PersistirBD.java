package app.persistir;

import app.filtropluginapi.Filtro;
import app.filtropluginapi.FiltroManager;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 *
 * @author kevin
 */
public class PersistirBD implements Filtro {
    
    private static final String URL = "jdbc:h2:~/imagenesdb"; // Base de datos en la carpeta de usuario
    private static final String USER = "sa";  // Usuario por defecto
    private static final String PASSWORD = ""; // Contraseña vacía por defecto

    @Override
    public String getNombre() {
        return "Persistir Imagen en Base de Datos";
    }

    @Override
    public Image aplicarFiltro(Image imagen) {
        
        if (imagen == null) {
            System.out.println("Error: La imagen es nula.");
            return null;
        }
        FiltroManager.setUltimoFiltro("Persisitir Imagen en Base de Datos");
        File archivo = FiltroManager.getArchivoActual();
        if (archivo != null) {
            //System.out.print(archivo.getAbsolutePath());
            crearBase();
            insertarImagen(archivo.getAbsolutePath());
        }
        
    return null;
}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void crearBase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Crear tabla si no existe
            String sql = "CREATE TABLE IF NOT EXISTS imagenes (id INT AUTO_INCREMENT PRIMARY KEY, imagen BLOB)";
            stmt.execute(sql);
            //System.out.println("Tabla 'imagenes' creada o ya existente.");
            
        } catch (SQLException e) {
            System.out.println("--------------Error al crear/verificar tabla imagenes.---------------");
        }
    }
    public static void insertarImagen(String imagePath) {
        String insertSQL = "INSERT INTO imagenes (imagen) VALUES (?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL);
             FileInputStream fis = new FileInputStream(imagePath)) {
            
            pstmt.setBinaryStream(1, fis, (int) new File(imagePath).length());
            pstmt.executeUpdate();
            //System.out.println("Imagen insertada desde: " + imagePath);
            String info = "Imagen Guardada con Exito";
            FiltroManager.setInfo(info);
        } catch (SQLException | IOException e) {
            System.out.println("Error al insertar imagen");
        }
    }
}
