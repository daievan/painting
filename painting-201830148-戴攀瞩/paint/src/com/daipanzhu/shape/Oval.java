package com.daipanzhu.shape;

import java.awt.*;

public class Oval extends AbstractShape {


    @Override
    public void draw(Graphics2D Gra) {
        Gra.setPaint(color);
        Gra.setStroke(new BasicStroke(width));
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    @Override
    public boolean contains(int x, int y) {
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        int radiusX = Math.abs(x1 - x2) / 2;
        int radiusY = Math.abs(y1 - y2) / 2;

        // 使用椭圆的数学方程来判断点 (x, y) 是否在椭圆内部
        double value = Math.pow((x - centerX) / (double) radiusX, 2) + Math.pow((y - centerY) / (double) radiusY, 2);
        return value <= 1.0;
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        // 计算椭圆的中心坐标
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;

        // 计算椭圆的半径
        int radiusX = Math.abs(x1 - x2) / 2;
        int radiusY = Math.abs(y1 - y2) / 2;

        // 计算矩形的左上角和右下角坐标
        int rectLeft = Math.min(rectX1, rectX2);
        int rectRight = Math.max(rectX1, rectX2);
        int rectTop = Math.min(rectY1, rectY2);
        int rectBottom = Math.max(rectY1, rectY2);

        // 判断椭圆与矩形是否重叠
        int closestX = clamp(centerX, rectLeft, rectRight);
        int closestY = clamp(centerY, rectTop, rectBottom);
        boolean insideEllipse = isInsideEllipse(centerX, centerY, radiusX, radiusY, closestX, closestY);

        if (insideEllipse) {
            return true;
        }

        // 检查矩形的四个角是否在椭圆内
        boolean topLeftInside = isInsideEllipse(centerX, centerY, radiusX, radiusY, rectLeft, rectTop);
        boolean topRightInside = isInsideEllipse(centerX, centerY, radiusX, radiusY, rectRight, rectTop);
        boolean bottomLeftInside = isInsideEllipse(centerX, centerY, radiusX, radiusY, rectLeft, rectBottom);
        boolean bottomRightInside = isInsideEllipse(centerX, centerY, radiusX, radiusY, rectRight, rectBottom);

        return topLeftInside || topRightInside || bottomLeftInside || bottomRightInside;
    }

    private boolean isInsideEllipse(int centerX, int centerY, int radiusX, int radiusY, int x, int y) {
        double value = Math.pow((x - centerX) / (double) radiusX, 2) + Math.pow((y - centerY) / (double) radiusY, 2);
        return value <= 1.0;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
