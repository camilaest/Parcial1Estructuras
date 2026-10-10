package co.edu.uniquindio;
import java.util.Comparator;

public class ComparadorPorCodigo implements Comparator<Paquete> {
    @Override
    public int compare(Paquete p1, Paquete p2) {
        // Orden alfabético ascendente
        return p1.getCodigo().compareTo(p2.getCodigo());
    }
}
