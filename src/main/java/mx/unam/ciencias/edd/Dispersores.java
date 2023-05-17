package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        // Aquí va su código.
        int ind = 0, res = 0;

        while (ind < llave.length)
            res ^= createInt(getInt(llave, ind++), getInt(llave, ind++), getInt(llave, ind++), getInt(llave, ind++));

        return res;
    }  

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        // Aquí va su código.
        int a = 0x9E3779B9, b = 0x9E3779B9, c = 0xFFFFFFFF;
        int ind = 0;

        boolean continuar = true;
        while (continuar) {
            a += createInt(getInt(llave, ind + 3), getInt(llave, ind + 2), getInt(llave, ind + 1), getInt(llave, ind));
            ind += 4;

            b += createInt(getInt(llave, ind + 3), getInt(llave, ind + 2), getInt(llave, ind + 1), getInt(llave, ind));
            ind += 4;

            if (llave.length - ind >= 4)
                c += createInt(getInt(llave, ind + 3), getInt(llave, ind + 2), getInt(llave, ind + 1), getInt(llave, ind));

            else {
                continuar = false;
                c += llave.length;
                c += createInt(getInt(llave, ind + 2), getInt(llave, ind + 1), getInt(llave, ind), 0);
            } ind += 4;


            //FUNCION MEZCLA -------------------

            //Pimera parte del libro
            a -= b + c;
            a ^= (c >>> 13);
            b -= c + a;
            b ^= (a << 8);
            c -= a + b;
            c ^= (b >>> 13);

            //Segunda parte del libro
            a -= b + c;
            a ^= (c >>> 12);
            b -= c + a;
            b ^= (a << 16);
            c -= a + b;
            c ^= (b >>> 5);

            //Tercera parte del libro
            a -= b + c;
            a ^= (c >>> 3);
            b -= c + a;
            b ^= (a << 10);
            c -= a + b;
            c ^= (b >>> 15);
        }

        return c;

    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        // Aquí va su código.
        int h = 5381;

        for (int i = 0; i < llave.length; i++) {
            h += (h << 5) + getInt(llave, i);
        }

        return h;
    }

    private static int createInt(int a, int b, int c, int d) {
        return a << 24 | (b) << 16 | (c) << 8 | (d);
    }

    private static int getInt(byte[] key, int ind) {
        if (ind < key.length)
            return (0xFF & key[ind]);

        return 0;
    }


}
