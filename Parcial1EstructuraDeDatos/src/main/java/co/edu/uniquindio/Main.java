package co.edu.uniquindio;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        QuindioExpress sistema = new QuindioExpress();

        // cargar datos de los paquetes y repartidores
        cargarDatosIniciales(sistema);

        boolean salir = false;

        while (!salir) {
            String menu = """
                     QUINDÍO EXPRESS - SISTEMA DE OPERACIONES 
                    1. Registrar nuevo paquete
                    2. Registrar nuevo repartidor
                    3. Consultar paquete por código 
                    4. Ver municipios destino 
                    5. Ver paquetes agrupados por municipio
                    6. Atender paquete por orden de llegada 
                    7. Despachar siguiente paquete prioritario 
                    8. Asignar paquete a repartidor y realizar entrega
                    9. Demostrar ordenamientos alternativos ????
                    10. Análisis
                    11. Salir
                    
                    Seleccione una opción:
                    """;

            String opcionStr = JOptionPane.showInputDialog(null, menu, "Menú QuindíoExpress", JOptionPane.QUESTION_MESSAGE);

            if (opcionStr == null) {
                break; // Cierre de ventana
            }

            try {
                int opcion = Integer.parseInt(opcionStr.trim());

                switch (opcion) {
                    case 1 -> registrarPaqueteGUI(sistema);
                    case 2 -> registrarRepartidorGUI(sistema);
                    case 3 -> consultarPaqueteGUI(sistema);
                    case 4 -> mostrarMunicipiosGUI(sistema);
                    case 5 -> mostrarAgrupadosGUI(sistema);
                    case 6 -> atenderOrdenLlegadaGUI(sistema);
                    case 7 -> despacharPrioritarioGUI(sistema);
                    case 8 -> asignarYEntregarGUI(sistema);
                    case 9 -> mostrarComparadoresGUI(sistema);
                    case 10 -> mostrarAnalisisGUI(sistema);
                    case 11 -> salir = true;
                    default -> JOptionPane.showMessageDialog(null, "Opción no válida. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un número entero válido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(null, "¡Gracias por utilizar el sistema QuindíoExpress!", "Fin de Ejecución", JOptionPane.INFORMATION_MESSAGE);
    }


    // MÉTODOS AUXILIARES DE LA INTERFAZ


    private static void registrarPaqueteGUI(QuindioExpress sistema) {
        try {
            String codigo = JOptionPane.showInputDialog("Ingrese el código único del paquete (Ej: PQ999):");
            if (codigo == null || codigo.isBlank()) return;

            Municipio destinoEnum = (Municipio) JOptionPane.showInputDialog(
                    null, "Seleccione el municipio de destino:", "Destino",
                    JOptionPane.QUESTION_MESSAGE, null, Municipio.values(), Municipio.ARMENIA);
            if (destinoEnum == null) return;

            double peso = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el peso en kg"));
            int prioridad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la prioridad (1 a 5):"));
            int tiempo = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el tiempo estimado en minutos (> 0):"));

            Paquete nuevo = new Paquete(codigo.trim(), destinoEnum.name(), peso, prioridad, tiempo);
            boolean exito = sistema.registrarPaquete(nuevo);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Paquete " + nuevo.getCodigo() + " registrado con éxito.");
            } else {
                JOptionPane.showMessageDialog(null, "Error: Ya existe un paquete con el código " + codigo, "Error al Registrar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Datos inválidos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void registrarRepartidorGUI(QuindioExpress sistema) {
        try {
            String cedula = JOptionPane.showInputDialog("Ingrese la cédula/ID del repartidor:");
            if (cedula == null || cedula.isBlank()) return;

            String nombre = JOptionPane.showInputDialog("Ingrese el nombre completo:");
            if (nombre == null || nombre.isBlank()) return;

            Municipio zonaEnum = (Municipio) JOptionPane.showInputDialog(
                    null, "Seleccione la zona asignada:", "Zona Repartidor",
                    JOptionPane.QUESTION_MESSAGE, null, Municipio.values(), Municipio.ARMENIA);
            if (zonaEnum == null) return;

            Repartidor nuevo = new Repartidor(cedula.trim(), nombre.trim(), zonaEnum.name(), true);
            boolean exito = sistema.registrarRepartidor(nuevo);

            if (exito) {
                JOptionPane.showMessageDialog(null, "Repartidor " + nuevo.getNombre() + " registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(null, "Error: Ya existe un repartidor con la cédula " + cedula, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void consultarPaqueteGUI(QuindioExpress sistema) {
        String codigo = JOptionPane.showInputDialog("Ingrese el código del paquete a consultar:");
        if (codigo == null || codigo.isBlank()) return;

        Paquete p = sistema.consultarPorCodigo(codigo.trim());

        if (p != null) {
            JOptionPane.showMessageDialog(null, "INFORMACIÓN DEL PAQUETE\n" + p, "Consulta Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró ningún paquete activo con el código: " + codigo, "Paquete No Encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void mostrarMunicipiosGUI(QuindioExpress sistema) {
        Set<String> unicos = sistema.obtenerMunicipiosUnicos();
        Set<String> ordenados = sistema.obtenerMunicipiosOrdenados();

        String mensaje = "• Municipios sin repetir (HashSet):\n"
                + unicos + "\n"
                + "• Municipios ordenados alfabéticamente (TreeSet):\n"
                + ordenados;

        JOptionPane.showMessageDialog(null, mensaje, "Municipios Registrados", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarAgrupadosGUI(QuindioExpress sistema) {
        Map<String, List<Paquete>> agrupados = sistema.agruparPaquetesPorMunicipio();

        if (agrupados.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay paquetes pendientes para agrupar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String texto = "=== PAQUETES AGRUPADOS POR MUNICIPIO ===\n\n";

        // Iteramos directamente sobre cada municipio
        for (String mun : agrupados.keySet()) {
            List<Paquete> lista = agrupados.get(mun);

            texto += " " + mun + " (Total: " + lista.size() + " paquetes):\n";

            for (Paquete p : lista) {
                texto += "   • " + p.getCodigo() + " | Prioridad: " + p.getPrioridad() + " | Peso: " + p.getPeso() + " kg\n";
            }
            texto += "\n";
        }

        JOptionPane.showMessageDialog(null, texto, "Agrupación por Destino", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void atenderOrdenLlegadaGUI(QuindioExpress sistema) {
        Paquete p = sistema.atenderPorOrdenLlegada();
        if (p != null) {
            JOptionPane.showMessageDialog(null, "Atendido por ORDEN DE LLEGADA //(Queue):\n" + p, "Atención FIFO", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No hay paquetes pendientes en la cola de llegada.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void despacharPrioritarioGUI(QuindioExpress sistema) {
        Paquete p = sistema.despacharSiguientePaquete();
        if (p != null) {
            JOptionPane.showMessageDialog(null, "Despachado por MAYOR PRIORIDAD //(PriorityQueue):\n" + p, "Despacho Prioritario", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No hay paquetes pendientes para despachar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void asignarYEntregarGUI(QuindioExpress sistema) {
        String codigo = JOptionPane.showInputDialog("Ingrese el código del paquete a entregar:");
        String cedula = JOptionPane.showInputDialog("Ingrese la cédula del repartidor asignado:");

        if (codigo == null || cedula == null) return;

        try {
            sistema.asignarYEntregarPaquete(codigo.trim(), cedula.trim());
            JOptionPane.showMessageDialog(null, "¡Entrega registrada exitosamente!\nEl paquete " + codigo + " ha pasado al historial de entregas.", "Entrega Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo realizar la entrega:\n" + e.getMessage(), "Fallo en Asignación/Entrega", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void mostrarComparadoresGUI(QuindioExpress sistema) {
        List<Paquete> listaCopia = new ArrayList<>(sistema.getListaPaquetesPendientes());

        if (listaCopia.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay paquetes pendientes para ordenar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ordenamiento con ComparadorPorTiempo
        listaCopia.sort(new ComparadorPorTiempo());

        StringBuilder sb = new StringBuilder("PRIMEROS 5 PAQUETES ORDENADOS POR TIEMPO ESTIMADO (ASC) \n\n");
        for (int i = 0; i < Math.min(5, listaCopia.size()); i++) {
            sb.append("• ").append(listaCopia.get(i)).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Demostración de Comparator", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void mostrarAnalisisGUI(QuindioExpress sistema) {

        String[] opciones = {"Consultar peso total por municipio", "Búsqueda binaria por código", "Cantidad de paquetes mayores o iguales a una prioridad", "Paquete más pesado", "Volver"};
        boolean salir = false;

        while (!salir) {
            int eleccion = mostrarMenuVertical("Análisis de paquetes", opciones);

            switch (eleccion) {
                case 0 -> pesoTotalMunicipioGUI(sistema);
                case 1 -> busquedaBinariaGUI(sistema);
                case 2 -> paquetesPrioridadGUI(sistema);
                case 3 -> pesoMayorGUI(sistema);
                case 4, JOptionPane.CLOSED_OPTION -> salir = true; // "Volver" o cerrar con la X
            }
        }
    }
    public static void pesoTotalMunicipioGUI(QuindioExpress sistema) {
        String municipio = JOptionPane.showInputDialog("Municipio:");
        if (municipio == null) return;            // canceló
        municipio = municipio.trim();

        if (municipio.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El municipio no puede estar vacío",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = sistema.pesoTotalPorMunicipio(municipio);

        JOptionPane.showMessageDialog(null,
                String.format("Peso total hacia %s: %.1f kg", municipio, total),
                "Peso por municipio", JOptionPane.INFORMATION_MESSAGE);

    }
    private static void busquedaBinariaGUI(QuindioExpress sistema) {
        String codigo = JOptionPane.showInputDialog("Código del paquete:");
        if (codigo == null) return;               // canceló
        codigo = codigo.trim().toUpperCase();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El código no puede estar vacío",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int posicion = sistema.busquedaBinaria(codigo);

        if (posicion == -1) {
            JOptionPane.showMessageDialog(null, "El paquete " + codigo + " no existe",
                    "Búsqueda binaria", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "El paquete " + codigo + " está en la posición " + posicion
                            + " (índice, empezando en 0)",
                    "Búsqueda binaria", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public static void paquetesPrioridadGUI(QuindioExpress sistema){
        String texto = JOptionPane.showInputDialog("Prioridad mínima (0-5):");
        if (texto == null) return;                // canceló

        int minima;
        try {
            minima = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un número entero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (minima < 0 || minima > 5) {
            JOptionPane.showMessageDialog(null, "La prioridad debe estar entre 0 y 5",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = sistema.numeroPaquetesPrioridadMinima( minima);

        JOptionPane.showMessageDialog(null,
                "Paquetes con prioridad >= " + minima + ": " + cantidad,
                "Prioridad", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void pesoMayorGUI(QuindioExpress sistema){
        if (sistema.paqueteMayorPeso() == null){
            JOptionPane.showMessageDialog(null, "No hay paquetes registrados",
                    "Paquete más pesado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Paquete masPesado = sistema.paqueteMayorPeso();

        JOptionPane.showMessageDialog(null,
                "Paquete más pesado:\n\n"
                        + "Código: " + masPesado.getCodigo() + "\n"
                        + "Destino: " + masPesado.getDestino() + "\n"
                        + "Peso: " + masPesado.getPeso() + " kg\n"
                        + "Prioridad: " + masPesado.getPrioridad() + "\n"
                        + "Tiempo estimado: " + masPesado.getTiempoEstimado() + " min",
                "Paquete más pesado", JOptionPane.INFORMATION_MESSAGE);
    }
    private static int mostrarMenuVertical(String titulo, String[] opciones) {
        final int[] seleccion = {-1};                       // -1 si cierran con la X

        JDialog dialogo = new JDialog((Frame) null, titulo, true);   // true = modal
        dialogo.setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridLayout(opciones.length, 1, 0, 8)); // 1 columna
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        for (int i = 0; i < opciones.length; i++) {
            final int indice = i;
            JButton boton = new JButton(opciones[i]);
            boton.addActionListener(e -> {
                seleccion[0] = indice;
                dialogo.dispose();
            });
            panel.add(boton);
        }

        dialogo.add(new JLabel("¿Qué desea hacer?", SwingConstants.CENTER), BorderLayout.NORTH);
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);                // centrado en pantalla
        dialogo.setVisible(true);                           // se detiene aquí hasta que se cierre

        return seleccion[0];
    }
    // CARGA DE DATOS INICIALES (30 paquetes, 6 repartidores)


    private static void cargarDatosIniciales(QuindioExpress sistema) {
        // 6 repartidores cubriendo municipios del enum
        sistema.registrarRepartidor(new Repartidor("1001", "Carlos Pérez", "ARMENIA", true));
        sistema.registrarRepartidor(new Repartidor("1002", "Ana Gómez", "SALENTO", true));
        sistema.registrarRepartidor(new Repartidor("1003", "Luis Martínez", "CALARCA", true));
        sistema.registrarRepartidor(new Repartidor("1004", "Sofia López", "QUIMBAYA", true));
        sistema.registrarRepartidor(new Repartidor("1005", "Jorge Torres", "CIRCASIA", true));
        sistema.registrarRepartidor(new Repartidor("1006", "Elena Ríos", "LATEBAIDA", true));

        // 30 paquetes con datos variados, no consecutivos y varios destinos
        Paquete[] datos = {
                new Paquete("PQ731", "ARMENIA", 4.5, 3, 30),
                new Paquete("PQ105", "SALENTO", 3.0, 5, 25),
                new Paquete("PQ942", "ARMENIA", 7.2, 4, 20),
                new Paquete("PQ318", "CALARCA", 12.0, 2, 40),
                new Paquete("PQ567", "SALENTO", 1.5, 5, 15),
                new Paquete("PQ829", "ARMENIA", 2.3, 1, 50),
                new Paquete("PQ420", "QUIMBAYA", 8.4, 3, 30),
                new Paquete("PQ111", "CIRCASIA", 5.0, 4, 35),
                new Paquete("PQ205", "ARMENIA", 9.1, 2, 45),
                new Paquete("PQ333", "SALENTO", 6.2, 3, 20),
                new Paquete("PQ404", "CALARCA", 11.3, 5, 10),
                new Paquete("PQ505", "QUIMBAYA", 3.8, 1, 60),
                new Paquete("PQ606", "CIRCASIA", 4.1, 4, 25),
                new Paquete("PQ707", "ARMENIA", 15.0, 5, 50),
                new Paquete("PQ808", "SALENTO", 2.9, 3, 18),
                new Paquete("PQ909", "CALARCA", 7.7, 2, 22),
                new Paquete("PQ123", "QUIMBAYA", 1.2, 4, 12),
                new Paquete("PQ234", "CIRCASIA", 8.8, 5, 40),
                new Paquete("PQ345", "ARMENIA", 10.0, 1, 30),
                new Paquete("PQ456", "SALENTO", 6.5, 3, 28),
                new Paquete("PQ568", "CALARCA", 5.4, 4, 15),
                new Paquete("PQ679", "QUIMBAYA", 3.3, 2, 32),
                new Paquete("PQ780", "CIRCASIA", 14.2, 5, 55),
                new Paquete("PQ891", "ARMENIA", 0.9, 3, 14),
                new Paquete("PQ902", "SALENTO", 4.7, 4, 21),
                new Paquete("PQ013", "CALARCA", 2.8, 1, 45),
                new Paquete("PQ124", "QUIMBAYA", 6.0, 5, 27),
                new Paquete("PQ235", "LATEBAIDA", 1.9, 2, 19),
                new Paquete("PQ346", "ARMENIA", 8.1, 4, 33),
                new Paquete("PQ457", "SALENTO", 3.6, 3, 24)
        };

        for (Paquete p : datos) {
            sistema.registrarPaquete(p);
        }
    }
}