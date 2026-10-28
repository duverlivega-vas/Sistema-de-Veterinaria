package pe.edu.upeu.entregable1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import pe.edu.upeu.entregable1.utils.ConexionBD;

class DatabaseIntegrationTest {
    @Test void sqliteConnectionEnforcesForeignKeysAndCanReopen() throws Exception {
        String previous = System.getProperty("veterinaria.db.url");
        System.setProperty("veterinaria.db.url","jdbc:sqlite::memory:");
        try {
            Connection first = ConexionBD.getConnection();
            assertSame(first,ConexionBD.getConnection());
            try (Statement st=first.createStatement()) {
                try(ResultSet rs=st.executeQuery("PRAGMA foreign_keys")) {assertTrue(rs.next());assertEquals(1,rs.getInt(1));}
                st.execute("CREATE TABLE parent(id INTEGER PRIMARY KEY)");
                st.execute("CREATE TABLE child(id INTEGER REFERENCES parent(id))");
                assertThrows(SQLException.class,()->st.execute("INSERT INTO child VALUES(99)"));
            }
            first.close();
            assertFalse(ConexionBD.getConnection().isClosed());
            assertNotSame(first,ConexionBD.getConnection());
        } finally {
            if(ConexionBD.connection!=null) ConexionBD.connection.close();
            ConexionBD.connection=null;
            if(previous==null)System.clearProperty("veterinaria.db.url");
            else System.setProperty("veterinaria.db.url",previous);
        }
    }
}
