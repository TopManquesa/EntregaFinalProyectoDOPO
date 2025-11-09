package SilkRoads;

import Shapes.*;

/**
 * Clase abstracta RobotBase:
 * Define la estructura general de un robot en el simulador SilkRoads.
 * Contiene atributos, tamaños, color, movimiento y lógica compartida.
 * Subclases: Robot, NeverBackRobot, TenderRobot, etc.
 */
public abstract class RobotBase {

    // --- ATRIBUTOS GENERALES ---
    protected String type;              // tipo de robot (normal, tender, neverback, etc.)
    protected int currentPosition;      // posición actual
    protected int startingPosition;     // posición inicial
    protected int tengesCollected;      // dinero acumulado
    protected SilkRoad road;            // referencia al tablero
    protected String color;             // color del robot

    // --- COMPONENTES VISUALES ---
    protected Circle head;
    protected Rectangle neck;
    protected Rectangle body;
    protected Rectangle leftleg;
    protected Rectangle rightleg;

    // Última celda dibujada
    protected int lastRow = 0, lastCol = 0;

    // --- TAMAÑOS (CONSTANTES GLOBALES PARA TODOS LOS ROBOTS) ---
    protected static final int S = Cell.SIZE;
    protected static final double K = 0.65;
    protected static final int HEAD_D = (int)(S * 0.28 * K);
    protected static final int NECK_H = (int)(S * 0.08 * K);
    protected static final int NECK_W = (int)(S * 0.12 * K);
    protected static final int BODY_H = (int)(S * 0.40 * K);
    protected static final int BODY_W = (int)(S * 0.48 * K);
    protected static final int LEG_H  = (int)(S * 0.22 * K);
    protected static final int LEG_W  = (int)(S * 0.10 * K);

    // Constructor: inicializa atributos y componentes visuales del robot
    public RobotBase(String type, int position, int tenges, String color) {
        this.type = type;
        this.currentPosition = position;
        this.startingPosition = position;
        this.tengesCollected = tenges;
        this.color = color;

        // Inicializar formas
        head = new Circle();      head.changeSize(HEAD_D);
        neck = new Rectangle();   neck.changeSize(NECK_H, NECK_W);
        body = new Rectangle();   body.changeSize(BODY_H, BODY_W);
        leftleg  = new Rectangle();  leftleg.changeSize(LEG_H, LEG_W);
        rightleg = new Rectangle();  rightleg.changeSize(LEG_H, LEG_W);

        // Posición base en el tablero
        head.moveHorizontal(134); head.moveVertical(45);
        neck.moveHorizontal(90);  neck.moveVertical(45 + HEAD_D);
        body.moveHorizontal(80);  body.moveVertical(45 + HEAD_D + NECK_H);
        leftleg.moveHorizontal (BODY_W/3 + 80 - LEG_W/2);
        leftleg.moveVertical   (45 + HEAD_D + NECK_H + BODY_H);
        rightleg.moveHorizontal(2*BODY_W/3 + 80 - LEG_W/2);
        rightleg.moveVertical  (45 + HEAD_D + NECK_H + BODY_H);

        applyColor(color);
    }

    // Enlaza el robot con el tablero SilkRoad
    public void setRoad(SilkRoad road) {
        this.road = road;
    }

    // Añade ganancias al robot y actualiza líder en el tablero
    public synchronized void addEarnings(int amount) {
        if (amount <= 0) return;
        tengesCollected += amount;
        if (road != null) road.updateLeader();
    }

    // Devuelve las ganancias acumuladas
    public synchronized int getEarnings() {
        return tengesCollected;
    }

    // Cambia el color del robot
    public void colorChange(String newColor) {
        if (newColor != null) color = newColor;
        applyColor(color);
    }

    // Aplica color a todas las partes del robot
    protected void applyColor(String color) {
        head.changeColor(color);
        neck.changeColor(color);
        body.changeColor(color);
        leftleg.changeColor(color);
        rightleg.changeColor(color);
    }

    // Activa o desactiva parpadeo del robot
    public void setBlinking(boolean active) {
        if (active) blinkRepeated(6);
        else applyColor(color);
    }

    // Parpadea una vez (cambia a blanco y vuelve al color original)
    public void blinkOnce() {
        try {
            applyColor("white");
            Thread.sleep(250);
            applyColor(color);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Parpadea varias veces
    public void blinkRepeated(int times) {
        for (int i = 0; i < times; i++) {
            blinkOnce();
        }
    }

    // Hace visible al robot en el tablero
    public void makeVisible() {
        neck.makeVisible();
        body.makeVisible();
        leftleg.makeVisible();
        rightleg.makeVisible();
        head.makeVisible();
    }

    // Oculta al robot del tablero
    public void makeInvisible() {
        head.makeInvisible();
        neck.makeInvisible();
        body.makeInvisible();
        leftleg.makeInvisible();
        rightleg.makeInvisible();
    }

    // Mueve gráficamente al robot en el tablero según fila y columna
    public void moveRobot(int row, int col) {
        int dx = (col - lastCol) * Cell.STEP_X;
        int dy = (row - lastRow) * Cell.STEP_Y;

        head.moveHorizontal(dx);   head.moveVertical(dy);
        neck.moveHorizontal(dx);   neck.moveVertical(dy);
        body.moveHorizontal(dx);   body.moveVertical(dy);
        leftleg.moveHorizontal(dx); leftleg.moveVertical(dy);
        rightleg.moveHorizontal(dx); rightleg.moveVertical(dy);

        lastRow = row;
        lastCol = col;
    }

    // Devuelve posición actual del robot
    public int getPosition() { return currentPosition; }

    // Devuelve posición inicial del robot
    public int getInitialPosition() { return startingPosition; }

    // Devuelve tipo de robot
    public String getType() { return type; }

    // Reinicia robot a su posición inicial y ganancias en 0
    public void reset() {
        this.currentPosition = startingPosition;
        this.tengesCollected = 0;
    }

    // Método abstracto: cada subclase define su movimiento lógico
    public abstract void move();

    // Representación textual del robot
    @Override
    public String toString() {
        return "Robot{" +
                "tipo='" + type + '\'' +
                ", posición=" + currentPosition +
                ", inicio=" + startingPosition +
                ", ganancias=" + tengesCollected +
                ", color='" + color + '\'' +
                '}';
    }
}
