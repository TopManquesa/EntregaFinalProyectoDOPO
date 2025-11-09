package SilkRoads;

import Shapes.*;

/**
 * Clase Cell: representa una celda del tablero con color, borde y tamaño.
 */
public class Cell {
    private String color;        // color de la celda
    private boolean hole;        // indica si es un hueco
    private boolean hasMarbel;   // indica si contiene una canica
    private Rectangle box;       // cuadro interno
    private Rectangle border;    // borde externo
    private int size;            // tamaño de la celda
    private int margin;          // margen interno
    private boolean isHeadless;  // celda sin gráficos

    // Tamaño básico de cada celda
    public static final int SIZE = 80;

    // Margen interno para que se vea el borde negro
    public static final int MARGIN = Math.max(1, Math.round(SIZE * 0.016f));

    // Separación visual entre caminos
    public static final int GAP_X = 10; 
    public static final int GAP_Y = 10; 

    // Paso efectivo al ubicar celdas en el tablero
    public static final int STEP_X = SIZE + GAP_X;
    public static final int STEP_Y = SIZE + GAP_Y;

    // Constructor simple: crea celda con color, hueco y coordenadas
    public Cell(String color, boolean hole, int x, int y) {
        this(color, hole, x, y, false);
    }

    // Constructor completo: permite definir si la celda es "headless"
    public Cell(String color, boolean hole, int x, int y, boolean isHeadless) {
        this.color = color;
        this.hole = hole;
        this.isHeadless = isHeadless;

        if (!isHeadless) {
            border = new Rectangle();
            border.changeSize(SIZE, SIZE);
            border.changeColor("black");
            border.moveHorizontal(x);
            border.moveVertical(y);

            box = new Rectangle();
            box.changeSize(SIZE - 2 * MARGIN, SIZE - 2 * MARGIN);
            box.changeColor(color);
            box.moveHorizontal(x + MARGIN);
            box.moveVertical(y + MARGIN);
        }
    }

    // Hace visible la celda en el tablero
    public void makeVisible() {
        if (isHeadless) return;
        border.makeVisible();
        box.makeVisible();
    }

    // Oculta la celda del tablero
    public void makeInvisible() {
        if (isHeadless) return;
        border.makeInvisible();
        box.makeInvisible();
    }

    // Devuelve el color actual de la celda
    public String getColor() {
        return color;
    }

    // Devuelve el tamaño básico de la celda
    public int getSize() {
        return SIZE;
    }

    // Cambia el color de la celda y lo aplica al cuadro interno
    public void setColor(String newColor) {
        if (isHeadless) return;
        this.color = newColor;
        box.changeColor(newColor);
        box.makeVisible();
    }
}
