package loyer.serial;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class USBHelperTool {

  private JFrame frame;
  private JTextField orderField;
  private JTextField nameField;
  private JButton viewButton;
  private JList<Object> viewList;
  private JLabel nameLabel;
  private JButton openButt;
  private JButton closeButt;
  private JButton writeButt;
  private JButton clearButt;
  private JButton readButt;
  private JButton checkButt;
  
  USBHelper usb = new USBHelper();
 
  /**
   * 提供实例化本类方法
   */
  public static void getUsbHelperTool() {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          USBHelperTool window = new USBHelperTool();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  
  /*public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          USBHelperTool window = new USBHelperTool();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }*/

  /**
   * 本类构造器
   */
  public USBHelperTool() {
    initialize();
  }

  /**
   * 初始化窗口
   */
  private void initialize() {
    frame = new JFrame();
    frame.getContentPane().setBackground(new Color(255, 228, 225));
    frame.setBounds(100, 100, 681, 562);
    //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false); //窗口大小不可更改
    //frame.setUndecorated(true); //去掉标题栏
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    
    //更换背景图片 
    ImageIcon img_1 = new ImageIcon(frame.getClass().getResource("/flower.jpg"));
    //ImageIcon img_1 = new ImageIcon("src/back.jpg"); 
    JLabel imgLabel = new JLabel(img_1);
    frame.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE)); 
    imgLabel.setBounds(0,0,img_1.getIconWidth(), img_1.getIconHeight()); //背景图片的位置
    //将contentPane设置成透明的 
    ((JPanel)frame.getContentPane()).setOpaque(false);
    
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image img = tk.getImage(frame.getClass().getResource("/Kyokuto.png")); //替换窗口的咖啡图标
    //Image img = tk.getImage("src/Kyokuto.png"); //替换窗口的咖啡图标
    frame.setIconImage(img);
    frame.getContentPane().setLayout(null);
    
    viewButton = new JButton("查看可用USB资源");
    viewButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Vector<String> vector = new Vector<>();
        vector.add("USB0::0x1AB1::0x09C4::DM3R200200081::0::INSTR");
        vector.add("USB0::0x1AB1::0x09C4::DM3R194802656::0::INSTR");
        vector.add("USB0::0x1AB1::0x0E11::DP8G193900084::0::INSTR");
        vector.add("TCPIP[board]::host address[::LAN device name][::INSTR]");
        vector.add("TCPIP[board]::host address::port::SOCKET");
        viewList.setListData(vector);
        viewButton.setEnabled(false);
        openButt.setEnabled(true);
        closeButt.setEnabled(true);
        
      }
    });
    viewButton.setBounds(466, 10, 196, 148);
    viewButton.setBackground(new Color(102, 205, 170));
    viewButton.setForeground(new Color(255, 0, 255));
    viewButton.setFont(new Font("微软雅黑", Font.PLAIN, 20));
    frame.getContentPane().add(viewButton);
    
    viewList = new JList<>(); 
    viewList.addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        String str = viewList.getSelectedValue().toString();
        nameField.setText(str);
      }
    });
    viewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    viewList.setValueIsAdjusting(true);
    viewList.setForeground(new Color(0, 255, 0));
    viewList.setFont(new Font("等线", Font.PLAIN, 15));
    viewList.setBackground(Color.BLACK);
    viewList.setBounds(10, 10, 431, 148);
   
    frame.getContentPane().add(viewList);
    
    nameLabel = new JLabel("资源名称:");
    nameLabel.setBounds(10, 168, 75, 15);
    nameLabel.setForeground(new Color(0, 250, 154));
    nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    frame.getContentPane().add(nameLabel);
    
    openButt = new JButton("打开USB接口");
    openButt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        try {
          USBHelper.setUpClass();
          usb.setLogFile();
          usb.getVisaResourceManagerHandle();
          usb.openInstrument(nameField.getText());
          usb.setTimeout();
        } catch(Exception ex) {
          JOptionPane.showMessageDialog(null, "USB打开失败，请检查后重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        checkButt.setEnabled(true);
        //readButt.setEnabled(true);
        writeButt.setEnabled(true);
        clearButt.setEnabled(true);
        
      }
    });
    openButt.setEnabled(false);
    openButt.setBounds(550, 225, 112, 36);
    openButt.setBackground(new Color(102, 205, 170));
    openButt.setFont(new Font("微软雅黑", Font.PLAIN, 12));
    frame.getContentPane().add(openButt);
    
    closeButt = new JButton("关闭USB接口");
    closeButt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        usb.visaWrite("*RST");
        usb.visaClear();
      }
    });
    closeButt.setEnabled(false);
    closeButt.setBounds(428, 225, 112, 36);
    closeButt.setBackground(new Color(102, 205, 170));
    closeButt.setFont(new Font("微软雅黑", Font.PLAIN, 12));
    frame.getContentPane().add(closeButt);
    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setBounds(10, 364, 530, 159);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    frame.getContentPane().add(scrollPane);
    
    JTextArea recieveField = new JTextArea();
    recieveField.setForeground(new Color(0, 0, 255));
    recieveField.setBackground(new Color(255, 245, 238));
    recieveField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
    scrollPane.setViewportView(recieveField);
    
    clearButt = new JButton("清除");
    clearButt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        recieveField.setText("");
      }
    });
    clearButt.setEnabled(false);
    clearButt.setBounds(556, 419, 106, 60);
    clearButt.setFont(new Font("微软雅黑", Font.PLAIN, 18));
    clearButt.setBackground(new Color(102, 205, 170));
    frame.getContentPane().add(clearButt);
    
    orderField = new JTextField();
    orderField.setText("*IDN?");
    orderField.setForeground(new Color(0, 0, 255));
    orderField.setHorizontalAlignment(SwingConstants.CENTER);
    orderField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    orderField.setBounds(10, 280, 655, 27);
    frame.getContentPane().add(orderField);
    orderField.setColumns(10);
    
    JLabel orderLabel = new JLabel("字符会话指令：");
    orderLabel.setForeground(new Color(0, 250, 154));
    orderLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    orderLabel.setBounds(10, 253, 109, 23);
    frame.getContentPane().add(orderLabel);
    
    writeButt = new JButton("写");
    writeButt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        readButt.setEnabled(true);
        usb.visaWrite(orderField.getText());
      }
    });
    writeButt.setEnabled(false);
    writeButt.setBackground(new Color(102, 205, 170));
    writeButt.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    writeButt.setBounds(578, 317, 84, 36);
    frame.getContentPane().add(writeButt);
    
    readButt = new JButton("读");
    readButt.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        //usb.visaWrite(nameField.getText());
        recieveField.setText(usb.visaRead());
      }
    });
    readButt.setEnabled(false);
    readButt.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    readButt.setBackground(new Color(102, 205, 170));
    readButt.setBounds(484, 317, 84, 36);
    frame.getContentPane().add(readButt);
    
    checkButt = new JButton("查询");
    checkButt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(orderField.getText().equals("*IDN?")) {
          usb.visaWrite("*IDN?");
          recieveField.setText(usb.visaRead());
        }
      }
    });
    checkButt.setEnabled(false);
    checkButt.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    checkButt.setBackground(new Color(102, 205, 170));
    checkButt.setBounds(390, 317, 84, 36);
    frame.getContentPane().add(checkButt);
    
    nameField = new JTextField();
    nameField.setText("GPIB0::2::INSTR");
    nameField.setForeground(new Color(0, 255, 0));
    nameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    nameField.setHorizontalAlignment(SwingConstants.CENTER);
    nameField.setBackground(new Color(0, 0, 0));
    nameField.setBounds(10, 188, 652, 27);
    frame.getContentPane().add(nameField);
    nameField.setColumns(10);
    
    JLabel recieveLabel = new JLabel("接收区：");
    recieveLabel.setForeground(new Color(0, 250, 154));
    recieveLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    recieveLabel.setBounds(10, 341, 109, 23);
    frame.getContentPane().add(recieveLabel);

  }
  
}
