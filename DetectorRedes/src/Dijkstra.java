// Algoritmo de Dijkstra: calcula el camino de menor costo desde
// un dispositivo de origen hacia todos los demas.
public class Dijkstra {

    public ResultadoDijkstra calcular(Grafo grafo, int origen) {
        int n = grafo.getCantidad();
        int[] distancias = new int[n];
        int[] predecesores = new int[n];
        boolean[] visitado = new boolean[n];

        // Al inicio todo esta lejos (INFINITO) y sin predecesor (-1)
        for (int i = 0; i < n; i++) {
            distancias[i] = Grafo.INFINITO;
            predecesores[i] = -1;
        }
        distancias[origen] = 0;

        for (int paso = 0; paso < n; paso++) {

            // 1. Buscar el dispositivo no visitado con menor distancia
            int actual = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && (actual == -1 || distancias[i] < distancias[actual])) {
                    actual = i;
                }
            }

            // Si ya no quedan dispositivos alcanzables, se termina
            if (actual == -1 || distancias[actual] == Grafo.INFINITO) {
                break;
            }
            visitado[actual] = true;

            // 2. Revisar si pasando por "actual" se llega mas barato a sus vecinos
            for (int vecino = 0; vecino < n; vecino++) {
                if (grafo.existeConexion(actual, vecino) && !visitado[vecino]) {
                    int nuevaDistancia = distancias[actual] + grafo.getCosto(actual, vecino);
                    if (nuevaDistancia < distancias[vecino]) {
                        distancias[vecino] = nuevaDistancia;
                        predecesores[vecino] = actual;
                    }
                }
            }
        }
        return new ResultadoDijkstra(distancias, predecesores);
    }
}
