package co.edu.uniquindio;
import java.util.Comparator;

public class ComparadorPorTiempo implements Comparator<Paquete>{
    @Override
    public int compare(Paquete p1, Paquete p2) {
        // Orden ascendente (menor a mayor)
        return Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
    }
}
