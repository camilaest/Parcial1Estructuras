package co.edu.uniquindio;
import java.util.Comparator;

public class ComparadorPorPrioridad implements Comparator<Paquete> {
    @Override
    public int compare(Paquete p1, Paquete p2) {
        // Orden descendente (mayor a menor)
        return Integer.compare(p2.getPrioridad(), p1.getPrioridad());
    }
}
