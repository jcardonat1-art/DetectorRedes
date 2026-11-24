import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MergeSort (O(n log n) garantizado, estable) + QuickSort (O(n log n)
 * promedio, O(n^2) peor caso). MergeSort es el metodo principal.
 */
public final class AlgoritmosOrdenamiento {

    private AlgoritmosOrdenamiento() { }

    public static <T> List<T> mergeSort(List<T> original, Comparator<? super T> cmp) {
        List<T> copia = new ArrayList<>(original);
        mergeSortRango(copia, cmp, 0, copia.size() - 1);
        return copia;
    }

    private static <T> void mergeSortRango(List<T> lista, Comparator<? super T> cmp,
                                           int izq, int der) {
        if (izq >= der) return;
        int medio = (izq + der) >>> 1;
        mergeSortRango(lista, cmp, izq, medio);
        mergeSortRango(lista, cmp, medio + 1, der);
        merge(lista, cmp, izq, medio, der);
    }

    private static <T> void merge(List<T> lista, Comparator<? super T> cmp,
                                  int izq, int medio, int der) {
        List<T> aux = new ArrayList<>(der - izq + 1);
        int i = izq, j = medio + 1;
        while (i <= medio && j <= der) {
            if (cmp.compare(lista.get(i), lista.get(j)) <= 0) aux.add(lista.get(i++));
            else aux.add(lista.get(j++));
        }
        while (i <= medio) aux.add(lista.get(i++));
        while (j <= der) aux.add(lista.get(j++));
        for (int k = 0; k < aux.size(); k++) lista.set(izq + k, aux.get(k));
    }

    public static <T> List<T> quickSort(List<T> original, Comparator<? super T> cmp) {
        List<T> copia = new ArrayList<>(original);
        quickSortRango(copia, cmp, 0, copia.size() - 1);
        return copia;
    }

    private static <T> void quickSortRango(List<T> lista, Comparator<? super T> cmp,
                                           int izq, int der) {
        if (izq >= der) return;
        int pivote = particionar(lista, cmp, izq, der);
        quickSortRango(lista, cmp, izq, pivote - 1);
        quickSortRango(lista, cmp, pivote + 1, der);
    }

    private static <T> int particionar(List<T> lista, Comparator<? super T> cmp,
                                       int izq, int der) {
        int medio = (izq + der) >>> 1;
        if (cmp.compare(lista.get(medio), lista.get(izq)) < 0) swap(lista, medio, izq);
        if (cmp.compare(lista.get(der), lista.get(izq)) < 0) swap(lista, der, izq);
        if (cmp.compare(lista.get(der), lista.get(medio)) < 0) swap(lista, der, medio);
        swap(lista, medio, der - 1);
        T pivote = lista.get(der - 1);
        int i = izq, j = der - 1;
        while (true) {
            while (cmp.compare(lista.get(++i), pivote) < 0) { }
            while (cmp.compare(lista.get(--j), pivote) > 0) { }
            if (i >= j) break;
            swap(lista, i, j);
        }
        swap(lista, i, der - 1);
        return i;
    }

    private static <T> void swap(List<T> lista, int i, int j) {
        T tmp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, tmp);
    }
}