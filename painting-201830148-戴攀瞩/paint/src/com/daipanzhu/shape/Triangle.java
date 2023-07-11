package com.daipanzhu.shape;

import java.awt.*;

public class Triangle extends AbstractShape {


    @Override
    public void draw(Graphics2D Gra) {
        Gra.setPaint(color);
        Gra.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        int[] x = {(x1 + x2) / 2, x1, x2};
        int[] y = {y1, y2, y2};
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawPolygon(x, y, 3);
    }
    @Override
    public boolean contains(int x, int y) {
        int[] px = {(x1 + x2) / 2, x1, x2};
        int[] py = {y1, y2, y2};

        // 使用Java的Polygon类创建一个多边形对象
        Polygon polygon = new Polygon(px, py, 3);

        // 使用Polygon类的contains方法检查点是否在三角形内部
        return polygon.contains(x, y);
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        // 计算矩形的左上角和右下角坐标
        int rectLeft = Math.min(rectX1, rectX2);
        int rectRight = Math.max(rectX1, rectX2);
        int rectTop = Math.min(rectY1, rectY2);
        int rectBottom = Math.max(rectY1, rectY2);

        // 判断三角形的顶点是否在矩形内部
        int[] px = {(x1 + x2) / 2, x1, x2};
        int[] py = {y1, y2, y2};

        for (int i = 0; i < 3; i++) {
            if (px[i] >= rectLeft && px[i] <= rectRight && py[i] >= rectTop && py[i] <= rectBottom) {
                return true;
            }
        }

        // 判断矩形的边是否与三角形相交
        for (int i = 0; i < 3; i++) {
            int j = (i + 1) % 3;
            if (lineIntersectsRectangle(px[i], py[i], px[j], py[j], rectLeft, rectTop, rectRight, rectBottom)) {
                return true;
            }
        }

        return false;
    }
    private boolean lineIntersectsRectangle(int x1, int y1, int x2, int y2, int rectLeft, int rectTop, int rectRight, int rectBottom) {
        return lineIntersectsLine(x1, y1, x2, y2, rectLeft, rectTop, rectRight, rectTop) ||
                lineIntersectsLine(x1, y1, x2, y2, rectRight, rectTop, rectRight, rectBottom) ||
                lineIntersectsLine(x1, y1, x2, y2, rectRight, rectBottom, rectLeft, rectBottom) ||
                lineIntersectsLine(x1, y1, x2, y2, rectLeft, rectBottom, rectLeft, rectTop);
    }

    private boolean lineIntersectsLine(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int d1 = direction(x3, y3, x4, y4, x1, y1);
        int d2 = direction(x3, y3, x4, y4, x2, y2);
        int d3 = direction(x1, y1, x2, y2, x3, y3);
        int d4 = direction(x1, y1, x2, y2, x4, y4);
        return (d1 > 0 && d2 < 0 || d1 < 0 && d2 > 0) && (d3 > 0 && d4 < 0 || d3 < 0 && d4 > 0);
    }

    private int direction(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (x3 - x1) * (y2 - y1) - (x2 - x1) * (y3 - y1);
    }

}