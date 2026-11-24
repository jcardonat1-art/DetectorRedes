import java.util.Arrays;

public class Grafo {
    public static final int INFINITO = 1_000_000_000;

    private final String[] dispositivos;
    private final String[] tipos;
    private final boolean[] vulnerables;
    private final int[][] costos;

    public Grafo(String[] dispositivos, String[] tipos, boolean[] vulnerables) {
        if (dispositivos == null || dispositivos.length == 0) {
            throw new IllegalArgumentException("Debe existir al menos un dispositivo.");
        }
        if (tipos.length != dispositivos.length || vulnerables.length != dispositivos.length) {
            throw new IllegalArgumentException("Los datos de los dispositivos no coinciden.");
        }
        this.dispositivos = dispositivos.clone();
        this.tipos = tipos.clone();
        this.vulnerables = vulnerables.clone();
        costos = new int[dispositivos.length][dispositivos.length];
        for (int i = 0; i < costos.length; i++) {
            Arrays.fill(costos[i], INFINITO);
            costos[i][i] = 0;
        }
    }

    public void agregarConexion(int origen, int destino, int costo) {
        validarIndice(origen);
        validarIndice(destino);
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        costos[origen][destino] = costo;
        costos[destino][origen] = costo;
    }

    public int getCantidadCiudades() { return dispositivos.length; }

    public String getNombreCiudad(int indice) {
        validarIndice(indice);
        return dispositivos[indice];
    }

    public String getTipo(int indice) {
        validarIndice(indice);
        return tipos[indice];
    }

    public boolean esVulnerable(int indice) {
        validarIndice(indice);
        return vulnerables[indice];
    }

    public int getCosto(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        return costos[origen][destino];
    }

    public boolean existeArista(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        return origen != destino && costos[origen][destino] != INFINITO;
    }

    public int contarConexiones(int dispositivo) {
        validarIndice(dispositivo);
        int cantidad = 0;
        for (int i = 0; i < dispositivos.length; i++) {
            if (existeArista(dispositivo, i)) cantidad++;
        }
        return cantidad;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= dispositivos.length) {
            throw new IndexOutOfBoundsException("Dispositivo no valido: " + indice);
        }
    }
}