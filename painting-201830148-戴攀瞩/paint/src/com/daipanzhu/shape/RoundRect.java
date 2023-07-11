package com.daipanzhu.shape;

import java.awt.*;

public class RoundRect extends AbstractShape {
    @Override
    public void draw(Graphics2D Gra) {
        Gra.setPaint(color);
        Gra.setStroke(new BasicStroke(width));
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawRoundRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), 50, 35);
    }

    @Override
    public boolean contains(int x, int y) {
        int rx = Math.min(x1, x2);
        int ry = Math.min(y1, y2);
        int rw = Math.abs(x1 - x2);
        int rh = Math.abs(y1 - y2);
        int arcWidth = 50; // 圆角的宽度
        int arcHeight = 35; // 圆角的高度

        // 判断点 (x, y) 是否在圆角矩形内部
        if (x >= rx && x <= rx + rw && y >= ry && y <= ry + rh) {
            int dx = x - rx;
            int dy = y - ry;

            if (dx < arcWidth && dy < arcHeight) {
                // 左上角的圆角区域
                return Math.pow(dx - arcWidth, 2) + Math.pow(dy - arcHeight, 2) <= Math.pow(arcWidth, 2);
            } else if (dx < arcWidth && dy > rh - arcHeight) {
                // 左下角的圆角区域
                return Math.pow(dx - arcWidth, 2) + Math.pow(dy - (rh - arcHeight), 2) <= Math.pow(arcWidth, 2);
            } else if (dx > rw - arcWidth && dy < arcHeight) {
                // 右上角的圆角区域
                return Math.pow(dx - (rw - arcWidth), 2) + Math.pow(dy - arcHeight, 2) <= Math.pow(arcWidth, 2);
            } else if (dx > rw - arcWidth && dy > rh - arcHeight) {
                // 右下角的圆角区域
                return Math.pow(dx - (rw - arcWidth), 2) + Math.pow(dy - (rh - arcHeight), 2) <= Math.pow(arcWidth, 2);
            } else {
                // 矩形内部
                return true;
            }
        }

        return false;
    }

    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        int rx = Math.min(x1, x2);
        int ry = Math.min(y1, y2);
        int rw = Math.abs(x1 - x2);
        int rh = Math.abs(y1 - y2);

        // 判断矩形框与圆角矩形是否有重叠
        if (rx + rw < rectX1 || rectX2 < rx || ry + rh < rectY1 || rectY2 < ry) {
            // 矩形框在圆角矩形的外部
            return false;
        } else {
            return true;
        }
    }

}