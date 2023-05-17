package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        // Aquí va su código.
        String r = "";

        Nodo n = cabeza;

        while (n != null) {
            r += n.elemento + "\n";
            n = n.siguiente;
        }

        return r;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.

        if (elemento == null)
            throw new IllegalArgumentException();

        Nodo n = new Nodo(elemento);

        if (esVacia()) {
            cabeza = n;
            rabo = n;
            return;
        }

        n.siguiente = cabeza;
        cabeza = n;

    }
}