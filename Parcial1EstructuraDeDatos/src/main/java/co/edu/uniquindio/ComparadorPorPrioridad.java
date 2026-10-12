package co.edu.uniquindio;
import java.util.Comparator;

/**
 * Comparador que permite ordenar objetos de tipo Paquete según su nivel de prioridad.
 * En caso de empate, desencadena la comparación por tiempo estimado y finalmente por código.
 */

public class ComparadorPorPrioridad implements Comparator<Paquete> {

    /**
     * Compara dos paquetes priorizando la mayor prioridad (descendente).
     * Si las prioridades son iguales, compara por menor tiempo estimado (ascendente).
     * Si ambos criterios empatan, desempata por orden alfabético de código.
     *
     * @param p1 Primer paquete a comparar
     * @param p2 Segundo paquete a comparar
     * @return Resultado numérico según el criterio de ordenamiento establecido
     */

    @Override
    public int compare(Paquete p1, Paquete p2) {

        int cmp = Integer.compare(p2.getPrioridad(), p1.getPrioridad());
        if (cmp != 0) return cmp;

        cmp = Integer.compare(p1.getTiempoEstimado(), p2.getTiempoEstimado());
        if (cmp != 0) return cmp;

        return p1.getCodigo().compareTo(p2.getCodigo());
    }
}
