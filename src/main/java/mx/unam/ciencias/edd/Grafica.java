package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
            iterador = vertices.iterator();

        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La distancia del vértice. */
        private double distancia;
        /* El índice del vértice. */
        private int indice;
        /* La lista de vecinos del vértice. */
        private Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            vecinos = new Lista<Vecino>();
            color = Color.NINGUNO;
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            // Aquí va su código.
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            if (distancia < vertice.distancia)
                return -1;

            else if (distancia > vertice.distancia)
                return 1;

            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            // Aquí va su código.
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica<T>.Vertice v, Grafica<T>.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elemento == null || contiene(elemento))
            throw new IllegalArgumentException();

        vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
        Vertice ying = (Vertice) vertice(a);
        Vertice yang = (Vertice) vertice(b);

        if (a.equals(b) || sonVecinos(a, b))
            throw new IllegalArgumentException();

        ying.vecinos.agrega(new Vecino(yang, 1));
        yang.vecinos.agrega(new Vecino(ying, 1));

        aristas++;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        Vertice ying = (Vertice) vertice(a);
        Vertice yang = (Vertice) vertice(b);

        if (a.equals(b) || sonVecinos(a, b) || peso < 0)
            throw new IllegalArgumentException();

        ying.vecinos.agrega(new Vecino(yang, peso));
        yang.vecinos.agrega(new Vecino(ying, peso));

        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
        Vertice ying = (Vertice) vertice(a);
        Vertice yang = (Vertice) vertice(b);

        if (a.equals(b) || !sonVecinos(a, b))
            throw new IllegalArgumentException();

        Vecino ve = null;
        Vecino ev = null;

        for (Vecino x : ying.vecinos)
            if (x.vecino.get().equals(b)) 
                ve = x;

        for (Vecino x : yang.vecinos)
            if (x.vecino.get().equals(a))
                ev = x;
        
        ying.vecinos.elimina(ve);
        yang.vecinos.elimina(ev);

        aristas--;
        

    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        for (Vertice v : vertices)
            if (v.elemento.equals(elemento))
                return true;

        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        if (!contiene(elemento)) {
            throw new NoSuchElementException();
        }

        Vertice v = (Vertice) vertice(elemento);

        for (Vecino x : v.vecinos)
            if (sonVecinos(x.vecino.elemento, elemento))
                desconecta(x.vecino.elemento, elemento);
        
        vertices.elimina(v);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        if (!contiene(a) || !contiene(b)) 
            throw new NoSuchElementException();

        Vertice ying = (Vertice) vertice(a);
        Vertice yang = (Vertice) vertice(b);

        for (Vecino x : ying.vecinos)
            if (x.vecino == yang)
                return true;

        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        if (!contiene(a) || !contiene(b))
            throw new NoSuchElementException();

        if (!sonVecinos(a, b))
            throw new IllegalArgumentException();

        Vertice ying = (Vertice) vertice(a);
        Vertice yang = (Vertice) vertice(b);

        for (Vecino x : ying.vecinos)
            if (x.vecino.equals(yang))
                return x.peso;

        return -1;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
        if (!contiene(a) || !contiene(b))
            throw new NoSuchElementException();

        if (!sonVecinos(a, b) || peso < 0)
            throw new IllegalArgumentException();

        Vertice ying = (Vertice) vertice(a);
        Vertice yang = (Vertice) vertice(b);

        for (Vecino x : ying.vecinos)
            if (x.vecino.equals(yang))
                x.peso = peso;

        for (Vecino x : yang.vecinos)
            if (x.vecino.equals(ying))
                x.peso = peso;

    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
        for (Vertice v : vertices) 
            if (v.elemento.equals(elemento))
                return v;

        throw new NoSuchElementException();
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        // Aquí va su código.
        if (vertice == null || !(vertice.getClass() == Vertice.class || vertice.getClass() == Vecino.class))
            throw new IllegalArgumentException();

        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice) vertice;
            v.color = color;
        }

        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino) vertice;
            v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
        Lista <Vertice> lista = new Lista<Vertice>();
        bfs(vertices.getPrimero().get(), y -> lista.agrega((Vertice) y));

        return lista.getLongitud() == vertices.getLongitud();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        for (Vertice v : vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        Cola<Vertice> c = new Cola<Vertice>();
        recorrer(elemento, accion, c);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        Pila<Vertice> c = new Pila<Vertice>();
        recorrer(elemento, accion, c);
    }

    //Metodo para no escribir dos veces los recorridos BFS y DFS ---- Son los mismos
    private void recorrer(T elem, AccionVerticeGrafica<T> a, MeteSaca<Vertice> punpun) {
        Vertice v = (Vertice) vertice(elem);
        punpun.mete(v);

        while (!punpun.esVacia()) {
            Vertice temp = punpun.saca();
            temp.color = Color.ROJO;
            a.actua(temp);

            for (Vecino uwu : temp.vecinos)
                if (uwu.vecino.color != Color.ROJO) {
                    punpun.mete(uwu.vecino);
                    uwu.vecino.color = Color.ROJO;
                }
        }

        paraCadaVertice(y -> setColor(y, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return vertices.getLongitud() == 0;
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String r = "{";
        
        for (Vertice v : vertices) {
            r += v.get().toString() + ", ";
        } r += "}, {";

        for (Vertice v : vertices) {
            for (Vecino u : v.vecinos) {
                if (u.vecino.color != Color.ROJO)
                    r += "(" + v.get().toString() + ", " + u.vecino.get().toString() + "), ";
            }

            v.color = Color.ROJO;
        } r += "}";

        paraCadaVertice(y -> setColor(y, Color.NINGUNO));

        return r;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        if (grafica.aristas != aristas || grafica.vertices.getLongitud() != vertices.getLongitud())
            return false;

        for (Vertice v : vertices) {
            if (!grafica.contiene(v.get()))
                return false;

            for (Vecino x : v.vecinos) {
                if (!grafica.sonVecinos(v.get(), x.vecino.get()))
                    return false;
            }
        }

        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        if (!contiene(origen) || !contiene(destino))
            throw new NoSuchElementException();

        Vertice origin = (Vertice) vertice(origen);
        Vertice destination = (Vertice) vertice(destino);

        for (Vertice v : vertices) 
            v.distancia = Double.POSITIVE_INFINITY;
        origin.distancia = 0;

        ponDistancias(false);

        return rebuild(origin, destination, false);

    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        if (!contiene(origen) || !contiene(destino))
            throw new NoSuchElementException();

        Vertice origin = (Vertice) vertice(origen);
        Vertice destination = (Vertice) vertice(destino);

        for (Vertice v : vertices) 
            v.distancia = Double.POSITIVE_INFINITY;
        origin.distancia = 0;

        ponDistancias(true);

        return rebuild(origin, destination, true);

    }

    //Metodo auxiliar para poner las distancias
    private void ponDistancias(boolean dis) {
        MonticuloMinimo<Vertice> mont = new MonticuloMinimo<Vertice>(vertices); 

        while (!mont.esVacia()) {
            Vertice v = mont.elimina();

            for (Vecino x : v.vecinos) {
                if (x.vecino.distancia == Double.POSITIVE_INFINITY || v.distancia + x.peso < x.vecino.distancia) {
                    x.vecino.distancia = v.distancia + (dis ? x.peso : 1);
                    mont.reordena(x.vecino);
                }
            }
        }
    }

    //Metodo para reconstruir la lista de vertices de la trayectoria
    private Lista<VerticeGrafica<T>> rebuild(Vertice origin, Vertice destino, boolean dis) {
        Lista<VerticeGrafica<T>> res = new Lista<VerticeGrafica<T>>();

        if (destino.distancia == Double.POSITIVE_INFINITY)
            return res;

        Vertice aux = destino;
        while (aux != origin) {
            for (Vecino x : aux.vecinos) {
                if (x.vecino.distancia == aux.distancia - (dis ? x.peso : 1)) {
                    res.agregaInicio((VerticeGrafica<T>) aux);
                    aux = x.vecino;
                    break;
                }
            }
        }

        res.agregaInicio((VerticeGrafica<T>) aux);

        return res;
    }
}




