// Algoritmos de ordenamiento: MergeSort y QuickSort.
// Ordenan un arreglo con los numeros (ids) de los dispositivos,
// segun el criterio que se elija.
public class AlgoritmosOrdenamiento {

    // Criterios de ordenamiento
    public static final int POR_NOMBRE = 0;
    public static final int POR_CONEXIONES = 1;
    public static final int POR_RIESGO = 2;

    // Compara dos dispositivos segun el criterio.
    // Devuelve negativo si "a" va antes que "b", positivo si va despues, 0 si son iguales.
    private static int comparar(Grafo red, int a, int b, int criterio) {
        if (criterio == POR_CONEXIONES) {
            int conexionesA = red.contarConexiones(a);
            int conexionesB = red.contarConexiones(b);
            if (conexionesA != conexionesB) {
                return conexionesB - conexionesA;   // el que tiene mas conexiones va primero
            }
        } else if (criterio == POR_RIESGO) {
            if (red.esVulnerable(a) != red.esVulnerable(b)) {
                if (red.esVulnerable(a)) {
                    return -1;   // el vulnerable va primero
                } else {
                    return 1;
                }
            }
        }
        // Si el criterio es por nombre (o hay empate), se ordena alfabeticamente
        return red.getNombre(a).compareToIgnoreCase(red.getNombre(b));
    }

    // ---------------- MERGESORT ----------------
    // Divide el arreglo en dos mitades, ordena cada una y las mezcla.

    public static void mergeSort(int[] ids, Grafo red, int criterio) {
        dividir(ids, 0, ids.length - 1, red, criterio);
    }

    private static void dividir(int[] ids, int izq, int der, Grafo red, int criterio) {
        if (izq >= der) {
            return;   // con un solo elemento ya esta ordenado
        }
        int medio = (izq + der) / 2;
        dividir(ids, izq, medio, red, criterio);
        dividir(ids, medio + 1, der, red, criterio);
        mezclar(ids, izq, medio, der, red, criterio);
    }

    // Mezcla dos mitades ya ordenadas en un solo bloque ordenado
    private static void mezclar(int[] ids, int izq, int medio, int der, Grafo red, int criterio) {
        int[] aux = new int[der - izq + 1];
        int i = izq;        // posicion en la mitad izquierda
        int j = medio + 1;  // posicion en la mitad derecha
        int k = 0;          // posicion en el arreglo auxiliar

        while (i <= medio && j <= der) {
            if (comparar(red, ids[i], ids[j], criterio) <= 0) {
                aux[k] = ids[i];
                i++;
            } else {
                aux[k] = ids[j];
                j++;
            }
            k++;
        }
        // Copiar lo que sobre de cada mitad
        while (i <= medio) {
            aux[k] = ids[i];
            i++;
            k++;
        }
        while (j <= der) {
            aux[k] = ids[j];
            j++;
            k++;
        }
        // Devolver el resultado al arreglo original
        for (int m = 0; m < aux.length; m++) {
            ids[izq + m] = aux[m];
        }
    }

    // ---------------- QUICKSORT ----------------
    // Escoge un pivote, deja los menores a su izquierda y los mayores a su derecha.

    public static void quickSort(int[] ids, Grafo red, int criterio) {
        ordenarRapido(ids, 0, ids.length - 1, red, criterio);
    }

    private static void ordenarRapido(int[] ids, int izq, int der, Grafo red, int criterio) {
        if (izq >= der) {
            return;
        }
        int posPivote = particionar(ids, izq, der, red, criterio);
        ordenarRapido(ids, izq, posPivote - 1, red, criterio);
        ordenarRapido(ids, posPivote + 1, der, red, criterio);
    }

    // Usa el ultimo elemento como pivote y devuelve la posicion final del pivote
    private static int particionar(int[] ids, int izq, int der, Grafo red, int criterio) {
        int pivote = ids[der];
        int i = izq - 1;   // ultima posicion de los elementos menores al pivote

        for (int j = izq; j < der; j++) {
            if (comparar(red, ids[j], pivote, criterio) < 0) {
                i++;
                intercambiar(ids, i, j);
            }
        }
        intercambiar(ids, i + 1, der);   // el pivote queda en su lugar
        return i + 1;
    }

    private static void intercambiar(int[] ids, int i, int j) {
        int temporal = ids[i];
        ids[i] = ids[j];
        ids[j] = temporal;
    }
}
