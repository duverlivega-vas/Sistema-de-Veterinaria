package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;

public class Mascota {

    private final IntegerProperty idMascota = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty especie = new SimpleStringProperty();
    private final StringProperty raza = new SimpleStringProperty();
    private final ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>();
    private final StringProperty estado = new SimpleStringProperty();

    public Mascota() {
    }

    public Mascota(int idMascota, String nombre, String especie, String raza, Cliente cliente) {
        setIdMascota(idMascota);
        setNombre(nombre);
        setEspecie(especie);
        setRaza(raza);
        setCliente(cliente);
    }

    public final int getIdMascota() {
        return idMascota.get();
    }

    public final void setIdMascota(int value) {
        idMascota.set(value);
    }

    public final IntegerProperty idMascotaProperty() {
        return idMascota;
    }

    public final String getNombre() {
        return nombre.get();
    }

    public final void setNombre(String value) {
        nombre.set(value);
    }

    public final StringProperty nombreProperty() {
        return nombre;
    }

    public final String getEspecie() {
        return especie.get();
    }

    public final void setEspecie(String value) {
        especie.set(value);
    }

    public final StringProperty especieProperty() {
        return especie;
    }

    public final String getRaza() {
        return raza.get();
    }

    public final void setRaza(String value) {
        raza.set(value);
    }

    public final StringProperty razaProperty() {
        return raza;
    }

    public final Cliente getCliente() {
        return cliente.get();
    }

    public final void setCliente(Cliente value) {
        cliente.set(value);
    }

    public final ObjectProperty<Cliente> clienteProperty() {
        return cliente;
    }

    public final String getEstado() {
        return estado.get();
    }

    public final void setEstado(String value) {
        estado.set(value);
    }

    public final StringProperty estadoProperty() {
        return estado;
    }
    @Override
    public String toString() {
        return getIdMascota() + " - " + getNombre();
    }
}
