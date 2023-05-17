package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            pila = new Pila<Vertice>();
            Vertice aux = null;

            if (raiz != null) {
                pila.mete(raiz);
                aux = raiz.izquierdo;

            }

            while (aux != null) {
                pila.mete(aux);
                aux = aux.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
            Vertice temp = pila.saca();
            T e = temp.get();
            if (temp.derecho != null) {
                //pila.mete(temp.derecho);
                //Vertice aux = temp.derecho.izquierdo;
                temp = temp.derecho;
                while (temp != null) {
                    pila.mete(temp);
                    temp = temp.izquierdo;
                }
            }

            return e;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elemento == null)
            throw new IllegalArgumentException();

        if (raiz == null)
            raiz = ultimoAgregado = nuevoVertice(elemento);

        else
            agrega(raiz, elemento);

        elementos ++;
    }

    private void agrega(Vertice v, T elemento){
        if (elemento.compareTo(v.get()) < 0)
            if (!v.hayIzquierdo()) {
                Vertice v2 = nuevoVertice(elemento);
                v2.padre = v;
                v.izquierdo = ultimoAgregado = v2;
            } else
                agrega(v.izquierdo, elemento);

        else
            if (!v.hayDerecho()) {
                Vertice v1= nuevoVertice(elemento);
                v1.padre = v;
                v.derecho = ultimoAgregado = v1;
            } else
                agrega(v.derecho, elemento);
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice eliminar = vertice(busca(elemento));

        if (eliminar == null)
            return;

        if (!(eliminar.derecho != null && eliminar.izquierdo != null)) {
            if (eliminar.padre == null && eliminar.derecho == null && eliminar.izquierdo == null && eliminar.elemento.equals(elemento)) {
                limpia();
                return;
            }
            eliminaVertice(eliminar);
            return;
        }

        Vertice aux = intercambiaEliminable(eliminar);
        eliminaVertice(aux);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
        if (vertice  == null)
            return null;

        Vertice temp = vertice.izquierdo; //Nos vamos al subarbol izquierdo
        if (!(temp == null)) {
            while (temp != null) {
                if (temp.derecho != null)
                    temp = temp.derecho;

                else
                    break;
            }

            T element = vertice.elemento;
            vertice.elemento = temp.elemento;
            temp.elemento = element;

            return temp;
        }

        temp = vertice.derecho;
        if (!(temp == null)) {
            while (temp != null) {
                if (temp.izquierdo != null)
                    temp = temp.izquierdo;

                else
                    break;
            }

            T element = vertice.elemento;
            vertice.elemento = temp.elemento;
            temp.elemento = element;

            return temp;
        }

        return vertice;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
        if (vertice == null || (vertice.derecho != null && vertice.izquierdo != null))
            return;

        if (vertice.derecho == null && vertice.izquierdo == null) {
            if (vertice.padre != null) {
                if (vertice.padre.derecho == vertice)
                    vertice.padre.derecho = null;

                else
                    vertice.padre.izquierdo = null;

                vertice.padre  = null;
                elementos--;

            }
        }

        if (vertice.padre != null) {
            Vertice p = vertice.padre;
            Vertice sustituto = null;

            if (vertice.izquierdo != null)
                sustituto = vertice.izquierdo;

            else
                sustituto = vertice.derecho;

            if (p.izquierdo == vertice) {
                p.izquierdo = sustituto;
                sustituto.padre = p;
                elementos--;
                return;
            }

            p.derecho = sustituto;
            sustituto.padre = p;
            elementos--;
        }

        else {
            if (vertice.izquierdo != null) {
                vertice.izquierdo.padre = null;
                raiz = vertice.izquierdo;
                elementos--;
            }

            else if (vertice.derecho != null) {
                vertice.derecho.padre = null;
                raiz = vertice.derecho;
                elementos--;
            }
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        Vertice n = raiz;

        while (n != null) {
            if (n.elemento.equals(elemento))
                return (VerticeArbolBinario<T>) n;

            if (n.elemento.compareTo(elemento) > 0)
                n = n.izquierdo;

            else
                n = n.derecho;
        }

        return null;
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        if (esVacia() || vertice == null)
            return;

        Vertice q =  vertice(vertice);

        if(!q.hayIzquierdo())
            return;

        //Variables temporales
        Vertice p = q.izquierdo;
        Vertice r = p.izquierdo;
        Vertice s = p.derecho;
        Vertice t = q.derecho;
        Vertice a = null;

        if (q.padre != null)
            a = q.padre;

        p.derecho = q;
        q.padre = p;
        q.izquierdo = s;

        if (s != null)
            s.padre = q;

        if (a != null) {
            p.padre = a;

            if(a.derecho == q)
                a.derecho = p;

            else
                a.izquierdo = p;
        }

        else {
            p.padre = null;
            raiz = p;
        }
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        if (esVacia() || vertice == null)
            return;

        Vertice q =  vertice(vertice);

        if(!vertice.hayDerecho())
            return;

        //Se comprobo que si se puede
        Vertice p = q.derecho;
        Vertice r = p.izquierdo;
        Vertice s = p.derecho;
        Vertice t = q.izquierdo;
        Vertice a = null;

        if (q.padre != null)
            a = q.padre;

        p.izquierdo = q;
        q.padre = p;
        q.derecho = r;

        if (r != null)
            r.padre = q;

        if (a != null) { //Se queda igual
            p.padre = a;

            if(a.derecho == q)
                a.derecho = p;

            else
                a.izquierdo = p;
        }

        else {
            p.padre = null;
            raiz = p;
        }
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPreOrder(raiz, accion);
    }

    private void dfsPreOrder(Vertice n, AccionVerticeArbolBinario<T> accion) {
        if (n != null) {
            accion.actua(n);
            dfsPreOrder(n.izquierdo, accion);
            dfsPreOrder(n.derecho, accion);
        }
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsInOrder(raiz, accion);
    }

    private void dfsInOrder(Vertice n, AccionVerticeArbolBinario<T> accion) {
        if (n != null) {
            dfsInOrder(n.izquierdo, accion);
            accion.actua(n);
            dfsInOrder(n.derecho, accion);
        }
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPostOrder(raiz, accion);
    }

    private void dfsPostOrder(Vertice n, AccionVerticeArbolBinario<T> accion) {
        if (n != null) {
            dfsPostOrder(n.izquierdo, accion);
            dfsPostOrder(n.derecho, accion);
            accion.actua(n);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
