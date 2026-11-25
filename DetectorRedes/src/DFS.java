import java.util.ArrayList;

// Recorrido en profundidad (DFS): se va por un camino hasta el final
// y luego se devuelve. Usa RECURSIVIDAD.
public class DFS {

    public ArrayList<Integer> recorrer(Grafo grafo, int inicio) {
        boolean[] visitado = new boolean[grafo.getCantidad()];
        ArrayList<Integer> recorrido = new ArrayList<>();
        visitar(grafo, inicio, visitado, recorrido);
        return recorrido;
    }

    private void visitar(Grafo grafo, int actual, boolean[] visitado,
                         ArrayList<Integer> recorrido) {
        visitado[actual] = true;
        recorrido.add(actual);

        // Por cada vecino no visitado, se repite el proceso (recursivo)
        for (int vecino = 0; vecino < grafo.getCantidad(); vecino++) {
            if (grafo.existeConexion(actual, vecino) && !visitado[vecino]) {
                visitar(grafo, vecino, visitado, recorrido);
            }
        }
    }
}
