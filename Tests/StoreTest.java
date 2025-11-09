package Tests;

import SilkRoads.Store;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoreTest {

    private Store store;

    @Before
    public void setUp() {
        store = new Store(5, 100); // posición 5, 100 tenges
    }

    @Test
    public void constructorInicializaCorrecto() {
        assertEquals("normal", store.getType());
        assertEquals(5, store.getPosition());
        assertEquals(100, store.getTenges());
        assertEquals(0, store.getTimesEmptied());
    }

    @Test
    public void emptyOnceVaciaYSumaContador() {
        int collected = store.emptyOnce();
        assertEquals(100, collected);
        assertEquals(0, store.getTenges());
        assertEquals(1, store.getTimesEmptied());
    }

    @Test
    public void noSePuedeVaciarDosVecesSeguidas() {
        store.emptyOnce();
        int again = store.emptyOnce();
        assertEquals(0, again);
        assertEquals(1, store.getTimesEmptied());
    }

    @Test
    public void resupplyRestauraYConservaVaciados() {
        store.emptyOnce();
        store.resupply();
        assertEquals(100, store.getTenges());
        assertEquals(1, store.getTimesEmptied());
    }

    @Test
    public void ceroTengesSeComportaComoVacia() {
        Store s0 = new Store(3, 0);
        assertEquals(0, s0.getTenges());
        assertEquals(0, s0.getTimesEmptied());
        int c = s0.emptyOnce();
        assertEquals(0, c);
        assertEquals(0, s0.getTenges());
        assertEquals(0, s0.getTimesEmptied());
    }
}
