import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia en datos_red/dispositivos.csv y datos_red/conexiones.csv.
 * E/S secuencial O(N+E) con BufferedWriter/BufferedReader.
 */
public final class GestorArchivos {

    private static final String DIRECTORIO = "datos_red";
    private static final String ARCHIVO_DISPOSITIVOS = "dispositivos.csv";
    private static final String ARCHIVO_CONEXIONES = "conexiones.csv";
    private static final String SEPARADOR = "|";

    private GestorArchivos() { }

    public static void guardar(Grafo red) throws IOException {
        Files.createDirectories(Paths.get(DIRECTORIO));
        try (BufferedWriter w = Files.newBufferedWriter(
                Paths.get(DIRECTORIO, ARCHIVO_DISPOSITIVOS), StandardCharsets.UTF_8)) {
            w.write("id" + SEPARADOR + "nombre" + SEPARADOR + "tipo" + SEPARADOR + "vulnerable");
            w.newLine();
            for (int i = 0; i < red.getCantidadCiudades(); i++) {
                w.write(i + SEPARADOR + red.getNombreCiudad(i) + SEPARADOR
                    + red.getTipo(i) + SEPARADOR + red.esVulnerable(i));
                w.newLine();
            }
        }
        try (BufferedWriter w = Files.newBufferedWriter(
                Paths.get(DIRECTORIO, ARCHIVO_CONEXIONES), StandardCharsets.UTF_8)) {
            w.write("origen" + SEPARADOR + "destino" + SEPARADOR + "costo");
            w.newLine();
            for (int i = 0; i < red.getCantidadCiudades(); i++) {
                for (int j = i + 1; j < red.getCantidadCiudades(); j++) {
                    if (red.existeArista(i, j)) {
                        w.write(i + SEPARADOR + j + SEPARADOR + red.getCosto(i, j));
                        w.newLine();
                    }
                }
            }
        }
    }

    public static Grafo cargar() throws IOException {
        Path rutaDisp = Paths.get(DIRECTORIO, ARCHIVO_DISPOSITIVOS);
        Path rutaConex = Paths.get(DIRECTORIO, ARCHIVO_CONEXIONES);
        if (!Files.exists(rutaDisp)) {
            throw new IOException("No existe " + rutaDisp + ". Guarde la red primero.");
        }
        List<String> nombres = new ArrayList<>();
        List<String> tipos = new ArrayList<>();
        List<Boolean> vulnerables = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(rutaDisp, StandardCharsets.UTF_8)) {
            String linea;
            boolean primera = true;
            while ((linea = r.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.isBlank()) continue;
                String[] partes = linea.split("\\" + SEPARADOR, -1);
                if (partes.length < 4) continue;
                nombres.add(partes[1]);
                tipos.add(partes[2]);
                vulnerables.add(Boolean.parseBoolean(partes[3].trim()));
            }
        }
        if (nombres.isEmpty()) throw new IOException("El archivo de dispositivos esta vacio.");
        Grafo red = new Grafo(nombres.toArray(new String[0]),
                              tipos.toArray(new String[0]),
                              convertir(vulnerables));
        if (Files.exists(rutaConex)) {
            try (BufferedReader r = Files.newBufferedReader(rutaConex, StandardCharsets.UTF_8)) {
                String linea;
                boolean primera = true;
                while ((linea = r.readLine()) != null) {
                    if (primera) { primera = false; continue; }
                    if (linea.isBlank()) continue;
                    String[] partes = linea.split("\\" + SEPARADOR, -1);
                    if (partes.length < 3) continue;
                    int origen = Integer.parseInt(partes[0].trim());
                    int destino = Integer.parseInt(partes[1].trim());
                    int costo = Integer.parseInt(partes[2].trim());
                    red.agregarConexion(origen, destino, costo);
                }
            }
        }
        return red;
    }

    private static boolean[] convertir(List<Boolean> lista) {
        boolean[] arr = new boolean[lista.size()];
        for (int i = 0; i < lista.size(); i++) arr[i] = lista.get(i);
        return arr;
    }
}