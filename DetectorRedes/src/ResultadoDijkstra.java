import java.util.ArrayList;

// Guarda lo que calcula Dijkstra desde un dispositivo de origen:
//  - distancias: el costo minimo para llegar a cada dispositivo
//  - predecesores: desde cual dispositivo se llega (sirve para armar la ruta)
public class ResultadoDijkstra {

    private int[] distancias;
    private int[] predecesores;

    public ResultadoDijkstra(int[] distancias, int[] predecesores) {
        this.distancias = distancias;
        this.predecesores = predecesores;
    }

    public int getDistanciaHasta(int destino) {
        return distancias[destino];
    }

    // Arma la ruta yendo desde el destino hacia atras, usando los predecesores
    public ArrayList<Integer> getRutaHasta(int destino) {
        ArrayList<Integer> ruta = new ArrayList<>();

        // Si la distancia es infinita, no hay ruta
        if (distancias[destino] == Grafo.INFINITO) {
            return ruta;
        }

        int actual = destino;
        while (actual != -1) {
            ruta.add(0, actual);          // se agrega al inicio de la lista
            actual = predecesores[actual];
        }
        return ruta;
    }
}
