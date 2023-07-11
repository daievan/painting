# painting

## 引言

```
开发任务介绍:选择选项2-基础图形绘制软件使用设计模式实现
核心技术简介:采用java语言 通过数据结构的组合实现
```

## 对目标系统的分析和设计

### 设计模式

```
1.工厂模式
AbstractShape为图形对象的抽象类 在该类下有Triangle pencil Brush Rubber等具体图形对象实现
2.单例模式
将ColorPanel类的构造函数设为私有，并通过getInstance()方法返回单例对象，我们确保在整个应用程序中只有一个ColorPanel实例存在。在MyFrame类中，我们通过ColorPanel.getInstance()获取该单例对象并将其赋值给colorPanel成员变量。
3.原型模式
在向剪切板内添加数据时 利用复制现有对象方法
4.组合模式
在功能实现中有单选和复选功能 单选时设计了一个数据结构抽象图形类AbstractShape 而多选时构建了一个复合图形类CompositeShape 其包含一个AbstractShape的list
```

## 实现功能介绍

### 1.算法

```
1.绘图算法
对抽象图像类下的具体图像重写了draw();contain();overlap();
draw()即是最基本的绘图实现; contain() 用于实现鼠标是否"单选"中图形 overlap();用于实现鼠标构建的多选框是否与画板中的图形有重叠
下面是一个具体图像的例子(矩形)
@Override
    public void draw(Graphics2D Gra) {

        Gra.setColor(color);
        Gra.setStroke(new BasicStroke(width));
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2),
                Math.abs(y1 - y2));
    }
    @Override
    public boolean contains(int x, int y) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        int rectLeft = Math.min(rectX1, rectX2);
        int rectRight = Math.max(rectX1, rectX2);
        int rectTop = Math.min(rectY1, rectY2);
        int rectBottom = Math.max(rectY1, rectY2);

        // 判断矩形与矩形是否重叠
        return !(rectLeft > maxX || rectRight < minX || rectTop > maxY || rectBottom < minY);
    }
```

```
2.工具栏与画板交互
通过监听一个变量currentChoice的变化来设置绘制的具体图形
switch (currentChoice) {
                case 0:
                    itemList[index] = new Images();
                    break;
                case 3:
                    itemList[index] = new Pencil();
                    break;
                case 4:
                    itemList[index] = new Line();
                    break;
                case 5:
                    itemList[index] = new CustomRectangle();
                    break;
                case 6:
                    itemList[index] = new Oval();
                    break;
                case 7:
                    itemList[index] = new Circle();
                    break;
                case 8:
                    itemList[index] = new RoundRect();
                    break;
                case 9:
                    itemList[index] = new Triangle();
                    break;
                case 10:
                    itemList[index] = new Pentagon();
                    break;
                case 11:
                    itemList[index] = new Hexagon();
                    break;
                case 12:
                    itemList[index] = new Rubber();
                    break;
                case 13:
                    itemList[index] = new Brush();
                    break;
                case 14:
                    itemList[index] = new Text();
                    String input;
                    input = JOptionPane.showInputDialog("请输入文字");
                    itemList[index].s = input;
                    itemList[index].fontSize = fSize;
                    itemList[index].fontName = fontName;
                    itemList[index].italic = italic;
                    itemList[index].blodtype = blodtype;
                    break;
                case 15:
                    itemList[index] = new Select();
                    break;
                case 16:
                    itemList[index] = new CompositeShape();
                default:
            }
            itemList[index].color = color;
            itemList[index].width = stroke;

        }
```

```
3.鼠标事件
在监听鼠标事件中 有两个需要着重处理的操作-单选和复选
对于单选:鼠标点击(clicked)及按下(pressed)时 需要判断是否鼠标位置在一个图形内 若在 设置剪切版 用一个抽象图形类变量selectedShape来存储选择的图形 鼠标释放时 分两种情况 若按下左键(selectedShape不为空时)进行拖拽操作 若按下右键(剪贴板不为空)
进行复制粘贴操作
对于多选 大致情况同上 区别有二 一是将AbstractShape替换为CompositeShape(组合模式)
二是多选会先通过鼠标拖拽生成一个虚线矩形框 将与虚线矩形框有重叠的图形加入到compositeShape和CompClipboard中进行存储
对于单选 若鼠标按下位置不在任何矩形内部 会清空selectedShape和剪贴板
对于单选 若鼠标形成矩形框不与任何图形重叠 会清空compositeShape和剪贴板
下面是具体的实现代码:
class MouseAction extends MouseAdapter {
            private int startX;
            private int startY;
            private boolean isDragging;
            private boolean showSelection;
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("鼠标");
                // 如果当前选择的是"Select"（15），且鼠标点击位置包含在图形内，则选中该图形
                if (currentChoice == 15) {
                    for (int i = index - 1; i >= 0; i--) {
                        if (itemList[i].contains(e.getX(), e.getY())) {
                            selectedShape = itemList[i];
                            selx1=selectedShape.x1;
                            selx2=selectedShape.x2;
                            sely1=selectedShape.y1;
                            sely2=selectedShape.y2;
                            sselx1=selectedShape.x1;
                            sselx2=selectedShape.x2;
                            ssely1=selectedShape.y1;
                            ssely2=selectedShape.y2;
                            clipboard.clear();
                            clipboard.add(selectedShape.clone());
                            break;
                        }
                    }
                }
                else if (currentChoice == 16) {
                    if(compositeShape==null){
                        compositeShape = new CompositeShape();
                    }
                    else {
                        if(e.getX()<=Rectx2&&e.getX()>=Rectx1&&e.getY()<=Recty2&&e.getY()>=Recty1){
                            //System.out.println("已选中");
                            CompClipboard.clear();
                            CompClipboard.add(compositeShape.clone());
                            AbstractShape firstShape=compositeShape.getShapes().get(0);
                            selx1=firstShape.x1;
                            selx2=firstShape.x2;
                            sely1=firstShape.y1;
                            sely2=firstShape.y2;
                            sselx1=firstShape.x1;
                            sselx2=firstShape.x2;
                            ssely1=firstShape.y1;
                            ssely2=firstShape.y2;
                        }
                        else {
                            CompClipboard.clear();
                            compositeShape.clearShape();
                            overlapShape = false;
                            copyNum=0;
                            Rectx1=0;
                            Rectx2=0;
                            Recty1=0;
                            Recty2=0;
                        }
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                isDragging = false;
                selectedShape = null;
//                compositeShape = null;
                // 设置状态提示
                statusBar.setText("坐标:[" + e.getX() + "," + e.getY() + "]像素");
                itemList[index].x1 = itemList[index].x2 = e.getX();
                itemList[index].y1 = itemList[index].y2 = e.getY();
                // 如果当前选择的图形是画笔或者橡皮檫，则进行下面的操作

                if (currentChoice == 3 || currentChoice == 12 || currentChoice == 13) {
                    lengthCount = 0;
                    itemList[index].x1 = itemList[index].x2 = e.getX();
                    itemList[index].y1 = itemList[index].y2 = e.getY();
                    index++;
                    lengthCount++;
                    createNewGraphics();
                } else if (currentChoice == 15) {
                    boolean containsShape = false;
                    for (int i = index - 1; i >= 0; i--) {
                        if (itemList[i].contains(startX, startY)) {
                            selectedShape = itemList[i];
                            selx1=selectedShape.x1;
                            selx2=selectedShape.x2;
                            sely1=selectedShape.y1;
                            sely2=selectedShape.y2;
                            sselx1=selectedShape.x1;
                            sselx2=selectedShape.x2;
                            ssely1=selectedShape.y1;
                            ssely2=selectedShape.y2;
                            containsShape = true;
                            break;
                        }
                    }
                    if (!containsShape) {
                        selectedShape = null;
                    }
                }
                else if (currentChoice == 16) {

                    if(compositeShape==null){
                        System.out.println("新建");
                        compositeShape = new CompositeShape();
                    }
                    else {
                        if(startX<=Rectx2&&startX>=Rectx1&&startY<=Recty2&&startY>=Recty1){
                            //System.out.println("已按下");
                            AbstractShape firstShape=compositeShape.getShapes().get(0);
                            selx1=firstShape.x1;
                            selx2=firstShape.x2;
                            sely1=firstShape.y1;
                            sely2=firstShape.y2;
                            sselx1=firstShape.x1;
                            sselx2=firstShape.x2;
                            ssely1=firstShape.y1;
                            ssely2=firstShape.y2;
                        }
                        else {
                            CompClipboard.clear();
                            //System.out.println("清空啦");
                            compositeShape.clearShape();
                            overlapShape = false;
                            copyNum=0;
                            Rectx1=0;
                            Rectx2=0;
                            Recty1=0;
                            Recty2=0;
                        }
                    }
                }
            }




@Override
public void mouseReleased(MouseEvent e) {
    statusBar.setText("坐标:[" + e.getX() + "," + e.getY() + "]像素");
    int endX = e.getX();
    int endY = e.getY();
    itemList[index].x2 = e.getX();
    itemList[index].y2 = e.getY();
    if (currentChoice == 16 ){
        if(!overlapShape){
            if(itemList[index].x1!=itemList[index].x2&&itemList[index].y1!=itemList[index].y2){
                Rectx1=itemList[index].x1;
                Rectx2=itemList[index].x2;
                Recty1=itemList[index].y1;
                Recty2=itemList[index].y2;
                for (int i = index - 1; i >= 0; i--) {
                    if (itemList[i].overlap(startX, startY,endX,endY)) {
                        compositeShape.addShape(itemList[i]);
                        System.out.println(i);
                        overlapShape = true;
                    }
                }
                if(overlapShape){
                    CompClipboard.clear();
                    CompClipboard.add(compositeShape.clone());
                }

            }
        }
        else {
            if(!e.isPopupTrigger()) {
                int dx=endX - startX;
                int dy=endY - startY;
                compositeShape.move(dx, dy);
                Rectx1+=dx;
                Rectx2+=dx;
                Recty1+=dy;
                Recty2+=dy;
                AbstractShape firstShape=compositeShape.getShapes().get(0);
                sselx1=firstShape.x1;
                sselx2=firstShape.x2;
                ssely1=firstShape.y1;
                ssely2=firstShape.y2;
                if (!CompClipboard.isEmpty()) {
                    CompClipboard.get(0).move(endX - startX, endY - startY);
                }
            }
            if (e.isPopupTrigger()&& !CompClipboard.isEmpty()) {
//              System.out.println(clipboard.get(0).x1);
//              System.out.println(clipboard.get(0).x2);
//              System.out.println(startX);
                CompositeShape clonedShape = CompClipboard.get(0).clone();
                int deltaX = 20; // 新图形在x方向的偏移量
                int deltaY = 20; // 新图形在y方向的偏移量
                clonedShape.move(deltaX, deltaY);
                //compositeShape.addShapesToItemList(itemList,index);
                //itemList[index] = clonedShape;
                int startIndex=index;
                for (AbstractShape shape : clonedShape.getShapes()) {
                    itemList[index] = shape;
                    index++;
                }
                copyNum=index-startIndex;
                //index++;
                repaint();
            }
        }

    }
    else if (currentChoice == 15 ) {
        if(!e.isPopupTrigger()&&selectedShape != null) {
            selectedShape.move(endX - startX, endY - startY);
            sselx1=selectedShape.x1;
            sselx2=selectedShape.x2;
            ssely1=selectedShape.y1;
            ssely2=selectedShape.y2;
            if (!clipboard.isEmpty()) {
                clipboard.get(0).move(endX - startX, endY - startY);
            }
        }
          if (e.isPopupTrigger()&& !clipboard.isEmpty()) {
            AbstractShape clonedShape = clipboard.get(0).clone();
            int deltaX = 20; // 新图形在x方向的偏移量
            int deltaY = 20; // 新图形在y方向的偏移量
            clonedShape.translate(deltaX, deltaY);
            itemList[index] = clonedShape;
            index++;
            //repaint();
        }

    }
    else if (currentChoice == 3 || currentChoice == 12 || currentChoice == 13) {
                    itemList[index].x1 = e.getX();
                    itemList[index].y1 = e.getY();
                    lengthCount++;
                    itemList[index].length = lengthCount;

        itemList[index].x2 = e.getX();
        itemList[index].y2 = e.getY();
        index++;
        }
    else {
        index++;
    }
    repaint();
    createNewGraphics();
}
            @Override
            public void mouseDragged(MouseEvent e) {
                statusBar.setText("坐标:[" + e.getX() + "," + e.getY() + "]像素");

                if (currentChoice == 16 && !overlapShape) {
                    int dx = e.getX() - startX;
                    int dy = e.getY() - startY;
                    compositeShape.move(dx, dy);
                    Rectx1+=dx;
                    Rectx2+=dx;
                    Recty1+=dy;
                    Recty2+=dy;
                    startX = e.getX();
                    startY = e.getY();
                    isDragging = true;
                    repaint();
                }
                else if (currentChoice == 15 && selectedShape != null) {
                    int dx = e.getX() - startX;
                    int dy = e.getY() - startY;
                    selectedShape.move(dx, dy);
                    startX = e.getX();
                    startY = e.getY();
                    isDragging = true;
                    repaint();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                statusBar.setText("坐标:[" + e.getX() + "," + e.getY() + "]像素");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                statusBar.setText("坐标：");
            }
        }
```

```
4.撤销操作(仅实现单步)
由于本画板的实现主要基于itemList[]
撤销的基本思路是将index-- 删除图形后repaint即可
对于单选和多选需要进行特别操作
单选:拖拽操作 记录始末位置 撤销时移动上次的取反即可
复制:本质还是构建新图形 删去即可
多选:拖拽:同上
复制:需记录复制后加入到itemList的图形个数index1 撤销时index-=index1即可
具体代码：
 void undo() {
            index--;
            if (index >= 0) {
                if (currentChoice == 3 || currentChoice == 12 || currentChoice == 13) {
                    index -= itemList[index].length;
                }
                else if(currentChoice == 15&&selectedShape!=null&&(sselx1-selx1)!=0&&(ssely1-sely1)!=0){
                    selectedShape.move(selx1-sselx1,sely1-ssely1);
                }
                else if(currentChoice == 16){
                    if(compositeShape!=null&&(sselx1-selx1)!=0&&(ssely1-sely1)!=0){
                        compositeShape.move(selx1-sselx1,sely1-ssely1);
                    }
                    else if(copyNum!=0){
                        index-=copyNum;
                        copyNum=0;
                    }

                }
                else {
                    index--;
                }
                drawingArea.repaint();

            }
            index++;
            drawingArea.createNewGraphics();
        }
```

###  2.数据结构

```
1.抽象图形和复合图形(实现了组合模式)
抽象产品类
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
具体产品类:
package com.daipanzhu.shape;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeShape extends AbstractShape {
    private List<AbstractShape> shapes;
    private static final float[] DASH_PATTERN = { 5f, 5f };
    public CompositeShape() {
        shapes = new ArrayList<>();
    }

    public void addShape(AbstractShape shape) {
        shapes.add(shape);
    }
    public List<AbstractShape> getShapes() {
        return shapes;
    }

    public void clearShape() {
        shapes.clear();
    }

    @Override
    public void draw(Graphics2D Gra) {
        Gra.setColor(color);
        Gra.setStroke(createDashedStroke());
        Gra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Gra.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    private Stroke createDashedStroke() {
        return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, DASH_PATTERN, 0f);
    }
    @Override
    public boolean contains(int x, int y) {
        for (AbstractShape shape : shapes) {
            if (shape.contains(x, y)) {
                return true;
            }
        }
        return false;
    }
    public void setBounds(int x, int y, int width, int height) {
        x1 = x;
        y1 = y;
        x2 = x + width;
        y2 = y + height;
    }
    public boolean intersects(AbstractShape shape) {
        return (x1 < shape.x2 && x2 > shape.x1 && y1 < shape.y2 && y2 > shape.y1);
    }
    public boolean overlap(int rectX1, int rectY1, int rectX2, int rectY2) {
        return false;
    }
    @Override
    public CompositeShape clone() {
        CompositeShape clonedShape = (CompositeShape) super.clone();
        clonedShape.shapes = new ArrayList<>(shapes.size());
        for (AbstractShape shape : shapes) {
            clonedShape.addShape(shape.clone());
        }
        return clonedShape;
    }
    @Override
    public void move(int dx, int dy) {
        for (AbstractShape shape : shapes) {
            shape.x1 += dx;
            shape.y1 += dy;
            shape.x2 += dx;
            shape.y2 += dy;
        }
    }
}

```

```
2.剪贴板
   private List<AbstractShape> clipboard; // 剪贴板，用于存储复制的图形信息
   private List<CompositeShape> CompClipboard;
3.图形存储单元
    public static AbstractShape[] itemList = new AbstractShape[5000];
```

### 3.设计模式具体实现

```
1.工厂模式
AbstractShape为图形对象的抽象类 在该类下有Triangle pencil Brush Rubber等具体图形对象实现
如:Triangle对象
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
2.单例模式
将ColorPanel类的构造函数设为私有，并通过getInstance()方法返回单例对象，我们确保在整个应用程序中只有一个ColorPanel实例存在。在MyFrame类中，我们通过ColorPanel.getInstance()获取该单例对象并将其赋值给colorPanel成员变量。
相关代码:
public class ColorPanel extends JPanel {
    private static ColorPanel instance;

    private ColorPanel() {
        setBackground(Color.GRAY);
    }

    public static ColorPanel getInstance() {
        if (instance == null) {
            instance = new ColorPanel();
        }
        return instance;
    }
    ...
}
public class MyFrame extends JFrame {
    private ColorPanel colorPanel = ColorPanel.getInstance();

    ...
}
3.原型模式
在向剪切板内添加数据时 利用复制现有对象方法
相关代码:
if (e.isPopupTrigger()&& !clipboard.isEmpty()) {
            AbstractShape clonedShape = clipboard.get(0).clone();
            int deltaX = 20; // 新图形在x方向的偏移量
            int deltaY = 20; // 新图形在y方向的偏移量
            clonedShape.translate(deltaX, deltaY);
            itemList[index] = clonedShape;
            index++;
     }
 AbstractShape类中:
  @Override
    public AbstractShape clone() {
        try {
            return (AbstractShape) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle clone failure
            return null;
        }
    }
4.组合模式--见数据结构-1
```

## 实现功能及界面展示

```
基本功能
1. 良好的图形用户界面，界面中有默认大小的三角形、方框、圆形、椭圆、连接线等元素可供用户选择后，绘制到画布上；
2. 允许用户添加文字描述；
3. 单击可以选中图形，并允许对图形的拷贝复制；
4. 多个图形可以组合，组合后的图形同样有拷贝复制的功能；
5. 支持撤销上一步操作的功能。
拓展功能:
1. 添加刷子 橡皮 多边形 圆角矩形 铅笔 刷新操作；
2. 支持个性化颜色 并在设置-粗细中可设置粗细 设置-颜色有详细的调色板；
3. 支持图形（包括组合图形）的拖拽调整图形位置；
4. 支持输入文字的字体 大小 粗体 斜体等；
5. 设计了一种硬盘文件存储格式可以保存用户绘制的图形，并可以加载(文件-打开 文件-保存)。
```

## 小结

```
展望:
还有一些功能有待完善：
1.多步撤销
2.可新增一些图形:如星形 聊天框 箭头等
```

```
说明:
启动文件:start下的StartProject
```

