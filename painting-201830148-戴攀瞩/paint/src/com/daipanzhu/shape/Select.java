package com.daipanzhu.shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

public class Select extends AbstractShape {

    private static final float[] DASH_PATTERN = { 5f, 5f };

    public Select() {

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
        return false;
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        return false;
    }
}
