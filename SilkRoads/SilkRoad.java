package SilkRoads;

import Shapes.*;
import java.util.*;

/**
 * Clase principal SilkRoad: maneja tablero, tiendas, robots y lógica de la espiral.
 */
public class SilkRoad {

    // --- Estado principal ---
    private final int initialLength;
    private int length;

    private final List<StoreBase> stores;
    private final List<RobotBase> robots;
    private RobotBase currentLeader;

    // --- Visual y control ---
    private Board board;
    private boolean showing;
    private boolean lastOperationOk;
    private int profit;
    private int lastPrintedProfit = -1;

    // Barra de progreso
    private Rectangle profitBarBackground;
    private Rectangle profitBarFill;
    private static final int MAX_PROFIT = 1000;

    // Visibilidad de la espiral
    private int visibleTarget;

    // Paletas de colores
    private final String[] robotColors = {"red", "blue", "green", "orange", "magenta", "pink"};
    private final String[] storeColors = {"yellow", "cyan", "gray", "black", "lightgray"};

    // Offset para mapear coords lógicas a board
    private int originRow = 0;
    private int originCol = 0;

    // Log de ganancias
    private final List<int[]> robotGainLog = new ArrayList<>();

    // Constructor: prepara tablero, espiral y barra de progreso
    public SilkRoad(int length) {
        this.initialLength = length;
        this.length = 0;

        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();

        this.lastOperationOk = true;
        this.profit = 0;
        this.showing = false;

        this.board = new Board(1, 1);

        this.visibleTarget = Math.max(1, initialLength);
        preGrowToVisibleTarget();
        applyVisibleMaskForFirstN();

        board.makeVisible();

        profitBarBackground = new Rectangle();
        profitBarFill = new Rectangle();

        profitBarBackground.changeSize(20, 200);
        profitBarBackground.changeColor("gray");
        profitBarFill.changeSize(20, 0);
        profitBarFill.changeColor("green");

        layoutProfitBarAboveBoard();
        profitBarBackground.makeVisible();
        profitBarFill.makeVisible();

        showing = true;
        updateProfitBar();
    }

    // Muestra tablero, tiendas, robots y barra
    public void makeVisible() {
        board.makeVisible();
        for (StoreBase s : stores) s.makeVisible();
        for (RobotBase r : robots) r.makeVisible();

        if (isBarReady()) {
            profitBarBackground.makeVisible();
            profitBarFill.makeVisible();
        }
        showing = true;
        updateProfitBar();
    }

    // Oculta tablero, tiendas, robots y barra
    public void makeInvisible() {
        for (StoreBase s : stores) s.makeInvisible();
        for (RobotBase r : robots) r.makeInvisible();
        board.makeInvisible();

        if (isBarReady()) {
            profitBarBackground.makeInvisible();
            profitBarFill.makeInvisible();
        }
        showing = false;
    }

    // Devuelve el robot más rico
    public RobotBase getRichestRobot() {
        if (robots.isEmpty()) return null;
        RobotBase richest = robots.get(0);
        for (RobotBase r : robots) if (r.getEarnings() > richest.getEarnings()) richest = r;
        return richest;
    }

    // Verifica si ya existe una tienda en esa posición
    private boolean storeExistsAt(int location) {
        for (StoreBase s : stores) if (s.getPosition() == location) return true;
        return false;
    }

    // Coloca una tienda en el tablero
    public void placeStore(int location, String type, int tenges) {
        if (storeExistsAt(location)) { lastOperationOk = false; return; }

        ensureBoardCanFitForLocation(location);
        int[] rcBoard = boardCoordsForLocation(location);

        StoreBase newStore;
        switch (type.toLowerCase()) {
            case "autonomous" -> newStore = new AutonomousStore(location, tenges);
            case "fighter"    -> newStore = new FighterStore(location, tenges);
            default           -> newStore = new Store(location, tenges);
        }

        newStore.setRoad(this);
        newStore.moveStore(rcBoard[0], rcBoard[1]);
        newStore.setColor(storeColors[stores.size() % storeColors.length]);

        stores.add(newStore);

        if (showing) {
            newStore.makeVisible();
            resyncVisuals();
        }
        lastOperationOk = true;
    }

    // Coloca tienda normal
    public void placeStore(int location, int tenges) {
        placeStore(location, "normal", tenges);
    }

    // Coloca un robot en el tablero
    public void placeRobot(int location, String type) {
        ensureBoardCanFitForLocation(location);
        int[] rcBoard = boardCoordsForLocation(location);

        RobotBase newRobot;
        switch (type.toLowerCase()) {
            case "neverback" -> newRobot = new NeverBackRobot(location, 0);
            case "tender"    -> newRobot = new TenderRobot(location, 0);
            default          -> newRobot = new Robot(location, 0);
        }

        newRobot.setRoad(this);
        newRobot.moveRobot(rcBoard[0], rcBoard[1]);
        robots.add(newRobot);

        if (showing) {
            newRobot.makeVisible();
            resyncVisuals();
        }
        lastOperationOk = true;
    }

    // Coloca robot normal
    public void placeRobot(int position) { placeRobot(position, "normal"); }

    // Elimina tienda
    public void removeStore(int location) {
        boolean removed = stores.removeIf(s -> {
            if (s.getPosition() == location) {
                s.makeInvisible();
                return true;
            }
            return false;
        });
        lastOperationOk = removed;
    }

    // Elimina robot
    public void removeRobot(int location) {
        boolean removed = robots.removeIf(r -> {
            if (r.getInitialPosition() == location) {
                r.makeInvisible();
                return true;
            }
            return false;
        });
        lastOperationOk = removed;
    }
    // Mueve un robot manualmente según su posición y distancia
    public void moveRobot(int currentPosition, int meters) {
        for (RobotBase r : robots) {
            if (r.getPosition() == currentPosition) {
                int newLocation = currentPosition + meters;
                if (!insideVisibleTarget(newLocation)) ensureBoardCanFitForLocation(newLocation);
                r.currentPosition = newLocation;

                int[] rcBoard = boardCoordsForLocation(newLocation);
                r.moveRobot(rcBoard[0], rcBoard[1]);
                r.makeVisible();

                for (StoreBase s : stores) {
                    if (s.getPosition() == newLocation && s.getTenges() > 0) {
                        int collected = s.getTenges();
                        s.emptyOnce();
                        int gain = collected - Math.abs(meters);
                        if (gain > 0) {
                            profit += gain;
                            updateProfitBar();
                            r.addEarnings(gain);
                        }
                    }
                }
                resyncVisuals();
                lastOperationOk = true;
                return;
            }
        }
        lastOperationOk = false;
    }

    // Mueve automáticamente el robot que más ganancia puede obtener
    public void moveRobot() {
        int bestGain = Integer.MIN_VALUE;
        RobotBase bestRobot = null;
        StoreBase bestStore = null;

        for (RobotBase r : robots) {
            for (StoreBase s : stores) {
                if (s.getTenges() <= 0) continue;
                int dist = Math.abs(r.getPosition() - s.getPosition());
                int gain = s.getTenges() - dist;
                if (gain > bestGain) {
                    bestGain = gain;
                    bestRobot = r;
                    bestStore = s;
                }
            }
        }

        if (bestRobot == null || bestStore == null || bestGain <= 0) {
            lastOperationOk = false;
            return;
        }

        int meters = bestStore.getPosition() - bestRobot.getPosition();
        if (!insideVisibleTarget(bestStore.getPosition())) ensureBoardCanFitForLocation(bestStore.getPosition());
        int[] rc = boardCoordsForLocation(bestStore.getPosition());

        bestRobot.currentPosition = bestStore.getPosition();
        bestRobot.moveRobot(rc[0], rc[1]);

        int collected = bestStore.getTenges();
        bestStore.emptyOnce();
        int gain = collected - Math.abs(meters);

        if (gain > 0) {
            bestRobot.addEarnings(gain);
            profit += gain;
            updateProfitBar();
        }

        resyncVisuals();
        lastOperationOk = true;
    }

    // Restaura todas las tiendas a su dinero inicial
    public void resupplyStores() {
        for (StoreBase s : stores) s.resupply();
        lastOperationOk = true;
    }

    // Devuelve todos los robots a su posición inicial
    public void returnRobots() {
        for (RobotBase r : robots) {
            r.reset();
            int loc = r.getInitialPosition();
            ensureBoardCanFitForLocation(loc);
            int[] rc = boardCoordsForLocation(loc);
            r.moveRobot(rc[0], rc[1]);
            r.makeVisible();
        }
        lastOperationOk = true;
    }

    // Reinicia todo: tiendas, robots y ganancias
    public void reboot() {
        resupplyStores();
        returnRobots();
        profit = 0;
        updateProfitBar();
        lastOperationOk = true;
    }

    // Actualiza la barra de ganancias
    private void updateProfitBar() {
        if (!showing) return;
        int fillHeight = (int) (200.0 * profit / MAX_PROFIT);
        if (fillHeight > 200) fillHeight = 200;
        profitBarFill.changeSize(20, Math.max(fillHeight, 0));

        if (profit != lastPrintedProfit) {
            System.out.println("Profit actual: " + profit);
            lastPrintedProfit = profit;
        }
    }

    // Verifica si la barra está lista
    private boolean isBarReady() {
        return profitBarBackground != null && profitBarFill != null;
    }

    // Coloca la barra sobre el tablero
    private void layoutProfitBarAboveBoard() {
        profitBarBackground.moveHorizontal(0);
        profitBarBackground.moveVertical(0);
        profitBarFill.moveHorizontal(0);
        profitBarFill.moveVertical(0);
    }

    // Convierte posición lógica a coordenadas en espiral
    public int[] locationToCoords(int location) {
        if (location == 0) return new int[]{0, 0};
        int r = 0, c = 0;
        final int[] dr = {0, +1, 0, -1};
        final int[] dc = {+1, 0, -1, 0};
        int dir = 0, segLen = 1, used = 0, steps = location;
        while (steps-- > 0) {
            r += dr[dir];
            c += dc[dir];
            used++;
            if (used == segLen) {
                used = 0;
                dir = (dir + 1) % 4;
                if (dir % 2 == 0) segLen++;
            }
        }
        return new int[]{r, c};
    }

    // Convierte coordenadas lógicas a tablero
    private int[] boardCoordsFromLogical(int rLog, int cLog) {
        return new int[]{rLog + originRow, cLog + originCol};
    }

    // Devuelve coordenadas de tablero para una posición
    private int[] boardCoordsForLocation(int location) {
        int[] rcLog = locationToCoords(location);
        return boardCoordsFromLogical(rcLog[0], rcLog[1]);
    }
    // Ajusta el tablero para crecer hasta cubrir la espiral visible
    private void preGrowToVisibleTarget() {
        int minR = 0, maxR = 0, minC = 0, maxC = 0;
        for (int i = 0; i < visibleTarget; i++) {
            int[] rc = locationToCoords(i);
            minR = Math.min(minR, rc[0]);
            maxR = Math.max(maxR, rc[0]);
            minC = Math.min(minC, rc[1]);
            maxC = Math.max(maxC, rc[1]);
        }

        originRow = -minR;
        originCol = -minC;

        int needRows = maxR - minR + 1;
        int needCols = maxC - minC + 1;

        if (needRows > board.getRows() || needCols > board.getCols()) {
            boolean wasVisible = showing;
            if (wasVisible) board.makeInvisible();
            board = new Board(needRows, needCols);
            if (wasVisible) board.makeVisible();
            if (wasVisible) resyncVisuals();
            if (isBarReady()) layoutProfitBarAboveBoard();
        }
    }

    // Aplica máscara de visibilidad para las primeras N celdas
    private void applyVisibleMaskForFirstN() {
        int rows = board.getRows();
        int cols = board.getCols();
        boolean[][] mask = new boolean[rows][cols];
        for (int i = 0; i < visibleTarget; i++) {
            int[] rc = locationToCoords(i);
            int br = rc[0] + originRow;
            int bc = rc[1] + originCol;
            if (br >= 0 && br < rows && bc >= 0 && bc < cols) mask[br][bc] = true;
        }
        board.setVisibleCells(mask);
    }

    // Asegura que el tablero pueda mostrar una posición dada
    private void ensureBoardCanFitForLocation(int location) {
        if (location + 1 > visibleTarget) {
            visibleTarget = location + 1;
            preGrowToVisibleTarget();
            applyVisibleMaskForFirstN();
        }
        int[] rcLog = locationToCoords(location);
        ensureBoardCanFitLogical(rcLog[0], rcLog[1]);
        applyVisibleMaskForFirstN();
        layoutProfitBarAboveBoard();
    }

    // Ajusta tablero para coordenadas lógicas específicas
    private void ensureBoardCanFitLogical(int rLog, int cLog) {
        int br = rLog + originRow;
        int bc = cLog + originCol;

        int newRows = board.getRows();
        int newCols = board.getCols();
        int addTop = 0, addLeft = 0;

        if (br < 0) { addTop = -br; originRow += addTop; newRows += addTop; br = 0; }
        if (bc < 0) { addLeft = -bc; originCol += addLeft; newCols += addLeft; bc = 0; }
        if (br >= newRows) newRows = br + 1;
        if (bc >= newCols) newCols = bc + 1;

        if (newRows == board.getRows() && newCols == board.getCols() && addTop == 0 && addLeft == 0) return;

        boolean wasVisible = showing;
        if (wasVisible) board.makeInvisible();

        board = new Board(newRows, newCols);

        if (wasVisible) {
            board.makeVisible();
            resyncVisuals();
        }
        if (isBarReady()) layoutProfitBarAboveBoard();
    }

    // Verifica si una posición está dentro del rango visible
    private boolean insideVisibleTarget(int loc) {
        return loc >= 0 && loc < visibleTarget;
    }

    // Re-sincroniza visuales de tiendas y robots
    private void resyncVisuals() {
        for (StoreBase s : stores) {
            int[] rc = boardCoordsForLocation(s.getPosition());
            s.moveStore(rc[0], rc[1]);
            s.makeVisible();
        }
        for (RobotBase rr : robots) {
            int[] rc = boardCoordsForLocation(rr.getPosition());
            rr.moveRobot(rc[0], rc[1]);
            rr.makeVisible();
        }
    }

    // Actualiza el robot líder (más rico) y lo hace parpadear
    public void updateLeader() {
        if (robots.isEmpty()) return;
        RobotBase leader = robots.get(0);
        for (RobotBase r : robots) if (r.getEarnings() > leader.getEarnings()) leader = r;

        if (leader != currentLeader) {
            if (currentLeader != null) currentLeader.setBlinking(false);
            leader.setBlinking(true);
            currentLeader = leader;
        }
    }

    // Verifica si una celda está libre para colocar tienda
    public boolean isFreeForStore(int loc) {
        for (StoreBase s : stores) if (s.getPosition() == loc) return false;
        return true;
    }

    // Devuelve tamaño actual de la ruta
    public int getRouteSize() {
        return Math.max(visibleTarget, 1);
    }

    // Devuelve ganancias acumuladas
    public int getProfit() {
        return profit;
    }

    // Devuelve copia de la lista de robots
    public java.util.List<RobotBase> getRobots() {
        return new java.util.ArrayList<>(robots);
    }

    // Devuelve copia de la lista de tiendas
    public java.util.List<StoreBase> getStores() {
        return new java.util.ArrayList<>(stores);
    }
}
