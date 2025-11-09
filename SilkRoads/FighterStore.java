package SilkRoads;

/**
 * Clase FighterStore: representa una tienda que solo atiende a robots más ricos.
 * Si un robot con menos dinero intenta recoger, no se lo entrega.
 */
public class FighterStore extends StoreBase {

    // Constructor: crea una tienda "fighter" con posición inicial y tenges
    public FighterStore(int position, int tenges) {
        super("fighter", position, tenges);
    }

    // Al colocarse en el tablero, no cambia su posición (mantiene comportamiento especial)
    public void onPlaced() {
        // No hace nada, solo conserva su lógica propia
    }

    // Verifica si un robot puede ser atendido: solo si tiene más dinero que la tienda
    public boolean canBeServedBy(int robotEarnings) {
        return robotEarnings > this.tenges;
    }

    // Intenta entregar el dinero al robot: si es más rico, lo recibe; si no, se rechaza
    public int giveTengesToRobot(int robotEarnings) {
        if (canBeServedBy(robotEarnings)) {
            int amount = this.tenges;
            emptyOnce(); // vacía la tienda y aumenta contador de veces vaciada
            System.out.println("Robot rico tomó " + amount + " tenges de la tienda fighter en " + this.position);
            return amount;
        } else {
            System.out.println("Robot pobre no pudo recoger dinero de la tienda fighter en " + this.position);
            return 0;
        }
    }
}
