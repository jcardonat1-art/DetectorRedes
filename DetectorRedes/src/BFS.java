import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFS {
    public List<Integer> recorrer(Grafo grafo, int inicio) {
        int n = grafo.getCantidadCiudades();
        if (inicio < 0 || inicio >= n) {
            throw new IllegalArgumentException("El dispositivo inicial no existe.");
        }
        boolean[] visitado = new boolean[n];
        Queue<Integer> cola = new LinkedList<>();
        List<Integer> recorrido = new ArrayList<>();
        visitado[inicio] = true;
        cola.add(inicio);
        while (!cola.isEmpty()) {
            int actual = cola.poll();
            recorrido.add(actual);
            for (int vecino = 0; vecino < n; vecino++) {
                if (grafo.existeArista(actual, vecino) && !visitado[vecino]) {
                    visitado[vecino] = true;
                    cola.add(vecino);
                }
            }
        }
        return recorrido;
    }
}
