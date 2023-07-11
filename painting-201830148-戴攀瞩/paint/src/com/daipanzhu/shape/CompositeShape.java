package com.daipanzhu.shape;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeShape extends AbstractShape {
    private List<AbstractShape> shapes;
    private static final float[] DASH_PATTERN = { 5f, 5f };
    public CompositeShape() {
        shapes = new ArrayList<>();
    }

    public void addShape(AbstractShape shape) {
        shapes.add(shape);
    }
    public List<AbstractShape> getShapes() {
        return shapes;
    }

    public void clearShape() {
        shapes.clear();
    }

    @Override
    public void draw(Graphics2D Gra) {
        Gra.setColor(color);
        Gra.setStroke(createDashedStroke());
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    private Stroke createDashedStroke() {
        return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, DASH_PATTERN, 0f);
    }
    @Override
    public boolean contains(int x, int y) {
        for (AbstractShape shape : shapes) {
            if (shape.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
    public void setBounds(int x, int y, int width, int height) {
        x1 = x;
        y1 = y;
        x2 = x + width;
        y2 = y + height;
    }
    public boolean intersects(AbstractShape shape) {
        return (x1 < shape.x2 && x2 > shape.x1 && y1 < shape.y2 && y2 > shape.y1);
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        return false;
    }
    @Override
    public CompositeShape clone() {
        CompositeShape clonedShape = (CompositeShape) super.clone();
        clonedShape.shapes = new ArrayList<>(shapes.size());
        for (AbstractShape shape : shapes) {
            clonedShape.addShape(shape.clone());
        }
        return clonedShape;
    }
    @Override
    public void move(int dx, int dy) {
        for (AbstractShape shape : shapes) {
            shape.x1 += dx;
            shape.y1 += dy;
            shape.x2 += dx;
            shape.y2 += dy;
        }
    }
}
