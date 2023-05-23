package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            // Aquí va su código.
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            // Aquí va su código.
            for (int i = 0; i < entradas.length; i++) {
                if (entradas[i] != null) {
                    indice = i;
                    iterador = entradas[i].iteradorLista();
                    break;
                }
            }
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            // Aquí va su código.
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            // Aquí va su código.
            if (iterador == null)
                throw new NoSuchElementException();

            Entrada p = iterador.next();

            if (!iterador.hasNext()) {
                iterador = null;

                for (; ++indice < entradas.length;) {
                    if (entradas[indice] != null) {
                        iterador = entradas[indice].iteradorLista();
                        break;
                    }
                }
            }

            return p;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            // Aquí va su código.
            return siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            // Aquí va su código.
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        // Aquí va su código.
        if (capacidad < MINIMA_CAPACIDAD)
            capacidad = MINIMA_CAPACIDAD;

        else {
            int base = 2;
            while (base < capacidad * 2)
                base *= 2;

            capacidad = base;
        }

        this.entradas = nuevoArreglo(capacidad);
        this.dispersor = dispersor;

    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        // Aquí va su código.
        if (llave == null || valor == null)
            throw new IllegalArgumentException();

        Entrada temp = new Entrada(llave, valor);
        int i = dispersor.dispersa(llave) & getMascara();

        if (entradas[i] == null) {
            Lista<Entrada> lista = new Lista<Entrada>();
            lista.agrega(temp);
            elementos++;
            entradas[i] = lista;
        }

        else {
            boolean agregado = false;

            for (Entrada ent : entradas[i]) {
                if (ent.llave.equals(llave)) {
                    ent.valor = valor;
                    agregado = true;
                    break;
                }
            }

            if (!agregado) {
                entradas[i].agrega(temp);
                elementos++;
            }
        }

        //Carga
        if (carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] nuevecita = nuevoArreglo(entradas.length * 2);

            for (int x = 0; x < entradas.length; x++) {
                if (entradas[x] != null) {
                    for (Entrada ent : entradas[x]) {
                        int h = dispersor.dispersa(ent.llave) & (nuevecita.length - 1);

                        if (nuevecita[h] == null) {
                            Lista<Entrada> aux = new Lista<Entrada>();
                            aux.agrega(ent);
                            nuevecita[h] = aux;
                        }

                        else {
                            nuevecita[h].agrega(ent);
                        }
                    }
                }
            }

            entradas = nuevecita;
        }
    }

    private int getMascara() {
        return entradas.length - 1;
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        // Aquí va su código.
        if (llave == null)
            throw new IllegalArgumentException();

        int i = dispersor.dispersa(llave) & getMascara();

        if (entradas[i] == null)
            throw new NoSuchElementException();

        for (Entrada ent : entradas[i]) {
            if (ent.llave.equals(llave))
                return ent.valor;
        }

        throw new NoSuchElementException();
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        // Aquí va su código.
        if (llave == null)
            return false;

        int i = dispersor.dispersa(llave) & getMascara();

        if (entradas[i] == null)
            return false;

        for (Entrada ent : entradas[i])
            if (ent.llave.equals(llave))
                return true;

        return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        // Aquí va su código.
        if (llave == null)
            throw new IllegalArgumentException();

        int i = dispersor.dispersa(llave) & getMascara();

        if (entradas[i] == null)
            throw new NoSuchElementException();

        for (Entrada ent : entradas[i]) {
            if (ent.llave.equals(llave)) {
                entradas[i].elimina(ent);
                elementos--;
            }
        }

        if (entradas[i].esVacia())
            entradas[i] = null; 
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        // Aquí va su código.
        int r = 0;

        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null)
                r += entradas[i].getLongitud() - 1;
        }

        return r;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        // Aquí va su código.
        int max = 0;

        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null)
                if (entradas[i].getLongitud() > max)
                    max = entradas[i].getLongitud();
        }

        return max - 1;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        // Aquí va su código.
        return (double) elementos/entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        // Aquí va su código.
        this.entradas = nuevoArreglo(entradas.length);
        this.elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        // Aquí va su código.
        if (esVacia())
            return "{}";

        String r = "{ ";

        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                for (Entrada ent : entradas[i]) {
                    r += String.format("'%s': '%s', ", ent.llave.toString(), ent.valor.toString());
                }
            }
        }

        return r + "}";
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        // Aquí va su código.
            if (this.elementos != d.elementos)
                return false;

            for (int i = 0; i < entradas.length; i++) {
                if (entradas[i] != null) {
                    for (Entrada ent : entradas[i]) {
                        if (!d.contiene(ent.llave) || !d.get(ent.llave).equals(ent.valor))
                            return false;
                    }
                }
            }

            return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
