import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Guarda y carga la red en dos archivos de texto dentro de la carpeta datos_red:
//   dispositivos.csv -> id|nombre|tipo|vulnerable
//   conexiones.csv   -> origen|destino|costo
public class GestorArchivos {

    private static final String CARPETA = "datos_red";
    private static final String ARCHIVO_DISPOSITIVOS = "datos_red/dispositivos.csv";
    private static final String ARCHIVO_CONEXIONES = "datos_red/conexiones.csv";

    // Guarda la red en los dos archivos
    public static void guardar(Grafo red) throws IOException {
        new File(CARPETA).mkdir();   // crea la carpeta si no existe

        // 1. Archivo de dispositivos (try-with-resources cierra el archivo solo)
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO_DISPOSITIVOS))) {
            escritor.write("id|nombre|tipo|vulnerable");
            escritor.newLine();
            for (int i = 0; i < red.getCantidad(); i++) {
                escritor.write(i + "|" + red.getNombre(i) + "|" + red.getTipo(i)
                        + "|" + red.esVulnerable(i));
                escritor.newLine();
            }
        }

        // 2. Archivo de conexiones (solo j > i para no repetir la misma conexion)
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO_CONEXIONES))) {
            escritor.write("origen|destino|costo");
            escritor.newLine();
            for (int i = 0; i < red.getCantidad(); i++) {
                for (int j = i + 1; j < red.getCantidad(); j++) {
                    if (red.existeConexion(i, j)) {
                        escritor.write(i + "|" + j + "|" + red.getCosto(i, j));
                        escritor.newLine();
                    }
                }
            }
        }
    }

    // Lee los archivos y reconstruye la red
    public static Grafo cargar() throws IOException {
        File archivoDispositivos = new File(ARCHIVO_DISPOSITIVOS);
        File archivoConexiones = new File(ARCHIVO_CONEXIONES);

        if (!archivoDispositivos.exists()) {
            throw new IOException("No existe " + ARCHIVO_DISPOSITIVOS + ". Guarde la red primero.");
        }

        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<String> tipos = new ArrayList<>();
        ArrayList<Boolean> vulnerables = new ArrayList<>();

        // 1. Leer los dispositivos
        try (BufferedReader lector = new BufferedReader(new FileReader(archivoDispositivos))) {
            lector.readLine();   // se salta la primera linea (el encabezado)
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                // El caracter | es especial en split, por eso se escribe \\|
                String[] partes = linea.split("\\|");
                if (partes.length < 4) {
                    continue;
                }
                nombres.add(partes[1]);
                tipos.add(partes[2]);
                vulnerables.add(Boolean.parseBoolean(partes[3].trim()));
            }
        }

        if (nombres.isEmpty()) {
            throw new IOException("El archivo de dispositivos esta vacio.");
        }

        // Pasar las listas a arreglos para crear el Grafo
        int n = nombres.size();
        String[] arregloNombres = new String[n];
        String[] arregloTipos = new String[n];
        boolean[] arregloVulnerables = new boolean[n];
        for (int i = 0; i < n; i++) {
            arregloNombres[i] = nombres.get(i);
            arregloTipos[i] = tipos.get(i);
            arregloVulnerables[i] = vulnerables.get(i);
        }
        Grafo red = new Grafo(arregloNombres, arregloTipos, arregloVulnerables);

        // 2. Leer las conexiones (si el archivo existe)
        if (archivoConexiones.exists()) {
            try (BufferedReader lector = new BufferedReader(new FileReader(archivoConexiones))) {
                lector.readLine();   // se salta el encabezado
                String linea;
                while ((linea = lector.readLine()) != null) {
                    if (linea.trim().isEmpty()) {
                        continue;
                    }
                    String[] partes = linea.split("\\|");
                    if (partes.length < 3) {
                        continue;
                    }
                    int origen = Integer.parseInt(partes[0].trim());
                    int destino = Integer.parseInt(partes[1].trim());
                    int costo = Integer.parseInt(partes[2].trim());
                    red.agregarConexion(origen, destino, costo);
                }
            }
        }
        return red;
    }
}
