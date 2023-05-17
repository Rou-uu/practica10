package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
            super(elemento);
            altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
            return elemento + " " + altura() + "/" + balance();
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            // Aquí va su código.
            return vertice.altura() == altura() && super.equals(objeto);
        }

        private int superAltura() {
            return super.altura();
        }

        private int balance() {
            VerticeAVL i = (VerticeAVL) izquierdo;
            VerticeAVL d = (VerticeAVL) derecho;

            if (i != null && d != null)
                return i.altura() - d.altura();

            else if (i != null)
                return i.altura() + 1;

            else if (derecho != null)
                return - 1 - d.altura();

            return 0;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        VerticeAVL padre = getPadre((VerticeAVL) ultimoAgregado);
        rebalancea(padre);
    }
    
    private void rebalancea(VerticeAVL v) {
        if (v == null)
            return;

        v.altura = v.superAltura();
        int balance = v.balance();
        boolean cambio = false;
        VerticeAVL padre = getPadre(v);

        if (balance == -2) {
            VerticeAVL q = getHijoD(v);
            VerticeAVL x = getHijoI(q);
            VerticeAVL y = getHijoD(q);

            if (q.balance() == 1) {
                super.giraDerecha(q);
                q.altura = q.superAltura();
                x.altura = x.superAltura();
                
            }

            q = getHijoD(v);
            x = getHijoI(q);
            y = getHijoD(q);

            int qBal = q.balance();

            if (qBal == 0 || qBal == -1 || qBal == -2) {
                super.giraIzquierda(v);
                v.altura = v.superAltura();
                q.altura = q.superAltura();
            }

            if (cambio)
                padre = getPadre(q);
        }

        else if (balance == 2) {
            VerticeAVL p = getHijoI(v);
            VerticeAVL x = getHijoI(p);
            VerticeAVL y = getHijoD(p);

            if (p.balance() == -1) {
                super.giraIzquierda(p);
                p.altura = p.superAltura();
                y.altura = y.superAltura();
                
            }

            p = getHijoI(v);
            x = getHijoI(p);
            y = getHijoD(p);

            int pBal = p.balance();

            if (pBal == 0 || pBal == 1 || pBal == 2) {
                super.giraDerecha(v);
                v.altura = v.superAltura();
                p.altura = p.superAltura();
            }

            if (cambio)
                padre = getPadre(p);
        }

        rebalancea(padre);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeAVL eliminar = (VerticeAVL) busca(elemento);

        if (eliminar == null)
            return;

        if (eliminar == raiz && elementos == 1)
            limpia();

        VerticeAVL intercambiado = (VerticeAVL) intercambiaEliminable(eliminar);
        VerticeAVL padre = (VerticeAVL) intercambiado.padre;
        eliminaVertice(intercambiado);
        rebalancea(padre);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /*private VerticeAVL vertice (VerticeArbolBinario v) {
        return (VerticeAVL) v;
    }*/

    private int max (int a,  int b) {
        if (a > b)
            return a;
        return b;
    }


    private VerticeAVL getPadre(VerticeAVL r) {
        if (r != null)
            return (VerticeAVL) r.padre;

        return null;
    }

    private VerticeAVL getHijoI(VerticeAVL r) {
        if (r != null)
            return (VerticeAVL) r.izquierdo;

        return null;
    }

    private VerticeAVL getHijoD(VerticeAVL r) {
        if (r != null)
            return (VerticeAVL) r.derecho;

        return null;
    }
}
