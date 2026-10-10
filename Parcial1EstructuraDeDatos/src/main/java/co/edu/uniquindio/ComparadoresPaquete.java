package co.edu.uniquindio;

import java.util.Comparator;

public class ComparadoresPaquete {

    public static final Comparator<Paquete> POR_PRIORIDAD = new ComparadorPorPrioridad();
    public static final Comparator<Paquete> POR_TIEMPO = new ComparadorPorTiempo();
    public static final Comparator<Paquete> POR_CODIGO = new ComparadorPorCodigo();

    
    public static final Comparator<Paquete> DESPACHO_PRIORITARIO =
            POR_PRIORIDAD
                    .thenComparing(POR_TIEMPO)
                    .thenComparing(POR_CODIGO);
}
