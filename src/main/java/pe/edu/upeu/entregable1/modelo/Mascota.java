package pe.edu.upeu.entregable1.modelo;

// CAMBIO: imports de las properties de JavaFX
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Mascota {

    // CAMBIO: atributos simples -> properties de JavaFX
    private final IntegerProperty idMascota = new SimpleIntegerProperty(this, "idMascota");
    private final StringProperty nombre = new SimpleStringProperty(this, "nombre");
    private final StringProperty especie = new SimpleStringProperty(this, "especie");
    private final StringProperty raza = new SimpleStringProperty(this, "raza");
    // CAMBIO: el dueno (Cliente) es un objeto, por eso se usa ObjectProperty
    private final ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>(this, "cliente");
    // NUEVO: la tabla mascota tiene la columna estado (Activo / Inactivo)
    private final StringProperty estado = new SimpleStringProperty(this, "estado", "Activo");

    public Mascota() {
    }

    public Mascota(int idMascota, String nombre, String especie, String raza, Cliente cliente) {
        setIdMascota(idMascota);
        setNombre(nombre);
        setEspecie(especie);
        setRaza(raza);
        setCliente(cliente);
    }

    // ---------- idMascota ----------
    public final int getIdMascota() {
        return idMascota.get();
    }

    public final void setIdMascota(int value) {
        idMascota.set(value);
    }

    public final IntegerProperty idMascotaProperty() {
        return idMascota;
    }

    // ---------- nombre ----------
    public final String getNombre() {
        return nombre.get();
    }

    public final void setNombre(String value) {
        nombre.set(value);
    }

    public final StringProperty nombreProperty() {
        return nombre;
    }

    // ---------- especie ----------
    public final String getEspecie() {
        return especie.get();
    }

    public final void setEspecie(String value) {
        especie.set(value);
    }

    public final StringProperty especieProperty() {
        return especie;
    }

    // ---------- raza ----------
    public final String getRaza() {
        return raza.get();
    }

    public final void setRaza(String value) {
        raza.set(value);
    }

    public final StringProperty razaProperty() {
        return raza;
    }

    // ---------- cliente (dueno) ----------
    public final Cliente getCliente() {
        return cliente.get();
    }

    public final void setCliente(Cliente value) {
        cliente.set(value);
    }

    public final ObjectProperty<Cliente> clienteProperty() {
        return cliente;
    }

    // ---------- estado ----------
    public final String getEstado() {
        return estado.get();
    }

    public final void setEstado(String value) {
        estado.set(value);
    }

    public final StringProperty estadoProperty() {
        return estado;
    }
}
