package Tests;

import SilkRoads.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestCiclo2 {

    private SilkRoad sr;

    @BeforeEach
    public void setUp() {
        sr = new SilkRoad(20); // tamaño inicial del tablero
        sr.makeInvisible();     // aseguramos que nada se dibuje
    }

    @AfterEach
    public void tearDown() {
        sr = null;
    }

    @Test
    public void testCreateRobotsAndStores() {
        int[] t = {1, 2, 2};  
        int[] x = {0, 5, 10}; 
        int[] c = {10, 20};   

        sr.create(t, x, c);
        sr.makeInvisible();

        assertTrue(sr.ok(), "La creación debería ser válida");
        assertEquals(1, sr.robots().length, "Debe existir 1 robot");
        assertEquals(2, sr.stores().length, "Deben existir 2 tiendas");
        assertEquals(0, sr.profit(), "La ganancia inicial debe ser 0");
    }

    @Test
    public void testManualMoveCollectsFromStore() {
        int[] t = {1, 2};  
        int[] x = {0, 5}; 
        int[] c = {12};   

        sr.create(t, x, c);
        sr.makeInvisible();

        sr.moveRobot(0, 5); // robot en pos 0 se mueve 5
        sr.makeInvisible();

        assertTrue(sr.ok(), "Movimiento manual debería ser válido");
        assertTrue(sr.profit() > 0, "Debe generarse ganancia positiva");
    }

    @Test
    public void testManualMoveFailsIfNoRobotAtPosition() {
        int[] t = {1};  
        int[] x = {0}; 
        int[] c = {};  

        sr.create(t, x, c);
        sr.makeInvisible();

        sr.moveRobot(10, 5); // no hay robot en pos 10
        sr.makeInvisible();

        assertFalse(sr.ok(), "No debe permitir mover desde una posición sin robot");
        assertEquals(0, sr.profit(), "La ganancia debe seguir en 0");
    }

    @Test
    public void testAutomaticMoveChoosesBestGain() {
        int[] t = {1, 2, 2}; 
        int[] x = {0, 5, 8}; 
        int[] c = {10, 15};  

        sr.create(t, x, c);
        sr.makeInvisible();

        sr.moveRobot(); // movimiento automático
        sr.makeInvisible();

        assertTrue(sr.ok(), "El movimiento automático debería ser válido");
        assertTrue(sr.profit() > 0, "Debe dejar una ganancia positiva");
    }

    @Test
    public void testAutomaticMoveFailsIfNoProfitableStore() {
        int[] t = {1, 2}; 
        int[] x = {0, 5}; 
        int[] c = {1}; // muy poco dinero, no supera la distancia

        sr.create(t, x, c);
        sr.makeInvisible();

        sr.moveRobot(); // no debería haber movimiento rentable
        sr.makeInvisible();

        assertFalse(sr.ok(), "No debe moverse si no hay ganancia positiva posible");
        assertEquals(0, sr.profit(), "La ganancia debe permanecer en 0");
    }
}
