package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            this.anterior = null;
            this.siguiente = null;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            // Aquí va su código.
            this.anterior = null;
            this.siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
            if (!(hasNext())) {
                throw new NoSuchElementException();
            }

            T r = siguiente.elemento;

            anterior = siguiente;
            siguiente = siguiente.siguiente;

            return r;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
            if (!(hasPrevious())) {
                throw new NoSuchElementException();
            }

            T r = anterior.elemento;

            siguiente = anterior;
            anterior = anterior.anterior;

            return r;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        if (longitud == 0)
            return true;

        if (cabeza == rabo && cabeza == null)
            return true;

        return false;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException();
        }

        Nodo a = new Nodo(elemento);

        if (esVacia()) {
            cabeza = a;
            rabo = a;
            longitud = 1;
            return;
        }

        Nodo temp = rabo;
        temp.siguiente = a;
        a.anterior = temp;

        rabo = a;

        longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
        this.agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
        if (elemento == null) {
            throw new IllegalArgumentException();
        }

        Nodo a = new Nodo(elemento);

        if (esVacia()) {
            cabeza = a;
            rabo = a;
            longitud = 1;
            return;
        }

        Nodo temp = cabeza;
        temp.anterior = a;
        a.siguiente = temp;

        cabeza = a;

        longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
        if (elemento == null)
            throw new IllegalArgumentException();

        if (i <= 0) {
            agregaInicio(elemento);
            return;
        }

        if (i >= longitud) {
            agregaFinal(elemento);
            return;
        }

        Nodo n = new Nodo(elemento);
        Nodo aux = cabeza;

        for (int x = 0; x < i - 1; x++)
            aux = aux.siguiente;

        n.siguiente = aux.siguiente;
        aux.siguiente.anterior = n;
        n.anterior = aux;
        aux.siguiente = n;

        longitud++;

    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Nodo n = cabeza;

        while (n != null) {
            if (n.elemento.equals(elemento)) {
                if (n.anterior == null) {
                    eliminaPrimero();
                }

                else if (n.siguiente == null) {
                    eliminaUltimo();
                }

                else {
                    n.anterior.siguiente = n.siguiente;
                    n.siguiente.anterior = n.anterior;
                    longitud--;
                }

                return;
            }

            n = n.siguiente;
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
        if (esVacia())
            throw new NoSuchElementException();

        T r = cabeza.elemento;

        cabeza = cabeza.siguiente;

        if (cabeza != null) {
            cabeza.anterior = null;
            longitud--;
        }

        else {
            rabo = null;
            longitud = 0;
        }


        return r;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
        if (esVacia())
            throw new NoSuchElementException();

        T r = rabo.elemento;

        rabo = rabo.anterior;

        if (rabo != null) {
            rabo.siguiente = null;
            longitud--;
        }

        else {
            cabeza = null;
            longitud = 0;
        }


        return r;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        Nodo n = cabeza;

        while (n != null) {
            if (n.elemento.equals(elemento)) {
                return true;
            }

            n = n.siguiente;
        }

        return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
        Lista<T> r = new Lista<T>();

        Nodo n = cabeza;

        while (n != null) {
            r.agregaInicio(n.elemento);
            n = n.siguiente;
        }

        return r;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
        Lista<T> r = new Lista<T>();

        Nodo n = cabeza;

        while (n != null) {
            r.agregaFinal(n.elemento);
            n = n.siguiente;
        }

        return r;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
        if (esVacia())
            throw new NoSuchElementException();

        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
        if (esVacia())
            throw new NoSuchElementException();

        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
        if (i < 0 || i >= longitud)
            throw new ExcepcionIndiceInvalido();

        Nodo n = cabeza;

        for (int j = 0; j < i; j++) {
            n = n.siguiente;
        }

        return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
        if (esVacia())
            return -1;

        Nodo n = cabeza;
        int count = 0;

        while (n != null) {
            if(n.elemento.equals(elemento)) {
                return count;
            }

            count ++;

            n = n.siguiente;
        }

        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        String r = "[";

        if (esVacia())
            return r + "]";

        Nodo temp = cabeza;

        for (int i = 0; i < longitud - 1; i++) {
            r += temp.elemento + ", ";
            temp = temp.siguiente;
        }

        return r + temp.elemento + "]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;

        // Aquí va su código.
        if (longitud != lista.getLongitud()) {
            return false;
        }

        Nodo n = cabeza;
        Nodo p = lista.cabeza;

        while (n != null) {
            if (!(n.elemento.equals(p.elemento)))
                return false;

            n = n.siguiente;
            p = p.siguiente;
        }

        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        // Aquí va su código.
        if (longitud == 1) {
            return this;
        }

        Lista <T> a = new Lista<T>();
        Lista <T> b = new Lista<T>();

        for (int i = 0; i < longitud/2; i++)
            a.agregaFinal(this.get(i));

        for (int i = longitud/2; i < longitud; i++)
            b.agregaFinal(this.get(i));

        a = a.mergeSort(comparador);
        b = b.mergeSort(comparador);

        return auxMerge(a, b, comparador);
    }

    private Lista<T> auxMerge(Lista<T> izquierda, Lista<T> derecha, Comparator<T> c) { //Método para juntar dos listas
        Lista<T> aux = new Lista<T>();

        Nodo nA = izquierda.cabeza;
        Nodo nB = derecha.cabeza;

        while (nA != null && nB != null) {
            if (c.compare(nB.elemento, nA.elemento) < 0) {
                aux.agregaFinal(nB.elemento);
                nB = nB.siguiente;
            }

            else {
                aux.agregaFinal(nA.elemento);
                nA = nA.siguiente;
            }
        }

        while (nA != null) {
            aux.agregaFinal(nA.elemento);
            nA = nA.siguiente;
        }

        while (nB != null) {
            aux.agregaFinal(nB.elemento);
            nB = nB.siguiente;
        }

        return aux;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        // Aquí va su código.
        if (esVacia())
            return false;

        Nodo n = cabeza;

        while (n != null) {
            if (comparador.compare(n.elemento, elemento) == 0)
                return true;

            n = n.siguiente;
        }

        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}