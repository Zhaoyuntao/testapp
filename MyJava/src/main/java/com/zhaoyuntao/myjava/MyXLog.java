package com.zhaoyuntao.myjava;

import com.meizu.sysmonitor.XlogFileDecoder;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentListener;


/**
 * created by zhaoyuntao
 * on 24/11/2021
 * description:
 */
public class MyXLog {
    static DocumentListener listener;

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setBackground(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.width;//得到宽
        int height = (int) screenSize.height;//得到高
        S.s("w:" + width + " h:" + height);
        int wFrame = (int) (width * 0.7f);
        int hFrame = (int) (height * 0.7f);
        int x = (int) ((width - wFrame) / 2f);
        int y = (int) ((height - hFrame) / 2f);
        frame.setBounds(x, y, wFrame, hFrame);
        frame.setMinimumSize(new Dimension(wFrame, hFrame));
        frame.setTitle("Json Formatter");
        frame.setLayout(new GridLayout(1, 1));

        final JTextArea label = new JTextArea();
        label.setLineWrap(true);
        label.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane jScrollPane = new JScrollPane(label);
        jScrollPane.getVerticalScrollBar().setBackground(null);
        jScrollPane.setBackground(null);
        frame.getContentPane().add(jScrollPane);
        new MyFileDropListener(label) {

            @Override
            void onSelectFile(final File file) {
                S.s("file:" + file);
                label.append("\n"+file.getAbsolutePath());
                System.out.println("Task start...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        label.append("\nstart decrypt");
                        File outfile = new File(file.getParent(), file.getName() + ".logs");
                        if(!outfile.exists()){
                            try {
                                outfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        long startTime = System.currentTimeMillis();
                        XlogFileDecoder.ParseFile(file.getAbsolutePath(), outfile.getAbsolutePath());
                        label.append("\nOut file:"+outfile.getAbsolutePath());
                        label.append(String.format("\nTask end and spent %d ms", System.currentTimeMillis() - startTime));
                    }
                }).start();
            }
        };
        // 显示窗口
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
