package com.zhaoyuntao.mapdesigner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Created by zhaoyuntao on 2019-4-28.
 */

public class ADBTool extends JFrame {

    Thread time;
    boolean flag = true;

    int w_screen, h_screen;

    /**
     * 鼠标拖动窗口模块：变量
     */
    private int window_x, window_y;
    private int mouse_x, mouse_y;

    // JTextArea jTextArr;//数组
    JScrollPane jScrollPane;
    //
    JPanel jPanelList;
    //
    JTextArea terminalView;
    JTextArea modelView;
    private final String USED = "USED";
    private final String SPLIT = "|";
    private final String BRACKET_LEFT = "(";
    private final String BRACKET_RIGHT = ")";
    // private final String xiaomiConfigFilePath = "G:" + File.separator +
    // "desktop" + File.separator + "SWDK设备id配置程序" + File.separator + "program"
    // + File.separator + "swdk-config.txt";
    private final String xiaomiConfigFilePath = "swdk-config.txt";
    private final String myConfigFilePath = "conf/swdkSoftware.conf";
    /// mnt/res/etc/miio/device.conf
    private final String devicefileDirectory = "/mnt/res/etc/miio/";
    private final String devicefileName = "device.conf";
    private final String pic_close = ".\\pic\\close.png";
    private final String pic_ico = ".\\pic\\title.png";
    // 本地临时存储一个文件,然后推送进去
    private final String tmpFile = devicefileName;

    private final String TAB = "\n";
    private final String usedStr = "已导入";
    private final String unUsedStr = "导入设备";
    private final String rex_ls_no = " *ls.*(No +such +file +or +directory) *";
    private final String LINE = "------------------------";

    private int state = 0;
    private final int STATE_OK = 0;
    private final int STATE_ADB = 1;
    private final int STATE_MORETHANONE = 2;
    private final int STATE_NODEVICE = 3;

    private boolean isExecing;

    public ADBTool() {

        JFrame jFrame = initWindow();

        JPanel bigPanel = initBigPanel(jFrame);

        // 地图区
        JPanel mapPanel = initTerminalPanel(bigPanel);
        // 操作区
        JPanel buttonPanel = initRightPanel(bigPanel);

        // 信息
        initTerminalView(mapPanel);
        // 第一组按钮
        initButtonPanel(buttonPanel);
        // 数组区域
        initArrPanel(buttonPanel);

        this.setVisible(true);
        //
        initModel();

        initConfig();
    }

    FileLock fileLockModel = null;
    RandomAccessFile randomAccessFileModel;

    private boolean initRandomAccessFileModel(String path) {
        if (randomAccessFileModel == null) {
            File file = new File(path);

            if (!file.exists()) {
                return false;
            }
            try {
                randomAccessFileModel = new RandomAccessFile(file, "rw");
                fileLockModel = randomAccessFileModel.getChannel().tryLock();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    private void initModel() {

        if (initRandomAccessFileModel(myConfigFilePath)) {
            try {
                String model;
                while ((model = randomAccessFileModel.readLine()) != null) {
                    if (!isEmpty(model)) {
                        if (modelView != null) {
                            modelView.setText(model);
                        }
                        showTerminal("默认model:" + model);
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String modelDefault = getMode();
        showTerminal("默认model:" + modelDefault);
        modelView.setText(modelDefault);
    }

    public void saveModel(String model) {
        if (isEmpty(model)) {
            showTerminal("model不能为空,无法保存");
            return;
        }
        if (initRandomAccessFileModel(myConfigFilePath)) {
            try {
                randomAccessFileModel.setLength(0);
                randomAccessFileModel.writeBytes(model);
                showTerminal("model保存成功:" + model);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getMode() {
        if (modelView != null && modelView.getText() != null && !modelView.getText().trim().equals("")) {
            return modelView.getText();
        } else {
            return "kxf321.mop.mo001";
        }
    }

    List<Keys> list;

    private void initConfig() {
        if (list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }

        String[] configs = readFile(xiaomiConfigFilePath);

        String rex = BRACKET_LEFT + "[a-zA-z0-9]+" + "\\" + SPLIT + "[0-9]+" + "\\" + SPLIT + "[a-zA-z0-9]+" + BRACKET_LEFT + "\\" + SPLIT + USED + BRACKET_RIGHT + "?" + BRACKET_LEFT + "\\" + SPLIT + "[0-9]+" + BRACKET_RIGHT + "?" + BRACKET_RIGHT;
        Pattern pattern = Pattern.compile(rex);
        for (int i = 0; configs != null && i < configs.length; i++) {
            String line = configs[i];
            if (!isEmpty(line)) {
                if (pattern.matcher(line).matches()) {
                    String[] content = line.split("\\" + SPLIT);
                    if (content != null && content.length > 2) {
                        Keys keys = new Keys();
                        keys.setMac(content[0]);
                        keys.setDid(content[1]);
                        keys.setKey(content[2]);
                        if (content.length > 3 && USED.equals(content[3])) {
                            keys.setUsed(true);
                            if (content.length > 4) {
                                try {
                                    long time = Long.parseLong(content[4]);
                                    keys.setTime(time);
                                } catch (NumberFormatException e) {
                                    S.e(e);
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            keys.setUsed(false);
                        }
                        list.add(keys);
                    }
                } else {
                    S.e("不匹配:" + line);
                }
            }
        }

        flushUI();
        showTerminalLine();
    }

    JLabel jTextDevice;

    public JFrame initWindow() {

        // 设置鼠标可拖动
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                ADBTool.this.setLocation(window_x + (e.getXOnScreen() - mouse_x), window_y + (e.getYOnScreen() - mouse_y));
                window_x = ADBTool.this.getX();
                mouse_x = e.getXOnScreen();
                window_y = ADBTool.this.getY();
                mouse_y = e.getYOnScreen();
            }
        });
        this.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                window_x = ADBTool.this.getX();
                mouse_x = e.getXOnScreen();
                window_y = ADBTool.this.getY();
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
        int w_Window = (int) (w_screen * 0.8);
        int h_window = (int) (h_screen * 0.7);
        this.setBounds((w_screen - w_Window) / 2, (h_screen - h_window) / 2, w_Window, h_window);
        this.setLayout(null);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setBounds(0, 0, w_Window, 30);
        titlePanel.setBackground(Color.LIGHT_GRAY);
        this.add(titlePanel);

        JButton closeButton = new JButton();
        int wClose = (int) (25);
        int hClose = (int) (25);
        ImageIcon imageIcon = new ImageIcon(pic_close);
        imageIcon.getImage();
        Image temp = imageIcon.getImage().getScaledInstance(wClose, hClose, Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(temp);
        closeButton.setIcon(imageIcon);
        closeButton.setIcon(imageIcon);
        closeButton.setOpaque(false);// 设置控件是否透明，true为不透明，false为透明
        closeButton.setContentAreaFilled(false);// 设置图片填满按钮所在的区域
        // closeButton.setMargin(new Insets(0, 0, 0, 0));// 设置按钮边框和标签文字之间的距离
        closeButton.setFocusPainted(false);// 设置这个按钮是不是获得焦点
        // closeButton.setBorderPainted(false);//设置是否绘制边框
        closeButton.setBorderPainted(false);

        closeButton.setBounds(w_Window - wClose - 5, 2, wClose, hClose);
        closeButton.setFont(new Font("宋体", Font.BOLD, 15));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(ADBTool.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        titlePanel.add(closeButton);

        JButton jButton_ico = new JButton();
        int x_ico=5;
        int w_ico=22;
        int h_ico=22;
        ImageIcon imageIcon2 = new ImageIcon(pic_ico);
        imageIcon2.getImage();
        Image temp2 = imageIcon2.getImage().getScaledInstance(wClose, hClose, Image.SCALE_DEFAULT);
        imageIcon2 = new ImageIcon(temp2);
        jButton_ico.setIcon(imageIcon2);
        jButton_ico.setOpaque(false);// 设置控件是否透明，true为不透明，false为透明
        jButton_ico.setContentAreaFilled(false);// 设置图片填满按钮所在的区域
        // jButton_ico.setMargin(new Insets(0, 0, 0, 0));// 设置按钮边框和标签文字之间的距离
        jButton_ico.setFocusPainted(false);// 设置这个按钮是不是获得焦点
        jButton_ico.setBounds(x_ico, 3, w_ico, h_ico);
        jButton_ico.setFont(new Font("宋体", Font.BOLD, 15));

        titlePanel.add(jButton_ico);

        final JLabel jTextTime = new JLabel();
        int wMsg = 120;
        int hMsg = 20;
        int x_time=x_ico+w_ico+10;
        jTextTime.setBounds(x_time, 5, wMsg, hMsg);
        titlePanel.add(jTextTime);

        jTextDevice = new JLabel("未连接设备");
        jTextDevice.setForeground(Color.RED);
        int x_device = x_time + wMsg + 10;
        jTextDevice.setBounds(x_device, 5, wMsg, hMsg);
        titlePanel.add(jTextDevice);

        time = new Thread(new Runnable() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("Y-M-d h:mm:ss");

            @Override
            public void run() {
                while (flag) {
                    String msg = simpleDateFormat.format(new Date(System.currentTimeMillis()));

                    if (jTextTime != null) {
                        jTextTime.setText(msg);
                    }

                    judgeDevice();

                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        time.start();

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (randomAccessFileModel != null) {
                    try {
                        randomAccessFileModel.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    if (fileLockModel != null && fileLockModel.isValid()) {
                        fileLockModel.release();
                    }
                    S.s("model文件锁已释放");
                } catch (IOException e1) {
                }
                try {
                    if (fileLock != null && fileLock.isValid()) {
                        fileLock.release();
                    }
                    S.s("文件锁已释放");
                } catch (IOException e1) {
                }
                time.interrupt();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

        jTextMsg = new JLabel();
        jTextMsg.setBounds(x_device + wMsg + 20, 5, w_Window - wMsg - 40 - wClose, hMsg);
        jTextMsg.setBackground(Color.RED);
        titlePanel.add(jTextMsg);

        return this;
    }

    private void judgeDevice() {
        String res[] = execSystem(" adb devices ");
        String res_suc = res[0].trim();
        String msg = "List of devices attached";
        if (res_suc.contains("") || res_suc.contains(msg)) {
            res_suc = res_suc.replaceAll(msg, "");
            res_suc = res_suc.replaceAll("devices", "");
            int i = 0;
            while (res_suc.contains("device")) {
                i++;
                res_suc = res_suc.replaceFirst("device", "");
            }
            if (i == 1) {
                state = STATE_OK;
                jTextDevice.setText("设备已连接");
                jTextDevice.setForeground(Color.BLACK);
            } else if (i <= 0) {
                state = STATE_NODEVICE;
                jTextDevice.setText("未连接设备");
                jTextDevice.setForeground(Color.RED);
            } else {
                state = STATE_MORETHANONE;
                jTextDevice.setText("连接超过一台设备");
                jTextDevice.setForeground(Color.RED);
            }
        } else if (res_suc.contains("不是内部") || res_suc.contains("DNS")) {
            state = STATE_ADB;
            jTextDevice.setText("请正确安装adb驱动");
            jTextDevice.setForeground(Color.RED);
        }
    }

    JLabel jTextMsg;

    public void showMsg(String msg) {
        if (jTextMsg != null) {
            msg_last = jTextMsg.getText();
            jTextMsg.setText(msg);
        }
    }

    public void showTerminalLine() {
        showTerminal(LINE + new SimpleDateFormat("M月d日 h:mm:ss ").format(new Date(System.currentTimeMillis())) + LINE);
    }

    public void showTerminal(String msg) {
        S.s(msg);
        if (terminalView != null) {
            terminalView.setText(terminalView.getText() + msg + TAB);
        }
    }

    private String msg_last;

    public void resetMsg() {
        showMsg(msg_last);
    }

    public void clearMsg() {
        if (jTextMsg != null) {
            jTextMsg.setText("");
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

    public JPanel initTerminalPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        int w_view = (int) (w_container * 0.25f - 20);
        int h_view = h_container - 20;

        final JLabel modelLabel = new JLabel("默认model:");
        int w_label = 70;
        modelLabel.setBounds(20, 20, w_label, 20);

        modelView = new JTextArea();
        modelView.setEnabled(true);
        modelView.setLineWrap(false);
        int x_model = 20 + w_label;
        int y_model = 20;
        int w_model = 170;
        modelView.setBounds(x_model, y_model, w_model, 20);

        JButton saveModel = new JButton("保存");
        saveModel.setOpaque(false);// 设置控件是否透明，true为不透明，false为透明
        saveModel.setContentAreaFilled(false);// 设置图片填满按钮所在的区域
        int x_save = x_model + w_model + 10;
        int w_save = w_view - w_model - w_label - 30;
        saveModel.setBounds(x_save, 20, w_save, 20);
        saveModel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveModel(modelView.getText());
            }
        });

        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(null);
        mapPanel.setBounds(10, y_model + 20 + 20, w_view, h_view - 60);

        container.add(modelLabel);
        container.add(modelView);
        container.add(saveModel);
        container.add(mapPanel);
        return mapPanel;
    }

    public JPanel initRightPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        int w_view = (int) (w_container * 0.75f - 20);
        int h_view = h_container - 20;

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBounds(w_container - w_view - 10, 10, w_view, h_view);
        container.add(rightPanel);
        return rightPanel;
    }

    public void initTerminalView(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        terminalView = new JTextArea();
        terminalView.setLineWrap(true);
        terminalView.setEnabled(false);
        terminalView.setDisabledTextColor(Color.DARK_GRAY);

        JScrollPane jsp1 = new JScrollPane(terminalView);
        jsp1.setBounds(10, 10, w_container - 20, h_container - 20);
        container.add(jsp1);
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

        final JRadioButton jRadioButton_unused = new JRadioButton("仅显示未使用", false);
        int x0 = 10;
        jRadioButton_unused.setBounds(x0, 0, w_child, h_view);
        jPanel.add(jRadioButton_unused);

        final JRadioButton jRadioButton_used = new JRadioButton("仅显示使用过的", false);
        int x1 = x0 + w_child + 10;
        jRadioButton_used.setBounds(x1, 0, w_child, h_view);
        jPanel.add(jRadioButton_used);

        final JRadioButton jRadioButton_all = new JRadioButton("显示全部", true);
        int x2 = x1 + w_child + 10;
        jRadioButton_all.setBounds(x2, 0, w_child, h_view);
        jPanel.add(jRadioButton_all);

        final JButton jButton_test = new JButton("检测当前设备");
        int x3 = x2 + w_child + 10;
        jButton_test.setBounds(x3, 0, w_child / 2, h_view);
        jButton_test.setContentAreaFilled(false);
        jPanel.add(jButton_test);

        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButton_unused);
        group.add(jRadioButton_used);
        group.add(jRadioButton_all);
        group.add(jButton_test);

        jRadioButton_unused.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent changeEvent) {
                if (jRadioButton_unused.isSelected()) {
                    flushUI(false);
                }
                showTerminalLine();
                ;
            }
        });
        jRadioButton_used.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (jRadioButton_used.isSelected()) {
                    flushUI(true);
                }
                showTerminalLine();
                ;
            }
        });
        jRadioButton_all.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (jRadioButton_all.isSelected()) {
                    flushUI();
                }
                showTerminalLine();
                ;
            }
        });

        jButton_test.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!execAccess()) {
                    return;
                }
                Keys keys_DeviceNow = isDeviceConfigExists(true);
                showTerminalLine();
                if (keys_DeviceNow != null) {
                    JOptionPane.showMessageDialog(null, "当前设备已经配置过", "检测结果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void flushUI(List<Keys> list_tmp) {
        jPanelList.removeAll();
        for (int i = 0; i < list_tmp.size(); i++) {
            Keys keys = list_tmp.get(i);
            JPanel jPanel = getItem(keys, i);
            jPanelList.add(jPanel);
        }
    }

    /**
     * 显示全部
     */
    private void flushUI() {
        flushUI(list);
        showTerminal("总计:" + list.size() + "条");
    }

    /**
     * 按条件显示
     *
     * @param isUsed
     */
    private void flushUI(boolean isUsed) {
        List<Keys> list_tmp = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Keys keys = list.get(i);
            if (isUsed == keys.isUsed()) {
                list_tmp.add(keys);
            }
        }
        flushUI(list_tmp);
        if (isUsed) {
            showTerminal("已使用:" + list_tmp.size() + "条");
        } else {
            showTerminal("未使用" + list_tmp.size() + "条");
        }
    }

    int w_arrView;
    int h_arrView;

    private void initArrPanel(JPanel container) {
        int w_container = container.getWidth();
        int h_container = container.getHeight();

        int w_view = w_container;
        int h_view = h_container - 30;
        JPanel arrPanel = new JPanel();
        arrPanel.setLayout(null);
        arrPanel.setBounds(0, 30, w_view, h_view);
        container.add(arrPanel);

        int w_container_panel = arrPanel.getWidth();
        int h_container_panel = arrPanel.getHeight();

        // jTextArr = new JTextArea();
        // jTextArr.setLineWrap(true);
        w_arrView = w_container_panel - 20;
        h_arrView = h_container_panel - 20;

        jPanelList = new JPanel();
        jPanelList.setLayout(new VirticalFlowLayout(0, 0, 0, true, false));
        jPanelList.setBackground(Color.WHITE);

        jScrollPane = new JScrollPane(jPanelList);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        jScrollPane.setBounds(10, 10, w_arrView, h_arrView);

        arrPanel.add(jScrollPane);
    }

    private final Color colorUse_font = Color.LIGHT_GRAY;
    private final Color colorUnUse_font = Color.BLACK;
    private final Color colorUse_item = Color.LIGHT_GRAY;
    private final Color colorUnuse_item = Color.WHITE;

    private JPanel getItem(final Keys keys, int index) {
        if (keys == null) {
            return null;
        }
        final JPanel item = new JPanel();

        int hButton = 30;
        int w_jPanelList = w_arrView - 60;
        item.setPreferredSize(new Dimension(w_jPanelList, hButton + 10));
        // item.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        item.setBorder(null);

        int childCount = 5;
        // item总宽度
        int w_item = w_jPanelList - 10 * childCount;

        int indexSize = (int) (w_item * 0.06f);
        final int resetSize = (int) (w_item * 0.17f);
        int didSize = (int) (w_item * 0.4f);
        int copySize = (int) (w_item * 0.1f);
        int importSize = (int) (w_item * 0.2f);

        // number
        JLabel jLabelIndex = new JLabel();
        jLabelIndex.setText("" + (index + 1));
        jLabelIndex.setSize(indexSize, hButton);
        int x_index = 10;
        jLabelIndex.setLocation(x_index, 5);
        // reset
        final JButton jButton_reset = new JButton("重置");
        jButton_reset.setContentAreaFilled(false);
        jButton_reset.setSize(resetSize, hButton);
        int x_reset = x_index + indexSize + 10;
        jButton_reset.setLocation(x_reset, 5);
        // did
        final JLabel jLabel_did = new JLabel();
        jLabel_did.setSize(didSize, hButton);
        int x_did = x_reset + resetSize + 10;
        jLabel_did.setLocation(x_did, 5);
        // copy
        final JButton jButton_copy = new JButton();
        jButton_copy.setContentAreaFilled(false);
        jButton_copy.setText("复制信息");
        jButton_copy.setSize(copySize, hButton);
        jButton_copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

                String arrStr = keys.toString();
                Transferable trandata = new StringSelection(arrStr);
                clipboard.setContents(trandata, null);
                showMsg("已复制到剪切板");
                showTerminal("已复制到剪切板");
            }
        });
        int x_copy = x_did + didSize + 10;
        jButton_copy.setLocation(x_copy, 5);
        // import
        final JButton jButton_import = new JButton();
        jButton_import.setContentAreaFilled(false);
        jButton_import.setSize(importSize, hButton);
        int x_import = x_copy + copySize + 10;
        jButton_import.setLocation(x_import, 5);

        final Call call = new Call() {
            @Override
            public void initView() {
                jLabel_did.setEnabled(!keys.isUsed());
                jButton_import.setText(keys.isUsed() ? usedStr : unUsedStr);
                jButton_import.setEnabled(!keys.isUsed());
                item.setBackground(keys.isUsed() ? colorUse_item : colorUnuse_item);
                String did = keys.getDid();
                if (keys.isUsed()) {
                    did = did + "  [" + new SimpleDateFormat("Y-M-d H:mm:ss").format(keys.getTime())+"]";
                }
                jButton_reset.setEnabled(keys.isUsed());
                jLabel_did.setText(did);
                jLabel_did.setForeground(keys.isUsed() ? colorUse_font : colorUnUse_font);
            }
        };

        jButton_reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!execAccess()) {
                    return;
                }
                Object[] options = { " 确定 ", " 取消 " };
                int response = JOptionPane.showOptionDialog(null, "确定要重置该Key的使用记录吗,将会清空该key的使用记录并重新置为可用", "重置Key的使用记录", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (response == 0) {
                    // 记录上次的数值
                    long timeLast = keys.getTime();
                    boolean isUsed = keys.isUsed();
                    // 设置本次要写入的数值
                    keys.setUsed(false);
                    keys.setTime(0);
                    boolean ok = writeFile(xiaomiConfigFilePath, list);
                    if (ok) {
                        showTerminal("did:" + keys.getDid() + "重置为未使用");
                    } else {
                        keys.setUsed(isUsed);
                        keys.setTime(timeLast);
                        showTerminal("did:" + keys.getDid() + "重置失败");
                        JOptionPane.showMessageDialog(null, "重置失败", "重置失败", JOptionPane.WARNING_MESSAGE);
                    }

                    call.initView();
                }
                showTerminalLine();
                ;
            }
        });

        jButton_import.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (!execAccess()) {
                    return;
                }
                Keys keys_DeviceNow = isDeviceConfigExists();
                boolean push = true;
                if (keys_DeviceNow != null) {
                    S.s("keys_DeviceNow设备已被配置过");
                    Object[] options = { " 确定 ", " 取消 " };
                    int response = JOptionPane.showOptionDialog(null, "当前设备已经配置过,是否要导入新的配置并覆盖之前的配置?", "设备已配置", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (response != 0) {
                        push = false;
                    }
                }
                if (push) {
                    // 记录上次的数值
                    long timeLast = keys.getTime();
                    boolean isUsed = keys.isUsed();
                    keys.setUsed(true);
                    keys.setTime(System.currentTimeMillis());
                    boolean ok_push = pushIntoDevice(keys);
                    if (ok_push) {
                        boolean ok = writeFile(xiaomiConfigFilePath, list);
                        if (ok) {
                            showTerminal("文件保存成功");
                        } else {
                            keys.setTime(timeLast);
                            keys.setUsed(isUsed);
                            showTerminal("文件保存失败");
                            JOptionPane.showMessageDialog(null, "文件保存失败", "导入失败", JOptionPane.ERROR_MESSAGE);
                        }

                        // Object[] options = {" 立即重启 ", " 稍后重启 "};
                        // int response = JOptionPane.showOptionDialog(null,
                        // "配置已导入,是否需要立即重启设备?", "设备已配置", JOptionPane.YES_OPTION,
                        // JOptionPane.QUESTION_MESSAGE, null, options,
                        // options[0]);
                        // if (response == 0) {
                        // showTerminal("正在重启设备...");
                        // exec("adb reboot");
                        // showTerminal("设备已关闭,正在重启...");
                        // } else {
                        // showTerminal("已取消重启,请勿直接断电,防止设备配置信息丢失");
                        // }
                    } else {
                        keys.setTime(timeLast);
                        keys.setUsed(isUsed);
                        showTerminal("导入失败,请检查设备连接是否正常");
                        JOptionPane.showMessageDialog(null, "导入失败,请检查设备连接是否正常", "导入失败", JOptionPane.ERROR_MESSAGE);
                    }
                }
                call.initView();
                showTerminalLine();
            }
        });
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                showMsg(keys.toString());
                item.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                clearMsg();
                item.setBorder(null);
            }
        };
        jButton_reset.addMouseListener(mouseListener);
        item.addMouseListener(mouseListener);
        jLabelIndex.addMouseListener(mouseListener);
        jLabel_did.addMouseListener(mouseListener);
        jButton_copy.addMouseListener(mouseListener);
        jButton_import.addMouseListener(mouseListener);

        item.setLayout(null);

        item.add(jLabelIndex);
        item.add(jButton_reset);
        item.add(jLabel_did);
        item.add(jButton_copy);
        item.add(jButton_import);

        call.initView();

        return item;
    }

    interface Call {
        void initView();
    }

    public Keys isDeviceConfigExists() {
        return isDeviceConfigExists(false);
    }

    public Keys isDeviceConfigExists(boolean dialog) {
        showTerminal("正在检测设备配置文件...");
        String ret[] = exec(" adb shell cat " + devicefileDirectory + devicefileName);
        if (ret == null) {
            showTerminal("空的文件内容");
            return null;
        }
        String ret_suc = ret[0];
        String ret_fail = ret[1];

        S.s("检测结果:" + ret);

        if (ret_suc.contains("No such file") || ret_fail.contains("No such file")) {
            showTerminal("设备中没有检测到配置文件");
            if (dialog) {
                JOptionPane.showMessageDialog(null, "设备中没有检测到配置文件", "检测失败", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        } else if (ret_suc.contains("no devices") || ret_fail.contains("no devices")) {
            showTerminal("设备连接出错:请检查设备连接.");
            if (dialog) {
                JOptionPane.showMessageDialog(null, "设备连接出错:请检查设备连接", "检测失败", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        } else if (ret_suc.contains("不是内部") || ret_fail.contains("不是内部")) {
            showTerminal("设备连接出错:请正确安装adb驱动");
            if (dialog) {
                JOptionPane.showMessageDialog(null, "设备连接出错:请正确安装adb驱动", "检测失败", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        } else {
            String[] arr = ret_suc.trim().split("\\" + SPLIT);
            S.s("arr split:" + Arrays.toString(arr));
            if (arr.length >= 5) {
                Keys keys = new Keys();
                String did = "";
                String key = "";
                String mac = "";
                String vendor = "";
                String model = "";
                showTerminal("设备已配置过");
                for (int i = 0; i < arr.length; i++) {
                    S.s("正则匹配:" + arr[i]);
                    if (isEmpty(arr[i])) {
                        continue;
                    }
                    String line = arr[i].trim();
                    S.s("Line:" + line);
                    line = line.replaceAll(" ", "");
                    Matcher matcher = Pattern.compile("(?<=did\\=)[0-9]*").matcher(line);
                    if (matcher.find()) {
                        did = matcher.group(0);
                        keys.setDid(did);
                        showTerminal("did:" + did);
                    }
                    matcher = Pattern.compile("(?<=key\\=)[0-9a-zA-Z]*").matcher(line);
                    if (matcher.find()) {
                        key = matcher.group(0);
                        keys.setKey(key);
                        showTerminal("key:" + key);
                    }
                    matcher = Pattern.compile("(?<=mac\\=).*").matcher(line);
                    if (matcher.find()) {
                        mac = matcher.group(0);
                        String rex = "(([a-fA-F0-9]{2}:)|([a-fA-F0-9]{2}-)){5}[a-fA-F0-9]{2}";
                        keys.setMac(mac);
                        if (Pattern.compile(rex).matcher(mac).matches()) {
                            showTerminal("mac:" + mac);
                        } else {
                            showTerminal("非法的mac:" + mac);
                        }
                    }
                    matcher = Pattern.compile("(?<=vendor\\=)[0-9a-zA-Z]*").matcher(line);
                    if (matcher.find()) {
                        vendor = matcher.group(0);
                        keys.setVendor(vendor);
                        showTerminal("vendor:" + vendor);
                    }
                    matcher = Pattern.compile("(?<=model\\=).*").matcher(line);
                    if (matcher.find()) {
                        model = matcher.group(0);
                        showTerminal("model:" + model);
                    }
                }

                return keys;
            }
            return null;
        }
    }

    public boolean pushIntoDevice(Keys keys) {

        showTerminal("检测目录...");

        boolean exists = isDirExist(devicefileDirectory);

        if (!exists) {
            showTerminal("文件目录[" + devicefileDirectory + "]不存在,创建目录...");
            boolean mkdirOk = mkdirDevice(devicefileDirectory);
            if (mkdirOk) {
                exists = isDirExist(devicefileDirectory);
                if (!exists) {
                    showTerminal("创建目录失败");
                    return false;
                } else {
                    showTerminal("目录创建成功");
                }
            } else {
                showTerminal("mkdir执行失败");
                return false;
            }
        }

        File file = new File(tmpFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!file.exists()) {
                showTerminal("临时文件创建失败,导入设备失败");
                return false;
            } else {
                showTerminal("临时文件创建成功");
            }
        }

        showTerminal("开始写入配置...");

        String did = "did=" + keys.getDid();
        String key = "key=" + keys.getKey();
        String mac = "mac=" + keys.getMacFormat();
        String vendor = "vendor=" + keys.getVendor();
        String model = "model=" + getMode();

        String deviceConfig = did + TAB + key + TAB + mac + TAB + vendor + TAB + model + TAB;
        showTerminal(deviceConfig);
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.setLength(0);
            randomAccessFile.writeBytes(deviceConfig);
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showTerminal("临时文件不存在,无法写入");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            showTerminal("文件读写错误");
            return false;
        }

        String res_push[] = exec("adb push " + tmpFile + " " + devicefileDirectory);
        if (res_push == null) {
            showTerminal("推送配置文件发生错误:exec result is null");
            return false;
        } else {

            String res_suc = res_push[0];
            String res_fail = res_push[1];
            if (res_suc.contains("1 file pushed") || res_suc.contains("KB/s") || res_suc.contains("bytes in") || res_fail.contains("1 file pushed") || res_fail.contains("KB/s") || res_fail.contains("bytes in")) {
                if (isDeviceConfigExists() != null) {
                    showTerminal("导入成功");
                }
            } else {
                showTerminal("导入失败:" + res_fail);
                return false;
            }
        }

        return true;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 给设备创建目录
     *
     * @param dir
     * @return
     */
    public boolean mkdirDevice(String dir) {
        String res[] = exec(" adb shell mkdir -p " + dir);
        if (res == null) {
            showTerminal("发生未知错误:exec result is null");
            return false;
        } else {
            String ret_fail = res[1];
            if (!isEmpty(ret_fail)) {
                if (ret_fail.contains("error")) {
                    showTerminal("创建目标路径失败");
                }
                if (ret_fail.contains("no devices")) {
                    showTerminal("设备连接异常,请检查设备连接");
                    JOptionPane.showMessageDialog(null, "w设备连接异常,请检查设备连接", "设备连接异常", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            }
            return true;
        }
    }

    /**
     * 判断设备目录是否存在
     *
     * @param dir
     * @return
     */
    public boolean isDirExist(String dir) {
        String res[] = exec(" adb shell ls " + dir);
        if (res == null) {
            showTerminal("发生未知错误:exec result is null");
            return false;
        } else {
            if (Pattern.compile(rex_ls_no).matcher(res[0]).matches() || Pattern.compile(rex_ls_no).matcher(res[1]).matches()) {
                showTerminal("目录不存在");
                return false;
            } else {
                showTerminal("目录存在");
                return true;
            }
        }
    }

    private boolean execAccess() {
        if (isExecing) {
            JOptionPane.showMessageDialog(null, "请等待其他操作完成再执行当前操作!", "系统繁忙", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        switch (state) {
            case STATE_OK:
                return true;
            case STATE_NODEVICE:
                JOptionPane.showMessageDialog(null, "设备连接未连接", "设备未连接", JOptionPane.ERROR_MESSAGE);
                break;
            case STATE_MORETHANONE:
                JOptionPane.showMessageDialog(null, "请确保只有一台设备连接", "超过一台设备在连接", JOptionPane.ERROR_MESSAGE);
                break;
            case STATE_ADB:
                JOptionPane.showMessageDialog(null, "请确保adb驱动正确安装", "adb驱动异常", JOptionPane.ERROR_MESSAGE);
                break;
        }
        return false;
    }

    private String[] exec(String cmd) {
        isExecing = true;
        S.s("exec:" + cmd);
        String[] arr = execSystem(cmd);
        S.s("res_suc:" + arr[0]);
        S.s("res_fail:" + arr[1]);
        isExecing = false;
        return arr;
    }

    private String[] execSystem(String cmd) {

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream inputStreamErr = process.getErrorStream();

            InputStream inputStream = process.getInputStream();// 执行结果
            // 得到进程的标准输出信息流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!(Pattern.compile("[\\s]*").matcher(line).matches()) && !line.equals("")) {
                    stringBuilder.append(line + SPLIT);
                }
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStreamErr));
            StringBuilder stringBuilderErr = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                if (!(Pattern.compile("[\\s]*").matcher(line).matches()) && !line.equals("")) {
                    stringBuilderErr.append(line + SPLIT);
                }
            }
            process.destroy();

            return new String[] { stringBuilder.toString(), stringBuilderErr.toString() };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class ZButton extends JButton {
        ImageIcon imageIcon;

        public void setImageIcon(ImageIcon imageIcon) {
            this.imageIcon = imageIcon;
            repaint();
        }

        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (imageIcon != null) {
                g2d.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public boolean writeFile(String path, List<Keys> list) {
        if (!initRandomAccessFile(path)) {
            return false;
        }
        String[] arr = new String[0];
        if (list == null || list.size() == 0) {
            String err = "写入内容为空";
            showTerminal(err);
            JOptionPane.showMessageDialog(null, err, "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                Keys keys = list.get(i);
                if (keys == null) {
                    continue;
                }
                String key = keys.getKey();
                String mac = keys.getMac();
                String did = keys.getDid();
                boolean used = keys.isUsed();

                if (isEmpty(key) || isEmpty(mac) || isEmpty(did)) {
                    continue;
                }
                String content = mac + SPLIT + did + SPLIT + key;
                if (used) {
                    content = content + SPLIT + USED + SPLIT + keys.getTime();
                }
                stringBuilder.append(content + TAB);
            }
            randomAccessFile.setLength(0);
            randomAccessFile.seek(0);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showTerminal("正在同步文件...");
            randomAccessFile.writeBytes(stringBuilder.toString());
            exec("adb shell sync");
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isEmpty(String content) {
        return content == null || "".equals(content.trim()) || Pattern.compile("\\s*").matcher(content).matches();
    }

    FileLock fileLock = null;
    RandomAccessFile randomAccessFile;

    private boolean initRandomAccessFile(String path) {
        if (randomAccessFile == null) {
            File file = new File(path);

            if (!file.exists()) {
                String err = "未检测到配置文件,请检查文件是否存在!";
                showTerminal(err);
                JOptionPane.showMessageDialog(null, err, "错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                fileLock = randomAccessFile.getChannel().tryLock();
                if (fileLock == null || !fileLock.isValid()) {
                    S.e("文件被占用!");
                    System.exit(0);
                    return false;
                } else {
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    public String[] readFile(String path) {
        showTerminal("正在读取文件...");
        String[] arr = new String[0];
        if (!initRandomAccessFile(path)) {
            return arr;
        }

        try {
            int linesCount = 0;
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                linesCount++;
                arr = Arrays.copyOfRange(arr, 0, arr.length + 1);
                arr[arr.length - 1] = line;
            }
            S.s("lines:" + linesCount);
            showTerminal("读取完毕,正在初始化数据...");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static void main(String[] args) {
        new ADBTool();
    }
}
