import java.util.ArrayList;

// Algoritmos de busqueda de dispositivos por nombre.
//  - Binaria: muy rapida, pero la lista debe estar ORDENADA por nombre.
//  - Lineal: revisa uno por uno, sirve para buscar coincidencias parciales.
public class AlgoritmosBusqueda {

    // Busca un nombre exacto en el arreglo de ids ordenado por nombre.
    // Devuelve la posicion donde lo encontro, o -1 si no existe.
    public static int busquedaBinaria(int[] idsOrdenados, Grafo red, String nombre) {
        int izq = 0;
        int der = idsOrdenados.length - 1;

        while (izq <= der) {
            int medio = (izq + der) / 2;
            String nombreMedio = red.getNombre(idsOrdenados[medio]);
            int resultado = nombreMedio.compareToIgnoreCase(nombre);

            if (resultado == 0) {
                return medio;          // lo encontro
            } else if (resultado < 0) {
                izq = medio + 1;       // el nombre buscado esta en la mitad derecha
            } else {
                der = medio - 1;       // el nombre buscado esta en la mitad izquierda
            }
        }
        return -1;
    }

    // Revisa todos los dispositivos y devuelve los ids cuyo nombre contiene el texto
    public static ArrayList<Integer> busquedaLineal(Grafo red, String texto) {
        ArrayList<Integer> encontrados = new ArrayList<>();
        String textoMinuscula = texto.toLowerCase();

        for (int i = 0; i < red.getCantidad(); i++) {
            if (red.getNombre(i).toLowerCase().contains(textoMinuscula)) {
                encontrados.add(i);
            }
        }
        return encontrados;
    }
}
