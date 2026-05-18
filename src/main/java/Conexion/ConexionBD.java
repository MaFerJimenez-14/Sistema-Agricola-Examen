package Conexion;
 
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionBD {
    
    // Nombre del controlador JDBC de MySQL
    private static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // Nombre de la base de datos
    private static String JDBC_DB = "gamabasis_rec_maria";
    
    // URL de conexión a la base de datos
    private static String JDBC_URL = "jdbc:mysql:// mysql.us.cloudlogin.co:3306/" + JDBC_DB  //127.0.0.1
       + "?useUnicode=true&useJDBCCompliantTimezoneShift=true"
       + "&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false"
       + "&allowPublicKeyRetrieval=true";

    // Usuario de la base de datos
    private static String JDBC_USER = "gamabasis_rec_maria";
    // Contraseña de la base de datos
    private static String JDBC_PASS = "maWX7s31I=";
    // Instancia del controlador JDBC
    private static Driver driver = null;
    /**
    * Método sincronizado para obtener una conexión a la base de datos.
    *
    * @return Connection - la conexión a la base de datos.
    * @throws SQLException - si ocurre un error al establecer la conexión.
    */
    public static synchronized Connection getConnection() throws SQLException {
        try {
            // Cargar la clase del controlador JDBC
            Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
            // Crear una nueva instancia del controlador
            driver = (Driver) jdbcDriverClass.getDeclaredConstructor().newInstance();
            // Registrar el controlador con DriverManager
            DriverManager.registerDriver(driver);
        }catch(Exception e){
            e.printStackTrace();
            throw new SQLException("Error al registrar el driver de MySQL");
        }
        // Retornar la conexión utilizando DriverManager
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }
    /**
    * Método para cerrar un ResultSet.
    *
    * @param rs - el ResultSet a cerrar.
    */
    public static void close(ResultSet rs) {
        try{
            if(rs != null){
                rs.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    /**
    * Método para cerrar un PreparedStatement.
    *
    * @param stmt - el PreparedStatement a cerrar.
    */
    public static void close(PreparedStatement stmt) {
         try{
            if(stmt != null){
                stmt.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    /**
    * Método para cerrar una conexión a la base de datos.
    *
    * @param conn - la conexión a cerrar.
    */
    public static void close(Connection conn) {
        try{
            if(conn != null){
                conn.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
 
}