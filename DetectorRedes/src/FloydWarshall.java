// Algoritmo de Floyd-Warshall: calcula el costo minimo entre TODOS
// los pares de dispositivos. Usa tres ciclos for anidados.
public class FloydWarshall {

    public ResultadoFloydWarshall calcular(Grafo grafo) {
        int n = grafo.getCantidad();
        int[][] distancias = new int[n][n];
        int[][] siguiente = new int[n][n];

        // Se empieza con los costos directos de la red
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = grafo.getCosto(i, j);
                if (distancias[i][j] == Grafo.INFINITO) {
                    siguiente[i][j] = -1;   // no hay ruta todavia
                } else {
                    siguiente[i][j] = j;
                }
            }
        }

        // k es el dispositivo "intermedio" que se prueba para ir de i a j
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] != Grafo.INFINITO && distancias[k][j] != Grafo.INFINITO) {
                        int alternativa = distancias[i][k] + distancias[k][j];
                        if (alternativa < distancias[i][j]) {
                            distancias[i][j] = alternativa;
                            siguiente[i][j] = siguiente[i][k];
                        }
                    }
                }
            }
        }
        return new ResultadoFloydWarshall(distancias, siguiente);
    }
}
