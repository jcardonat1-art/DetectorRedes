// Representa la red de dispositivos como un grafo.
// Se usa una matriz de costos: costos[i][j] es el costo de la conexion entre i y j.
public class Grafo {

    // Este valor significa "no hay conexion"
    public static final int INFINITO = 1000000000;

    private String[] nombres;
    private String[] tipos;
    private boolean[] vulnerables;
    private int[][] costos;

    public Grafo(String[] nombres, String[] tipos, boolean[] vulnerables) {
        this.nombres = nombres;
        this.tipos = tipos;
        this.vulnerables = vulnerables;

        // Al inicio no hay conexiones: todo es INFINITO,
        // menos la diagonal (un dispositivo hacia si mismo cuesta 0)
        int n = nombres.length;
        costos = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    costos[i][j] = 0;
                } else {
                    costos[i][j] = INFINITO;
                }
            }
        }
    }

    // Conecta dos dispositivos. La conexion funciona en los dos sentidos.
    public void agregarConexion(int origen, int destino, int costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        costos[origen][destino] = costo;
        costos[destino][origen] = costo;
    }

    public int getCantidad() {
        return nombres.length;
    }

    public String getNombre(int i) {
        return nombres[i];
    }

    public String getTipo(int i) {
        return tipos[i];
    }

    public boolean esVulnerable(int i) {
        return vulnerables[i];
    }

    public int getCosto(int origen, int destino) {
        return costos[origen][destino];
    }

    // Dice si hay una conexion directa entre dos dispositivos distintos
    public boolean existeConexion(int origen, int destino) {
        return origen != destino && costos[origen][destino] != INFINITO;
    }

    // Cuenta con cuantos dispositivos esta conectado el dispositivo i
    public int contarConexiones(int i) {
        int cantidad = 0;
        for (int j = 0; j < nombres.length; j++) {
            if (existeConexion(i, j)) {
                cantidad++;
            }
        }
        return cantidad;
    }
}
     
