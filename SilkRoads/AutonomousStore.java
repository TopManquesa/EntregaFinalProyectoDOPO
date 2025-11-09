package SilkRoads;

/**
 * Clase AutonomousStore: representa una tienda que se recoloca automáticamente
 * al ser colocada en el tablero.
 */
public class AutonomousStore extends StoreBase {

    // Constructor: crea una tienda "autonomous" con posición inicial y tenges
    public AutonomousStore(int position, int tenges) {
        super("autonomous", position, tenges);
    }

    // Al colocarse en el tablero, busca recolocarse usando el mapa de posiciones libres
    @Override
    public void onPlaced(SilkRoad road) {
        if (road == null) return;

        int routeSize = road.getRouteSize();
        boolean[] isFree = new boolean[routeSize];

        for (int i = 0; i < routeSize; i++) {
            isFree[i] = road.isFreeForStore(i);
        }

        relocateUsingFreeMap(routeSize, isFree);
    }

    // Recolocación auxiliar: busca la primera posición libre en [position .. position+4]
    public void relocateUsingFreeMap(int routeSize, boolean[] isFree) {
        int limit = Math.min(this.position + 5, routeSize);
        int newPos = this.position;

        for (int i = this.position; i < limit; i++) {
            if (i >= 0 && i < isFree.length && isFree[i]) {
                newPos = i;
                break;
            }
        }

        if (newPos != this.position) {
            this.position = newPos;
        }
    }
}
