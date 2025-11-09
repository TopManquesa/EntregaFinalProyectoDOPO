package Tests;

import SilkRoads.FighterStore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas para FighterStore – JUnit4.
 * Regla: solo entrega si robotEarnings > tenges.
 */
public class FighterStoreTest {

    private FighterStore store;

    @Before
    public void setUp() {
        store = new FighterStore(8, 150); // pos 8, 150 tenges
    }

    @Test
    public void constructorInicializaCorrecto() {
        assertEquals("fighter", store.getType());
        assertEquals(8, store.getPosition());
        assertEquals(150, store.getTenges());
        assertEquals(0, store.getTimesEmptied());
    }

    @Test
    public void canBeServedByEsEstrictamenteMayor() {
        assertFalse(store.canBeServedBy(0));
        assertFalse(store.canBeServedBy(149));
        assertFalse(store.canBeServedBy(150)); // igual NO sirve
        assertTrue(store.canBeServedBy(151));
        assertTrue(store.canBeServedBy(1000));
    }

    @Test
    public void robotRicoRecibeYVacia() {
        int collected = store.giveTengesToRobot(200); // 200 > 150
        assertEquals(150, collected);
        assertEquals(0, store.getTenges());
        assertEquals(1, store.getTimesEmptied());
    }

    @Test
    public void robotPobreNoCambiaEstado() {
        int t0 = store.getTenges();
        int e0 = store.getTimesEmptied();
        int collected = store.giveTengesToRobot(100); // 100 < 150
        assertEquals(0, collected);
        assertEquals(t0, store.getTenges());
        assertEquals(e0, store.getTimesEmptied());
    }

    @Test
    public void resupplyTrasEntregaARicoRestauraYConservaContador() {
        store.giveTengesToRobot(999); // vacía: 0, vaciados=1
        assertEquals(0, store.getTenges());
        assertEquals(1, store.getTimesEmptied());

        store.resupply();
        assertEquals(150, store.getTenges());
        assertEquals(1, store.getTimesEmptied());
    }
}
