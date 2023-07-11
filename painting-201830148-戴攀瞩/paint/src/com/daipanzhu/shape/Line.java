package com.daipanzhu.shape;

import java.awt.*;

public class Line extends AbstractShape {

    public Line() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setStroke(new BasicStroke(width));
        g.drawLine(x1, y1, x2, y2);
    }
    @Override
    public boolean contains(int x, int y) {
        // 计算线段的斜率
        double slope = (double) (y2 - y1) / (x2 - x1);

        // 计算点到直线的距离
        double distance = Math.abs((slope * x - y + y1 - slope * x1) / Math.sqrt(slope * slope + 1));

        // 定义一个容差范围，用于判断点是否在直线附近
        double tolerance = 5;

        // 如果点到直线的距离在容差范围内，则认为点在直线上
        return distance <= tolerance;
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        return false;
    }
}
