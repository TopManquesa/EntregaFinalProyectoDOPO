package Shapes;
import SilkRoads.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Canvas {

    private static Canvas canvasSingleton;

    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 2000, 2000, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    // --- Instance fields ---
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;
    private List<TextDescription> texts;  // reemplaza los labels

    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<>();
        shapes = new HashMap<>();
        texts = new ArrayList<>();
    }

    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    // ------------------------- Dibujo de figuras -------------------------

    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    // ------------------------- Dibujo de texto -------------------------

    public void drawText(String text, int x, int y, String color) {
        texts.add(new TextDescription(text, x, y, color));
        redraw();
    }

    public void eraseAllText() {
        texts.clear();
        redraw();
    }

    // ------------------------- Colores y utilidades -------------------------

    public void setForegroundColor(String colorString) {
        switch (colorString.toLowerCase()) {
            case "red" -> graphic.setColor(Color.red);
            case "black" -> graphic.setColor(Color.black);
            case "blue" -> graphic.setColor(Color.blue);
            case "yellow" -> graphic.setColor(Color.yellow);
            case "green" -> graphic.setColor(Color.green);
            case "magenta" -> graphic.setColor(Color.magenta);
            case "white" -> graphic.setColor(Color.white);
            default -> graphic.setColor(Color.black);
        }
    }

    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ignored) {}
    }

    // ------------------------- Redibujar -------------------------

    private void redraw() {
        erase();
        for (Object ref : objects) {
            shapes.get(ref).draw(graphic);
        }

        // Dibuja los textos
        for (TextDescription text : texts) {
            setForegroundColor(text.color);
            graphic.drawString(text.text, text.x, text.y);
        }

        canvas.repaint();
    }

    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    // ------------------------- Clases internas -------------------------

    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    private class ShapeDescription {
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            this.colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }

    private class TextDescription {
        String text;
        int x, y;
        String color;

        public TextDescription(String text, int x, int y, String color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}
