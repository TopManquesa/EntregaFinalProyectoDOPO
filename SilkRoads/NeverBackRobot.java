package SilkRoads;

import Shapes.*;

/**
 * Clase NeverBackRobot: representa un robot que nunca retrocede en el tablero.
 * Hereda de RobotBase y define su movimiento especial.
 */
public class NeverBackRobot extends RobotBase {

    // Constructor: crea un robot "neverback" con posición inicial y tenges
    public NeverBackRobot(int position, int tenges) {
        super("neverback", position, tenges, "yellow"); // color amarillo para diferenciarlo
    }

    // Movimiento básico: avanza una celda hacia adelante, nunca retrocede
    @Override
    public void move() {
        int nextPosition = currentPosition + 1;
        if (nextPosition < currentPosition) {
            return; // ignora si intentara retroceder
        }
        currentPosition = nextPosition;
    }

    // Movimiento directo: lo lleva a una nueva posición, pero solo si es hacia adelante
    public void moveTo(int newPosition) {
        if (newPosition > currentPosition) {
            currentPosition = newPosition;
        }
        // si es menor o igual, no hace nada (no retrocede)
    }
}
