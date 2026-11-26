package pe.edu.upeu.entregable1.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;

public class ConexionBD {
    private static Connection connection;

    public static void cerrarConexion() {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo cerrar la conexión a la base de datos", e);
        } finally {
            connection = null;
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String userHome = System.getProperty("user.home");
                File directory = new File(userHome, "SistemaVeterinaria");

                if (!directory.exists() && !directory.mkdirs() && !directory.isDirectory()) {
                    throw new SQLException("No se pudo crear la carpeta de la base de datos: " + directory);
                }

                File database = new File(directory, "veterinaria.db");
                String configuredUrl = System.getProperty("veterinaria.db.url");

                // En el primer inicio, copia la base inicial incluida con el programa.
                if ((configuredUrl == null || configuredUrl.isBlank()) && !database.exists()) {
                    String resource = "/pe/edu/upeu/entregable1/data/veterinaria.db";
                    try (InputStream initialDatabase = ConexionBD.class.getResourceAsStream(resource)) {
                        if (initialDatabase != null) {
                            Files.copy(initialDatabase, database.toPath());
                        }
                    }
                }

                String url = configuredUrl;
                if (url == null || url.isBlank()) {
                    url = "jdbc:sqlite:" + database.getAbsolutePath();
                }

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);

                try (Statement statement = connection.createStatement()) {
                    statement.execute("PRAGMA foreign_keys = ON;");
                    statement.execute("PRAGMA busy_timeout = 5000;");
                }

                // La opción de URL se usa en las pruebas para que estas controlen sus propias tablas.
                if (configuredUrl == null || configuredUrl.isBlank()) {
                    initDatabase();
                }

                System.out.println("Conexión exitosa a SQLite");
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión a la base de datos: " + e.getMessage(), e);
        }
    }

    private static void initDatabase() {
        String sqlCliente = "CREATE TABLE IF NOT EXISTS cliente (" +
                "idcliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombres TEXT NOT NULL, apellidos TEXT NOT NULL, dni TEXT NOT NULL UNIQUE, " +
                "telefono TEXT, direccion TEXT, estado TEXT NOT NULL DEFAULT 'Activo');";

        String sqlVeterinario = "CREATE TABLE IF NOT EXISTS veterinario (" +
                "idveterinario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombres TEXT NOT NULL, apellidos TEXT NOT NULL, dni TEXT NOT NULL UNIQUE, " +
                "telefono TEXT, especialidad TEXT, estado TEXT NOT NULL DEFAULT 'Activo');";

        String sqlMascota = "CREATE TABLE IF NOT EXISTS mascota (" +
                "idmascota INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, " +
                "especie TEXT NOT NULL, raza TEXT, clienteId INTEGER NOT NULL, " +
                "estado TEXT NOT NULL DEFAULT 'Activo', " +
                "FOREIGN KEY (clienteId) REFERENCES cliente(idcliente));";

        String sqlCita = "CREATE TABLE IF NOT EXISTS cita (" +
                "idcita INTEGER PRIMARY KEY AUTOINCREMENT, mascotaId INTEGER NOT NULL, " +
                "veterinarioId INTEGER NOT NULL, fecha TEXT NOT NULL, hora TEXT NOT NULL, " +
                "motivo TEXT, estado TEXT NOT NULL DEFAULT 'Pendiente', " +
                "FOREIGN KEY (mascotaId) REFERENCES mascota(idmascota), " +
                "FOREIGN KEY (veterinarioId) REFERENCES veterinario(idveterinario));";

        String sqlConsulta = "CREATE TABLE IF NOT EXISTS consulta (" +
                "idconsulta INTEGER PRIMARY KEY AUTOINCREMENT, citaId INTEGER NOT NULL UNIQUE, " +
                "diagnostico TEXT NOT NULL, observaciones TEXT, " +
                "FOREIGN KEY (citaId) REFERENCES cita(idcita));";

        String sqlTratamiento = "CREATE TABLE IF NOT EXISTS tratamiento (" +
                "idtratamiento INTEGER PRIMARY KEY AUTOINCREMENT, consultaId INTEGER NOT NULL, " +
                "descripcion TEXT NOT NULL, indicaciones TEXT, duracionDias INTEGER, " +
                "FOREIGN KEY (consultaId) REFERENCES consulta(idconsulta));";

        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario (" +
                "idusuario INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, tipoUser TEXT NOT NULL, nombres TEXT NOT NULL, " +
                "apellidos TEXT NOT NULL, dni TEXT NOT NULL UNIQUE);";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCliente);
            statement.execute(sqlVeterinario);
            statement.execute(sqlMascota);
            statement.execute(sqlCita);
            statement.execute(sqlConsulta);
            statement.execute(sqlTratamiento);
            statement.execute(sqlUsuario);

            try (ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM usuario")) {
                if (result.next() && result.getInt(1) == 0) {
                    statement.executeUpdate(
                            "INSERT INTO usuario (user, password, tipoUser, nombres, apellidos, dni) " +
                                    "VALUES ('admin', 'admin123', 'Administrador', 'Administrador', 'Sistema', '00000000')"
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar las tablas: " + e.getMessage(), e);
        }
    }
}
