package SilkRoads;

import java.util.Arrays;

/**
 * Clase de pruebas y simulación lógica/visual del tablero.
 */
public class SilkRoadContestTest {

    // Muestra en consola el estado del tablero en un día específico
    private void mostrarEstadoDia(SilkRoad camino, int dia) {
        System.out.println("==============================");
        System.out.println(" Día " + dia);
        System.out.println("Robots: " + camino.getRobots());
        System.out.println("Tiendas: " + camino.getStores());
        System.out.println(" Ganancia total: " + camino.getProfit());

        RobotBase lider = camino.getRichestRobot();
        if (lider != null) {
            System.out.println("Robot líder del día: " + lider.getType() +
                               " | Tenges: " + lider.getEarnings() +
                               " | Posición: " + lider.getPosition());
        }
        System.out.println("==============================\n");
    }

    // Pausa la simulación por la cantidad de milisegundos indicada
    private void pausarSimulacion(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Simula visualmente varios días en el tablero con acciones predefinidas
    public void simularDias(int[][] accionesPorDia, boolean lento) {
        SilkRoad camino = new SilkRoad(20);
        camino.makeVisible();

        for (int i = 0; i < accionesPorDia.length; i++) {
            int tipo = accionesPorDia[i][0];
            int posicion = accionesPorDia[i][1];
            int cantidad = (accionesPorDia[i].length > 2) ? accionesPorDia[i][2] : 0;

            switch (tipo) {
                case 1 -> camino.placeRobot(posicion, "normal");
                case 2 -> camino.placeStore(posicion, cantidad);
                case 3 -> camino.placeRobot(posicion, "tender");
                case 4 -> camino.placeRobot(posicion, "neverback");
                default -> System.out.println("Tipo no reconocido en día " + (i + 1));
            }

            camino.moveRobot();
            mostrarEstadoDia(camino, i + 1);

            if (lento) pausarSimulacion(800);
        }

        RobotBase mejorRobot = camino.getRichestRobot();
        if (mejorRobot != null) {
            System.out.println("\nRobot con más tenges al final: " + mejorRobot);
            mejorRobot.blinkOnce();
        }

        System.out.println("\n Ganancia final: " + camino.getProfit());
    }

    // Ejecuta la simulación lógica sin gráficos y devuelve ganancias acumuladas por día
    public int[] resolverSimulacion(int[][] accionesPorDia) {
        SilkRoad camino = new SilkRoad(20);
        int[] resultados = new int[accionesPorDia.length];

        for (int i = 0; i < accionesPorDia.length; i++) {
            int tipo = accionesPorDia[i][0];
            int posicion = accionesPorDia[i][1];
            int cantidad = (accionesPorDia[i].length > 2) ? accionesPorDia[i][2] : 0;

            switch (tipo) {
                case 1 -> camino.placeRobot(posicion, "normal");
                case 2 -> camino.placeStore(posicion, cantidad);
                case 3 -> camino.placeRobot(posicion, "tender");
                case 4 -> camino.placeRobot(posicion, "neverback");
                default -> System.out.println("Tipo no reconocido en día " + (i + 1));
            }

            camino.moveRobot();
            resultados[i] = camino.getProfit();
            mostrarEstadoDia(camino, i + 1);
        }

        return resultados;
    }
}
