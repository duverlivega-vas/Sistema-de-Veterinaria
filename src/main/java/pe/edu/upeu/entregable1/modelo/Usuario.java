package pe.edu.upeu.entregable1.modelo;

import javafx.beans.property.*;

public class Usuario {
    private final IntegerProperty idusuario = new SimpleIntegerProperty();
    private final StringProperty usuario = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty tipoUser = new SimpleStringProperty();
    private final StringProperty nombres = new SimpleStringProperty();
    private final StringProperty apellidos = new SimpleStringProperty();
    private final StringProperty dni = new SimpleStringProperty();

    public int getIdusuario() {
        return idusuario.get(); }
    public void setIdusuario(int value) {
        idusuario.set(value); }
    public IntegerProperty idusuarioProperty() {
        return idusuario; }

    public String getUsuario() { return usuario.get(); }
    public void setUsuario(String value) { usuario.set(value); }
    public StringProperty usuarioProperty() { return usuario; }

    public String getPassword() { return password.get(); }
    public void setPassword(String value) { password.set(value); }
    public StringProperty passwordProperty() { return password; }

    public String getTipoUser() { return tipoUser.get(); }
    public void setTipoUser(String value) { tipoUser.set(value); }
    public StringProperty tipoUserProperty() { return tipoUser; }

    public String getNombres() { return nombres.get(); }
    public void setNombres(String value) { nombres.set(value); }
    public StringProperty nombresProperty() { return nombres; }

    public String getApellidos() { return apellidos.get(); }
    public void setApellidos(String value) { apellidos.set(value); }
    public StringProperty apellidosProperty() { return apellidos; }

    public String getDni() { return dni.get(); }
    public void setDni(String value) { dni.set(value); }
    public StringProperty dniProperty() { return dni; }
}
