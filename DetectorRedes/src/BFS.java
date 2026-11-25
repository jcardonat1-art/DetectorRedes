import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

// Recorrido en anchura (BFS): visita primero los vecinos mas cercanos,
// nivel por nivel. Usa una COLA.
public class BFS {

    public ArrayList<Integer> recorrer(Grafo grafo, int inicio) {
        int n = grafo.getCantidad();
        boolean[] visitado = new boolean[n];
        Queue<Integer> cola = new LinkedList<>();
        ArrayList<Integer> recorrido = new ArrayList<>();

        // Se empieza marcando y encolando el dispositivo inicial
        visitado[inicio] = true;
        cola.add(inicio);

        while (!cola.isEmpty()) {
            int actual = cola.poll();   // saca el primero de la cola
            recorrido.add(actual);

            // Se encolan los vecinos que todavia no se han visitado
            for (int vecino = 0; vecino < n; vecino++) {
                if (grafo.existeConexion(actual, vecino) && !visitado[vecino]) {
                    visitado[vecino] = true;
                    cola.add(vecino);
                }
            }
        }
        return recorrido;
    }
}
