package co.edu.uniquindio;

public class Paquete implements Comparable<Paquete>{
    public String codigo;
    public String destino;
    public double peso;
    public int prioridad;
    public int tiempoEstimado;

    public Paquete() {
    }

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "codigo='" + codigo + '\'' +
                ", destino='" + destino + '\'' +
                ", peso=" + peso +
                ", prioridad=" + prioridad +
                ", tiempoEstimado=" + tiempoEstimado +
                '}';
    }


    @Override
    public int compareTo(Paquete o) {
        return codigo.compareTo(o.getCodigo());
    }
}
