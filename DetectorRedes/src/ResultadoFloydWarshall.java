import java.util.ArrayList;

// Guarda lo que calcula Floyd-Warshall:
//  - distancias: matriz con el costo minimo entre cualquier par de dispositivos
//  - siguiente: matriz que dice cual es el siguiente dispositivo en la ruta
public class ResultadoFloydWarshall {

    private int[][] distancias;
    private int[][] siguiente;

    public ResultadoFloydWarshall(int[][] distancias, int[][] siguiente) {
        this.distancias = distancias;
        this.siguiente = siguiente;
    }

    public int getDistancia(int origen, int destino) {
        return distancias[origen][destino];
    }

    // Arma la ruta siguiendo la matriz "siguiente" paso a paso
    public ArrayList<Integer> getRuta(int origen, int destino) {
        ArrayList<Integer> ruta = new ArrayList<>();

        // -1 significa que no hay ruta
        if (siguiente[origen][destino] == -1) {
            return ruta;
        }

        int actual = origen;
        ruta.add(actual);
        while (actual != destino) {
            actual = siguiente[actual][destino];
            ruta.add(actual);
        }
        return ruta;
    }
}
