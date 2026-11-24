import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class ResultadoDijkstra {
    private final int origen;
    private final int[] distancias;
    private final int[] predecesores;
    private final List<Estado> estados;

    ResultadoDijkstra(int origen, int[] distancias, int[] predecesores,
                      List<Estado> estados) {
        this.origen = origen;
        this.distancias = distancias.clone();
        this.predecesores = predecesores.clone();
        this.estados = Collections.unmodifiableList(new ArrayList<>(estados));
    }

    public int getDistanciaHasta(int destino) {
        validarIndice(destino);
        return distancias[destino];
    }

    public int getPredecesorDe(int ciudad) {
        validarIndice(ciudad);
        return predecesores[ciudad];
    }

    public List<Integer> getRutaHasta(int destino) {
        validarIndice(destino);
        LinkedList<Integer> ruta = new LinkedList<>();
        for (int actual = destino; actual != -1; actual = predecesores[actual]) {
            ruta.addFirst(actual);
            if (actual == origen) return Collections.unmodifiableList(ruta);
        }
        return Collections.emptyList();
    }

    public List<Estado> getEstados() { return estados; }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= distancias.length) {
            throw new IndexOutOfBoundsException("Indice de ciudad fuera del resultado: " + indice);
        }
    }

    public static final class Estado {
        private final int ciudadSeleccionada;
        private final int[] distancias;
        private final int[] predecesores;

        Estado(int ciudadSeleccionada, int[] distancias, int[] predecesores) {
            this.ciudadSeleccionada = ciudadSeleccionada;
            this.distancias = distancias.clone();
            this.predecesores = predecesores.clone();
        }
        public int getCiudadSeleccionada() { return ciudadSeleccionada; }
        public int getDistancia(int ciudad) {
            validarIndice(ciudad);
            return distancias[ciudad];
        }
        public int getPredecesor(int ciudad) {
            validarIndice(ciudad);
            return predecesores[ciudad];
        }
        private void validarIndice(int indice) {
            if (indice < 0 || indice >= distancias.length) {
                throw new IndexOutOfBoundsException(
                    "Indice de ciudad fuera del estado: " + indice);
            }
        }
    }
}