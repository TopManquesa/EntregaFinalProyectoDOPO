package Tests;
import SilkRoads.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    private Robot robot;

    @BeforeEach
    public void setUp() {
        // Inicializamos el robot en posición 0 con 100 tenges
        robot = new Robot(0, 100);
    }

    @Test
    public void testConstructor() {
        // Verificamos que los atributos iniciales son correctos
        assertEquals("normal", robot.getType(), "El tipo del robot debe ser 'normal'");
        assertEquals(0, robot.getPosition(), "La posición inicial debe ser 0");
        assertEquals(100, robot.getEarnings(), "Los tenges iniciales deben ser 100");
    }

    @Test
    public void testMove() {
        int initialPosition = robot.getPosition();
        
        robot.move();  // Movemos el robot
        
        assertEquals(initialPosition + 1, robot.getPosition(), "La posición debe incrementarse en 1 al moverse");
    }

    @Test
    public void testReset() {
        robot.move();
        robot.addEarnings(50);

        robot.reset();

        assertEquals(robot.getInitialPosition(), robot.getPosition(), "La posición debe volver a la inicial tras reset");
        assertEquals(0, robot.getEarnings(), "Los tenges deben ser 0 tras reset");
    }

    @Test
    public void testAddEarnings() {
        int initialTenges = robot.getEarnings();
        robot.addEarnings(30);
        assertEquals(initialTenges + 30, robot.getEarnings(), "Los tenges deben aumentar correctamente");
    }
}