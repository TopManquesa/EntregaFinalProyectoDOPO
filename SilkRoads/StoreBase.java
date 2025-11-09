package SilkRoads;

import Shapes.*;

/**
 * Clase abstracta StoreBase:
 * Define la estructura general de una tienda en el simulador SilkRoads.
 * Contiene atributos, tamaños, color, ganancias, visibilidad y lógica compartida.
 * Subclases: Store, AutonomousStore, FighterStore, etc.
 */
public abstract class StoreBase {

    // --- Estado lógico ---
    protected String type;        // tipo de tienda
    protected int position;       // posición lógica en la SilkRoad
    protected int tenges;         // dinero actual disponible
    protected int initialTenges;  // dinero inicial (para resupply)
    protected int timesEmptied;   // número de veces vaciada
    protected int drawRow = 0;    // fila dibujada
    protected int drawCol = 0;    // columna dibujada

    // --- Integración con el camino ---
    protected SilkRoad road;      // referencia al SilkRoad (opcional)

    // --- Componentes visuales ---
    protected Rectangle base;
    protected Triangle roof;

    // Constructor: inicializa tipo, posición y dinero de la tienda
    public StoreBase(String type, int position, int tenges) {
        this.type = type;
        this.position = position;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.timesEmptied = 0;

        // Configuración visual básica
        base = new Rectangle();
        roof = new Triangle();
        base.changeColor("magenta");
        roof.changeColor("green");
    }

    // Vacía la tienda una vez: devuelve el dinero y aumenta contador si había
    public int emptyOnce() {
        if (tenges > 0) {
            int collected = tenges;
            tenges = 0;
            timesEmptied++;
            return collected;
        }
        return 0;
    }

    // Restaura el dinero al valor inicial
    public void resupply() {
        tenges = initialTenges;
    }

    // Devuelve el tipo de tienda
    public String getType() { return type; }

    // Devuelve la posición lógica
    public int getPosition() { return position; }

    // Devuelve el dinero actual
    public int getTenges() { return tenges; }

    // Devuelve cuántas veces ha sido vaciada
    public int getTimesEmptied() { return timesEmptied; }

    // Hace visible la tienda en el tablero
    public void makeVisible() {
        base.makeVisible();
        roof.makeVisible();
    }

    // Oculta la tienda del tablero
    public void makeInvisible() {
        base.makeInvisible();
        roof.makeInvisible();
    }

    // Mueve la tienda a una fila y columna específicas en el tablero
    public void moveStore(int row, int col) {
        base.moveHorizontal(col * 60);
        base.moveVertical(row * 60);
        roof.moveHorizontal(col * 60);
        roof.moveVertical(row * 60 - 20);
    }

    // Cambia el color de la tienda (alias para compatibilidad)
    public void setColor(String color) { changeColor(color); }

    // Aplica un nuevo color al rectángulo base
    public void changeColor(String color) { base.changeColor(color); }

    // Asocia la tienda al tablero SilkRoad y dispara hook de colocación
    public final void attachToRoad(SilkRoad road) {
        this.road = road;
        onPlaced(road);
    }

    // Alias por compatibilidad (SilkRoad usa setRoad)
    @Deprecated
    public final void setRoad(SilkRoad road) {
        attachToRoad(road);
    }

    // Hook sobrescribible por subclases (por defecto no hace nada)
    public void onPlaced(SilkRoad road) {
        // no-op por defecto
    }
}
