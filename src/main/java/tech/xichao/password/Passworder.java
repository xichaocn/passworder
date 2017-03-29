package tech.xichao.password;

import tech.xichao.password.util.AESUtil;
import tech.xichao.password.util.EncryptUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author xichao
 * @version 2017.03.28
 */
public class Passworder {

    /**
     * 屏幕宽度
     */
    private static int screenW = 1920;
    /**
     * 屏幕高度
     */
    private static int screenH = 1080;
    /**
     * 选择文件 Frame
     */
    private static JFrame selectFrame = new JFrame("选择文件");
    /**
     * 选择文件输入框
     */
    private static JTextField selectedField = new JTextField("请选择待加密（解密）文件", 59);
    /**
     * 关键词提示文案
     */
    private static final String KEYWORD_HINT = "请输入关键词（用以提取密码）";
    /**
     * 选中的文件
     */
    private static File selectedFile;
    static {
        //初始化屏幕size
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        screenW = (int) dimension.getWidth();
        screenH = (int) dimension.getHeight();
        selectFile();
    }

    /**
     * 初始向量
     */
    private static final byte[] iv = {'j', '7', '*', 'c', '_', '\\', 'r', '.', '8', '(', 'Y', '1', 'g', '#', 'y', 'p'};
    /**
     * 明文分割符
     */
    private static final String PLAIN_SPLITER = "-->0-->";
    /**
     * 密文分割符
     */
    private static final String CIPHER_SPLITER = "-->1-->";
    /**
     * 加密工具类
     */
    private static final EncryptUtil encryptUtil = new AESUtil(iv);

    public static void main(String[] args) {
        //主Frame
        JFrame jFrame = new JFrame("Passworder");
        //主Panel
        JPanel jPanel = new JPanel();
        jFrame.add(jPanel);

        //密码输入框
        JPasswordField jPasswordField = new JPasswordField(65);
        jPasswordField.setToolTipText("请输入密码");
        jPanel.add(jPasswordField);

        //选择文件输入框
        selectedField.setFocusable(false);
        selectedField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectFrame.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jPanel.add(selectedField);
        //加密按钮
        JButton encryptBtn = new JButton("加密");
        encryptBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String result = encryptFile(jPasswordField.getText());
                JOptionPane.showMessageDialog(null, result);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jPanel.add(encryptBtn);

        //关键词输入款
        JTextField keywordField = new JTextField(KEYWORD_HINT, 59);
        keywordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(KEYWORD_HINT.equals(keywordField.getText())) {
                    keywordField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(null == keywordField.getText() || "".equals(keywordField.getText())) {
                    keywordField.setText(KEYWORD_HINT);
                }
            }
        });
        jPanel.add(keywordField);

        //解密结果域
        JTextArea decryptArea = new JTextArea(25, 65);
        decryptArea.setLineWrap(false);
        //解密结果域自动滚动条
        JScrollPane jScrollPane = new JScrollPane(decryptArea);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //解密按钮
        JButton decryptBtn = new JButton("解密");
        decryptBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(KEYWORD_HINT.equals(keywordField.getText())) {
                    JOptionPane.showMessageDialog(null, "关键词不能为空！");
                    return;
                }
                String result = decryptFile(jPasswordField.getText(), keywordField.getText());
                decryptArea.setText(result);
//                JOptionPane.showMessageDialog(null, result);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        jPanel.add(decryptBtn);
        jPanel.add(jScrollPane);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        int jFrameW = 800;
        int jFrameH = 600;
        jFrame.setSize(jFrameW, jFrameH);
        jFrame.setLocation(new Point((screenW - jFrameW) / 2, (screenH - jFrameH) / 2));
        jFrame.setResizable(false);
        jFrame.setVisible(true);

    }

    /**
     * 选择文件
     */
    private static void selectFile() {
        //选择文件 Panel
        JPanel jPanel = new JPanel();
        selectFrame.add(jPanel);

        //选择文件组件
        JFileChooser textFileChooser = new JFileChooser(".");
        textFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        textFileChooser.setApproveButtonText("选择");
        textFileChooser.setAcceptAllFileFilterUsed(false);//不允许所有文件
        textFileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) {
                    return true;
                }
                if(f.getName().endsWith(".cipher")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "cipher";
            }
        });
        textFileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if(f.isDirectory()) {
                    return true;
                }
                if(f.getName().endsWith(".plain")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "plain";
            }
        });
        textFileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    selectedFile = textFileChooser.getSelectedFile();
                    selectedField.setText(selectedFile.getAbsolutePath());
                    selectFrame.dispose();
                } else if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
                    selectFrame.dispose();
                }
            }
        });
        jPanel.add(textFileChooser);

        selectFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int selectFrameW = 510;
        int selectFrameH = 370;
        selectFrame.setSize(selectFrameW, selectFrameH);
        selectFrame.setLocation(new Point((screenW - selectFrameW) / 2, (screenH - selectFrameH) / 2));
        selectFrame.setResizable(false);
        selectFrame.setVisible(false);
    }

    /**
     * 加密文件
     */
    private static String encryptFile(String password) {
        StringBuilder builder = new StringBuilder();

        if(null == password) {
            return "请输入密码！";
        }
        if(password.length() < 8) {
            return "密码不能短于8位！";
        }
        if(null == selectedFile) {
            return "请选择文件！";
        }
        try(FileInputStream fis = new FileInputStream(selectedFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);) {
            String line = br.readLine();
            while(line != null) {
                //逐行加密
                builder.append(encryptLine(line, password)).append("\n");
                line = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if(builder.length() <= 0) {
            return "加密失败，文件内容为空！";
        }

//        System.out.println(builder.toString());

        Path path = Paths.get(selectedFile.getAbsolutePath());
        File wFile = new File(path.getParent() + "/" + selectedFile.getName().replace(".plain", ".cipher"));
        try(FileOutputStream fos = new FileOutputStream(wFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);) {
            bw.write(builder.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "加密成功！";
    }

    /**
     * 逐行加密
     */
    private static String encryptLine(String line, String password) {
        if(null != line && !"".equals(line) && line.contains(PLAIN_SPLITER)) {
            String[] strArray = line.split(PLAIN_SPLITER);
            return encryptUtil.encrypt(strArray[0], password)
                    + CIPHER_SPLITER + encryptUtil.encrypt(strArray[1], password);
        }
        return line;
    }

    /**
     * 解密文件
     */
    private static String decryptFile(String password, String keyword) {
        if(null == password) {
            return "请输入密码！";
        }
        if(password.length() < 8) {
            return "密码不能短于8位！";
        }
        if(null == selectedFile) {
            return "请选择文件！";
        }

        StringBuilder builder = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(selectedFile);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);) {
            String line = br.readLine();
            while(line != null) {
                //逐行解密
                line = decryptLine(line, password, keyword);
                if(null == line) {
                    return "解密失败！";
                }
                if (!"".equals(line)) {
                    builder.append(line).append("\n");
                }
                line = br.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if(builder.length() <= 0) {
            return "解密失败！";
        }

        return builder.toString();
    }

    /**
     * 逐行解密
     */
    private static String decryptLine(String line, String password, String keyword) {
        if (null == line || "".equals(line)) {
            return "";
        }
        if (line.contains(CIPHER_SPLITER)) {
            String[] strArray = line.split(CIPHER_SPLITER);
            String title = encryptUtil.decrypt(strArray[0], password);
            if (null == title) {
                return null;
            }
            line = title + PLAIN_SPLITER + encryptUtil.decrypt(strArray[1], password);
            if (!title.toLowerCase().contains(keyword.toLowerCase())) {
                return "";
            }
            return line;
        }
        return "";
    }


}
