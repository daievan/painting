package com.daipanzhu.shape;

import java.awt.*;

public class Rubber extends AbstractShape {
	public Rubber(){
		
	}
	@Override
	public void draw(Graphics2D g) {
		g.setPaint(Color.white);
		g.setStroke(new BasicStroke(20, BasicStroke.CAP_SQUARE , BasicStroke.JOIN_BEVEL));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawLine(x1, y1, x2, y2);
	}
	@Override
	public boolean contains(int x, int y) {
		// Rubber类是一个自由绘制的橡皮擦，不进行包含判断
		// 因此始终返回false
		return false;
	}

	public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
		return false;
	}
}