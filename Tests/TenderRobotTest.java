package Tests;

import SilkRoads.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase TenderRobot.
 * Verifica inicialización, movimiento, recolección y manejo de errores.
 */
public class TenderRobotTest {

    // Mock simple de Store para pruebas controladas
    static class MockStore extends Store {
        public MockStore(int tenges) {
            super(0, tenges); // posición fija en 0
        }
    }

    // Verifica que el robot se inicializa correctamente
    @Test
    public void testInicializacion() {
        TenderRobot robot = new TenderRobot(5, 10);

        assertEquals(5, robot.getPosition(), "Debe iniciar en posición 5");
        assertEquals(10, robot.getEarnings(), "Debe iniciar con 10 tenges");
        assertEquals("tender", robot.getType(), "Tipo debe ser 'tender'");
    }

    // Verifica que el robot avanza una posición al moverse
    @Test
    public void testMovimiento() {
        TenderRobot robot = new TenderRobot(0, 0);
        robot.move();
        assertEquals(1, robot.getPosition(), "Debe avanzar una posición");
    }

    // Verifica que el robot recolecta la mitad del dinero disponible en la tienda
    @Test
    public void testCollectFromStore_MitadDelDinero() {
        TenderRobot robot = new TenderRobot(0, 0);
        MockStore store = new MockStore(100);

        robot.collectFromStore(store);

        assertEquals(50, robot.getEarnings(), "Debe recolectar la mitad (50)");
        assertEquals(50, store.getTenges(), "La tienda debe conservar la mitad (50)");
    }

    // Verifica que no se recolecta nada si la tienda está vacía
    @Test
    public void testCollectFromStore_TiendaVacia() {
        TenderRobot robot = new TenderRobot(0, 0);
        MockStore store = new MockStore(0);

        robot.collectFromStore(store);

        assertEquals(0, robot.getEarnings(), "No debe ganar nada si la tienda está vacía");
        assertEquals(0, store.getTenges(), "La tienda sigue vacía");
    }

    // Verifica que no lanza error si la tienda es null
    @Test
    public void testCollectFromStore_Null() {
        TenderRobot robot = new TenderRobot(0, 0);
        robot.collectFromStore(null);

        assertEquals(0, robot.getEarnings(), "No debe lanzar error si la tienda es null");
    }

    // Verifica que no se suman ganancias negativas
    @Test
    public void testNoSumaGananciaNegativa() {
        TenderRobot robot = new TenderRobot(0, 10);
        robot.addEarnings(-20);

        assertEquals(10, robot.getEarnings(), "No debe aceptar ganancias negativas");
    }
}
