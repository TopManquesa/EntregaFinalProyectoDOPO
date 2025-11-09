package Tests;

import SilkRoads.AutonomousStore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas JUnit4 para AutonomousStore.
 * Usan el helper headless relocateUsingFreeMap(...) para evitar crear tablero/UI.
 */
public class AutonomousStoreTest {

    private AutonomousStore store;

    @Before
    public void setUp() {
        store = new AutonomousStore(6, 120);
    }

    @Test
    public void constructorInicializaCorrecto() {
        assertEquals("autonomous", store.getType());
        assertEquals(6, store.getPosition());
        assertEquals(120, store.getTenges());
        assertEquals(0, store.getTimesEmptied());
    }

    @Test
    public void reubicaALaSiguienteLibre() {
        boolean[] free = new boolean[20];
        free[7] = true; // 6 ocupado, 7 libre
        store.relocateUsingFreeMap(20, free);
        assertEquals(7, store.getPosition());
    }

    @Test
    public void saltaOcupadasYVaALaPrimeraLibreEnVentana() {
        store = new AutonomousStore(10, 100);
        boolean[] free = new boolean[30];
        // 10 y 11 ocupadas (false), 12 libre (true)
        free[12] = true;
        store.relocateUsingFreeMap(30, free);
        assertEquals(12, store.getPosition());
    }

    @Test
    public void noSeMueveSiNoHayLibresEnLaVentana() {
        store = new AutonomousStore(8, 50);
        boolean[] free = new boolean[30]; // todo ocupado
        store.relocateUsingFreeMap(30, free);
        assertEquals(8, store.getPosition());
    }

    @Test
    public void vaciadoYResurtidoFuncionanComoEnLaBase() {
        int collected = store.emptyOnce();
        assertEquals(120, collected);
        assertEquals(0, store.getTenges());
        assertEquals(1, store.getTimesEmptied());

        store.resupply();
        assertEquals(120, store.getTenges());
        assertEquals(1, store.getTimesEmptied());
    }
}
