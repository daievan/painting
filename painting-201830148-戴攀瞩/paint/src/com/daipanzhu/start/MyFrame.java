package com.daipanzhu.start;

import com.daipanzhu.shape.CustomRectangle;
import com.daipanzhu.shape.AbstractShape;
import com.daipanzhu.shape.CompositeShape;
import com.daipanzhu.shape.Select;
import com.daipanzhu.shape.*;
import com.daipanzhu.tools.ColorPanel;
import java.util.ArrayList;
import com.daipanzhu.shape.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    /**
     * 保存文件的标志
     */
    private static int saved = 0;

    /**
     * 铅笔或橡皮擦图形的存储长度
     */
    private int lengthCount;
    private static String fontName = " 宋体 ";
    private static int fSize = 16;
    /**
     * 粗体,默认正常
     */
    private static int blodtype = Font.PLAIN;
    /**
     * 斜体
     */
    private static int italic = Font.PLAIN;

    /**
     * 图形形状的标记
     */
    public static int index = 0;
    /**
     * 图形存储单元
     */
    public static AbstractShape[] itemList = new AbstractShape[5000];

    /**
     * 画图区域
     */
    private DrawPanel drawingArea;
    /**
     * 鼠标状态
     */
    private JLabel statusBar;
    /**
     * 画笔粗细
     */
    private static int stroke = 1;
    /**
     * 用于存放当前颜色
     */
    public static Color color = Color.black;
    /**
     * 初始状态是画笔
     */
    private static int currentChoice = 3;
    /**
     * 新增选择图形类
     */
    private AbstractShape selectedShape = null;
    private int selx1=0;
    private int selx2=0;
    private int sely1=0;
    private int sely2=0;
    private int sselx1=0;
    private int sselx2=0;
    private int ssely1=0;
    private int ssely2=0;
    private int Rectx1=0;
    private int Rectx2=0;
    private int Recty1=0;
    private int Recty2=0;

    private int copyNum=0;
    private CompositeShape compositeShape = null;

    private List<AbstractShape> clipboard; // 剪贴板，用于存储复制的图形信息
    private List<CompositeShape> CompClipboard;
    /**
     * 菜单类
     */
    private MyMenu menu;

    /**
     * 工具条
     */
    private MyToolbar myToolbar;

    /**
     * 调色板
     */
    private ColorPanel colorPanel;

    MyFrame(String s) {
        init(s);
        setVisible(true);

    }

    public MyFrame() {
    }

    private void init(String s) {
        // 设置标题
        this.setTitle(s);
        // 设置窗口大小
        this.setSize(1800, 1200);
        // 居中显示
        this.setLocationRelativeTo(null);

        // 添加菜单
        menu = new MyMenu();

        myToolbar = new MyToolbar();

        colorPanel = new ColorPanel();
        add(colorPanel, BorderLayout.WEST);

        // 设置窗体图标
        try {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/themeicon.png"));
            Image image = imageIcon.getImage();
            this.setIconImage(image);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "图标异常");
        }
        // 创建各种基本图形的按钮
        drawingArea = new DrawPanel();
        this.add(drawingArea, BorderLayout.CENTER);
        statusBar = new JLabel();
        this.add(statusBar, BorderLayout.SOUTH);
        statusBar.setText("坐标");

        /*
         * 由于JLable是透明的，当我们把JLabel控件加载到JPanel控件之上时， 会发现JLabel的背景色总是和JPanel的背景色保持一致,
         */
        // 设置该组件为透明
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(195, 195, 195));
        drawingArea.createNewGraphics();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (saved == 0) {
                    int n = JOptionPane.showConfirmDialog(null, "您还没保存，确定要退出？", "提示", JOptionPane.OK_CANCEL_OPTION);
                    if (n == 0) {
                        System.exit(0);
                    }
                }
                if (saved == 1) {
                    System.exit(0);
                }
            }
        });

    }

    /**
     * 画图面板类，用来画图
     */
    class DrawPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private boolean overlapShape = false;
        DrawPanel() {
            clipboard = new ArrayList<>();
            CompClipboard = new ArrayList<>();
            // 设置光标类型，为十字形
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            //设置背景颜色
            this.setBackground(Color.white);
            //设置鼠标监听
            this.addMouseListener(new MouseAction());
            this.addMouseMotionListener(new MouseMotion());
        }

        //重写paintComponent方法，使得画板每次刷新时可将之前的所有图形重新画出来。
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D Gra = (Graphics2D) g; // 定义画板
            int j = 0;

            while (j <= index) {
                draw(Gra, itemList[j]);
                j++;

            }
        }

        void draw(Graphics2D Gra, AbstractShape abstractShape) {
            // 将画笔传入到各个子类中，用来完成各自的绘图
            abstractShape.draw(Gra);
        }

        // 撤销操作的实现
        void undo() {
            index--;
            if (index >= 0) {
                if (currentChoice == 3 || currentChoice == 12 || currentChoice == 13) {
                    index -= itemList[index].length;
                }
                else if(currentChoice == 15&&selectedShape!=null&&(sselx1-selx1)!=0&&(ssely1-sely1)!=0){
//                    System.out.println(selx1);
//                    System.out.println(sselx1);
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

        /**
         * 新建一个画图基本单元对象的程序段
         */
        void createNewGraphics() {
            /*
             * MOVE_CURSOR:移动光标类型。 CROSSHAIR_CURSOR:十字光标 CUSTOM_CURSOR 制定类型 WAIT_CURSOR
             * 等待光标类型
             */
            if (currentChoice == 12) {
                try {
                    // 定义鼠标进入画板时的样式
                    String url = "/image/cursor.png"; // 储存鼠标图片的位置
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Image image = new ImageIcon(getClass().getResource(url)).getImage();
                    Cursor cursor = tk.createCustomCursor(image, new Point(10, 10), "norm");
                    drawingArea.setCursor(cursor);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "自定义光标异常");
                }

            } else if (currentChoice == 14) {
                drawingArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            } else {
                // 光标设置
                drawingArea.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }

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
        // 鼠标事件mouseAction类，继承了MouseAdapter，用来完成鼠标相应事件操作
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

        /**
         * 鼠标事件mouseMotion类继承了MouseMotionAdapter,用来完成鼠标拖动和鼠标移动时的响应操作
         */
        class MouseMotion extends MouseMotionAdapter {
            @Override
            public void mouseDragged(MouseEvent e) {
                statusBar.setText("坐标:[" + e.getX() + "," + e.getY() + "]像素");


                if (currentChoice == 3 || currentChoice == 12 || currentChoice == 13) {
                    itemList[index - 1].x1 = itemList[index].x2 = itemList[index].x1 = e.getX();
                    itemList[index - 1].y1 = itemList[index].y2 = itemList[index].y1 = e.getY();
                    index++;
                    lengthCount++;
                    createNewGraphics();
                    repaint();
                }
                //else if (currentChoice != 15 && currentChoice != 16){
                else if (currentChoice != 15){
                    itemList[index].x2 = e.getX();
                    itemList[index].y2 = e.getY();
                    if(currentChoice ==16){
//                        System.out.println("真的吗？");
                        if(overlapShape == false){
                            repaint();
                        }
                    }
                    else repaint();

                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                statusBar.setText("坐标:[" + e.getX() + "," + e.getY() + "]像素");
            }
        }

    }


    /**
     * 菜单初始化部分
     */
    class MyMenu {


        private String[] strokes = {"/image/stroke1.png", "/image/stroke2.png", "/image/stroke3.png",
                "/image/stroke4.png"};

        MyMenu() {
            addMenu();
        }

        void addMenu() {

            // 菜单条
            JMenuBar jMenuBar = new JMenuBar();
            JMenuItem[] strokeItems = new JMenuItem[strokes.length];
            // 实例化菜单对象
            // 定义文件、设置、帮助菜单
            JMenu fileMenu = new JMenu("文件");
            JMenu setMenu = new JMenu("设置");
            JMenu helpMenu = new JMenu("帮助");
            JMenu strokeMenu = new JMenu("粗细");
            // 实例化菜单项,并通过ImageIcon对象添加图片 定义文件菜单的菜单项
            JMenuItem fileItemNew = new JMenuItem("新建", new ImageIcon(getClass().getResource("/image/new.png")));
            JMenuItem fileItemOpen = new JMenuItem("打开", new ImageIcon(getClass().getResource("/image/open.png")));
            JMenuItem fileItemSave = new JMenuItem("保存", new ImageIcon(getClass().getResource("/image/save.png")));
            JMenuItem fileItemExit = new JMenuItem("退出", new ImageIcon(getClass().getResource("/image/exit.png")));
            // 定设置菜单的菜单项
            JMenuItem setItemColor = new JMenuItem("颜色", new ImageIcon(getClass().getResource("/image/color.png")));
            JMenuItem setItemUndo = new JMenuItem("撤销", new ImageIcon(getClass().getResource("/image/undo.png")));
            JMenuItem helpItemUse = new JMenuItem("使用手册");
            JMenuItem helpItemInfo = new JMenuItem("关于画图");
            for (int i = 0; i < 4; i++) {
                strokeItems[i] = new JMenuItem("", new ImageIcon(getClass().getResource(strokes[i])));
                strokeMenu.add(strokeItems[i]);
            }
            helpItemInfo.addActionListener(e -> JOptionPane.showMessageDialog(null,
                    "" + "关于画图\n" + "****该软件由戴攀瞩开发****\n"
                            + "****学号：201830148  *****\n" ,
                    "关于画图", JOptionPane.PLAIN_MESSAGE));
            helpItemUse.addActionListener(e -> JOptionPane.showMessageDialog(null, "" + "##################\r\n" + "#画图软件使用说明书#\r\n"
                    + "####################\r\n" + "1.本软件可以实现以下功能：\r\n" + "（1）在画布上绘制直线、矩形、椭圆等图形，橡皮擦\r\n"
                    + "（2）支持个性化颜色 并在设置-粗细中可设置粗细 设置-颜色有详细的调色板；\r\n" +  "（3）支持输入文字的字体 大小 粗体 斜体等；\r\n" + "（4）设计了一种硬盘文件存储格式可以保存用户绘制的图形，并可以加载(文件-打开 文件-保存)。\r\n"
                            + "（5）支持图形（包括组合图形）的拖拽调整图形位置；\r\n"+ "（6）支持图形（包括组合图形）的复制\r\n"+ "（6）支持刷新画布\r\n"+ "（6）支持撤销一步的功能\r\n"
                    , "使用说明", JOptionPane.PLAIN_MESSAGE));
            helpMenu.add(helpItemUse);
            helpMenu.add(helpItemInfo);
            // 设置快捷键
            fileItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
            fileItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
            fileItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
            fileItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
            setItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
            // 添加粗细子菜单

            // 添加菜单项到菜单
            fileMenu.add(fileItemNew);
            fileMenu.add(fileItemOpen);
            fileMenu.add(fileItemSave);
            fileMenu.add(fileItemExit);
            setMenu.add(setItemColor);
            setMenu.add(setItemUndo);
            setMenu.add(strokeMenu);

            // 添加菜单到菜单条
            jMenuBar.add(fileMenu);
            jMenuBar.add(setMenu);
            jMenuBar.add(helpMenu);
            // 添加菜单条
            setJMenuBar(jMenuBar);

            // 给文件菜单设置监听
            fileItemNew.addActionListener(e -> menu.newFile());
            fileItemSave.addActionListener(e -> {
                // 保存文件，并将标志符saved设置为1
                menu.saveFile();
                saved = 1;
            });
            fileItemOpen.addActionListener(e -> {
                // 打开文件，并将标志符saved设置为0
                menu.openFile();
                saved = 0;
            });
            fileItemExit.addActionListener(e -> {
                // 如果文件已经保存就直接退出，若果文件没有保存，提示用户选择是否退出

                if (saved == 1) {
                    System.exit(0);
                } else {
                    int n = JOptionPane.showConfirmDialog(null, "您还没保存，确定要退出？", "提示", JOptionPane.OK_CANCEL_OPTION);
                    if (n == 0) {
                        System.exit(0);
                    }
                }
            });
            // 给设置菜单添加监听
            setItemColor.addActionListener(e -> {
                // 设置粗细
                ColorPanel.chooseColor();

            });

            setItemUndo.addActionListener(e -> {
                // 撤销
                drawingArea.undo();

            });
            strokeItems[0].addActionListener(e -> {
                stroke = 1;
                itemList[index].width = stroke;

            });
            strokeItems[1].addActionListener(e -> {
                stroke = 5;
                itemList[index].width = stroke;

            });
            strokeItems[2].addActionListener(e -> {
                stroke = 15;
                itemList[index].width = stroke;

            });
            strokeItems[3].addActionListener(e -> {
                stroke = 25;
                itemList[index].width = stroke;

            });

        }

        // 保存图形文件
        void saveFile() {
            // 文件选择器
            JFileChooser fileChooser = getjFileChooser();
            // 弹出一个 "Save File" 文件选择器对话框
            int result = fileChooser.showSaveDialog(MyFrame.this);
            if (result == JFileChooser.CANCEL_OPTION) {
                return;
            }
            File fileName = fileChooser.getSelectedFile();

            if (!fileName.getName().endsWith(fileChooser.getFileFilter().getDescription())) {
                String t = fileName.getPath() + fileChooser.getFileFilter().getDescription();
                fileName = new File(t);
            }
            fileName.canWrite();
            if ("".equals(fileName.getName())) {
                JOptionPane.showMessageDialog(fileChooser, "无效的文件名", "无效的文件名", JOptionPane.ERROR_MESSAGE);
            }

            BufferedImage image = createImage(drawingArea);
            try {
                ImageIO.write(image, "png", fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 打开文件
        void openFile() {
            JFileChooser fileChooser = getjFileChooser();
            // 弹出一个 "Open File" 文件选择器对话框
            int result = fileChooser.showOpenDialog(MyFrame.this);
            if (result == JFileChooser.CANCEL_OPTION) {
                return;
            }
            // 得到选择文件的名字
            File fileName = fileChooser.getSelectedFile();
            if (!fileName.getName().endsWith(fileChooser.getFileFilter().getDescription())) {
                JOptionPane.showMessageDialog(MyFrame.this, "文件格式错误！");
                return;
            }
            fileName.canRead();

            if ("".equals(fileName.getName())) {
                JOptionPane.showMessageDialog(fileChooser, "无效的文件名", "无效的文件名", JOptionPane.ERROR_MESSAGE);
            }

            BufferedImage image;

            try {
                index = 0;
                currentChoice = 0;
                image = ImageIO.read(fileName);
                drawingArea.createNewGraphics();
                itemList[index].image = image;
                itemList[index].board = drawingArea;
                drawingArea.repaint();
                index++;
                currentChoice = 3;
                drawingArea.createNewGraphics();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }

        private JFileChooser getjFileChooser() {
            // 文件选择器
            JFileChooser fileChooser = new JFileChooser();
            // 设置文件显示类型为仅显示文件
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // 文件过滤器
            JpgFilter jpg = new JpgFilter();
            BmpFilter bmp = new BmpFilter();
            PngFilter png = new PngFilter();
            GifFilter gif = new GifFilter();
            // 向用户可选择的文件过滤器列表添加一个过滤器。
            fileChooser.addChoosableFileFilter(jpg);
            fileChooser.addChoosableFileFilter(bmp);
            fileChooser.addChoosableFileFilter(png);
            fileChooser.addChoosableFileFilter(gif);
            // 返回当前的文本过滤器，并设置成当前的选择
            fileChooser.setFileFilter(fileChooser.getFileFilter());
            return fileChooser;
        }

        //新建文件
        void newFile() {
            index = 0;
            currentChoice = 3;
            color = Color.black;
            stroke = 1;
            drawingArea.createNewGraphics();
            repaint();
        }

        // 创建image，由saveFile方法调用
        // 将画板内容画到panelImage上
        BufferedImage createImage(DrawPanel panel) {

            int width = MyFrame.this.getWidth();
            int height = MyFrame.this.getHeight();
            BufferedImage panelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = panelImage.createGraphics();

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.translate(0, 0);
            panel.paint(graphics);
            graphics.dispose();
            return panelImage;
        }

        // 文件过滤
        class JpgFilter extends FileFilter {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith(".jpg");
            }

            @Override
            public String getDescription() {
                return ".jpg";
            }

        }

        class BmpFilter extends FileFilter {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith(".bmp");
            }

            @Override
            public String getDescription() {
                return ".bmp";
            }

        }

        class GifFilter extends FileFilter {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith(".gif");
            }

            @Override
            public String getDescription() {
                return ".gif";
            }

        }

        class PngFilter extends FileFilter {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return f.getName().endsWith(".png");
            }

            @Override
            public String getDescription() {
                return ".png";
            }

        }

    }

    /**
     * 工具栏初始化部分
     */
    class MyToolbar {
        /**
         * 定义各种绘图的按钮
         */
        private JButton[] btnPaint;
        private JComboBox<String> jFont;
        private JComboBox<String> jFontSize;
        /**
         * 将图片资源的相对路径存放于数组中，方便使用
         */
//        private String[] images = {"/image/save.png", "/image/refresh.png", "/image/undo.png", "/image/pencil.png",
//                "/image/line.png", "/image/rectangle.png", "/image/rectangle3.png", "/image/oval.png",
//                "/image/oval2.png", "/image/circle.png", "/image/fillcircle.png", "/image/rectangle2.png",
//                "/image/rectangle4.png", "/image/triangle.png", "/image/pentagon.png", "/image/hexagon.png",
//                "/image/eraser.png", "/image/brush.png", "/image/font.png",};
//        private String[] tipText = {"保存", "清空", "撤销", "铅笔", "直线", "空心矩形", "填充矩形", "空心椭圆", "填充椭圆", "空心圆形", "填充圆形",
//                "空心圆角矩形", "填充圆角矩形", "三角形", "五边形", "六边形", "橡皮擦", "填充", "文本", "粗细"};

        private String[] images = {"/image/save.png", "/image/refresh.png", "/image/undo.png", "/image/pencil.png",
                "/image/line.png", "/image/rectangle.png", "/image/oval.png",
                 "/image/circle.png",  "/image/rectangle2.png",
                 "/image/triangle.png", "/image/pentagon.png", "/image/hexagon.png",
                "/image/eraser.png", "/image/brush.png", "/image/font.png", "/image/select1.png", "/image/select2.png",};
        private String[] tipText = {"保存", "清空", "撤销", "铅笔", "直线", "矩形", "椭圆", "圆形",
                "圆角矩形",  "三角形", "五边形", "六边形", "橡皮擦", "填充", "文本","选择","框选" ,"粗细"};
        private String[] font = {"宋体", "隶书", "华文彩云", "仿宋_GB2312", "华文行楷", "方正舒体"};
        private String[] fontSize = {"8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36",
                "48", "72"};

        MyToolbar() {
            addToorbar();
        }

        void addToorbar() {
            btnPaint = new JButton[images.length];// 定义指定个数的按钮
            // 定义按钮面板// 实例化一个水平的工具标签
            JToolBar toolbar = new JToolBar("工具栏");


            toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
            toolbar.setBackground(new Color(195, 195, 195));
            //中文会乱码
            // 粗体
            Checkbox btnBold = new Checkbox("bold");
            // 斜体
            Checkbox btnItalic = new Checkbox("italic");
            btnBold.setBackground(new Color(195, 195, 195));
            btnItalic.setBackground(new Color(195, 195, 195));
            btnBold.setPreferredSize(new Dimension(45, 30));
            btnItalic.setPreferredSize(new Dimension(45, 60));

            jFontSize = new JComboBox<>(fontSize);
            jFontSize.setPreferredSize(new Dimension(50, 30));
            jFont = new JComboBox<>(font);
            jFont.setPreferredSize(new Dimension(100, 30));
            //存放按钮的图片
            ImageIcon[] icon = new ImageIcon[images.length];

            // 设置按钮图标以及图片
            for (int i = 0; i < images.length; i++) {

                // System.out.println(images[i]);//测试
                btnPaint[i] = new JButton();
                icon[i] = new ImageIcon(getClass().getResource(images[i]));
                btnPaint[i].setIcon(icon[i]);
                btnPaint[i].setToolTipText(tipText[i]);
                // 设置图标大小
                btnPaint[i].setPreferredSize(new Dimension(24, 24));
                btnPaint[i].setBackground(Color.WHITE);
                toolbar.add(btnPaint[i]);

            }
            // 可以拖动
            toolbar.setFloatable(true);
            // toolbar.addSeparator();
            // 字体格式：斜体，粗体
            toolbar.add(btnItalic);
            toolbar.add(btnBold);

            // 将动作侦听器加入到按钮里面
            for (int i = 2; i < images.length; i++) {
                btnPaint[i].addActionListener(e -> {

                    for (int j = 0; j < images.length; j++) {
                        // 如果按钮被点击。则设置相应的画笔
                        if (e.getSource() == btnPaint[j]) {
                            currentChoice = j;
                            drawingArea.createNewGraphics();
                            repaint();
                        }
                    }

                });
            }

            btnPaint[0].addActionListener(e -> {
                menu.saveFile();
                saved = 1;

            });
            btnPaint[1].addActionListener(e -> menu.newFile());
            btnPaint[2].addActionListener(e -> drawingArea.undo());

            // 添加监听
            btnItalic.addItemListener(e -> italic = Font.ITALIC);
            btnBold.addItemListener(e -> blodtype = Font.BOLD);

            // 设置字体大小
            toolbar.add(jFontSize);
            jFontSize.addItemListener(e -> fSize = Integer.parseInt(fontSize[jFontSize.getSelectedIndex()]));

            // 设置字体
            toolbar.add(jFont);
            jFont.addItemListener(e -> fontName = font[jFont.getSelectedIndex()]);
            // 添加按钮面板到容器中
            MyFrame.this.add(toolbar, BorderLayout.NORTH);

        }
    }

}

