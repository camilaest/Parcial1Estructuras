package co.edu.uniquindio;
import java.util.Comparator;

/**
 * Comparador que permite ordenar objetos de tipo Paquete alfabéticamente según su código.
 */


public class ComparadorPorCodigo implements Comparator<Paquete> {

    /**
     * Compara dos paquetes por su código en orden alfabético.
     *
     * @param o1 Primer paquete a comparar
     * @param o2 Segundo paquete a comparar
     * @return Un valor negativo si o1 es menor que o2, 0 si son iguales, o un valor positivo si o1 es mayor
     */

        @Override
        public int compare(Paquete o1, Paquete o2) {
            // Orden alfabético
            return o1.getCodigo().compareTo(o2.getCodigo());

    }
}
