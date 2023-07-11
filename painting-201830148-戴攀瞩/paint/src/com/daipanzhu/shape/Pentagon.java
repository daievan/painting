package com.daipanzhu.shape;

import java.awt.*;

public class Pentagon extends AbstractShape {


    @Override
    public void draw(Graphics2D Gra) {
        Gra.setPaint(color);
        Gra.setStroke(new BasicStroke(width));
        int[] x = {(x1 + x2) / 2, Math.min(x1, x2), Math.min(x1, x2) + Math.abs(x1 - x2) / 4,
                Math.max(x1, x2) - Math.abs(x1 - x2) / 4, Math.max(x1, x2)};
        int[] y = {Math.min(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5), Math.max(y1, y2),
                Math.max(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5)};
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawPolygon(x, y, 5);
    }
    @Override
    public boolean contains(int x, int y) {
        int[] px = {(x1 + x2) / 2, Math.min(x1, x2), Math.min(x1, x2) + Math.abs(x1 - x2) / 4,
                Math.max(x1, x2) - Math.abs(x1 - x2) / 4, Math.max(x1, x2)};
        int[] py = {Math.min(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5), Math.max(y1, y2),
                Math.max(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5)};

        Polygon polygon = new Polygon(px, py, 5);
        return polygon.contains(x, y);
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        int[] px = {(x1 + x2) / 2, Math.min(x1, x2), Math.min(x1, x2) + Math.abs(x1 - x2) / 4,
                Math.max(x1, x2) - Math.abs(x1 - x2) / 4, Math.max(x1, x2)};
        int[] py = {Math.min(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5), Math.max(y1, y2),
                Math.max(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5)};

        Polygon polygon = new Polygon(px, py, 5);

        // 计算矩形的左上角和右下角坐标
        int rectLeft = Math.min(rectX1, rectX2);
        int rectRight = Math.max(rectX1, rectX2);
        int rectTop = Math.min(rectY1, rectY2);
        int rectBottom = Math.max(rectY1, rectY2);

        // 检查多边形的顶点是否在矩形内
        for (int i = 0; i < polygon.npoints; i++) {
            if (rectLeft <= polygon.xpoints[i] && polygon.xpoints[i] <= rectRight &&
                    rectTop <= polygon.ypoints[i] && polygon.ypoints[i] <= rectBottom) {
                return true;
            }
        }

        // 检查矩形的顶点是否在多边形内
        for (int i = 0; i < 4; i++) {
            if (polygon.contains(rectX1, rectY1) || polygon.contains(rectX2, rectY1) ||
                    polygon.contains(rectX2, rectY2) || polygon.contains(rectX1, rectY2)) {
                return true;
            }
        }

        return false;
    }

}