package co.edu.uniquindio;
import java.util.Comparator;

public class ComparadorPorPrioridad implements Comparator<Paquete> {
    @Override
    public int compare(Paquete p1, Paquete p2) {

        int cmp = Integer.compare(p2.getPrioridad(), p1.getPrioridad());
        if (cmp != 0) return cmp;

        cmp = Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
        if (cmp != 0) return cmp;

        return p1.getCodigo().compareTo(p2.getCodigo());
    }
}
