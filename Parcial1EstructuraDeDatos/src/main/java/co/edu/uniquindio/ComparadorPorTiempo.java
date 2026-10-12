package co.edu.uniquindio;
import java.util.Comparator;

/**
 * Comparador que permite ordenar objetos de tipo Paquete según su tiempo estimado de entrega.
 * En caso de empate en el tiempo, desempata por código alfabéticamente.
 */

public class ComparadorPorTiempo implements Comparator<Paquete>{
    /**
     * Compara dos paquetes por su tiempo estimado en orden ascendente (menor tiempo primero).
     * Si los tiempos son iguales, desempata por el código alfabéticamente.
     *
     * @param p1 Primer paquete a comparar
     * @param p2 Segundo paquete a comparar
     * @return Resultado de la comparación entre ambos paquetes
     */
    @Override
    public int compare(Paquete p1, Paquete p2) {

        int cmp = Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
        if (cmp != 0) return cmp;


        return p1.getCodigo().compareTo(p2.getCodigo());
    }
}
