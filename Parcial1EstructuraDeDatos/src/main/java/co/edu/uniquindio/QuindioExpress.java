package co.edu.uniquindio;

import java.util.*;

public class QuindioExpress {
    private final List<Paquete> listaPaquetesPendientes;

    //Asociar código a paquete
    private final Map<String, Paquete> mapaPaquetes;

    //Los municipios no pueden ser duplicados
    private final Set<String> municipios;

    //Los municipios deben estaar ordenadoos alfabeticamente
    private final Set<String> municipiosOrdenados;

    private final PriorityQueue<Paquete> despacharPaquetesPrioridad;
    private final Queue<Paquete> ordenLlegada;

    //el historial es respetando el orden de entrega
    private final ListaSimplementeEnlazada<Paquete> historialPaquetesEntregados;

    private final Map<String, Repartidor> mapaRepartidores;

    public QuindioExpress(){
        this.listaPaquetesPendientes = new LinkedList<>();
        this.mapaPaquetes = new HashMap<>();
        this.municipios = new HashSet<>();
        this.municipiosOrdenados = new TreeSet<>();
        this.despacharPaquetesPrioridad = new PriorityQueue<>(new ComparadorPorPrioridad());
        this.historialPaquetesEntregados = new ListaSimplementeEnlazada<>();
        this.mapaRepartidores = new HashMap<>();
        this.ordenLlegada = new LinkedList<>();
    }

    public List<Paquete> getListaPaquetesPendientes() {
        return listaPaquetesPendientes;
    }

    public ListaSimplementeEnlazada<Paquete> getHistorialPaquetesEntregados() {
        return historialPaquetesEntregados;
    }

    public PriorityQueue<Paquete> getDespacharPaquetesPrioridad() {
        return despacharPaquetesPrioridad;
    }

    public Map<String, Paquete> getMapaPaquetes() {
        return mapaPaquetes;
    }

    public Set<String> getMunicipios() {
        return municipios;
    }

    public Set<String> getMunicipiosOrdenados() {
        return municipiosOrdenados;
    }

    public Map<String, Repartidor> getMapaRepartidores() {
        return mapaRepartidores;
    }

    public boolean registrarPaquete(Paquete paquete) {
        if (paquete == null || mapaPaquetes.containsKey(paquete.getCodigo())) {
            return false;
        }

        mapaPaquetes.put(paquete.getCodigo(), paquete);
        listaPaquetesPendientes.add(paquete);
        despacharPaquetesPrioridad.add(paquete);
        municipios.add(paquete.getDestino());
        municipiosOrdenados.add(paquete.getDestino());
        ordenLlegada.add(paquete);

        return true;
    }


    public boolean registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null || mapaRepartidores.containsKey(repartidor.getCedula())) {
            return false;
        }
        mapaRepartidores.put(repartidor.getCedula(), repartidor);
        return true;
    }

    public Paquete consultarPorCodigo(String codigo) {
        return mapaPaquetes.get(codigo);
    }

    public Set<String> obtenerMunicipiosUnicos() {
        return municipios;
    }

    public Set<String> obtenerMunicipiosOrdenados() {
        return municipiosOrdenados;
    }

    /**
     * Método recursivo que me permite ver el peso total de un municipio
     * @param municipio
     * @return peso total del municipio ingresado
     */
    public double pesoTotalPorMunicipio(String municipio) {
        return AnalisisPaquetes.pesoTotalPorMunicipio(listaPaquetesPendientes, municipio );
    }

    /**
     * Método recursivo de análisis en el que puedo visualizar la cantidad de paquetes que tienen una prioridad igual o mayor a la ingresada
     * @param prioridad
     * @return cantidad de paquetes
     */
    public int numeroPaquetesPrioridadMinima(int prioridad){
        if(prioridad > 5){
            throw new IllegalArgumentException("Prioridad mayor que 5");
        }
        return AnalisisPaquetes.numeroPaquetesPrioridadMinima(listaPaquetesPendientes, prioridad);
    }

    /**
     * Método de divide y vencerás de análisis que me permite ver el paquete con mayor peso dentro de los paquetes pedientes
     * @return Paquete
     */
    public Paquete paqueteMayorPeso(){
        if(listaPaquetesPendientes.isEmpty()){
            return null;
        }
        return AnalisisPaquetes.paqueteMayorPeso(listaPaquetesPendientes);
    }

    /**
     * Método de análisis que me permite buscar un paquete por su código, devolviendo la posición en la que se encuentra
     * @param codigo
     * @return posicion
     */
    public int busquedaBinaria(String codigo) {
        List<Paquete> ordenados = new ArrayList<>(listaPaquetesPendientes);
        Collections.sort(ordenados);
        return AnalisisPaquetes.busquedaBinaria(ordenados, codigo);
    }

    /**
     * Agrupa los paquetes registrados según su municipio de destino.
     * Recorre todos los paquetes en el mapa y genera una estructura donde cada clave
     * es un municipio y su valor es la lista de paquetes asignados a dicho destino.
     *
     * @return Un mapa (Map) que relaciona cada municipio de destino
     * con su correspondiente lista de paquetes
     */
    public Map<String, List<Paquete>> agruparPaquetesPorMunicipio() {
        Map<String, List<Paquete>> agrupamiento = new HashMap<>();

        for (Paquete p : mapaPaquetes.values()) {
            String destino = p.getDestino();
            if (!agrupamiento.containsKey(destino)) {
                agrupamiento.put(destino, new ArrayList<>());
            }
            agrupamiento.get(destino).add(p);
        }

        return agrupamiento;
    }

    /**
     * Despacha el paquete de mayor prioridad en la cola de procesamiento.
     * Al atendenderlo, lo remueve de la lista de paquetes pendientes y lo agrega
     * al historial de paquetes entregados.
     *
     * @return El paquete despachado, o null si no hay paquetes para procesar
     */
    public Paquete despacharSiguientePaquete() {
        Paquete paqueteAtendido = despacharPaquetesPrioridad.poll();

        if (paqueteAtendido != null) {
            listaPaquetesPendientes.remove(paqueteAtendido);     // Lo remueve de pendientes
            historialPaquetesEntregados.agregar(paqueteAtendido);
        }

        return paqueteAtendido;
    }

    /**
     * Atiende el siguiente paquete respetando estrictamente su orden de llegada.
     * Retira el paquete de la cola de orden de llegada y actualiza simultáneamente
     * la lista de pendientes y la cola de prioridad.
     *
     * @return El paquete atendido, o null si la cola está vacía
     */
    public Paquete atenderPorOrdenLlegada() {
        Paquete p = ordenLlegada.poll();
        if (p != null) {
            listaPaquetesPendientes.remove(p);
            despacharPaquetesPrioridad.remove(p);
        }
        return p;
    }

    /**
     * Método del historial que me permite eliminar un paquete de la lista enlazada sin importar en qué parte de la lista esté
     * @param posicionPaquete
     * @return boolean
     */
    public boolean eliminarDelHistorial(int posicionPaquete) {
        Paquete p = historialPaquetesEntregados.obtener(posicionPaquete);

        try {
            historialPaquetesEntregados.eliminar(p);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Asigna y gestiona la entrega de un paquete a un repartidor específico.
     * Valida la existencia del paquete y del repartidor, que el repartidor esté disponible
     * y que la zona asignada al repartidor coincida con el municipio de destino del paquete.
     *
     * @param codigoPaquete Código único del paquete a entregar
     * @param cedulaRepartidor Cédula del repartidor asignado
     * @throws IllegalArgumentException Si el paquete o el repartidor no existen, o si la zona no coincide
     * @throws IllegalStateException Si el repartidor no se encuentra disponible
     */
    public void asignarYEntregarPaquete(String codigoPaquete, String cedulaRepartidor) {
        Paquete paquete = mapaPaquetes.get(codigoPaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("No existe un paquete con el código ingresado.");
        }

        Repartidor repartidor = mapaRepartidores.get(cedulaRepartidor);
        if (repartidor == null) {
            throw new IllegalArgumentException("No existe un repartidor con la cédula ingresada.");
        }

        if (!repartidor.isDisponible()) {
            throw new IllegalStateException("El repartidor " + repartidor.getNombre() + " no está disponible.");
        }

        if (!repartidor.getZona().equalsIgnoreCase(paquete.getDestino())) {
            throw new IllegalArgumentException("La zona del repartidor (" + repartidor.getZona() +
                    ") no coincide con el destino del paquete (" + paquete.getDestino() + ").");
        }




        // Efectuar proceso de entrega
        repartidor.setDisponible(false);


        listaPaquetesPendientes.remove(paquete);
        ordenLlegada.remove(paquete);
        despacharPaquetesPrioridad.remove(paquete);
        historialPaquetesEntregados.agregar(paquete);

        repartidor.setDisponible(true);
    }

    /**
     * Obtiene el total de paquetes que han sido entregados y registrados en el historial
     * @return Número entero que representa la cantidad de elementos almacenados en el historial de entregados
     */
    public int cantidadEntregados() {
        return historialPaquetesEntregados.cantidadElementos();
    }

    /**
     * Método de historial que me permite ver el paquete que está en una posición deternminada
     * @param posicion
     * @return Paquete en la posición ingresada
     */
    public Paquete obtenerEntregado(int posicion) {
        return historialPaquetesEntregados.obtener(posicion);
    }

    /**
     * Método de historial que me permite determinar si un paquete ya fue entregado o no
     * @param codigo
     * @return boolean
     */
    public boolean fueEntregado(String codigo) {
        for (Paquete p : historialPaquetesEntregados) {
            if (p.getCodigo().equals(codigo)) return true;
        }
        return false;
    }

    /**
     * Método para mostrar historial en el main en formato texto
     * @return String lista enlazada
     */
    public String historialComoTexto() {
        StringBuilder sb = new StringBuilder();
        boolean primero = true;
        for (Paquete p : historialPaquetesEntregados) {
            if (!primero) sb.append(" → ");
            sb.append(p.getCodigo());
            primero = false;
        }
        return sb.toString();
    }

}
