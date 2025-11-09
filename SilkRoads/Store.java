package SilkRoads;

/**
 * Clase Store: representa una tienda normal en el tablero.
 * Hereda de StoreBase y solo define su tipo como "normal".
 */
public class Store extends StoreBase {

    // Constructor: crea una tienda normal con posición y cantidad de tenges
    public Store(int position, int tenges) {
        super("normal", position, tenges);
    }
}
