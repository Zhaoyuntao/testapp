package com.zhaoyuntao.mapdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MapDesigner extends JFrame {

    Thread time;
    boolean flag = true;

    int w_screen, h_screen;

    final byte VALUE_BACK = 0;
    final byte VALUE_FLOOR = 1;
    final byte VALUE_FLOOR_ClEAN = 2;
    final byte VALUE_WALL = 3;
    String msg = "";
    /**
     * 鼠标拖动窗口模块：变量
     */
    private int window_x, window_y;
    private int mouse_x, mouse_y;

    JTextArea jTextW, jTextH;

    JTextArea jTextArr;//数组

    Map mapView;

    public MapDesigner() {

        JFrame jFrame = initWindow();

        int w_window = jFrame.getWidth();
        int h_window = jFrame.getHeight();

        JPanel bigPanel = initBigPanel(jFrame);

        //地图区
        JPanel mapPanel = initMapPanel(bigPanel);
        //操作区
        JPanel buttonPanel = initRightPanel(bigPanel);

        //地图
        initMapView(mapPanel);
        //第一组按钮
        initButtonPanel(buttonPanel);
        //第二组按钮
        initButtonPanel2(buttonPanel);
        //数组区域
        initArrPanel(buttonPanel);

        this.setVisible(true);
    }

    public JFrame initWindow() {

        // ----------------设置鼠标可拖动------------------------------
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                MapDesigner.this.setLocation(window_x + (e.getXOnScreen() - mouse_x), window_y + (e.getYOnScreen() - mouse_y));
                window_x = MapDesigner.this.getX();
                mouse_x = e.getXOnScreen();
                window_y = MapDesigner.this.getY();
                mouse_y = e.getYOnScreen();
            }
        });
        this.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                window_x = MapDesigner.this.getX();
                mouse_x = e.getXOnScreen();
                window_y = MapDesigner.this.getY();
                mouse_y = e.getYOnScreen();
            }

            public void mouseExited(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseClicked(MouseEvent e) {

            }
        });
        this.setUndecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dimension = tk.getScreenSize();

        w_screen = dimension.width;
        h_screen = dimension.height;
        int w_Window = w_screen / 2;
        int h_window = h_screen / 2;
        this.setBounds((w_Window) / 2, (h_window) / 2, w_Window, h_window);
        this.setLayout(null);


        JButton closeButton = new JButton("x");
        int wClose = (int) (30);
        int hClose = (int) (30);

        Insets insets = new Insets(0, 0, 0, 0);
        closeButton.setMargin(insets);
        closeButton.setBounds(w_Window - wClose - 10, 10, wClose, hClose);
        closeButton.setFont(new Font("宋体", Font.BOLD, 15));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
                time.interrupt();
                S.s("close");
            }
        });
        this.add(closeButton);

        final JLabel jTextTime = new JLabel();
        int wMsg = 120;
        int hMsg = 30;
        jTextTime.setBounds(10, 10, wMsg, hMsg);
        add(jTextTime);

        time = new Thread(new Runnable() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("Y-M-d h:m:s");

            @Override
            public void run() {
                while (flag) {
                    msg = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                    if (jTextTime != null) {
                        jTextTime.setText(msg);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        time.start();

        jTextMsg = new JLabel();
        jTextMsg.setBounds(w_Window - wClose - 10 - wMsg - 10, 10, wMsg, hMsg);
        add(jTextMsg);

        return this;
    }

    JLabel jTextMsg;

    public void showMsg(String msg) {
        if (jTextMsg != null) {
            jTextMsg.setText(msg);
        }
    }

    public JPanel initBigPanel(JFrame jFrame) {
        int w_container = jFrame.getWidth();
        int h_container = jFrame.getHeight();

        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(null);
        bigPanel.setBounds(0, 50, w_container, h_container - 50);
        jFrame.add(bigPanel);
        return bigPanel;
    }

    public JPanel initMapPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        int w_view = w_container / 2 - 20;
        int h_view = h_container - 20;

        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(null);
        mapPanel.setBounds(10, 10, w_view, h_view);
        container.add(mapPanel);
        return mapPanel;
    }

    public JPanel initRightPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        int w_view = w_container / 2 - 20;
        int h_view = h_container - 20;

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBounds(w_container / 2 + 10, 10, w_view, h_view);
        container.add(rightPanel);
        return rightPanel;
    }

    public void initMapView(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        mapView = new Map();
        mapView.setBounds(0, 0, w_container, h_container);
        container.add(mapView);
    }


    public void initButtonPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();
        int w_view = w_container;
        int h_view = 30;

        JPanel jPanel = new JPanel();
        jPanel.setBounds(0, 0, w_view, h_view);
        jPanel.setLayout(null);
        container.add(jPanel);

        int countChild = 4;

        int w_child = jPanel.getWidth() / countChild - 10;

        jTextW = new JTextArea(String.valueOf(10));
        int x0 = 0;
        jTextW.setBounds(x0, 0, w_child, h_view);
        jPanel.add(jTextW);

        jTextH = new JTextArea(String.valueOf(10));
        int x1 = x0 + w_child + 10;
        jTextH.setBounds(x1, 0, w_child, h_view);
        jPanel.add(jTextH);


        final JButton buttonMakeArr = new JButton("重置");
        buttonMakeArr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    int w_map = Integer.parseInt(jTextW.getText().trim());
                    int h_map = Integer.parseInt(jTextH.getText().trim());
                    if (w_map <= 300 && h_map <= 300) {
                        mapView.setSize(w_map, h_map);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
        int x2 = x1 + w_child + 10;
        buttonMakeArr.setBounds(x2, 0, w_child, h_view);
        jPanel.add(buttonMakeArr);

        final JButton buttonMakeMap = new JButton("4");
        int x3 = x2 + w_child + 10;
        buttonMakeMap.setBounds(x3, 0, w_child, h_view);
        jPanel.add(buttonMakeMap);

    }

    public void initButtonPanel2(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();
        int w_view = w_container;
        int h_view = 30;

        JPanel jPanel = new JPanel();
        jPanel.setBounds(0, h_view + 10, w_view, h_view);
        jPanel.setLayout(null);
        container.add(jPanel);

        int countChild = 4;

        int w_child = jPanel.getWidth() / countChild - 10;

        //按钮0
        JButton button0 = new JButton("0");
        int x0 = 0;
        button0.setBounds(x0, 0, w_child, h_view);
        jPanel.add(button0);
        //按钮1
        JButton button1 = new JButton("1");
        int x1 = x0 + w_child + 10;
        button1.setBounds(x1, 0, w_child, h_view);
        jPanel.add(button1);
        //按钮2
        final JButton button2 = new JButton("2");
        int x2 = x1 + w_child + 10;
        button2.setBounds(x2, 0, w_child, h_view);
        jPanel.add(button2);
        //按钮3
        final JButton button3 = new JButton("copy");
        int x3 = x2 + w_child + 10;
        button3.setBounds(x3, 0, w_child, h_view);
        jPanel.add(button3);


        button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                String arrStr = jTextArr.getText();
                Transferable trandata = new StringSelection(arrStr);
                clipboard.setContents(trandata, null);
                showMsg("已复制到剪切板");
            }
        });

    }

    private void initArrPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        int w_view = w_container;
        int h_view = h_container - 80;
        JPanel arrPanel = new JPanel();
        arrPanel.setLayout(null);
        arrPanel.setBounds(0, 80, w_view, h_view);
        container.add(arrPanel);

        int w_container_panel = arrPanel.getWidth();
        int h_container_panel = arrPanel.getHeight();


        jTextArr = new JTextArea();
        jTextArr.setLineWrap(true);
        int w_arrView = w_container_panel - 20;
        int h_arrView = h_container_panel - 20;


        JScrollPane jsp1 = new JScrollPane(jTextArr);
        jsp1.setBounds(10, 10, w_arrView, h_arrView);

        arrPanel.add(jsp1);
    }


    public class Map extends JPanel {

        int w_map = 10, h_map = 10;

        int startDrawX;
        int startDrawY;

        int w_px;

        int w_mapDraw, h_mapDraw;

        boolean draw = false;
        int[] mapData;

        int x_mouse, y_mouse;

        int mouseButton;

        public Map() {
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    if (!draw) {
                        int x = mouseEvent.getX();
                        int y = mouseEvent.getY();
                        x_mouse = x;
                        y_mouse = y;
                        mouseButton = mouseEvent.getButton();
                        if (mouseButton == MouseEvent.BUTTON1) {
                            setPx((int) ((x - startDrawX) / (float) w_px), (int) ((y - startDrawY) / (float) w_px), VALUE_WALL);
                        } else if (mouseButton == MouseEvent.BUTTON3) {
                            setPx((int) ((x - startDrawX) / (float) w_px), (int) ((y - startDrawY) / (float) w_px), VALUE_BACK);
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    mouseButton=-1;
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                }
            });
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent mouseEvent) {
                    if (!draw) {
                        int x = mouseEvent.getX();
                        int y = mouseEvent.getY();
                        x_mouse = x;
                        y_mouse = y;
                        setPx((int) ((x - startDrawX) / (float) w_px), (int) ((y - startDrawY) / (float) w_px), VALUE_WALL);
                        if (mouseButton == MouseEvent.BUTTON1) {
                            setPx((int) ((x - startDrawX) / (float) w_px), (int) ((y - startDrawY) / (float) w_px), VALUE_WALL);
                        } else if (mouseButton == MouseEvent.BUTTON3) {
                            setPx((int) ((x - startDrawX) / (float) w_px), (int) ((y - startDrawY) / (float) w_px), VALUE_BACK);
                        }
                    }
                }

                @Override
                public void mouseMoved(MouseEvent mouseEvent) {
                    int x = mouseEvent.getX();
                    int y = mouseEvent.getY();
                    int x_map = (int) ((x - startDrawX) / (float) w_px);
                    int y_map = (int) ((y - startDrawY) / (float) w_px);
                    if (x_map >= 0 && y_map >= 0 && x_map < w_map && y_map < h_map) {
                        showMsg("x:" + x_map + " y:" + y_map);
                    }

                }
            });
            initData();
        }

        public void setPx(int x, int y, byte value) {
            if (x >= 0 && y >= 0 && x < w_map && y < h_map) {
                if (mapData[y * w_map + x] != value) {
                    mapData[y * w_map + x] = value;
                    Map.this.repaint();
                }
            }
        }


        public void setSize(int w_map, int h_map) {
            this.w_map = w_map;
            this.h_map = h_map;
            initData();
            repaint();
        }

        private void initData() {
            mapData = new int[w_map * h_map];
        }

        private void flushMapSize() {

            int w_view = getWidth();
            int h_view = getHeight();

            if (w_view == 0 || h_view == 0) {
                return;
            }

            float propertionView = w_view / (float) h_view;
            float propertionMap = w_map / h_map;

            if (propertionMap > propertionView) {
                w_px = (int) (w_view * 0.9f / w_map);
            } else {
                w_px = (int) (h_view * 0.9f / h_map);
            }

            w_mapDraw = w_px * w_map;
            h_mapDraw = w_px * h_map;

            startDrawX = (w_view - w_mapDraw) / 2;
            startDrawY = (h_view - h_mapDraw) / 2;

            if (jTextArr != null) {
                jTextArr.setText(mapView.getString());
            }
        }

        public void paint(Graphics g) {
            super.paint(g);
            draw = true;
            flushMapSize();

            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.white);
            for (int x = 0; x < w_map; x++) {
                for (int y = 0; y < h_map; y++) {
                    int value = mapData[y * w_map + x];
                    int x_rect = startDrawX + x * w_px;
                    int y_rect = startDrawY + y * w_px;

                    switch (value) {
                        case VALUE_BACK:
                            g.setColor(Color.WHITE);
                            g.fillRect(x_rect, y_rect, w_px, w_px);
                            g.setColor(Color.BLACK);
                            g.drawRect(x_rect, y_rect, w_px, w_px);
                            break;
                        case VALUE_WALL:
                            g.setColor(Color.BLACK);
                            g.fillRect(x_rect, y_rect, w_px, w_px);
                            break;
                    }
                }
            }
            g.setColor(Color.RED);
            g.drawLine(startDrawX, startDrawY, startDrawX, (int) (startDrawY + h_mapDraw));
            g.drawLine(startDrawX, startDrawY, (int) (startDrawX + w_mapDraw), startDrawY);
            g.drawLine((int) (startDrawX + w_mapDraw), startDrawY, (int) (startDrawX + w_mapDraw), (int) (startDrawY + h_mapDraw));
            g.drawLine(startDrawX, (int) (startDrawY + h_mapDraw), (int) (startDrawX + w_mapDraw), (int) (startDrawY + h_mapDraw));
            draw = false;
        }

        public String getString() {
            StringBuilder sb = new StringBuilder();
//            sb.append("[");
            for (int i = 0; i < mapData.length; i++) {
                sb.append(mapData[i]);
                if (i < mapData.length - 1) {
                    sb.append(",");
                }
            }
//            sb.append("]");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        MapDesigner mapDrawer = new MapDesigner();

    }

}

