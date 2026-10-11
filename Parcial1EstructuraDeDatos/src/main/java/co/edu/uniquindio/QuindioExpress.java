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

    //el historial es respetando el orden de entrega
    private final ListaSimplementeEnlazada<Paquete> historialPaquetesEntregados;

    private final Map<String, Repartidor> mapaRepartidores;

    public QuindioExpress(){
        this.listaPaquetesPendientes = new LinkedList<>();
        this.mapaPaquetes = new HashMap<>();
        this.municipios = new HashSet<>();
        this.municipiosOrdenados = new TreeSet();
        this.despacharPaquetesPrioridad = new PriorityQueue<>(new ComparadorPorPrioridad());
        this.historialPaquetesEntregados = new ListaSimplementeEnlazada<>();
        this.mapaRepartidores = new HashMap();
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









}
