import java.util.ArrayList;
import java.util.List;

public class DFS {
    public List<Integer> recorrer(Grafo grafo, int inicio) {
        int n = grafo.getCantidadCiudades();
        if (inicio < 0 || inicio >= n) {
            throw new IllegalArgumentException("El dispositivo inicial no existe.");
        }
        boolean[] visitado = new boolean[n];
        List<Integer> recorrido = new ArrayList<>();
        visitar(grafo, inicio, visitado, recorrido);
        return recorrido;
    }

    private void visitar(Grafo grafo, int actual, boolean[] visitado,
                         List<Integer> recorrido) {
        visitado[actual] = true;
        recorrido.add(actual);
        for (int vecino = 0; vecino < grafo.getCantidadCiudades(); vecino++) {
            if (grafo.existeArista(actual, vecino) && !visitado[vecino]) {
                visitar(grafo, vecino, visitado, recorrido);
            }
        }
    }
}
