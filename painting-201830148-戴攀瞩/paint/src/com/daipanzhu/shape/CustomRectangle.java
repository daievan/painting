package com.daipanzhu.shape;

import java.awt.*;

public class CustomRectangle extends AbstractShape {


    public CustomRectangle() {


    }

    @Override
    public void draw(Graphics2D Gra) {

        Gra.setColor(color);
        Gra.setStroke(new BasicStroke(width));
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2),
                Math.abs(y1 - y2));
    }
    @Override
    public boolean contains(int x, int y) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        int rectLeft = Math.min(rectX1, rectX2);
        int rectRight = Math.max(rectX1, rectX2);
        int rectTop = Math.min(rectY1, rectY2);
        int rectBottom = Math.max(rectY1, rectY2);

        // 判断矩形与矩形是否重叠
        return !(rectLeft > maxX || rectRight < minX || rectTop > maxY || rectBottom < minY);
    }
}