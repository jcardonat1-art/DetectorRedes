import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/**
 * Busqueda lineal O(n) y binaria O(log n).
 *   - Binaria: sobre listas ordenadas, coincidencia exacta.
 *   - Lineal: para substring o listas no ordenadas.
 */
public final class AlgoritmosBusqueda {

    private AlgoritmosBusqueda() { }

    public static <T> int busquedaLineal(List<T> lista, Predicate<T> criterio) {
        for (int i = 0; i < lista.size(); i++) {
            if (criterio.test(lista.get(i))) return i;
        }
        return -1;
    }

    public static <T> int busquedaBinaria(List<T> lista, ToIntFunction<T> comparador) {
        int izq = 0, der = lista.size() - 1;
        while (izq <= der) {
            int medio = (izq + der) >>> 1;
            int c = comparador.applyAsInt(lista.get(medio));
            if (c == 0) return medio;
            if (c < 0) izq = medio + 1;
            else der = medio - 1;
        }
        return -1;
    }
}
