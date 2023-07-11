package com.daipanzhu.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.JPanel;


public abstract class AbstractShape implements Serializable, Cloneable {
    public int x1, y1, x2, y2;
    public Color color;
    public int width;
    public int currentChoice;
    public int length;
    public BufferedImage image;
    public JPanel board;
    public int  fontSize;
    public String fontName;
    public String s;
    public int blodtype;
    public int italic;


    public abstract void draw(Graphics2D g);
    public abstract boolean contains(int x, int y);
    public abstract boolean overlap(int x1,int x2,int y1,int y2);
    public void move(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
    @Override
    public AbstractShape clone() {
        try {
            return (AbstractShape) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle clone failure
            return null;
        }
    }
    public void translate(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }

}