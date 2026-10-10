package co.edu.uniquindio;
import java.util.Comparator;

public class ComparadorPorCodigo implements Comparator<Paquete> {

        @Override
        public int compare(Paquete o1, Paquete o2) {
            // Orden alfabético
            return o1.getCodigo().compareTo(o2.getCodigo());

    }
}
