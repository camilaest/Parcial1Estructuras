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
    public double pesoTotalPorMunicipio(String municipio) {
        return AnalisisPaquetes.pesoTotalPorMunicipio(listaPaquetesPendientes, municipio );
    }
    public int numeroPaquetesPrioridadMinima(int prioridad){
        if(prioridad > 5){
            throw new IllegalArgumentException("Prioridad mayor que 5");
        }
        return AnalisisPaquetes.numeroPaquetesPrioridadMinima(listaPaquetesPendientes, prioridad);
    }
    public Paquete paqueteMayorPeso(){
        if(listaPaquetesPendientes.isEmpty()){
            return null;
        }
        return AnalisisPaquetes.paqueteMayorPeso(listaPaquetesPendientes);
    }
    public int busquedaBinaria(String codigo) {
        List<Paquete> ordenados = new ArrayList<>(listaPaquetesPendientes);
        Collections.sort(ordenados);
        return AnalisisPaquetes.busquedaBinaria(ordenados, codigo);
    }

    //Este método nos permite agruar los paquetes según el municipio destino
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

    //Este metodo se encarga de despachar el paquete con mayor prioridad y agregarlo al hostorial de entregados
    public Paquete despacharSiguientePaquete() {
        Paquete paqueteAtendido = despacharPaquetesPrioridad.poll();

        if (paqueteAtendido != null) {
            listaPaquetesPendientes.remove(paqueteAtendido);     // Lo remueve de pendientes
            historialPaquetesEntregados.agregar(paqueteAtendido);
        }

        return paqueteAtendido;
    }
    public Paquete atenderPorOrdenLlegada() {
        Paquete p = ordenLlegada.poll();
        if (p != null) {
            listaPaquetesPendientes.remove(p);
            despacharPaquetesPrioridad.remove(p);
        }
        return p;
    }

    public boolean eliminarDelHistorial(String codigoPaquete) {
        //Utilizamos un paquete auxiliar
        Paquete paqueteABuscar = new Paquete();
        paqueteABuscar.setCodigo(codigoPaquete);

        try {
            historialPaquetesEntregados.eliminar(paqueteABuscar);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

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
    public int cantidadEntregados() {
        return historialPaquetesEntregados.cantidadElementos();
    }

    public Paquete obtenerEntregado(int posicion) {
        return historialPaquetesEntregados.obtener(posicion);
    }

    public boolean fueEntregado(String codigo) {
        for (Paquete p : historialPaquetesEntregados) {
            if (p.getCodigo().equals(codigo)) return true;
        }
        return false;
    }
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
