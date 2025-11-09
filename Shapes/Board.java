package Shapes;

import java.util.Arrays;
import SilkRoads.*;

/**
 * Autor: Diego Fabian Andrade
 * Clase Board: representa un tablero simple con margen externo.
 * Cada celda se dibuja con separación visual y puede ocultarse con máscara.
 */
public class Board {

    // --- Margen visual ---
    private static final int MARGIN_X = 60;
    private static final int MARGIN_Y = 20;

    // --- Estructura del tablero ---
    private final int rows;
    private final int cols;
    private final Cell[][] cells;
    private boolean visible = false;

    // --- Máscara de visibilidad por celda (true = visible) ---
    private boolean[][] visibleMask;

    // Constructor: crea un tablero de tamaño dado y lo inicializa con celdas blancas
    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        int stepX = Cell.STEP_X;
        int stepY = Cell.STEP_Y;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = MARGIN_X + c * stepX;
                int y = MARGIN_Y + r * stepY;
                cells[r][c] = new Cell("white", false, x, y);
            }
        }

        // Inicializa la máscara: todas las celdas visibles por defecto
        visibleMask = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            Arrays.fill(visibleMask[r], true);
        }
    }

    // Devuelve el número de filas
    public int getRows() { return rows; }

    // Devuelve el número de columnas
    public int getCols() { return cols; }

    // Indica si el tablero está visible
    public boolean isVisible() { return visible; }

    // Muestra el tablero respetando la máscara de visibilidad
    public void makeVisible() {
        if (visible) return;
        visible = true;
        applyMaskToCells();
    }

    // Oculta todas las celdas del tablero
    public void makeInvisible() {
        if (!visible) return;
        visible = false;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c].makeInvisible();
            }
        }
    }

    // Devuelve la celda en la posición (row, col)
    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    // Establece una nueva máscara de visibilidad y la aplica si el tablero está visible
    public void setVisibleCells(boolean[][] mask) {
        if (mask == null || mask.length != rows || mask[0].length != cols) {
            throw new IllegalArgumentException("Mask size must match board size");
        }
        this.visibleMask = mask;
        if (visible) applyMaskToCells();
    }

    // Aplica la máscara actual a todas las celdas
    private void applyMaskToCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (visibleMask[r][c]) {
                    cells[r][c].makeVisible();
                } else {
                    cells[r][c].makeInvisible();
                }
            }
        }
    }

    // --- Helpers públicos para obtener coordenadas en píxeles ---

    // Coordenada X de la esquina izquierda de una columna
    public int getPixelXForCol(int col) {
        return MARGIN_X + col * Cell.STEP_X;
    }

    // Coordenada Y de la esquina superior de una fila
    public int getPixelYForRow(int row) {
        return MARGIN_Y + row * Cell.STEP_Y;
    }

    // Coordenada X del borde derecho del tablero
    public int getBoardRightX() {
        return getPixelXForCol(cols - 1) + Cell.SIZE;
    }

    // Coordenada Y del borde inferior del tablero
    public int getBoardBottomY() {
        return getPixelYForRow(rows - 1) + Cell.SIZE;
    }

    // Coordenada X del borde izquierdo del tablero
    public int getBoardLeftX() {
        return getPixelXForCol(0);
    }

    // Coordenada Y del borde superior del tablero
    public int getBoardTopY() {
        return getPixelYForRow(0);
    }
}
