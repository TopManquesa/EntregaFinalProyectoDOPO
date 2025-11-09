package Tests;

import SilkRoads.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase NeverBackRobot.
 * Verifica inicialización, movimiento y control de ganancias.
 */
public class NeverBackRobotTest {

    // Verifica que el robot se inicializa correctamente
    @Test
    public void testInicializacion() {
        NeverBackRobot robot = new NeverBackRobot(5, 20);

        assertEquals(5, robot.getPosition(), "La posición inicial debe ser 5");
        assertEquals(20, robot.getEarnings(), "Debe iniciar con 20 tenges");
        assertEquals("neverback", robot.getType(), "El tipo debe ser 'neverback'");
    }

    // Verifica que el robot avanza una posición al moverse
    @Test
    public void testMovimientoAdelante() {
        NeverBackRobot robot = new NeverBackRobot(5, 0);
        robot.move();
        assertEquals(6, robot.getPosition(), "El robot debe avanzar una posición");
    }

    // Verifica que el robot ignora intentos de retroceder con moveTo
    @Test
    public void testMovimientoHaciaAtrasIgnorado() {
        NeverBackRobot robot = new NeverBackRobot(10, 0);
        robot.moveTo(8); // intento de retroceder
        assertEquals(10, robot.getPosition(), "El robot nunca debe retroceder");
    }

    // Verifica que el robot acepta avanzar con moveTo si es hacia adelante
    @Test
    public void testMovimientoHaciaAdelanteConMoveTo() {
        NeverBackRobot robot = new NeverBackRobot(3, 0);
        robot.moveTo(7);
        assertEquals(7, robot.getPosition(), "El robot debe avanzar a la nueva posición");
    }

    // Verifica que se suman correctamente las ganancias positivas
    @Test
    public void testAddEarnings() {
        NeverBackRobot robot = new NeverBackRobot(0, 0);
        robot.addEarnings(50);
        assertEquals(50, robot.getEarnings(), "Debe sumar correctamente las ganancias");
    }

    // Verifica que no se aceptan ganancias negativas
    @Test
    public void testNoSumaGananciaNegativa() {
        NeverBackRobot robot = new NeverBackRobot(0, 10);
        robot.addEarnings(-5);
        assertEquals(10, robot.getEarnings(), "No debe aceptar ganancias negativas");
    }
}
