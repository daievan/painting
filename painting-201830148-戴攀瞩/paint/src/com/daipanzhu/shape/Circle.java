package com.daipanzhu.shape;

import java.awt.*;

public class Circle extends AbstractShape {


	@Override
	public void draw(Graphics2D Gra) {
		Gra.setPaint(color);
		Gra.setStroke(new BasicStroke(width));
		Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Gra.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
				Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
	}
	@Override
	public boolean contains(int x, int y) {
		// 计算圆心坐标和半径
		int centerX = (x1 + x2) / 2;
		int centerY = (y1 + y2) / 2;
		int radius = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) / 2;

		// 计算点(x, y)与圆心的距离
		double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

		// 如果点与圆心的距离小于等于半径，表示点在圆形内部
		return distance <= radius;
	}
	public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
		// 计算圆形的圆心坐标和半径
		int centerX = (x1 + x2) / 2;
		int centerY = (y1 + y2) / 2;
		int radius = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) / 2;

		// 计算矩形的左上角和右下角坐标
		int rectLeft = Math.min(rectX1, rectX2);
		int rectRight = Math.max(rectX1, rectX2);
		int rectTop = Math.min(rectY1, rectY2);
		int rectBottom = Math.max(rectY1, rectY2);

		// 判断圆形与矩形是否重叠
		int closestX = clamp(centerX, rectLeft, rectRight);
		int closestY = clamp(centerY, rectTop, rectBottom);
		int distanceX = centerX - closestX;
		int distanceY = centerY - closestY;
		int distanceSquared = distanceX * distanceX + distanceY * distanceY;

		return distanceSquared <= radius * radius;
	}

	private int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

}