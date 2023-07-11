package com.daipanzhu.shape;

import java.awt.*;

public class Images extends AbstractShape {


    @Override
    public void draw(Graphics2D g) {
        g.drawImage(image, 0, 0, board);
    }

    @Override
    public boolean contains(int x, int y) {
        int imageX = Math.min(x1, x2);  // 图像左上角的 x 坐标
        int imageY = Math.min(y1, y2);  // 图像左上角的 y 坐标
        int imageWidth = Math.abs(x1 - x2);  // 图像的宽度
        int imageHeight = Math.abs(y1 - y2);  // 图像的高度

        // 检查点 (x, y) 是否在图像的矩形范围内
        if (x >= imageX && x <= imageX + imageWidth && y >= imageY && y <= imageY + imageHeight) {
            // 在矩形范围内，判断点是否在图像的实际区域内
            int relativeX = x - imageX;
            int relativeY = y - imageY;

            // 获取图像像素颜色值
            Color pixelColor = new Color(image.getRGB(relativeX, relativeY), true);

            // 判断像素颜色是否透明，如果透明则认为点不在图像内部
            return pixelColor.getAlpha() != 0;
        }

        return false;
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        return false;
    }
}
