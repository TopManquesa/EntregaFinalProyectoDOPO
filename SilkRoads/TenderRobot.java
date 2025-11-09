package SilkRoads;

import Shapes.*;

/**
 * Clase TenderRobot: representa un robot que solo toma la mitad del dinero disponible en las tiendas.
 * Hereda de RobotBase y define su comportamiento especial.
 */
public class TenderRobot extends RobotBase {

    // Constructor: crea un robot "tender" con posición inicial y tenges, color azul para distinguirlo
    public TenderRobot(int position, int tenges) {
        super("tender", position, tenges, "blue");
    }

    // Movimiento básico: avanza una posición hacia adelante como un robot normal
    @Override
    public void move() {
        currentPosition += 1;
    }

    // Recolección especial: este robot solo toma la mitad del dinero disponible en la tienda
    public void collectFromStore(Store store) {
        if (store == null) return;

        int available = store.getTenges();
        if (available > 0) {
            int half = available / 2;
            int remaining = available - half;

            try {
                // Ajusta el dinero de la tienda usando reflexión (sin modificar directamente Store)
                java.lang.reflect.Field f = Store.class.getDeclaredField("tenges");
                f.setAccessible(true);
                f.setInt(store, remaining);
            } catch (Exception e) {
                System.err.println("No se pudo ajustar el dinero de la tienda: " + e.getMessage());
                return;
            }

            // Si la tienda quedó vacía, actualiza su color
            if (remaining == 0) {
                try {
                    java.lang.reflect.Method updateColor = Store.class.getDeclaredMethod("updateColor");
                    updateColor.setAccessible(true);
                    updateColor.invoke(store);
                } catch (Exception ignored) {}
            }

            // Añade las ganancias al robot
            addEarnings(half);
        }
    }
}
