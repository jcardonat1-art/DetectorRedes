import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ResultadoFloydWarshall {
    private final int[][] distancias;
    private final int[][] siguiente;
    private final List<Actualizacion> actualizaciones;

    ResultadoFloydWarshall(int[][] distancias, int[][] siguiente,
                           List<Actualizacion> actualizaciones) {
        this.distancias = copiarMatriz(distancias);
        this.siguiente = copiarMatriz(siguiente);
        this.actualizaciones = Collections.unmodifiableList(new ArrayList<>(actualizaciones));
    }

    public int getCantidadCiudades() { return distancias.length; }

    public int getDistancia(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        return distancias[origen][destino];
    }

    public List<Integer> getRuta(int origen, int destino) {
        validarIndice(origen);
        validarIndice(destino);
        if (siguiente[origen][destino] == -1) return Collections.emptyList();
        List<Integer> ruta = new ArrayList<>();
        int actual = origen;
        ruta.add(actual);
        while (actual != destino) {
            actual = siguiente[actual][destino];
            if (actual == -1) return Collections.emptyList();
            ruta.add(actual);
        }
        return Collections.unmodifiableList(ruta);
    }

    public List<Actualizacion> getActualizaciones() { return actualizaciones; }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= distancias.length) {
            throw new IndexOutOfBoundsException("Indice de ciudad fuera del resultado: " + indice);
        }
    }

    private static int[][] copiarMatriz(int[][] matriz) {
        int[][] copia = new int[matriz.length][];
        for (int i = 0; i < matriz.length; i++) copia[i] = matriz[i].clone();
        return copia;
    }

    public static final class Actualizacion {
        private final int ciudadIntermedia;
        private final int origen;
        private final int destino;
        private final int distanciaAnterior;
        private final int distanciaNueva;

        Actualizacion(int ciudadIntermedia, int origen, int destino,
                      int distanciaAnterior, int distanciaNueva) {
            this.ciudadIntermedia = ciudadIntermedia;
            this.origen = origen;
            this.destino = destino;
            this.distanciaAnterior = distanciaAnterior;
            this.distanciaNueva = distanciaNueva;
        }
        public int getCiudadIntermedia() { return ciudadIntermedia; }
        public int getOrigen() { return origen; }
        public int getDestino() { return destino; }
        public int getDistanciaAnterior() { return distanciaAnterior; }
        public int getDistanciaNueva() { return distanciaNueva; }
    }
}
