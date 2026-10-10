package co.edu.uniquindio;
import java.util.Comparator;

public class ComparadorPorTiempo implements Comparator<Paquete>{
    @Override
    public int compare(Paquete p1, Paquete p2) {

        int cmp = Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
        if (cmp != 0) return cmp;


        return p1.getCodigo().compareTo(p2.getCodigo());
    }
}
