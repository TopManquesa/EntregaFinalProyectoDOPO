package Tests;

import SilkRoads.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class SilkRoadCycle1Test {

    private SilkRoad sr;

    @BeforeEach
    public void setUp() {
        sr = new SilkRoad(30); // tablero de 30 posiciones
        sr.makeInvisible();     // aseguramos modo invisible

        int[] t = {1, 2, 1, 2};  // robot, store, robot, store
        int[] x = {2, 5, 8, 12}; // posiciones
        int[] c = {10, 20};      // tenges en las dos tiendas
        sr.create(t, x, c);
        sr.makeInvisible();
    }

    @AfterEach
    public void tearDown() {
        sr = null; // limpiamos
    }

    @Test
    public void testCreate() {
        assertTrue(sr.ok(), "La creación debería ser válida");
        assertEquals(2, sr.robots().length, "Debe haber 2 robots");
        assertEquals(2, sr.stores().length, "Debe haber 2 tiendas");
        assertEquals(0, sr.profit(), "Al inicio la ganancia debe ser 0");
    }

    @Test
    public void testRobotsInitialPositions() {
        int[][] robots = sr.robots();
        assertEquals(2, robots.length, "Debe haber 2 robots");
        assertEquals(2, robots[0][0], "El primer robot debe empezar en la posición 2");
        assertEquals(8, robots[1][0], "El segundo robot debe empezar en la posición 8");
    }

    @Test
    public void testStoresInitialValues() {
        int[][] stores = sr.stores();
        assertEquals(2, stores.length, "Debe haber 2 tiendas");
        assertEquals(10, stores[0][0], "La primera tienda debe tener 10 tenges");
        assertEquals(20, stores[1][0], "La segunda tienda debe tener 20 tenges");
    }
}
