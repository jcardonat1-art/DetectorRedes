import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {

    public static void main(String[] args) {
        Grafo red = crearRed();
        boolean continuar = true;

        while (continuar) {
            // Menu principal
            String opcionTexto = JOptionPane.showInputDialog(null,
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

            // Si el usuario cierra o cancela la ventana, el programa termina
            if (opcionTexto == null) {
                continuar = false;
            } else {
                try {
                    int opcion = Integer.parseInt(opcionTexto.trim());
                    String resultado = "";

                    switch (opcion) {
                        case 1:
                            resultado = mostrarDispositivos(red);
                            break;
                        case 2:
                            resultado = mostrarConexiones(red);
                            break;
                        case 3:
                            resultado = mostrarVulnerabilidades(red);
                            break;
                        case 4:
                            resultado = ejecutarBFS(red);
                            break;
                        case 5:
                            resultado = ejecutarDFS(red);
                            break;
                        case 6:
                            resultado = ejecutarDijkstra(red);
                            break;
                        case 7:
                            resultado = ejecutarFloydWarshall(red);
                            break;
                        case 8:
                            resultado = detectarAislados(red);
                            break;
                        case 9:
                            resultado = ejecutarOrdenamiento(red);
                            break;
                        case 10:
                            resultado = ejecutarBusqueda(red);
                            break;
                        case 11:
                            try {
                                GestorArchivos.guardar(red);
                                resultado = "Red guardada en 'datos_red/' "
                                    + "(dispositivos.csv y conexiones.csv).";
                            } catch (Exception e) {
                                resultado = "Error al guardar: " + e.getMessage();
                            }
                            break;
                        case 12:
                            try {
                                red = GestorArchivos.cargar();
                                resultado = "Red cargada. Dispositivos: " + red.getCantidad() + ".";
                            } catch (Exception e) {
                                resultado = "Error al cargar: " + e.getMessage();
                            }
                            break;
                        case 13:
                            resultado = justificacionTecnica();
                            break;
                        case 0:
                            continuar = false;
                            JOptionPane.showMessageDialog(null, "Programa finalizado.");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Opcion no valida.");
                    }

                    // Se muestra el resultado de las opciones 1 a 13
                    if (opcion >= 1 && opcion <= 13) {
                        mostrarResultado(resultado);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un numero entero.",
                        "Entrada no valida", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    // ------------------------------------------------------------
    // DATOS DE EJEMPLO
    // ------------------------------------------------------------

    // Crea la red de prueba (los datos son simulados)
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

        // agregarConexion(dispositivo A, dispositivo B, costo)
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

    // ------------------------------------------------------------
    // OPCIONES BASICAS (1, 2, 3 y 8)
    // ------------------------------------------------------------

    private static String mostrarDispositivos(Grafo red) {
        String texto = "DISPOSITIVOS REGISTRADOS\n\n";
        for (int i = 0; i < red.getCantidad(); i++) {
            texto += i + " - " + red.getNombre(i) + " (" + red.getTipo(i) + ")\n";
        }
        return texto;
    }

    private static String mostrarConexiones(Grafo red) {
        String texto = "CONEXIONES DE LA RED\n\n";
        for (int i = 0; i < red.getCantidad(); i++) {
            for (int j = i + 1; j < red.getCantidad(); j++) {
                if (red.existeConexion(i, j)) {
                    texto += red.getNombre(i) + " <--> " + red.getNombre(j)
                        + " | costo: " + red.getCosto(i, j) + "\n";
                }
            }
        }
        return texto;
    }

    private static String mostrarVulnerabilidades(Grafo red) {
        String texto = "RESULTADO DEL ANALISIS\n\n";
        boolean encontro = false;
        for (int i = 0; i < red.getCantidad(); i++) {
            if (red.esVulnerable(i)) {
                encontro = true;
                texto += "[ALERTA] " + red.getNombre(i) + " (" + red.getTipo(i)
                    + ") aparece marcado como vulnerable.\n";
            }
        }
        if (!encontro) {
            texto += "No hay dispositivos marcados como vulnerables.\n";
        }
        texto += "\nNota: son alertas de datos simulados, no un escaneo real.";
        return texto;
    }

    // Un dispositivo aislado es el que no tiene ninguna conexion
    private static String detectarAislados(Grafo red) {
        String texto = "DISPOSITIVOS AISLADOS\n\n";
        boolean encontro = false;
        for (int i = 0; i < red.getCantidad(); i++) {
            if (red.contarConexiones(i) == 0) {
                encontro = true;
                texto += "[ALERTA] " + red.getNombre(i) + " no tiene conexiones.\n";
            }
        }
        if (!encontro) {
            texto += "No se encontraron dispositivos aislados.";
        }
        return texto;
    }

    // ------------------------------------------------------------
    // RECORRIDOS Y RUTAS (4, 5, 6 y 7)
    // ------------------------------------------------------------

    private static String ejecutarBFS(Grafo red) {
        int inicio = pedirDispositivo(red, "BFS - Seleccione el dispositivo inicial");
        if (inicio == -1) {
            return "Operacion cancelada.";
        }
        BFS bfs = new BFS();
        ArrayList<Integer> recorrido = bfs.recorrer(red, inicio);
        return recorridoATexto("BFS - recorrido por niveles", red, recorrido);
    }

    private static String ejecutarDFS(Grafo red) {
        int inicio = pedirDispositivo(red, "DFS - Seleccione el dispositivo inicial");
        if (inicio == -1) {
            return "Operacion cancelada.";
        }
        DFS dfs = new DFS();
        ArrayList<Integer> recorrido = dfs.recorrer(red, inicio);
        return recorridoATexto("DFS - recorrido en profundidad", red, recorrido);
    }

    private static String ejecutarDijkstra(Grafo red) {
        int origen = pedirDispositivo(red, "DIJKSTRA - Seleccione el origen");
        if (origen == -1) {
            return "Operacion cancelada.";
        }
        int destino = pedirDispositivo(red, "DIJKSTRA - Seleccione el destino");
        if (destino == -1) {
            return "Operacion cancelada.";
        }

        Dijkstra dijkstra = new Dijkstra();
        ResultadoDijkstra resultado = dijkstra.calcular(red, origen);

        return rutaATexto("DIJKSTRA", red, resultado.getRutaHasta(destino),
                resultado.getDistanciaHasta(destino))
            + "\n\nEl costo representa el riesgo/costo simulado de las conexiones.";
    }

    private static String ejecutarFloydWarshall(Grafo red) {
        FloydWarshall floyd = new FloydWarshall();
        ResultadoFloydWarshall resultado = floyd.calcular(red);

        // Se arma la matriz de costos minimos
        String texto = "FLOYD-WARSHALL\nMATRIZ DE COSTOS MINIMOS\n\n";
        texto += String.format("%-8s", "");
        for (int j = 0; j < red.getCantidad(); j++) {
            texto += String.format("%-8s", "D" + j);
        }
        texto += "\n";

        for (int i = 0; i < red.getCantidad(); i++) {
            texto += String.format("%-8s", "D" + i);
            for (int j = 0; j < red.getCantidad(); j++) {
                int costo = resultado.getDistancia(i, j);
                if (costo == Grafo.INFINITO) {
                    texto += String.format("%-8s", "INF");
                } else {
                    texto += String.format("%-8s", String.valueOf(costo));
                }
            }
            texto += "  " + red.getNombre(i) + "\n";
        }

        // Despues de la matriz, se pide una ruta especifica
        int origen = pedirDispositivo(red, "FLOYD-WARSHALL - Seleccione el origen");
        if (origen == -1) {
            return texto + "\nConsulta de ruta cancelada.";
        }
        int destino = pedirDispositivo(red, "FLOYD-WARSHALL - Seleccione el destino");
        if (destino == -1) {
            return texto;
        }

        texto += "\n" + rutaATexto("RUTA FLOYD-WARSHALL", red,
                resultado.getRuta(origen, destino), resultado.getDistancia(origen, destino));
        return texto;
    }

    // ------------------------------------------------------------
    // ORDENAMIENTO Y BUSQUEDA (9 y 10)
    // ------------------------------------------------------------

    private static String ejecutarOrdenamiento(Grafo red) {
        String[] criterios = {
            "Por nombre (alfabetico)",
            "Por cantidad de conexiones (descendente)",
            "Por nivel de riesgo (vulnerables primero)",
            "Por nombre usando QuickSort (comparativa)"
        };
        Object seleccion = JOptionPane.showInputDialog(null, "Criterio de ordenamiento:",
            "ORDENAMIENTO", JOptionPane.QUESTION_MESSAGE, null, criterios, criterios[0]);
        if (seleccion == null) {
            return "Operacion cancelada.";
        }

        // Se averigua cual opcion eligio el usuario (0, 1, 2 o 3)
        int opcion = 0;
        for (int i = 0; i < criterios.length; i++) {
            if (criterios[i].equals(seleccion)) {
                opcion = i;
            }
        }

        // Arreglo con los numeros de los dispositivos: 0, 1, 2, ...
        int[] ids = new int[red.getCantidad()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = i;
        }

        // Se ordena y se mide el tiempo
        String etiqueta;
        long inicio = System.nanoTime();
        if (opcion == 0) {
            AlgoritmosOrdenamiento.mergeSort(ids, red, AlgoritmosOrdenamiento.POR_NOMBRE);
            etiqueta = "nombre (MergeSort)";
        } else if (opcion == 1) {
            AlgoritmosOrdenamiento.mergeSort(ids, red, AlgoritmosOrdenamiento.POR_CONEXIONES);
            etiqueta = "cantidad de conexiones (MergeSort)";
        } else if (opcion == 2) {
            AlgoritmosOrdenamiento.mergeSort(ids, red, AlgoritmosOrdenamiento.POR_RIESGO);
            etiqueta = "nivel de riesgo (MergeSort)";
        } else {
            AlgoritmosOrdenamiento.quickSort(ids, red, AlgoritmosOrdenamiento.POR_NOMBRE);
            etiqueta = "nombre (QuickSort - comparativa)";
        }
        long fin = System.nanoTime();

        // Se muestra la lista ya ordenada
        String texto = "ORDENAMIENTO por " + etiqueta + "\n\n";
        for (int i = 0; i < ids.length; i++) {
            int id = ids[i];
            String vulnerable = "NO";
            if (red.esVulnerable(id)) {
                vulnerable = "SI";
            }
            texto += String.format("%-26s | tipo: %-12s | conexiones: %d | vulnerable: %s\n",
                red.getNombre(id), red.getTipo(id), red.contarConexiones(id), vulnerable);
        }
        texto += String.format("\nTiempo de ordenamiento: %.4f ms\n", (fin - inicio) / 1000000.0);
        texto += "Complejidad MergeSort: O(n log n) garantizada.\n";
        texto += "Complejidad QuickSort: O(n log n) promedio, O(n^2) peor caso.";
        return texto;
    }

    private static String ejecutarBusqueda(Grafo red) {
        String texto = JOptionPane.showInputDialog(null,
            "Ingrese el nombre (o parte) del dispositivo a buscar:",
            "BUSQUEDA", JOptionPane.QUESTION_MESSAGE);
        if (texto == null || texto.trim().isEmpty()) {
            return "Operacion cancelada.";
        }
        texto = texto.trim();

        // La busqueda binaria necesita los dispositivos ordenados por nombre
        int[] ids = new int[red.getCantidad()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = i;
        }
        AlgoritmosOrdenamiento.mergeSort(ids, red, AlgoritmosOrdenamiento.POR_NOMBRE);

        // Busqueda binaria (nombre exacto)
        long t0 = System.nanoTime();
        int posicion = AlgoritmosBusqueda.busquedaBinaria(ids, red, texto);
        long t1 = System.nanoTime();

        // Busqueda lineal (coincidencias parciales)
        long t2 = System.nanoTime();
        ArrayList<Integer> parciales = AlgoritmosBusqueda.busquedaLineal(red, texto);
        long t3 = System.nanoTime();

        String resultado = "RESULTADO DE BUSQUEDA: \"" + texto + "\"\n\n";
        if (posicion >= 0) {
            int id = ids[posicion];
            resultado += "[Binaria - coincidencia exacta] " + red.getNombre(id)
                + " (id=" + id + ")\n";
        } else {
            resultado += "[Binaria] Sin coincidencia exacta.\n";
        }

        resultado += "\n[Lineal - coincidencias parciales] " + parciales.size() + "\n";
        for (int i = 0; i < parciales.size(); i++) {
            int id = parciales.get(i);
            resultado += "  - " + red.getNombre(id) + " (" + red.getTipo(id) + ")\n";
        }

        resultado += String.format("\nTiempo busqueda binaria: %.4f ms (O(log n))",
            (t1 - t0) / 1000000.0);
        resultado += String.format("\nTiempo busqueda lineal:   %.4f ms (O(n))",
            (t3 - t2) / 1000000.0);
        return resultado;
    }

    // ------------------------------------------------------------
    // JUSTIFICACION TECNICA (13)
    // ------------------------------------------------------------

    private static String justificacionTecnica() {
        return "JUSTIFICACION TECNICA\n\n"
            + "ORDENAMIENTO: MergeSort (principal) y QuickSort (alternativa)\n"
            + "  - MergeSort: O(n log n) garantizado en mejor, promedio y peor caso.\n"
            + "  - Usa un arreglo auxiliar O(n), aceptable para el tamano de la red.\n"
            + "  - QuickSort: suele ser rapido, pero en el peor caso es O(n^2).\n\n"
            + "BUSQUEDA: Binaria (O(log n)) y Lineal (O(n))\n"
            + "  - Binaria: descarta la mitad de los datos en cada paso (lista ordenada).\n"
            + "  - Lineal: sirve para coincidencias parciales o listas sin ordenar.\n\n"
            + "ARCHIVOS\n"
            + "  - datos_red/dispositivos.csv -> id|nombre|tipo|vulnerable\n"
            + "  - datos_red/conexiones.csv   -> origen|destino|costo\n"
            + "  - Se leen y escriben linea por linea con BufferedReader/BufferedWriter.\n\n"
            + "ALGORITMOS DE GRAFOS (la red se guarda en una matriz de costos)\n"
            + "  - BFS y DFS: O(V^2), porque por cada dispositivo se revisa toda su fila.\n"
            + "  - Dijkstra: O(V^2), busca la ruta de menor costo desde un origen.\n"
            + "  - Floyd-Warshall: O(V^3), calcula las rutas entre todos los pares.";
    }

    // ------------------------------------------------------------
    // METODOS AUXILIARES
    // ------------------------------------------------------------

    // Muestra una lista desplegable con los dispositivos.
    // Devuelve el numero del dispositivo elegido, o -1 si el usuario cancela.
    private static int pedirDispositivo(Grafo red, String titulo) {
        String[] opciones = new String[red.getCantidad()];
        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = i + " - " + red.getNombre(i);
        }
        Object seleccion = JOptionPane.showInputDialog(null, "Seleccione un dispositivo:",
            titulo, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) {
            return -1;
        }
        // El texto es "3 - Servidor", se toma solo el numero del inicio
        String texto = seleccion.toString();
        return Integer.parseInt(texto.substring(0, texto.indexOf(" - ")));
    }

    // Convierte un recorrido (BFS o DFS) en texto: A -> B -> C
    private static String recorridoATexto(String titulo, Grafo red, ArrayList<Integer> recorrido) {
        String texto = titulo + "\n\n";
        for (int i = 0; i < recorrido.size(); i++) {
            if (i > 0) {
                texto += " -> ";
            }
            texto += red.getNombre(recorrido.get(i));
        }
        texto += "\n\nDispositivos visitados: " + recorrido.size();
        return texto;
    }

    // Convierte una ruta en texto, con su costo total
    private static String rutaATexto(String titulo, Grafo red, ArrayList<Integer> ruta, int costo) {
        String texto = titulo + "\n\n";
        if (ruta.isEmpty() || costo == Grafo.INFINITO) {
            return texto + "No existe una ruta entre esos dispositivos.";
        }
        for (int i = 0; i < ruta.size(); i++) {
            if (i > 0) {
                texto += " -> ";
            }
            texto += red.getNombre(ruta.get(i));
        }
        texto += "\n\nCosto total: " + costo;
        return texto;
    }

    // Muestra el resultado en una ventana con barra de desplazamiento
    private static void mostrarResultado(String texto) {
        JTextArea area = new JTextArea(texto, 20, 70);
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(null, scroll, "Resultado del analisis",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
