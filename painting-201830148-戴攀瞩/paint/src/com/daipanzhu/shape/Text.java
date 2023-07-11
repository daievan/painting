package com.daipanzhu.shape;

import java.awt.*;


public class Text extends AbstractShape {


    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setFont(new Font(fontName, italic + blodtype, fontSize));
        if (s != null) {

            g.drawString(s, x1, y1);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        return false;
    }
}