package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
        quickSort(arreglo, comparador, 0, arreglo.length-1);

    }

    private static <T> void quickSort(T[] a, Comparator <T> c, int ini, int fin) {
        // Aquí va su código
        if (fin <= ini)
            return;

        int i = ini + 1;
        int j = fin;

        while (i < j) {
            if (c.compare(a[i], a[ini]) <= 0)
                i++;

            else if (c.compare(a[j], a[ini]) > 0)
                j--;

            else if (c.compare(a[i], a[ini]) > 0 && c.compare(a[j], a[ini]) <= 0) {
                T temp = a[i];
                a[i++] = a[j];
                a[j--] = temp;
            }
        }

        if (c.compare(a[i], a[ini]) > 0) {
            i--;
        }

        T temp = a[ini];
        a[ini] = a[i];
        a[i] = temp;

        quickSort(a, c, ini, i-1);
        quickSort(a, c, i+1, fin);


    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código
        int min;

        for (int i = 0; i < (arreglo.length - 1); i++) {
            min = i;
            for (int j = i + 1; j < arreglo.length; j++) {
                if (comparador.compare(arreglo[j], arreglo[min]) < 0) {
                    min = j;
                }
            }

            T temp = arreglo[i];
            arreglo[i] = arreglo[min];
            arreglo[min] = temp;
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        return busquedaBinaria(arreglo, elemento, comparador, 0, arreglo.length - 1);
    }

    private static <T> int
    busquedaBinaria(T[] a, T e, Comparator <T> c, int begin, int end) {
        // Aquí va tu código
        if (begin > end)
            return -1;

        int mid = begin + (end-begin) / 2;

        if (c.compare(a[mid], e) == 0)
            return mid;

        else if (c.compare(a[mid], e) < 0)
            return busquedaBinaria(a, e, c, mid + 1, end);

        else
            return busquedaBinaria(a, e, c, begin, mid - 1);

    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }

}