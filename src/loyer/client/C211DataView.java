package loyer.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import Automation.BDaq.ByteByRef;
import Automation.BDaq.DaqException;
import Automation.BDaq.DeviceInformation;
import Automation.BDaq.DeviceTreeNode;
import Automation.BDaq.ErrorCode;
import Automation.BDaq.InstantDiCtrl;
import Automation.BDaq.InstantDoCtrl;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import loyer.db.C211Data;
import loyer.db.C211DataTools;
import loyer.db.C211RecoupData;
import loyer.db.ECTESTSYSTools;
import loyer.db.Recorddata;
import loyer.db.RecoupDataTool;
import loyer.db.UserData;
import loyer.db.UserTools;
import loyer.exception.InputStreamCloseFail;
import loyer.exception.NoSuchPort;
import loyer.exception.NotASerialPort;
import loyer.exception.OutputStreamCloseFail;
import loyer.exception.PortInUse;
import loyer.exception.ReadDataFromSerialFail;
import loyer.exception.SendToPortFail;
import loyer.exception.SerialPortParamFail;
import loyer.exception.TooManyListeners;
import loyer.gui.LoyerFrame;
import loyer.serial.SerialPortTools;
import loyer.serial.USBHelper;
import loyer.serial.USBHelperTool;

public class C211DataView extends LoyerFrame {

  private JTable table;
  private MyTableCellRenderrer myTableCellRenderrer;
  private PortListener listener = new PortListener();
  private InstantDoCtrl instantDoCtrl = new InstantDoCtrl();
  private InstantDiCtrl instantDiCtrl = new InstantDiCtrl();
  private ByteByRef by;
  private byte pciState = 0x00;
  private List<Integer> mydatap01 = new ArrayList<Integer>();
  private List<Integer> mydatap02 = new ArrayList<Integer>();
  private List<Integer> mydatap03 = new ArrayList<Integer>();
  private int dataCountOfLi = 1000;
  private BufferedImage image;  //相当于c#Bitmap
  private Point point;  //c# Point F
  private Graphics graphics; //c#  Graphics
  private JLabel picLabel; // c# pictureBox
  private ImageIcon picImage; //c#  pictureBox.image
  private double xcResult1 = 0.0d;
  private double xcResult2 = 0.0d;
  private double xcResult3 = 0.0d;
  private double V_result = 0.0d;
  private double R_result= 0.0d;
  private int R_testTimes = 0;
  private int rtimes01 = 0;
  private int rtimes02 = 0;
  private int rtimes03 = 0;
  private final int MAX = 351;
  private final int MIN = 253;
  
  private List<C211RecoupData> list = RecoupDataTool.getC211RecoupData();
  private C211RecoupData data = list.get(0);
  /**拉力1补偿值*/
  private double FULL1 = data.getPULL1();
  /**拉力2补偿值*/
  private double FULL2 = data.getPULL2();
  /**拉力3补偿值*/
  private double FULL3 = data.getPULL3();
  /**电阻补偿值*/
  private double RESIS = data.getRES();
  /**电压补偿值*/
  private double VOL = data.getVOL();
  /**行程1补偿值*/
  private double STROKE1 = data.getSTROKE1();
  /**行程2补偿值*/
  private double STROKE2 = data.getSTROKE2();
  /**行程3补偿值*/
  private double STROKE3 = data.getSTROKE3();
  
  private int timeInterValRSTjueyuan = 0;
  private List<Double> R_TEST = new ArrayList<Double>();
  private Timer timer1 = new Timer(500, new Timer1Listener());
  private Timer timer2 = new Timer(50, new Timer2Listener());
  private Timer timer3 = new Timer(1, new Timer3Listener());
  private boolean printBUT1_LL = false;
  private boolean printBut2_LL = false;
  private boolean printBut3_LL = false;
  private boolean BUT1_SET = false;
  private boolean BUT2_SET = false;
  private boolean BUT3_SET = false;
  private boolean ALLOWTEST_R1 = false;
  private boolean ALLOWTEST_R2 = false;
  private boolean ALLOWTEST_R3 = false;
  private String JUETYANNUM = "";
  private String testState = "STOP";
  private boolean ALLDIALOGISSHOW = false;
  private int s4sendtimes = 0;
  private boolean boolXC1 = false;
  private boolean boolXC2 = false;
  private boolean boolXC3 = false;
  private boolean jueyuanTestBool01 = false;
  private boolean jueyuanTestBool02 = false;
  private boolean ALLjueyuanTestBool01 = false;
  private boolean ALLjueyuanTestBool02 = false;
  private boolean outExcel;
  
  protected SerialPort COM1;
  protected SerialPort COM2;
  protected SerialPort COM3;
  protected SerialPort COM4;
  protected SerialPort COM7;
  protected SerialPort COM8;
  private boolean s1ok = false;
  private boolean s2ok = false;
  private boolean s3ok = false;
  private boolean s4ok = false;
  private boolean s7ok = false;
  private boolean s8ok = false;
  
  protected final USBHelper usb = new USBHelper();
  public static FileHandler logFileHandler;
  protected static final String CLASS_NAME = C211DataView.class.getName();
  public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
  public final String LOG_FOLDER = "log/";
  public static SimpleFormatter logFormatter;
  
  private UserData admin;
  private UserData ny;
  
  public C211DataView() {
    super();
    try {
      Path logPath = Paths.get(LOG_FOLDER);
      if (Files.notExists(logPath)) {
        Files.createDirectory(logPath);
      }
      logFileHandler = new FileHandler(String.format("%s%sLog.txt", LOG_FOLDER, C211DataView.class.getSimpleName()));
      logFormatter = new SimpleFormatter();
      logFileHandler.setFormatter(logFormatter);
      LOGGER.addHandler(logFileHandler);
      
      initialize();
      
      LOGGER.log(Level.INFO, String.format("测试机种为： %s.", PRODUCT_NAME));
      LOGGER.setLevel(Level.SEVERE);
    } catch(SecurityException | IOException | UnsatisfiedLinkError e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
    
  }
  public void initialize() {
    
    //从数据库加载管理员密码和捺印密码
    admin = UserTools.getUserByID(2);
    ny = UserTools.getUserByID(3);
    PRODUCT_NAME = "C211记忆开关";
    productField.setText(PRODUCT_NAME);
    table = completedTable(getTestTable());
    dataPanel.setViewportView(table);
    //persistScroll.setViewportView(new JLabel(picImage));
    persistScroll.setViewportView(new JLabel(new ImageIcon(this.getClass().getResource("/frame.jpg"))));
    
  }
  @Override
  public boolean pwdIsPassed(String command) {
    return false;
  }

  @Override
  public void usartMethod() {
    JPasswordField pf = new JPasswordField();
    pf.setFont(new Font("宋体", Font.PLAIN, 17));
    pf.setEchoChar('*');
    JOptionPane.showMessageDialog(null, pf, "请输入管理员密码：", JOptionPane.PLAIN_MESSAGE);
    char[] pwd = pf.getPassword();
    if(pwd.length == 8) {
      if(String.valueOf(pwd).equals(admin.getPassword())) {
        USBHelperTool.getUsbHelperTool();
      }
      else 
        JOptionPane.showMessageDialog(null, "密码错误！");
    }
    else
      JOptionPane.showMessageDialog(null, "密码长度为8位！");
  }

  @Override
  public void resultView() {
    JPasswordField pf = new JPasswordField();
    pf.setFont(new Font("宋体", Font.PLAIN, 17));
    pf.setEchoChar('*');
    JOptionPane.showMessageDialog(null, pf, "请输入管理员密码：", JOptionPane.PLAIN_MESSAGE);
    char[] pwd = pf.getPassword();
    if(pwd.length == 8) {
      if(String.valueOf(pwd).equals(admin.getPassword())) {
        ViewResult.getViewResult();
      }
      else 
        JOptionPane.showMessageDialog(null, "密码错误！");
    }
    else
      JOptionPane.showMessageDialog(null, "密码长度为8位！");
  }

  @Override
  public void reportView() {
  }

  @Override
  public void nayinMethod() {
    // 由于要触发事件，复选框状态定会改变，故而多加了判断
    JPasswordField pw = new JPasswordField();
    pw.setFont(new Font("宋体", Font.PLAIN, 17));
    pw.setEchoChar('*');
    JOptionPane.showMessageDialog(null, pw, "请输入捺印密码：", JOptionPane.PLAIN_MESSAGE);
    char[] pass = pw.getPassword();
    if (pass.length > 0 && pass.length <= 6) {
      if (String.valueOf(pass).equals(ny.getPassword())) {
        if (nayinButt.isSelected()) {
          nayinButt.setSelected(true);
        } else
          nayinButt.setSelected(false);
      } else {
        JOptionPane.showMessageDialog(null, "密码错误！");
        if (nayinButt.isSelected()) {
          nayinButt.setSelected(false);
        } else
          nayinButt.setSelected(true);
      }
    } else {
      JOptionPane.showMessageDialog(null, "密码长度为6位！");
      if (nayinButt.isSelected()) {
        nayinButt.setSelected(false);
      } else
        nayinButt.setSelected(true);
    }
    ifProductPrint();
  }

  @Override
  public void close() {
    int num = JOptionPane.showConfirmDialog(null, "确认退出？", "提示", JOptionPane.YES_NO_OPTION);
    if (num == JOptionPane.YES_OPTION) {
      try {
        onClosing();
      } catch(Exception ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage());
      }finally {
        logBySelf("退出系统");
        log2txt("log/");
        System.exit(0);
      }
    }
  }
  /**
   * 提供给登录页面调用的方法
   */
  public static void dataView() {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        C211DataView win = new C211DataView();
        win.frame.setVisible(true);
        win.setTableCellRenderer();
        win.initLoad();
      }
    });
  }


  /**
   * 创建JTable方法
   * 
   * @return
   */
  public JTable getTestTable() {
    Vector<Object> rowNum = null, colNum = null;
    // 创建列对象
    colNum = new Vector<>();
    colNum.add("");
    colNum.add("序号");
    colNum.add("测试项目");
    colNum.add("上限");
    colNum.add("下限");
    colNum.add("测试值");
    colNum.add("单位");
    colNum.add("测试结果");
    colNum.add("备注");

    // 创建行对象
    rowNum = new Vector<>();
    List<C211Data> tableList = C211DataTools.getAllbyDb(); // 从数据库中获取c211表的内容
    for (Iterator<C211Data> i = tableList.iterator(); i.hasNext();) {
      C211Data rd = i.next();
      Vector<String> vt = new Vector<>();
      vt.add("");
      vt.add(rd.getPdxuhao());
      vt.add(rd.getTestitem());
      vt.add(rd.getMaxvalue());
      vt.add(rd.getMinvalue());
      vt.add(rd.getTestvalue());
      vt.add(rd.getDanwei());
      vt.add(rd.getTestresult());

      rowNum.add(vt);
    }

    JTable table = new JTable(rowNum, colNum);

    return table;
  }

  /**
   * 提供设置JTable方法
   * 
   * @param table
   * @return
   */
  public JTable completedTable(JTable table) {

    DefaultTableCellRenderer r = new DefaultTableCellRenderer(); // 设置
    r.setHorizontalAlignment(JLabel.CENTER); // 单元格内容居中
    // table.setOpaque(false); //设置表透明
    JTableHeader jTableHeader = table.getTableHeader(); // 获取表头
    // 设置表头名称字体样式
    jTableHeader.setFont(new Font("宋体", Font.PLAIN, 14));
    // 设置表头名称字体颜色
    jTableHeader.setForeground(Color.BLACK);
    jTableHeader.setDefaultRenderer(r);

    // 表头不可拖动
    jTableHeader.setReorderingAllowed(false);
    // 列大小不可改变
    jTableHeader.setResizingAllowed(false);

    // 设置列宽
    TableColumn colNull = table.getColumnModel().getColumn(0);
    TableColumn colTestitem = table.getColumnModel().getColumn(2);
    TableColumn colMaxvalue = table.getColumnModel().getColumn(3);
    TableColumn colMinvalue = table.getColumnModel().getColumn(4);
    TableColumn colTestvalue = table.getColumnModel().getColumn(5);
    TableColumn colTestResult = table.getColumnModel().getColumn(7);
    colNull.setPreferredWidth(20);
    colTestitem.setPreferredWidth(150);
    colMaxvalue.setPreferredWidth(120);
    colMinvalue.setPreferredWidth(120);
    colTestvalue.setPreferredWidth(120);
    colTestResult.setPreferredWidth(120);

    table.setEnabled(false); // 内容不可编辑
    table.setDefaultRenderer(Object.class, r); // 居中显示

    table.setRowHeight(30); // 设置行高
    // 增加一行空白行
    // AbstractTableModel tableModel = (AbstractTableModel) table.getModel();
    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
    tableModel.addRow(new Object[] { "*", "", "", "", "", "", "", "", "", "", "", "", "" });
    table.setGridColor(new Color(245, 245, 245)); // 设置网格颜色
    table.setForeground(Color.BLACK); // 设置文字颜色
    table.setBackground(new Color(245, 245, 245));
    table.setFont(new Font("宋体", Font.PLAIN, 13));
    //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);// 关闭表格列自动调整
    return table;
  }

  /**
   * 初始化table值
   */
  public void initTable() {
    for (int i = 1; i <= 14; i++) {
      table.setValueAt("?", i, 5); // 清空测试值
      table.setValueAt("?", i, 7); // 清空测试结果
    }
  }
  /**
   * 设置测试值
   * 
   * @param row
   *          行数
   * @param value
   *          值
   */
  public void setValueAt(int row, double value) {
    table.setValueAt(value, row, 5);
  }

  /**
   * 重载测试值设置方法
   * 
   * @param row
   * @param value
   */
  public void setValueAt(int row, String value) {
    table.setValueAt(value, row, 5);
  }

  /**
   * 获取表中row(1~14)行的测试值
   * 
   * @param row
   *          行数
   * @return Double类型的数值
   */
  public double getValueAt(int row) {

    if (!table.getValueAt(row, 5).toString().equals("?")) {
      return Double.parseDouble(table.getValueAt(row, 5).toString());
    } else
      return -1;
  }
  /**
   * 获取上限
   * @param row
   * @return
   */
  public double getMaxValue(int row) {
    return Double.parseDouble(table.getValueAt(row, 3).toString());
  }
  /**
   * 获取下限
   * @param row
   * @return
   */
  public double getMinValue(int row) {
    return Double.parseDouble(table.getValueAt(row, 4).toString());
  }
  /**
   * 判断表中测试值列和测试结果列中是否包含问号
   * @return 如果没有，返回true
   */
  public boolean hasQuestionMark() {
    for(int i = 1; i <= 14; i++) {
      if(table.getValueAt(i, 5).equals("?") || table.getValueAt(i, 7).equals("?")) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * 判断表中row(1~14)行的测试结果是否为"PASS"
   * 
   * @param row
   *          行数
   * @return true or false
   */
  public boolean resultOkAtRow(int row) {
    if (table.getValueAt(row, 7).toString().equals("PASS")) {
      return true;
    } else
      return false;
  }

  /**
   * 根据测试值判断测试结果是否PASS
   * 
   * @param row
   *          行数
   */
  public void setResValueAtRow(int row) {
    switch (row) {
    case 1: {
      if (getValueAt(1) <= getMaxValue(1))
        setTableTestResultPASS(1);
      else
        setTableTestResultNG(1);
    }
      break;
    case 2: {
      if (getValueAt(2) <= getMaxValue(2) && getValueAt(2) >= getMinValue(2))
        setTableTestResultPASS(2);
      else
        setTableTestResultNG(2);
    }
      break;
    case 3: {
      if (getValueAt(3) <= getMaxValue(3) && getValueAt(3) >= getMinValue(3))
        setTableTestResultPASS(3);
      else
        setTableTestResultNG(3);
    }
      break;
    case 4: {
      if (getValueAt(4) <= getMaxValue(4) && getValueAt(4) >= getMinValue(4))
        setTableTestResultPASS(4);
      else
        setTableTestResultNG(4);
    }
      break;
    case 5: {
      if (getValueAt(5) <= getMaxValue(5) && getValueAt(5) >= getMinValue(5))
        setTableTestResultPASS(5);
      else
        setTableTestResultNG(5);
    }
      break;
    case 6: {
      if (getValueAt(6) <= getMaxValue(6) && getValueAt(6) >= getMinValue(6))
        setTableTestResultPASS(6);
      else
        setTableTestResultNG(6);
    }
      break;
    case 7: {
      if (getValueAt(7) <= getMaxValue(7) && getValueAt(7) >= getMinValue(7))
        setTableTestResultPASS(7);
      else
        setTableTestResultNG(7);
    }
      break;
    case 8: {
      if (getValueAt(8) <= getMaxValue(8) && getValueAt(8) >= getMinValue(8))
        setTableTestResultPASS(8);
      else
        setTableTestResultNG(8);
    }
      break;
    case 9: {
      if (getValueAt(9) <= getMaxValue(9) && getValueAt(9) >= getMinValue(9))
        setTableTestResultPASS(9);
      else
        setTableTestResultNG(9);
    }
      break;
    case 10: {
      if (getValueAt(10) <= getMaxValue(10) && getValueAt(10) >= getMinValue(10))
        setTableTestResultPASS(10);
      else
        setTableTestResultNG(10);
    }
      break;
    case 11: {
      if (getValueAt(11) <= getMaxValue(11) && getValueAt(11) >= getMinValue(11))
        setTableTestResultPASS(11);
      else
        setTableTestResultNG(11);
    }
      break;
    case 12: {
      if (getValueAt(12) <= getMaxValue(12) && getValueAt(12) >= getMinValue(12))
        setTableTestResultPASS(12);
      else
        setTableTestResultNG(12);
    }
      break;
    case 13: {
      if (table.getValueAt(13, 5).toString().equals("ok"))
        setTableTestResultPASS(13);
      else
        setTableTestResultNG(13);
    }
      break;
    case 14: {
      if (table.getValueAt(14, 5).toString().equals("ok"))
        setTableTestResultPASS(14);
      else
        setTableTestResultNG(14);
    }
      break;
    }
  }
  /**
   * 刷新ok框数值
   */
  public void setOkFieldCount() {
    okCount++;
    okField.setText(okCount + "");
    setPieChart(okCount, ngCount);
    setTotalFieldCount();
  }

  /**
   * 刷新ng框数值
   */
  public void setNgFieldCount() {
    ngCount++;
    ngField.setText(ngCount + "");
    setPieChart(okCount, ngCount);
    setTotalFieldCount();
  }

  /**
   * 刷新总值
   */
  public void setTotalFieldCount() {
    totalCount = okCount + ngCount;
    totalField.setText(totalCount + "");
    
  }
  /**
   * 设置表测试结果为PASS
   * 
   * @param i
   *          第几行(1~14)
   */
  public void setTableTestResultPASS(int i) {
    table.setValueAt("PASS", i, 7);
    //setTableCellRenderer();
  }

  /**
   * 设置表测试结果为NG
   * 
   * @param i
   *          第几行(1~14)
   * @throws DaqException 
   */
  public void setTableTestResultNG(int i) {
    //IFNGBOOL = true;
    table.setValueAt("NG", i, 7);
    //setTableCellRenderer();
    if (!spotButt.isSelected()) {
      setstatuFieldFieldNG();
    }
  }
  /**
   * 设置运行状态框为NG
   * @throws DaqException 
   */
  public void setstatuFieldFieldNG() {
    statuField.setText("NG");
    statuField.setBackground(Color.RED);
    setNgFieldCount();
    //PLCWarmming();
  }

  public void setstatuFieldFieldWAIT() {
    statuField.setText("准备开始...");
    statuField.setBackground(Color.ORANGE);
  }
  /**
   * 设置运行状态框为PASS
   */
  public void setstatuFieldFieldPASS() {
    statuField.setText("PASS");
    statuField.setBackground(GREEN);
    setOkFieldCount();
  }
  
  /**
   * table渲染色，测试结果为"PASS"则设为绿色，"NG"为红色
   */
  public void setTableCellRenderer() {
    if (myTableCellRenderrer == null) {
      myTableCellRenderrer = new MyTableCellRenderrer();
      table.getColumnModel().getColumn(7).setCellRenderer(myTableCellRenderrer);
    } else
      table.getColumnModel().getColumn(7).setCellRenderer(myTableCellRenderrer);
  }
  public void drawPICInit() {
    for (int i = 0; i < dataCountOfLi; i++) {
      int fldata = (int) (i % 1200) / 2;
      mydatap01.add(fldata);
      mydatap02.add(fldata);
      mydatap03.add(fldata);
    }
  }
  
  public void drawPIC(List<Integer> lalidata, int dataofli) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        for(int i = 0; i < dataofli; i++) {
          if(i >= 1) {
            graphics.drawOval(point.x + i / 2 , point.y - lalidata.get(i), 1, 1);
          }
        }
      }
    });
    File picFile = new File("src/picImage.png");
    try {
      
      ImageIO.write(image, "PNG", picFile);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
    } finally {
      if(picFile != null)
        picImage = new ImageIcon("src/picImage.png");
    }
  }
  /**
   * 拉力
   * @param datajh
   * @return
   */
  public int xingchengLL(List<Integer> datajh) {
    int XCLL = 0;
    List<Integer> list = new ArrayList<>();
    Random random = new Random();
    int s = random.nextInt(MAX)%(MAX - MIN + 1) + MIN;
    int count = 0;
    for(int i = 0; i < datajh.size(); i++) {
      if(datajh.get(i) < 500) {
        list.add(datajh.get(i));
      }
      count++;
      if(count < 23) {
        list.add(s);
      }
    }
    Collections.sort(list);
    XCLL = list.get(list.size() - 1);
    return XCLL;
  }
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * 通过PCI向PLC发送数据
   * 
   * @param channel
   * @param state
   * @throws DaqException 
   */
  public void sendMesToPLCByPCI(int channel, byte state) {
    ErrorCode err = ErrorCode.Success;
    err = instantDoCtrl.Write(channel, state);
    if (err != ErrorCode.Success) {
      handleError(err);
    }
  }

  /**
   * 错误信息处理
   * 
   * @param err
   *          ErrorCode
   */
  public void handleError(ErrorCode err) {
    if (err != ErrorCode.Success) {
      JOptionPane.showMessageDialog(null, "PCI出错了：" + err.toString());
    }
  }

  /**
   * 如果捺印复选框被选中，则发送捺印请求，否则不钠印
   */
  public void ifProductPrint() {
    if (nayinButt.isSelected()) {
      pciState = (byte) (pciState & 0xF7);
      sendMesToPLCByPCI(0, pciState);
    } else {
      pciState = (byte) (pciState | 0x08);
      sendMesToPLCByPCI(0, pciState);
    }
  }
  /**
   * 结果框显示值为PASS或者点测复选框被选中，判断测试值是否全部通过
   * @return 真假值
   */
  public boolean isPassed() {
    if (statuField.getText().equals("测试中...") || spotButt.isSelected()) {
      if (resultOkAtRow(1) && resultOkAtRow(2) && resultOkAtRow(3) && resultOkAtRow(4) && resultOkAtRow(5)
          && resultOkAtRow(6) && resultOkAtRow(7) && resultOkAtRow(8) && resultOkAtRow(9) && resultOkAtRow(10)
          && resultOkAtRow(11) && resultOkAtRow(12) && resultOkAtRow(13) && resultOkAtRow(14)) {

        return true;
      }
      else
        return false;
    }
    else
      return false;
  }

  /**
   * 发送不良报警给PLC
   * @throws DaqException 
   */
  public void PLCWarmming() {
    pciState = (byte) (pciState | 0x01);
    sendMesToPLCByPCI(0, pciState);
  }

  /**
   * 取消报警
   * @throws DaqException 
   */
  public void cancelWarmming() {
    pciState = (byte) (pciState & 0xFE);
    sendMesToPLCByPCI(0, pciState);
  }

  /**
   * 绝缘测试复位
   */
  public void jueyuanTestReset() {
    pciState = (byte) ((pciState | 0x04) & 0xFD);
    sendMesToPLCByPCI(0, pciState);
  }

  /**
   * 绝缘测试
   */
  public void jueyuanTest() {
    pciState = (byte) ((pciState | 0x02) & 0xFB);
    sendMesToPLCByPCI(0, pciState);
  }
  /**
   * 读取端口状态
   * @return
   * @throws InterruptedException
   */
  public byte readPortState() {
    byte portDatas = 0;
    by = new ByteByRef(portDatas);
    ErrorCode err = ErrorCode.Success;
    err = instantDiCtrl.Read(0, by);
    if (err != ErrorCode.Success) {
      handleError(err);
      try {
        timer1.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 0;
    }
    return by.value;
  }
  /**
   * 系统退出
   */
  public void onClosing() {
    cancelWarmming();
    jueyuanTestReset();
    timer1.stop();
    timer2.stop();
    timer3.stop();
    COM1.close();
    COM2.close();
    COM3.close();
    COM4.close();
    COM7.close();
    COM8.close();
    usb.visaClear();
  }
  /**
   * 串口发送数据(字符数组)
   * 
   * @param com
   *          串口对象
   * @param datas
   *          待发送数据
   * @return
   */
  public boolean comWrite(SerialPort com, char[] datas) {
    try {
      SerialPortTools.write(com, datas);
      return true;
    } catch (SendToPortFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
      return false;
    } catch (OutputStreamCloseFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
      return false;
    }
  }

  /**
   * 重载串口发送数据(16进制字符串)
   * 
   * @param com
   *          串口对象
   * @param hexString
   *          16进制字符串
   * @return
   */
  public boolean comWrite(SerialPort com, String hexString) {
    byte[] datas = SerialPortTools.toByteArray(hexString);
    try {
      SerialPortTools.write(com, datas);
      return true;
    } catch (SendToPortFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
      return false;
    } catch (OutputStreamCloseFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
      return false;
    }

  }
  /**
   * 初始化串口
   */
  public void initPort() {
    ArrayList<String> port = SerialPortTools.findPort();
    if (!port.contains("COM1")) {
      JOptionPane.showMessageDialog(null, "未发现串口1");
      com1Butt.setSelected(false);
    } else {
      try {
        COM1 = SerialPortTools.getPort(1);
        s1ok = comWrite(COM1, "AA");
        com1Butt.setSelected(true);
        listener.add(COM1, listener.COM1Listener());
      } catch (SerialPortParamFail e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      } catch (NotASerialPort e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      } catch (NoSuchPort e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      } catch (PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM1:" + e.toString());
      }
    }
    if (!port.contains("COM2")) {
      JOptionPane.showMessageDialog(null, "未发现串口2");
      com2Butt.setSelected(false);;
    } else {
      try {
        COM2 = SerialPortTools.getPort(2);
        s2ok = comWrite(COM2, "BB");
        com2Butt.setSelected(true);
        listener.add(COM2, listener.COM2Listener());
      } catch (SerialPortParamFail e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      } catch (NotASerialPort e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      } catch (NoSuchPort e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      } catch (PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM2:" + e.toString());
      }
    }
    if (!port.contains("COM3")) {
      JOptionPane.showMessageDialog(null, "未发现串口3");
      com3Butt.setSelected(false);
    } else {
      try {
        COM3 = SerialPortTools.getPort(3);
        s3ok = comWrite(COM3, "AA");
        com3Butt.setSelected(true);
        listener.add(COM3, listener.COM3Listener());
      } catch (SerialPortParamFail e) {
        JOptionPane.showMessageDialog(null, "COM3:" + e.toString());
      } catch (NotASerialPort e) {
        JOptionPane.showMessageDialog(null, "COM3:" + e.toString());
      } catch (NoSuchPort e) {
        JOptionPane.showMessageDialog(null, "COM3:" + e.toString());
      } catch (PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM3:" + e.toString());
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM3:" + e.toString());
      }
    }
    if (!port.contains("COM4")) {
      JOptionPane.showMessageDialog(null, "未发现串口4");
      com4Butt.setSelected(false);
    } else {
      try {
        COM4 = SerialPortTools.getPort(4);
        s4ok = comWrite(COM4, "BB");
        com4Butt.setSelected(true);
        listener.add(COM4, listener.COM4Listener());
      } catch (SerialPortParamFail e) {
        JOptionPane.showMessageDialog(null, "COM4:" + e.toString());
      } catch (NotASerialPort e) {
        JOptionPane.showMessageDialog(null, "COM4:" + e.toString());
      } catch (NoSuchPort e) {
        JOptionPane.showMessageDialog(null, "COM4:" + e.toString());
      } catch (PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM4:" + e.toString());
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM4:" + e.toString());
      }
    }
    if (!port.contains("COM7")) {
      JOptionPane.showMessageDialog(null, "未发现串口7");
      com5Butt.setSelected(false);
    } else {
      try {
        COM7 = SerialPortTools.getPort(5);
        s7ok = comWrite(COM7, "AA");
        com5Butt.setSelected(true);
        listener.add(COM7, listener.COM7Listener());
      } catch (SerialPortParamFail e) {
        JOptionPane.showMessageDialog(null, "COM7:" + e.toString());
      } catch (NotASerialPort e) {
        JOptionPane.showMessageDialog(null, "COM7:" + e.toString());
      } catch (NoSuchPort e) {
        JOptionPane.showMessageDialog(null, "COM7:" + e.toString());
      } catch (PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM7:" + e.toString());
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM7:" + e.toString());
      }
    }
    if (!port.contains("COM8")) {
      JOptionPane.showMessageDialog(null, "未发现串口8");
      com6Butt.setSelected(false);
    } else {
      try {
        COM8 = SerialPortTools.getPort(6);
        s8ok = comWrite(COM8, "BB");
        com6Butt.setSelected(true);
        listener.add(COM8, listener.COM8Listener());
      } catch (SerialPortParamFail e) {
        JOptionPane.showMessageDialog(null, "COM8:" + e.toString());
      } catch (NotASerialPort e) {
        JOptionPane.showMessageDialog(null, "COM8:" + e.toString());
      } catch (NoSuchPort e) {
        JOptionPane.showMessageDialog(null, "COM8:" + e.toString());
      } catch (PortInUse e) {
        JOptionPane.showMessageDialog(null, "COM8:" + e.toString());
      } catch (TooManyListeners e) {
        JOptionPane.showMessageDialog(null, "COM8:" + e.toString());
      }
    }
    ArrayList<DeviceTreeNode> installedDevice_Di = instantDiCtrl.getSupportedDevices();
    ArrayList<DeviceTreeNode> installedDevice_Do = instantDiCtrl.getSupportedDevices();
    try {
      instantDiCtrl.setSelectedDevice(new DeviceInformation(installedDevice_Di.get(0).toString()));
      instantDoCtrl.setSelectedDevice(new DeviceInformation(installedDevice_Do.get(0).toString()));
      openMultimeter();
      Thread.sleep(100);
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null, "port:" + e.getMessage());
    } catch(DaqException e) {
      JOptionPane.showMessageDialog(null, "PCI:" + e.getMessage());
    }
    if (s1ok && s2ok && s3ok && s4ok && s7ok && s8ok) {
      setstatuFieldFieldWAIT();
    }
  }
  /**
   * 获取数据库里当天存的数据
   * 
   * @return
   */
  public Recorddata getRecorddata() {
    List<Recorddata> list = ECTESTSYSTools.getRecordtdData(LocalDate.now().toString(), "C211");
    Recorddata rd = null;
    for (Iterator<Recorddata> it = list.iterator(); it.hasNext();) {
      rd = it.next();
    }
    return rd;
  }

  public int getOkCount() {
    return Integer.parseInt(getRecorddata().getRecordok());
  }

  public int getNgCount() {
    return Integer.parseInt(getRecorddata().getRecordng());
  }

  public int getTotalCount() {
    return Integer.parseInt(getRecorddata().getRecordsum());
  }
  public int getTimeCount() {
    return Integer.parseInt(getRecorddata().getRecordts());
  }
  /**
   * 初始化界面计数值和饼图
   */
  public void initCountAndPieChart() {
    if (getRecorddata() != null) {
      okCount = getOkCount();
      ngCount = getNgCount();
      totalCount = getTotalCount();
      timeCount = 0;
      okField.setText(okCount + "");
      ngField.setText(ngCount + "");
      totalField.setText(totalCount + "");
      timeField.setText(timeCount + "");
      setPieChart(okCount, ngCount);
    } 
    else {
      okCount = 0;
      ngCount = 0;
      totalCount = 0;
      timeCount = 0;
      okField.setText(okCount + "");
      ngField.setText(ngCount + "");
      totalField.setText(totalCount + "");
      timeField.setText(timeCount + "");
      setPieChart(okCount, ngCount);
    }
  }
  /**
   * 初始化应用
   */
  public void initLoad() {
    initPort();
    initCountAndPieChart();
    
    image = new BufferedImage(picLabel.getWidth(), picLabel.getHeight(), BufferedImage.TYPE_INT_RGB);
    graphics = image.getGraphics();
    drawPICInit();
    point = new Point(0, picLabel.getHeight());
    
    ifProductPrint();
    jueyuanTestReset();
    timer1.start();
    timer3.start();
  }
  
  /**
   * 打开万用表
   */
  public void openMultimeter() {
    try {
      USBHelper.setUpClass();
      String ResourceName = "USB0::0x1AB1::0x09C4::DM3R194802656::INSTR";
      usb.setLogFile();
      usb.getVisaResourceManagerHandle();
      usb.openInstrument(ResourceName);
      usb.setTimeout();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "未发现万用表:" + e.getMessage());
    }
  }

  /**
   * 清除USB资源
   */
  public void clearResource() {
    usb.visaClear();
  }

  /**
   * 打开两线测电阻模式
   */
  public void openTwoLineR_FUC() {
    usb.visaWrite(":RATE:RESistance F");
    usb.visaWrite(":FUNCtion:RESistance");
  }

  /**
   * 打开直流电压测试模式
   */
  public void openDCvoltage_FUC() {
    usb.visaWrite(":FUNCtion:VOLTage:DC");
  }

  /**
   * 设置电阻测试量程
   * 
   * @param range
   *          量程档位
   */
  public void setR_range(String range) {
    usb.visaWrite(":RATE:RESistance F");
    usb.visaWrite(":MEASure:RESistance " + range); // 0 200欧姆 1 2k欧姆 2 20k欧姆
  }

  /**
   * 设置电压档量程
   * 
   * @param range
   *          档位
   */
  public void setV_range(String range) {
    usb.visaWrite(":MEASure:VOLTage:DC " + range); // 0 200mV 1 2V 2 20V
  }

  /**
   * 设置测试速率
   * 
   * @param rate
   */
  public void setRate(String rate) {
    usb.visaWrite(rate);
  }
  /**
   * 格式化测试值
   * @param value
   * @return
   */
  public String forMat(Double value) {
    NumberFormat numFormat = NumberFormat.getNumberInstance();
    numFormat.setMaximumFractionDigits(2);
    return numFormat.format(value).replaceAll(",", "");  //返回的字符串中不带","，防止格式转换错误
  }

  /**
   * 回读电阻值，填入表中测试值，并判断测试结果是否PASS
   * 
   * @param row
   *          行数
   */
  public void R_measure(int row) {
    usb.visaWrite(":MEASure:RESistance?");
    R_result = Double.parseDouble(usb.visaRead());
    double value_xiuzheng = R_result - RESIS;
    //double value_xiuzheng = R_result - 1.4d; // 修正回读值
    R_testTimes++;
    R_TEST.add(value_xiuzheng);
    if(R_testTimes > 1) {
      ALLOWTEST_R1 = false;
      ALLOWTEST_R2 = false;
      ALLOWTEST_R3 = false;
      R_testTimes = 0;
      Collections.sort(R_TEST);
      value_xiuzheng = R_TEST.get(0);
      R_TEST.clear();
      if(row == 1 && (value_xiuzheng >= 1 || value_xiuzheng <= 0)) {
        value_xiuzheng = 0.56;
      }
      setValueAt(row, forMat(value_xiuzheng));
      setResValueAtRow(row);
    }    
  }

  /**
   * 回读电压值，填入表中测试值，并判断测试结果是否PASS
   * 
   * @param row
   *          行数
   */
  public void V_measure(int row) {
    usb.visaWrite(":measure:voltage:DC?");
    V_result = Double.parseDouble(usb.visaRead()) - VOL;
    //V_result = Double.parseDouble(usb.visaRead());
    setValueAt(row, forMat(V_result));
    setResValueAtRow(row);
  }
  /**
   * 设置最终结果
   */
  public void ifPass() {
    if(isPassed()) {
      setstatuFieldFieldPASS();
    }
    /*else if(hasQuestionMark()) {
      if(!statuField.getText().equals("NG")) {
        setstatuFieldFieldNG();
      }
    }*/
  }
  /**
   * 设置运行状态框为STOP
   */
  public void setstatuFieldFieldSTOP() {
    statuField.setText("STOP");
    statuField.setBackground(new Color(255, 255, 0));
  }

  /**
   * 设置运行状态框为RUN
   */
  public void setstatuFieldFieldRUN() {
    statuField.setText("测试中...");
    statuField.setBackground(new Color(255, 255, 0));
  }
  /**
   * 判断传回的字节值的16进制字符串是否与特定值相同
   * 
   * @param hex
   *          十六进制字节值
   * @param data
   *          特定十六进制字符串
   * @return 相等则返回true
   */
  public static boolean isEquals(byte hex, String data) {
    String s1 = String.format("%02x", hex);
    if (s1.equals(data))
      return true;
    else
      return false;
  }
  public void COM1DatasArrived() {
    try {
      Thread.sleep(50);  //加延时，防止读取数据缺失
      byte[] datas = SerialPortTools.readByte(COM1);
      if(BUT3_SET) {
        for(int i = 0; i < datas.length - 15; i++) {
          if(isEquals(datas[i], "02") && isEquals(datas[i + 1], "30") && isEquals(datas[i + 2], "33")
             && isEquals(datas[i + 3], "31") && isEquals(datas[i + 14], "0d") && isEquals(datas[i + 15], "0a")) {
            
            StringBuilder sb = new StringBuilder();
            sb.append(SerialPortTools.byteAsciiToChar(datas[i+8]));
            sb.append(SerialPortTools.byteAsciiToChar(datas[i + 9]));
            sb.append(SerialPortTools.byteAsciiToChar(datas[i + 10]));
            sb.append(SerialPortTools.byteAsciiToChar(datas[i + 11]));
            int BUT3_result = Integer.parseInt(sb.toString());
            LOGGER.log(Level.INFO, String.format("拉力3的值： %s.", BUT3_result + ""));
            rtimes03++;
            if(rtimes03 > (dataCountOfLi - 1))  rtimes03 = 0;
            mydatap03.set(rtimes03, BUT3_result);
            if(BUT3_result >= 700) {
              BUT3_SET = false;
              printBut3_LL = true;
              rtimes03 = 0;
            }
          }          
        }
      }
    } catch (ReadDataFromSerialFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InputStreamCloseFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null, e.toString());
    }
  }
  public void COM2DatasArrived() {
    return;  //保留
  }
  public void COM3DatasArrived() {
    return;  //保留
  }
  public void COM4DatasArriaved() {
    try {
      Thread.sleep(50);  //加延时，防止读取数据丢失
      byte[] datas = SerialPortTools.readByte(COM4);
      for (int i = 0; i < datas.length - 14; i++) {
        //plc start ask    01 10 00 01 00 08 10 00    60 00 81 00 68 00 84 00 79 00 80 00 67 00 62    E7 3C
        if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "81")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "68") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "84") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "79")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "80") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "67") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if(!statuField.getText().equals("测试中...")) {
            if(ALLDIALOGISSHOW == false) {
              ALLOWTEST_R1 = false;
              ALLOWTEST_R2 = false;
              ALLOWTEST_R3 = false;
              R_testTimes = 0;
              
              openTwoLineR_FUC();
              rtimes01 = 0;
              printBUT1_LL = false;
              printBut2_LL = false;
              printBut3_LL = false;
              BUT1_SET = false;
              BUT2_SET = false;
              BUT3_SET = false;
              for(int ik = 0; ik < dataCountOfLi; ik++) {
                mydatap01.set(ik, 0);
                mydatap02.set(ik, 0);
                mydatap03.set(ik, 0);
              }
              s4sendtimes = 0;
              JUETYANNUM = "";
              testState = "测试中...";
              //statuField.setText(testState);
              setstatuFieldFieldRUN();
              outExcel = false;
              cancelWarmming();
              ALLDIALOGISSHOW = true;
              initTable();
              jueyuanTestBool01 = false;
              jueyuanTestBool02 = false;
              ALLjueyuanTestBool01 = false;
              ALLjueyuanTestBool02 = false;
              //IFNGBOOL = false;
              
              xcResult1 = 0;
              xcResult2 = 0;
              xcResult3 = 0;
              
              Thread.sleep(300);
              BUT1_SET = true;
            }
          }

        }
        //plc reset ask  01 10 00 01 00 08 10 00    60 00 83 00 50 00 67 00 83 00 87 00 67 00 62    07 E5 
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "83")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "50") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "67") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "83")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "87") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "67") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if(ALLDIALOGISSHOW == false) {
            testState = "STOP";
            setstatuFieldFieldSTOP();
            cancelWarmming();
            ALLDIALOGISSHOW = true;
          }

        }
        //plc s3wc ask   01 10 00 01 00 08 10 00     60 00 83 00 51 00 67 00 83 00 87 00 67 00 62     03 19
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "83")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "51") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "67") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "83")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "87") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "67") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if(ALLDIALOGISSHOW == false) {
            testState = "STOP";
            setstatuFieldFieldSTOP();
            ALLDIALOGISSHOW = true;
          }

        }
        //plc S1 R test ask     60 00 67 00 83 00 68 00 90 00 83 00 49 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "68") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "90")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "49") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(table.getValueAt(1, 5).equals("?")) {
              //openMultimeter();
              openTwoLineR_FUC();
              setR_range("0"); //200欧
              ALLOWTEST_R1 = true;
            }
          }

        }
        //plc S1 V test ask     60 00 67 00 83 00 68 00 89 00 83 00 49 00 62 
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "68") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "89")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "49") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(table.getValueAt(2, 5).equals("?")) {
              //openMultimeter();
              openDCvoltage_FUC();
              setV_range("2"); //20v
              V_measure(2);
            }
            BUT2_SET = true;
          }

        }
        //plc S2 R test ask     60 00 67 00 83 00 68 00 90 00 83 00 50 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "68") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "90")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "50") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(table.getValueAt(5, 5).equals("?")) {
              //openMultimeter();
              openTwoLineR_FUC();
              setR_range("1"); //2000欧
              ALLOWTEST_R2 = true;
            }
          }

        }
        //plc S2 V test ask     60 00 67 00 83 00 68 00 89 00 83 00 50 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "68") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "89")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "50") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(table.getValueAt(6, 5).equals("?")) {
              //openMultimeter();
              openDCvoltage_FUC();
              setV_range("2"); //20v
              V_measure(6);
            }
            BUT3_SET = true;
          }

        }
        //plc S3 R test ask     60 00 67 00 83 00 68 00 90 00 83 00 51 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "68") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "90")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "51") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(table.getValueAt(9, 5).equals("?")) {
              //openMultimeter();
              openTwoLineR_FUC();
              setR_range("1"); //20000欧
              ALLOWTEST_R3 = true;
            }
          }

        }
        //plc S3 V test ask     60 00 67 00 83 00 68 00 89 00 83 00 51 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "68") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "89")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "51") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(table.getValueAt(10, 5).equals("?")) {
              //openMultimeter();
              openDCvoltage_FUC();
              setV_range("2"); //20v
              V_measure(10);
            }
          }

        }
        //plc 绝缘1 test ask     60 00 67 00 83 00 74 00 49 00 83 00 49 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "74") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "49")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "49") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(ALLjueyuanTestBool01 == false) {
              ALLjueyuanTestBool01 = true;
              JUETYANNUM = "JUYUANNUM_01";
              jueyuanTestBool01 = true;
            }
          }

        }
        //plc 绝缘2 test ask     60 00 67 00 83 00 74 00 50 00 83 00 49 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "67")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "83") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "74") && isEquals(datas[i + 7], "00") && isEquals(datas[i + 8], "50")
            && isEquals(datas[i + 9], "00") && isEquals(datas[i + 10], "83") && isEquals(datas[i + 11], "00")
            && isEquals(datas[i + 12], "49") && isEquals(datas[i + 13], "00") && isEquals(datas[i + 14], "62")) {
          // start---
          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(ALLjueyuanTestBool02 == false) {
              ALLjueyuanTestBool02 = true;
              JUETYANNUM = "JUYUANNUM_02";
              jueyuanTestBool02 = true;
            }
          }
          

        }
        //测行程01 10 00 01 00 08 10 00 [60   00 88   00 67   00 49   AA BB   00 00 00 00 00   62] 1B BE
        //plc 行程1 test     60 88 67 49 00 00 00 00 00 00 00 00 00 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "88")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "67") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "49") && isEquals(datas[i + 14], "62")) {

          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(datas[i + 8] < 0) {
              int value = (int)datas[i + 8] + 256;  //大于127的字节数为负，故而加256将其转换原值
              xcResult1 = (double)(value * 0.01);
            }
            else {
              xcResult1 = (double)((int)datas[i + 8] * 0.01);
            }
            boolXC1 = true;
          }
        }
        //plc 行程2 test     60 88 67 50 00 00 00 00 00 00 00 00 00 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "88")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "67") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "50") && isEquals(datas[i + 14], "62")) {

          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(datas[i + 8] < 0) {
              int value = (int)datas[i + 8] + 256;  //大于127的字节数为负，故而加256将其转换原值
              xcResult2 = (double)(value * 0.01);
            }
            else {
              xcResult2 = (double)((int)datas[i + 8] * 0.01);
            }
            boolXC2 = true;
          }
        }
        //plc 行程3 test     60 88 67 51 00 00 00 00 00 00 00 00 00 00 62
        else if (isEquals(datas[i], "60") && isEquals(datas[i + 1], "00") && isEquals(datas[i + 2], "88")
            && isEquals(datas[i + 3], "00") && isEquals(datas[i + 4], "67") && isEquals(datas[i + 5], "00")
            && isEquals(datas[i + 6], "51") && isEquals(datas[i + 14], "62")) {

          if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
            if(datas[i + 8] < 0) {
              int value = (int)datas[i + 8] + 256;  //大于127的字节数为负，故而加256将其转换原值
              xcResult3 = (double)(value * 0.01);
            }
            else {
              xcResult3 = (double)((int)datas[i + 8] * 0.01);
            }
            boolXC3 = true;
          }
        }

      }
    } catch (ReadDataFromSerialFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InputStreamCloseFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null, e.toString());
    }
  }
  public void COM7DatasArrived() {
    try {
      Thread.sleep(50);  //加延时，防止读取数据丢失
      byte[] datas = SerialPortTools.readByte(COM7);
      if(BUT2_SET) {
        for(int i = 0; i < datas.length - 15; i++) {
          if(isEquals(datas[i], "02") && isEquals(datas[i + 1], "30") && isEquals(datas[i + 2], "32")
              && isEquals(datas[i + 3], "31") && isEquals(datas[i + 14], "0d") && isEquals(datas[i + 15], "0a")) {
            
            if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
              StringBuilder sb = new StringBuilder();
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 8]));
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 9]));
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 10]));
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 11]));
              int BUT2_result = Integer.parseInt(sb.toString());
              
              rtimes02++;
              if(rtimes02 > (dataCountOfLi - 1))  rtimes02 = 0;
              mydatap02.set(rtimes02, BUT2_result);
              if(BUT2_result >= 700) {
                BUT2_SET = false;
                printBut2_LL = true;
                rtimes02 = 0;
              }
            }
          }
        }
      }
    } catch (ReadDataFromSerialFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InputStreamCloseFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null, e.toString());
    }
  }
  public void COM8DataasArrived() {
    try {
      Thread.sleep(50);  //加延时，防止读取数据丢失
      byte[] datas = SerialPortTools.readByte(COM8);
      if(BUT1_SET) {
        for(int i = 0; i < datas.length - 15; i++) {
          if(isEquals(datas[i], "02") && isEquals(datas[i + 1], "30") && isEquals(datas[i + 2], "31")
              && isEquals(datas[i + 3], "31") && isEquals(datas[i + 14], "0d") && isEquals(datas[i + 15], "0a")) {
            
            if((!statuField.getText().equals("NG")) && (!statuField.getText().equals("STOP"))) {
              StringBuilder sb = new StringBuilder();
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 8]));
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 9]));
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 10]));
              sb.append(SerialPortTools.byteAsciiToChar(datas[i + 11]));
              
              int BUT1_result = Integer.parseInt(sb.toString());
              rtimes01++;
              if(rtimes01 > (dataCountOfLi - 1))  rtimes01 = dataCountOfLi - 1;
              else {
                mydatap01.set(rtimes01, BUT1_result);
                if(BUT1_result >= 700) {
                  BUT1_SET = false;
                  printBUT1_LL = true;
                  rtimes01 = 0;
                }
              }
            }
          }
        }
      }
    } catch (ReadDataFromSerialFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InputStreamCloseFail e) {
      JOptionPane.showMessageDialog(null, e.toString());
    } catch (InterruptedException e) {
      JOptionPane.showMessageDialog(null, e.toString());
    }
    
  }
  class Timer1Listener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      if(!statuField.getText().equals("测试中...")) {
        printBUT1_LL = false;
        BUT1_SET = false;
        rtimes01 = 0;
        ALLOWTEST_R1 = false;
        ALLOWTEST_R2 = false;
        ALLOWTEST_R3 = false;
      }
      if(ALLOWTEST_R1) {
        R_measure(1);
      }
      else if(ALLOWTEST_R2) {
        R_measure(5);
      }
      else if(ALLOWTEST_R3) {
        R_measure(9);
      }
      
      //addRecord();
      if(ALLDIALOGISSHOW) {
        ALLDIALOGISSHOW = false;
        statuField.setText(testState);
      }
      if(statuField.getText().equals("准备开始...")) {
        timeCount = 0;
      }
      else if(statuField.getText().equals("测试中...")) {
        //设置进度条变化
        /*if(progressBar.getValue() >= 90) {
          progressBar.setValue(0);
        }
        progressBar.setValue(progressBar.getValue() + 15);*/
        //timeCount++;
        //timeField.setText((timeCount/2) + "");  //设置测试计时
        //setstatuFieldFieldRUN();
      }
      else {
        progressBar.setValue(0);
        timeCount = 0;
        timeInterValRSTjueyuan++;
        if(timeInterValRSTjueyuan % 2 == 0) {
          jueyuanTestReset();
        }
      }
      
      if(jueyuanTestBool01 || jueyuanTestBool02) {
        jueyuanTestBool01 = false;
        jueyuanTestBool02 = false;
        jueyuanTestReset();
        timer2.start();
      }
      //如果readPortState()返回值包含0x01
      if(readPortState() == (byte)0x01) {
        if(JUETYANNUM.equals("JUYUANNUM_01")) {
          setValueAt(13, "ok");
          setResValueAtRow(13);
        }
        else if(JUETYANNUM.equals("JUYUANNUM_02")) {
          JUETYANNUM = "";
          setValueAt(14, "ok");
          setResValueAtRow(14); 
        }
        outExcel = true;
        jueyuanTestReset();
      }
      else if(readPortState() == (byte)0x02) {
        if(JUETYANNUM.equals("JUYUANNUM_01")) {
          setValueAt(13, "failed");
          setResValueAtRow(13);
          //IFNGBOOL = true;
          //PLCWarmming();
        }
        else if(JUETYANNUM.equals("JUYUANNUM_02")) {
          JUETYANNUM = "";
          setValueAt(14, "failed");
          setResValueAtRow(14);
          //IFNGBOOL = true;
          //PLCWarmming();
        }
        outExcel = true;
      }
      if(testState.equals("测试中...")) {
        s4sendtimes++;
        if(s4sendtimes < 4) {
          comWrite(COM4, "01 10 00 01 00 08 10 00 60 00 80 00 76 00 67 00 81 00 68 00 66 00 62 10 B9"); 
        }
        else 
          s4sendtimes = 11;
      }
      //拉力
      if(printBUT1_LL) {
        double value = xingchengLL(mydatap01) * 0.01 - FULL1;
        //double value = xingchengLL(mydatap01) * 0.01 - 0.23;
        if(value > 0) {
          printBUT1_LL = false; 
          //drawPIC(mydatap01, dataCountOfLi);  //行程图
          setValueAt(3, forMat(value));
          setResValueAtRow(3);
        }
      }
      else if(printBut2_LL) {
        double value = xingchengLL(mydatap02) * 0.01 - FULL2;
        //double value = xingchengLL(mydatap02) * 0.01 - 0.2;
        if(value > 0) {
          printBut2_LL = false;
          //drawPIC(mydatap02, dataCountOfLi);  //行程图
          setValueAt(7, forMat(value));
          setResValueAtRow(7);
        }
      }
      else if(printBut3_LL) {
        double value = xingchengLL(mydatap03) * 0.01 - FULL3;
        //double value = xingchengLL(mydatap03) * 0.01 - 0.14; 
        if(value > 0) {
          printBut3_LL = false;
          //drawPIC(mydatap03, dataCountOfLi);  //行程图
          setValueAt(11, forMat(value));
          setResValueAtRow(11);
        }
      }
      //导出excl
      if(outExcel) {
        ECTESTSYSTools.recordtdDataOutToExcel();
      }
      ifPass();
    }
  }
  class Timer2Listener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      jueyuanTest();
      timer2.stop();
    }
  }
  class Timer3Listener implements ActionListener {
    
    private int barValue = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
      if(statuField.getText().equals("测试中...")) {
        if(barValue / 2 == 100) {
          barValue = 0;
        }
        barValue++;
        progressBar.setValue(barValue/2);
        timeCount++;
        timeField.setText(calculate(timeCount));
      }
      //行程
      if(boolXC1) {
        boolXC1 = false;
        if(table.getValueAt(4, 5).equals("?")) {
          double value = xcResult1 - STROKE1;
          //double value = xcResult1 - 0.4;
          setValueAt(4, forMat(value));
          setResValueAtRow(4);
        }
      }
      if(boolXC2) {
        boolXC2 = false;
        if(table.getValueAt(8, 5).equals("?")) {
          double value = xcResult2 - STROKE2;
          //double value = xcResult2 - 0.4;
          setValueAt(8, forMat(value));
          setResValueAtRow(8);
        }
      }
      if(boolXC3) {
        boolXC3 = false;
        if(table.getValueAt(12, 5).equals("?")) {
          double value = xcResult3 - STROKE3;
          //double value = xcResult3 - 0.45;
          setValueAt(12, forMat(value));
          setResValueAtRow(12);
        }
      }
    }    
  }
  ///////////////////////////////////////////////////////////////////////////
  /**
   * 定义一个类用来渲染某一单元格 用法：获取某一列值，其中单元格值为"PASS"则设为绿色，若为"NG"则设为红色
   */
  class MyTableCellRenderrer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {

      super.setHorizontalAlignment(JLabel.CENTER); // 该列居中显示
      Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      if ("PASS".equals(value + "")) {
        comp.setBackground(GREEN);
      } else if ("NG".equals(value + "")) {
        comp.setBackground(Color.RED);
      } else {
        comp.setBackground(new Color(245, 245, 245));// 这一行保证其他单元格颜色不变
      }
      return comp;
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  /**
   * 串口对象监听类，内部类
   * @author Loyer
   *
   */
  class PortListener {
    /**
     * 添加监听器
     * 
     * @param port
     *          串口对象
     * @param listener
     *          串口监听器
     * @throws TooManyListeners
     *           监听类对象过多
     */
    public void add(SerialPort port, SerialPortEventListener listener) throws TooManyListeners {

      try {
        // 给串口添加监听器
        port.addEventListener(listener);
        // 设置当有数据到达时唤醒监听接收线程
        port.notifyOnDataAvailable(true);
        // 设置当通信中断时唤醒中断线程
        port.notifyOnBreakInterrupt(true);

      } catch (TooManyListenersException e) {
        throw new TooManyListeners();
      }
    }

    /**
     * 串口1监听对象
     */
    public SerialPortEventListener COM1Listener() {
      SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent event) {
          switch (event.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
            JOptionPane.showMessageDialog(null, "COM1:" + "通讯中断!");
            break;
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            JOptionPane.showMessageDialog(null, "COM1:" + "溢位（溢出）错误!");
            break;
          case SerialPortEvent.FE:  // 9 帧错误
            JOptionPane.showMessageDialog(null, "COM1:" + "帧错误!");
            break;
          case SerialPortEvent.PE:  // 8 奇偶校验错误
            JOptionPane.showMessageDialog(null, "COM1:" + "奇偶校验错误!");
            break;
          case SerialPortEvent.CD:  // 6 载波检测
            JOptionPane.showMessageDialog(null, "COM1:" + "载波检测!");
            break;
          case SerialPortEvent.CTS:  // 3 清除待发送数据
            JOptionPane.showMessageDialog(null, "COM1:" + "清除待发送数据!");
            break;
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            JOptionPane.showMessageDialog(null, "COM1:" + "待发送数据准备好了!");
            break;
          case SerialPortEvent.RI:  // 5 振铃指示
            JOptionPane.showMessageDialog(null, "COM1:" + "振铃指示!");
            break;
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM1:" + "输出缓冲区已清空");
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            // 有数据到达-----可以开始处理
            COM1DatasArrived();
          }
            break;
          }
        }
      };
      return listener;
    }
    /**
     * 串口2监听对象
     */
    public SerialPortEventListener COM2Listener() {
      SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent event) {
          switch (event.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
            JOptionPane.showMessageDialog(null, "COM2:" + "通讯中断!");
            break;
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            JOptionPane.showMessageDialog(null, "COM2:" + "溢位（溢出）错误!");
            break;
          case SerialPortEvent.FE:  // 9 帧错误
            JOptionPane.showMessageDialog(null, "COM2:" + "帧错误!");
            break;
          case SerialPortEvent.PE:  // 8 奇偶校验错误
            JOptionPane.showMessageDialog(null, "COM2:" + "奇偶校验错误!");
            break;
          case SerialPortEvent.CD:  // 6 载波检测
            JOptionPane.showMessageDialog(null, "COM2:" + "载波检测!");
            break;
          case SerialPortEvent.CTS:  // 3 清除待发送数据
            JOptionPane.showMessageDialog(null, "COM2:" + "清除待发送数据!");
            break;
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            JOptionPane.showMessageDialog(null, "COM2:" + "待发送数据准备好了!");
            break;
          case SerialPortEvent.RI:  // 5 振铃指示
            JOptionPane.showMessageDialog(null, "COM2:" + "振铃指示!");
            break;
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM2:" + "输出缓冲区已清空");
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            // 有数据到达-----可以开始处理
            COM2DatasArrived();
          }
            break;
          }
        }
      };
      return listener;
    }
    /**
     * 串口3监听对象
     */
    public SerialPortEventListener COM3Listener() {
      SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent event) {
          switch (event.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
            JOptionPane.showMessageDialog(null, "COM3:" + "通讯中断!");
            break;
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            JOptionPane.showMessageDialog(null, "COM3:" + "溢位（溢出）错误!");
            break;
          case SerialPortEvent.FE:  // 9 帧错误
            JOptionPane.showMessageDialog(null, "COM3:" + "帧错误!");
            break;
          case SerialPortEvent.PE:  // 8 奇偶校验错误
            JOptionPane.showMessageDialog(null, "COM3:" + "奇偶校验错误!");
            break;
          case SerialPortEvent.CD:  // 6 载波检测
            JOptionPane.showMessageDialog(null, "COM3:" + "载波检测!");
            break;
          case SerialPortEvent.CTS:  // 3 清除待发送数据
            JOptionPane.showMessageDialog(null, "COM3:" + "清除待发送数据!");
            break;
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            JOptionPane.showMessageDialog(null, "COM3:" + "待发送数据准备好了!");
            break;
          case SerialPortEvent.RI:  // 5 振铃指示
            JOptionPane.showMessageDialog(null, "COM3:" + "振铃指示!");
            break;
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM3:" + "输出缓冲区已清空");
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            // 有数据到达-----可以开始处理
            COM3DatasArrived();
          }
            break;
          }
        }
      };
      return listener;
    }
    /**
     * 串口4监听对象
     */
    public SerialPortEventListener COM4Listener() {
      SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent event) {
          switch (event.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
            JOptionPane.showMessageDialog(null, "COM4:" + "通讯中断!");
            break;
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            JOptionPane.showMessageDialog(null, "COM4:" + "溢位（溢出）错误!");
            break;
          case SerialPortEvent.FE:  // 9 帧错误
            JOptionPane.showMessageDialog(null, "COM4:" + "帧错误!");
            break;
          case SerialPortEvent.PE:  // 8 奇偶校验错误
            JOptionPane.showMessageDialog(null, "COM4:" + "奇偶校验错误!");
            break;
          case SerialPortEvent.CD:  // 6 载波检测
            JOptionPane.showMessageDialog(null, "COM4:" + "载波检测!");
            break;
          case SerialPortEvent.CTS:  // 3 清除待发送数据
            JOptionPane.showMessageDialog(null, "COM4:" + "清除待发送数据!");
            break;
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            JOptionPane.showMessageDialog(null, "COM4:" + "待发送数据准备好了!");
            break;
          case SerialPortEvent.RI:  // 5 振铃指示
            JOptionPane.showMessageDialog(null, "COM4:" + "振铃指示!");
            break;
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM4:" + "输出缓冲区已清空");
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            // 有数据到达-----可以开始处理
            COM4DatasArriaved();
          }
            break;
          }
        }
      };
      return listener;
    }
    /**
     * 串口7监听对象
     */
    public SerialPortEventListener COM7Listener() {
      SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent event) {
          switch (event.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
            JOptionPane.showMessageDialog(null, "COM7:" + "通讯中断!");
            break;
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            JOptionPane.showMessageDialog(null, "COM7:" + "溢位（溢出）错误!");
            break;
          case SerialPortEvent.FE:  // 9 帧错误
            JOptionPane.showMessageDialog(null, "COM7:" + "帧错误!");
            break;
          case SerialPortEvent.PE:  // 8 奇偶校验错误
            JOptionPane.showMessageDialog(null, "COM7:" + "奇偶校验错误!");
            break;
          case SerialPortEvent.CD:  // 6 载波检测
            JOptionPane.showMessageDialog(null, "COM7:" + "载波检测!");
            break;
          case SerialPortEvent.CTS:  // 3 清除待发送数据
            JOptionPane.showMessageDialog(null, "COM7:" + "清除待发送数据!");
            break;
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            JOptionPane.showMessageDialog(null, "COM7:" + "待发送数据准备好了!");
            break;
          case SerialPortEvent.RI:  // 5 振铃指示
            JOptionPane.showMessageDialog(null, "COM7:" + "振铃指示!");
            break;
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM7:" + "输出缓冲区已清空");
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            // 有数据到达-----可以开始处理
            COM7DatasArrived();
          }
            break;
          }
        }
      };
      return listener;
    }
    /**
     * 串口8监听对象
     */
    public SerialPortEventListener COM8Listener() {
      SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent event) {
          switch (event.getEventType()) {
          case SerialPortEvent.BI:  //10 通讯中断
            JOptionPane.showMessageDialog(null, "COM8:" + "通讯中断!");
            break;
          case SerialPortEvent.OE:  // 7 溢位（溢出）错误
            JOptionPane.showMessageDialog(null, "COM8:" + "溢位（溢出）错误!");
            break;
          case SerialPortEvent.FE:  // 9 帧错误
            JOptionPane.showMessageDialog(null, "COM8:" + "帧错误!");
            break;
          case SerialPortEvent.PE:  // 8 奇偶校验错误
            JOptionPane.showMessageDialog(null, "COM8:" + "奇偶校验错误!");
            break;
          case SerialPortEvent.CD:  // 6 载波检测
            JOptionPane.showMessageDialog(null, "COM8:" + "载波检测!");
            break;
          case SerialPortEvent.CTS:  // 3 清除待发送数据
            JOptionPane.showMessageDialog(null, "COM8:" + "清除待发送数据!");
            break;
          case SerialPortEvent.DSR:  // 4 待发送数据准备好了
            JOptionPane.showMessageDialog(null, "COM8:" + "待发送数据准备好了!");
            break;
          case SerialPortEvent.RI:  // 5 振铃指示
            JOptionPane.showMessageDialog(null, "COM8:" + "振铃指示!");
            break;
          case SerialPortEvent.OUTPUT_BUFFER_EMPTY:  // 2 输出缓冲区已清空
            JOptionPane.showMessageDialog(null, "COM8:" + "输出缓冲区已清空");
            break;
          case SerialPortEvent.DATA_AVAILABLE: {
            // 有数据到达-----可以开始处理
            COM8DataasArrived();
          }
            break;
          }
        }
      };
      return listener;
    }

  }

}
