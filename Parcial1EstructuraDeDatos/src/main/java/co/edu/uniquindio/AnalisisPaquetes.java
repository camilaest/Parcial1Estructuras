package co.edu.uniquindio;

import java.util.List;
import java.util.Map;

public class AnalisisPaquetes {
    /**
     * Método recursivo que devuelve el peso total según el municipio ingresado
     * @param paquetes Lista de paquetes
     * @param municipio Municipio a considerar
     * @return Peso total
     */
    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        return pesoTotalPorMunicipio(paquetes, municipio, 0);
    }
    private static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio, int posicion) {
        //Caso base
        if (posicion == paquetes.size()) {
            return 0;
        }

        Paquete actual = paquetes.get(posicion);

        // Caso recursivo: sumar si es del municipio y avanzar al siguiente
        if (actual.getDestino().equalsIgnoreCase(municipio)) {
            return actual.getPeso() + pesoTotalPorMunicipio(paquetes, municipio, posicion + 1);
        } else {
            return pesoTotalPorMunicipio(paquetes, municipio, posicion + 1);
        }
    }


    /**
     * Método recursivo que me determina el número de paquetes que tienen más o igual prioridad a la ingresada.
     * @param paquetes Lista de paquetes
     * @param prioridad Prioridad mínima a cumplir
     * @return numero de paquetes
     */
    public static int numeroPaquetesPrioridadMinima(List<Paquete> paquetes, int prioridad) {
        // Caso base: lista vacía, no hay nada que contar
        if (paquetes.isEmpty()) {
            return 0;
        }

        Paquete cabeza = paquetes.getFirst();
        List<Paquete> cola = paquetes.subList(1, paquetes.size());   // todo menos la cabeza

        int aporte = 0;
        if (cabeza.getPrioridad() >= prioridad) {
            aporte = 1;
        }

        // Caso recursivo: la cola es más pequeña, así puedo avanzar hacia el caso base
        return aporte + numeroPaquetesPrioridadMinima(cola,prioridad);
    }

    /**
     * Método divide y vencerás que devuelve el paquete con mayor peso de una lista de paquetes
     * @param paquetes Lista de paquetes
     * @return Paquete con más peso
     */
    public static Paquete paqueteMayorPeso(List<Paquete> paquetes) {
        if (paquetes.isEmpty()) {
            throw new IllegalArgumentException("No hay paquetes");
        }
        return paqueteMayorPeso(paquetes, 0, paquetes.size() - 1);
    }

    private static Paquete paqueteMayorPeso(List<Paquete> paquetes, int inicio, int fin) {
        // Caso base: un solo elemento
        if (inicio == fin) {
            return paquetes.get(inicio);
        }
        // División del problema
        int mitad = inicio + (fin - inicio) / 2;

        // Subproblema izquierdo y derecho
        Paquete mayorIzq = paqueteMayorPeso(paquetes, inicio, mitad);
        Paquete mayorDer = paqueteMayorPeso(paquetes, mitad + 1, fin);

        // Combinación
        if (mayorIzq.getPeso() >= mayorDer.getPeso()) {
            return mayorIzq;
        }
        return mayorDer;
    }

    /**
     * Método de búsqueda binaria  que tiene como pre condición que los paquetes están ordenados según su código.
     * @param ordenados Lista de paquetes ordenados por código
     * @param codigo Código a buscar
     * @return -1 si no se encuentra o si la lista está vacía o la posición en la que se encuentra el paquete
     *
     */
    public static int busquedaBinaria(List<Paquete> ordenados, String codigo) {
        return busquedaBinaria(ordenados, codigo, 0, ordenados.size() - 1);
    }

    private static int busquedaBinaria(List<Paquete> ordenados, String codigo, int inicio, int fin) {
        // Caso base: rango vacío, no está
        if (inicio > fin) {
            return -1;
        }

        int mitad = inicio + (fin - inicio) / 2; // Devuelve la posición del elemento del medio de la lista
        int cmp = codigo.compareTo(ordenados.get(mitad).getCodigo()); // Utilizo el compareTo para después poder determinar si el elemento buscado es igual, menor o mayor al de la mitad.

        if (cmp == 0) {
            return mitad; // encontrado
        } else if (cmp < 0) {
            return busquedaBinaria(ordenados, codigo, inicio, mitad - 1); // mitad izquierda
        } else {
            return busquedaBinaria(ordenados, codigo, mitad + 1, fin); // mitad derecha
        }
    }

}
