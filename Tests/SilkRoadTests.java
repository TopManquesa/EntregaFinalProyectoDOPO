package Tests;

import SilkRoads.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para SilkRoad en modo invisible.
 */
public class SilkRoadTests {

    private SilkRoad sr;

    @BeforeEach
    public void setUp() {
        sr = new SilkRoad(30);
        sr.makeInvisible(); // aseguramos que no se muestre nada al iniciar
    }

    // Métodos auxiliares para contar tiendas y robots
    private int countStores() {
        return sr.getStores().split("Store").length - 1;
    }

    private int countRobots() {
        return sr.getRobots().split("Robot").length - 1;
    }

    // ------------------ TESTS ------------------

    @Test
    public void testRemoveStore() {
        sr.placeStore(5, 10);
        sr.makeInvisible();
        assertEquals(1, countStores());

        sr.removeStore(5);
        sr.makeInvisible();
        assertEquals(0, countStores());
        assertTrue(sr.ok());
    }

    @Test
    public void testRemoveNonExistentStore() {
        sr.placeStore(5, 10);
        sr.makeInvisible();
        assertEquals(1, countStores());

        sr.removeStore(10);
        sr.makeInvisible();
        assertEquals(1, countStores());
        assertFalse(sr.ok());
    }

    @Test
    public void testRemoveRobot() {
        sr.placeRobot(5);
        sr.makeInvisible();
        assertEquals(1, countRobots());

        sr.removeRobot(5);
        sr.makeInvisible();
        assertEquals(0, countRobots());
        assertTrue(sr.ok());
    }

    @Test
    public void testRemoveNonExistentRobot() {
        sr.placeRobot(5);
        sr.makeInvisible();
        assertEquals(1, countRobots());

        sr.removeRobot(10);
        sr.makeInvisible();
        assertEquals(1, countRobots());
        assertFalse(sr.ok());
    }

    @Test
    public void testMoveRobotNoProfitableMove() {
        sr.placeRobot(0);
        sr.placeStore(10, 5);
        sr.makeInvisible();

        sr.moveRobot(); // movimiento automático
        sr.makeInvisible();
        assertFalse(sr.ok());
        assertEquals(0, sr.getProfit());
    }
}

