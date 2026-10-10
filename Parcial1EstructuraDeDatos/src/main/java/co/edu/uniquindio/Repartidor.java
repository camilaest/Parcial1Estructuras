package co.edu.uniquindio;

public class Repartidor {
    public String cedula;
    public String zona;
    public String nombre;
    public boolean disponible;

    public Repartidor(String cedula, String nombre, String zona, boolean disponile) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.zona = zona;
        this.disponible = disponile;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponile) {
        this.disponible = disponile;
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "cedula='" + cedula + '\'' +
                ", zona='" + zona + '\'' +
                ", nombre='" + nombre + '\'' +
                ", disponile=" + disponible +
                '}';
    }
}
