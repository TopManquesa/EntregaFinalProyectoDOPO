package SilkRoads;

/**
 * Clase Robot: representa un robot normal en el tablero.
 * Hereda de RobotBase y define su movimiento básico.
 */
public class Robot extends RobotBase {

    // Constructor: crea un robot normal en la posición indicada con tenges iniciales
    public Robot(int position, int tenges) {
        super("normal", position, tenges, "red");
    }

    // Movimiento básico: avanza una posición hacia adelante
    @Override
    public void move() {
        currentPosition += 1;
    }
}
