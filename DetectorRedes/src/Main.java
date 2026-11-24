import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {
        Grafo red = crearRed();
        boolean continuar = true;

        while (continuar) {
            String opcionTexto = JOptionPane.showInputDialog(
                null,
                "DETECTOR DE RIESGOS EN REDES INFORMATICAS\n\n"
                + "1. Mostrar dispositivos de la red\n"
                + "2. Mostrar conexiones y sus costos\n"
                + "3. Detectar dispositivos vulnerables\n"
                + "4. Recorrer la red con BFS\n"
                + "5. Recorrer la red con DFS\n"
                + "6. Buscar ruta con Dijkstra\n"
                + "7. Calcular rutas con Floyd-Warshall\n"
                + "8. Detectar dispositivos aislados\n"
                + "9. Ordenar dispositivos (MergeSort / QuickSort)\n"
                + "10. Buscar dispositivo por nombre\n"
                + "11. Guardar red en archivos\n"
                + "12. Cargar red desde archivos\n"
                + "13. Ver justificacion tecnica\n"
                + "0. Salir\n\n"
                + "Seleccione una opcion:",
                "Analizador de redes", JOptionPane.QUESTION_MESSAGE);

            if (opcionTexto == null) { continuar = false; continue; }

            try {
                int opcion = Integer.parseInt(opcionTexto.trim());
                String resultado = "";
                switch (opcion) {
                    case 1:  resultado = mostrarDispositivos(red); break;
                    case 2:  resultado = mostrarConexiones(red); break;
                    case 3:  resultado = mostrarVulnerabilidades(red); break;
                    case 4:  resultado = ejecutarBFS(red); break;
                    case 5:  resultado = ejecutarDFS(red); break;
                    case 6:  resultado = ejecutarDijkstra(red); break;
                    case 7:  resultado = ejecutarFloydWarshall(red); break;
                    case 8:  resultado = detectarAislados(red); break;
                    case 9:  resultado = ejecutarOrdenamiento(red); break;
                    case 10: resultado = ejecutarBusqueda(red); break;
                    case 11:
                        try {
                            GestorArchivos.guardar(red);
                            resultado = "Red guardada en 'datos_red/' "
                                + "(dispositivos.csv y conexiones.csv).";
                        } catch (IOException e) {
                            resultado = "Error al guardar: " + e.getMessage();
                        }
                        break;
                    case 12:
                        try {
                            red = GestorArchivos.cargar();
                            resultado = "Red cargada. Dispositivos: "
                                + red.getCantidadCiudades() + ".";
                        } catch (IOException e) {
                            resultado = "Error al cargar: " + e.getMessage();
                        }
                        break;
                    case 13: resultado = justificacionTecnica(); break;
                    case 0:
                        continuar = false;
                        JOptionPane.showMessageDialog(null, "Programa finalizado.");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opcion no valida.");
                }
                if (opcion >= 1 && opcion <= 13) mostrarResultado(resultado);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                    "Debe ingresar un numero entero.",
                    "Entrada no valida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private static String ejecutarOrdenamiento(Grafo red) {
        String[] criterios = {
            "Por nombre (alfabetico)",
            "Por cantidad de conexiones (descendente)",
            "Por nivel de riesgo (vulnerables primero)",
            "Por nombre usando QuickSort (comparativa)"
        };
        Object sel = JOptionPane.showInputDialog(
            null, "Criterio de ordenamiento:", "ORDENAMIENTO",
            JOptionPane.QUESTION_MESSAGE, null, criterios, criterios[0]);
        if (sel == null) return "Operacion cancelada.";

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < red.getCantidadCiudades(); i++) indices.add(i);

        Comparator<Integer> cmp;
        String etiqueta;
        int opcion = 0;
        for (int i = 0; i < criterios.length; i++) {
            if (criterios[i].equals(sel.toString())) { opcion = i; break; }
        }
        switch (opcion) {
            case 0:
                cmp = Comparator.comparing(red::getNombreCiudad, String.CASE_INSENSITIVE_ORDER);
                etiqueta = "nombre (MergeSort)"; break;
            case 1:
                cmp = Comparator.comparingInt(red::contarConexiones).reversed()
                    .thenComparing(red::getNombreCiudad);
                etiqueta = "cantidad de conexiones (MergeSort)"; break;
            case 2:
                cmp = Comparator.comparing((Integer i) -> !red.esVulnerable(i))
                    .thenComparing(red::getNombreCiudad);
                etiqueta = "nivel de riesgo (MergeSort)"; break;
            default:
                cmp = Comparator.comparing(red::getNombreCiudad, String.CASE_INSENSITIVE_ORDER);
                etiqueta = "nombre (QuickSort - comparativa)"; break;
        }

        long inicio = System.nanoTime();
        List<Integer> ordenados = (opcion == 3)
            ? AlgoritmosOrdenamiento.quickSort(indices, cmp)
            : AlgoritmosOrdenamiento.mergeSort(indices, cmp);
        long fin = System.nanoTime();

        StringBuilder sb = new StringBuilder("ORDENAMIENTO por " + etiqueta + "\n\n");
        for (int idx : ordenados) {
            sb.append(String.format(
                "%-26s | tipo: %-12s | conexiones: %d | vulnerable: %s%n",
                red.getNombreCiudad(idx), red.getTipo(idx),
                red.contarConexiones(idx),
                red.esVulnerable(idx) ? "SI" : "NO"));
        }
        sb.append(String.format("%nTiempo de ordenamiento: %.4f ms%n",
            (fin - inicio) / 1_000_000.0));
        sb.append("Complejidad MergeSort: O(n log n) garantizada.\n");
        sb.append("Complejidad QuickSort: O(n log n) promedio, O(n^2) peor caso.");
        return sb.toString();
    }

    private static String ejecutarBusqueda(Grafo red) {
        String texto = JOptionPane.showInputDialog(
            null, "Ingrese el nombre (o parte) del dispositivo a buscar:",
            "BUSQUEDA", JOptionPane.QUESTION_MESSAGE);
        if (texto == null || texto.isBlank()) return "Operacion cancelada.";
        final String objetivo = texto.trim().toLowerCase();

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < red.getCantidadCiudades(); i++) indices.add(i);
        indices = AlgoritmosOrdenamiento.mergeSort(indices,
            Comparator.comparing(red::getNombreCiudad, String.CASE_INSENSITIVE_ORDER));

        long t0 = System.nanoTime();
        int posBin = AlgoritmosBusqueda.busquedaBinaria(indices,
            idx -> red.getNombreCiudad(idx).compareToIgnoreCase(objetivo));
        long t1 = System.nanoTime();

        long t2 = System.nanoTime();
        List<Integer> parciales = new ArrayList<>();
        for (int i = 0; i < red.getCantidadCiudades(); i++) {
            if (red.getNombreCiudad(i).toLowerCase().contains(objetivo)) parciales.add(i);
        }
        long t3 = System.nanoTime();

        StringBuilder sb = new StringBuilder();
        sb.append("RESULTADO DE BUSQUEDA: \"").append(texto).append("\"\n\n");
        if (posBin >= 0) {
            int id = indices.get(posBin);
            sb.append("[Binaria - coincidencia exacta] ")
              .append(red.getNombreCiudad(id)).append(" (id=").append(id).append(")\n");
        } else {
            sb.append("[Binaria] Sin coincidencia exacta.\n");
        }
        sb.append("\n[Lineal - coincidencias parciales] ").append(parciales.size()).append("\n");
        for (int id : parciales) {
            sb.append("  - ").append(red.getNombreCiudad(id))
              .append(" (").append(red.getTipo(id)).append(")\n");
        }
        sb.append(String.format("%nTiempo busqueda binaria: %.4f ms (O(log n))",
            (t1 - t0) / 1_000_000.0));
        sb.append(String.format("%nTiempo busqueda lineal:   %.4f ms (O(n))",
            (t3 - t2) / 1_000_000.0));
        return sb.toString();
    }

    private static String justificacionTecnica() {
        return "JUSTIFICACION TECNICA\n\n"
            + "ORDENAMIENTO: MergeSort (principal) y QuickSort (alternativa)\n"
            + "  - MergeSort: O(n log n) garantizado en mejor, promedio y peor caso.\n"
            + "  - Estable: preserva orden de elementos iguales.\n"
            + "  - Apto para objetos con comparadores.\n"
            + "  - Espacio auxiliar O(n), aceptable para el tamano de la red.\n"
            + "  - QuickSort: menor constante practica, pero peor caso O(n^2).\n\n"
            + "BUSQUEDA: Binaria (O(log n)) y Lineal (O(n))\n"
            + "  - Binaria: reduce a la mitad el espacio en cada paso.\n"
            + "  - Lineal: para coincidencia parcial o listas no ordenadas.\n\n"
            + "ESTRUCTURA DE ARCHIVOS\n"
            + "  - datos_red/dispositivos.csv -> id|nombre|tipo|vulnerable\n"
            + "  - datos_red/conexiones.csv   -> origen|destino|costo\n"
            + "  - Separacion de responsabilidades (cargar solo dispositivos).\n"
            + "  - BufferedReader/BufferedWriter: E/S secuencial O(N + E).\n\n"
            + "ALGORITMOS DE GRAFOS\n"
            + "  - BFS: O(V+E), DFS: O(V+E).\n"
            + "  - Dijkstra: O((V+E) log V).\n"
            + "  - Floyd-Warshall: O(V^3).";
    }

    private static Grafo crearRed() {
        String[] nombres = {
            "Router principal", "Firewall", "Servidor de archivos",
            "Servidor de base de datos", "PC Administracion",
            "PC Contabilidad", "Laptop invitados", "Switch laboratorio"
        };
        String[] tipos = {
            "Router", "Firewall", "Servidor", "Servidor",
            "Computador", "Computador", "Portatil", "Switch"
        };
        boolean[] vulnerables = {
            false, false, true, false, false, true, true, false
        };
        Grafo red = new Grafo(nombres, tipos, vulnerables);
        red.agregarConexion(0, 1, 2);
        red.agregarConexion(1, 2, 4);
        red.agregarConexion(1, 3, 3);
        red.agregarConexion(1, 4, 5);
        red.agregarConexion(4, 5, 2);
        red.agregarConexion(4, 7, 3);
        red.agregarConexion(7, 6, 6);
        red.agregarConexion(2, 3, 2);
        return red;
    }

    private static String mostrarDispositivos(Grafo red) {
        StringBuilder sb = new StringBuilder("DISPOSITIVOS REGISTRADOS\n\n");
        for (int i = 0; i < red.getCantidadCiudades(); i++) {
            sb.append(i).append(" - ").append(red.getNombreCiudad(i))
              .append(" (").append(red.getTipo(i)).append(")\n");
        }
        return sb.toString();
    }

    private static String mostrarConexiones(Grafo red) {
        StringBuilder sb = new StringBuilder("CONEXIONES DE LA RED\n\n");
        for (int i = 0; i < red.getCantidadCiudades(); i++) {
            for (int j = i + 1; j < red.getCantidadCiudades(); j++) {
                if (red.existeArista(i, j)) {
                    sb.append(red.getNombreCiudad(i)).append(" <--> ")
                      .append(red.getNombreCiudad(j))
                      .append(" | costo: ").append(red.getCosto(i, j)).append("\n");
                }
            }
        }
        return sb.toString();
    }

    private static String mostrarVulnerabilidades(Grafo red) {
        StringBuilder sb = new StringBuilder("RESULTADO DEL ANALISIS\n\n");
        boolean encontro = false;
        for (int i = 0; i < red.getCantidadCiudades(); i++) {
            if (red.esVulnerable(i)) {
                encontro = true;
                sb.append("[ALERTA] ").append(red.getNombreCiudad(i))
                  .append(" (").append(red.getTipo(i))
                  .append(") aparece marcado como vulnerable.\n");
            }
        }
        if (!encontro) sb.append("No hay dispositivos marcados como vulnerables.\n");
        sb.append("\nNota: son alertas de datos simulados, no un escaneo real.");
        return sb.toString();
    }

    private static String ejecutarBFS(Grafo red) {
        Integer inicio = pedirDispositivo(red, "BFS - Seleccione el dispositivo inicial");
        if (inicio == null) return "Operacion cancelada.";
        List<Integer> recorrido = new BFS().recorrer(red, inicio);
        return imprimirRecorrido("BFS - recorrido por niveles", red, recorrido);
    }

    private static String ejecutarDFS(Grafo red) {
        Integer inicio = pedirDispositivo(red, "DFS - Seleccione el dispositivo inicial");
        if (inicio == null) return "Operacion cancelada.";
        List<Integer> recorrido = new DFS().recorrer(red, inicio);
        return imprimirRecorrido("DFS - recorrido en profundidad", red, recorrido);
    }

    private static String ejecutarDijkstra(Grafo red) {
        Integer origen = pedirDispositivo(red, "DIJKSTRA - Seleccione el origen");
        if (origen == null) return "Operacion cancelada.";
        Integer destino = pedirDispositivo(red, "DIJKSTRA - Seleccione el destino");
        if (destino == null) return "Operacion cancelada.";
        ResultadoDijkstra r = new Dijkstra().calcular(red, origen);
        return imprimirRuta("DIJKSTRA", red, r.getRutaHasta(destino),
            r.getDistanciaHasta(destino))
            + "\n\nEl costo representa el riesgo/costo simulado de las conexiones.";
    }

    private static String ejecutarFloydWarshall(Grafo red) {
        ResultadoFloydWarshall resultado = new FloydWarshall().calcular(red);
        StringBuilder sb = new StringBuilder("FLOYD-WARSHALL\nMATRIZ DE COSTOS MINIMOS\n\n");
        sb.append(String.format("%-8s", ""));
        for (int j = 0; j < red.getCantidadCiudades(); j++) {
            sb.append(String.format("%-8s", "D" + j));
        }
        sb.append("\n");
        for (int i = 0; i < red.getCantidadCiudades(); i++) {
            sb.append(String.format("%-8s", "D" + i));
            for (int j = 0; j < red.getCantidadCiudades(); j++) {
                int costo = resultado.getDistancia(i, j);
                sb.append(String.format("%-8s",
                    costo == Grafo.INFINITO ? "INF" : String.valueOf(costo)));
            }
            sb.append("  ").append(red.getNombreCiudad(i)).append("\n");
        }
        Integer origen = pedirDispositivo(red, "FLOYD-WARSHALL - Seleccione el origen");
        if (origen == null) return sb + "\nConsulta de ruta cancelada.";
        Integer destino = pedirDispositivo(red, "FLOYD-WARSHALL - Seleccione el destino");
        if (destino == null) return sb.toString();
        sb.append("\n").append(imprimirRuta("RUTA FLOYD-WARSHALL", red,
            resultado.getRuta(origen, destino), resultado.getDistancia(origen, destino)));
        return sb.toString();
    }

    private static String detectarAislados(Grafo red) {
        StringBuilder sb = new StringBuilder("DISPOSITIVOS AISLADOS\n\n");
        boolean encontro = false;
        for (int i = 0; i < red.getCantidadCiudades(); i++) {
            if (red.contarConexiones(i) == 0) {
                encontro = true;
                sb.append("[ALERTA] ").append(red.getNombreCiudad(i))
                  .append(" no tiene conexiones.\n");
            }
        }
        if (!encontro) sb.append("No se encontraron dispositivos aislados.");
        return sb.toString();
    }

    private static Integer pedirDispositivo(Grafo red, String titulo) {
        String[] opciones = new String[red.getCantidadCiudades()];
        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = i + " - " + red.getNombreCiudad(i);
        }
        Object sel = JOptionPane.showInputDialog(
            null, "Seleccione un dispositivo:", titulo,
            JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (sel == null) return null;
        String t = sel.toString();
        return Integer.parseInt(t.substring(0, t.indexOf(" - ")));
    }

    private static String imprimirRecorrido(String titulo, Grafo red, List<Integer> rec) {
        StringBuilder sb = new StringBuilder(titulo + "\n\n");
        for (int i = 0; i < rec.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(red.getNombreCiudad(rec.get(i)));
        }
        sb.append("\n\nDispositivos visitados: ").append(rec.size());
        return sb.toString();
    }

    private static String imprimirRuta(String titulo, Grafo red, List<Integer> ruta, int costo) {
        StringBuilder sb = new StringBuilder(titulo + "\n\n");
        if (ruta.isEmpty() || costo == Grafo.INFINITO) {
            return sb.append("No existe una ruta entre esos dispositivos.").toString();
        }
        for (int i = 0; i < ruta.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(red.getNombreCiudad(ruta.get(i)));
        }
        sb.append("\n\nCosto total: ").append(costo);
        return sb.toString();
    }

    private static void mostrarResultado(String texto) {
        javax.swing.JTextArea area = new javax.swing.JTextArea(texto, 20, 70);
        area.setEditable(false);
        area.setLineWrap(false);
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
        JOptionPane.showMessageDialog(null, scroll, "Resultado del analisis",
            JOptionPane.INFORMATION_MESSAGE);
    }
}