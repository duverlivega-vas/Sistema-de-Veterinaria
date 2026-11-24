package pe.edu.upeu.entregable1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import pe.edu.upeu.entregable1.modelo.*;

class ModelIntegrationTest {
    @Test void preservesOldConstructorsAndObservableProperties() {
        Persona persona = new Persona(1, "Ana", "Vega", "12345678", 987654321);
        assertEquals("987654321", persona.getTelefono());
        persona.setTelefono(912345678);
        assertEquals("912345678", persona.telefonoProperty().get());
        Cliente cliente = new Cliente(2, "Ana", "Vega", "12345678", "00123", "Lima");
        assertEquals("00123", cliente.getTelefono());
        assertEquals(2, cliente.getIdcliente());
        var nombre = new SimpleStringProperty();
        nombre.bind(cliente.nombresProperty());
        cliente.setNombres("Maria");
        assertEquals("Maria", nombre.get());
        cliente.direccionProperty().set("Puno");
        assertEquals("Puno", cliente.getDireccion());
        cliente.setEstado("Activo");
        assertEquals("Activo", cliente.estadoProperty().get());
        assertDoesNotThrow(() -> cliente.mostrarInfo(cliente));
        assertEquals("987654321", new Cliente(3,"A","B","1",987654321,"Lima").getTelefono());
        assertEquals("987654321", new Veterinario(4,"A","B","1",987654321,"General").getTelefono());
    }

    @Test void preservesRelationsAndLocalDate() {
        Cliente cliente = new Cliente();
        Mascota mascota = new Mascota(1,"Luna","Gato","Mestiza",cliente);
        Veterinario vet = new Veterinario(2,"Ana","Vega","123","00123","General");
        Cita cita = new Cita(); cita.setMascota(mascota); cita.setVeterinario(vet);
        cita.setFecha(LocalDate.of(2026,10,28));
        Consulta consulta = new Consulta(3,cita,"Control","Sin observaciones");
        Tratamiento tratamiento = new Tratamiento(4,consulta,"Seguimiento","Control",5);
        assertSame(cliente, mascota.clienteProperty().get());
        assertSame(vet, cita.veterinarioProperty().get());
        assertEquals(LocalDate.of(2026,10,28), cita.fechaProperty().get());
        assertSame(cita, consulta.citaProperty().get());
        assertSame(consulta, tratamiento.consultaProperty().get());
        assertEquals(5, tratamiento.duracionDiasProperty().get());
    }

}
