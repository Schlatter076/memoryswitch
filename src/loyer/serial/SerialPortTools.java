package loyer.serial;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.swing.JOptionPane;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import loyer.db.DBHelper;
import loyer.exception.InputStreamCloseFail;
import loyer.exception.NoSuchPort;
import loyer.exception.NotASerialPort;
import loyer.exception.OutputStreamCloseFail;
import loyer.exception.PortInUse;
import loyer.exception.ReadDataFromSerialFail;
import loyer.exception.SendToPortFail;
import loyer.exception.SerialPortParamFail;
import loyer.exception.TooManyListeners;

public class SerialPortTools implements SerialPortEventListener {

  private static SerialPortTools spTool = null;
  private static BufferedReader reader;
  private static PrintWriter writer;
  
  private AbstractReadCallback callback;
  private static InputStream is;
  

  static {
    // 初始化时创建一个串口对象
    if (spTool == null)
      spTool = new SerialPortTools();
  }

  private SerialPortTools() {
  } // 不允许其他类创建该类对象

  /**
   * 获取串口工具对象
   * 
   * @return 串口工具对象
   */
  public static SerialPortTools getSerialPortTool() {
    if (spTool == null) {
      spTool = new SerialPortTools();
    }
    return spTool;
  }

  /**
   * 查找可用端口
   * 
   * @return 可用端口名称列表
   */
  public static final ArrayList<String> findPort() {

    // 获取当前所有可用串口
    @SuppressWarnings("unchecked")
    Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
    ArrayList<String> portNameList = new ArrayList<>(); // 用来存储串口名
    while (portList.hasMoreElements()) {
      String portName = portList.nextElement().getName();
      portNameList.add(portName);
    }
    return portNameList;
  }

  public static SerialPortData getPortByDB(int number) {
    
    SerialPortData data = null;
    String sql = "select * from serialports where xuhao='"+number+"'";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      if(rs.next()) {
        data = new SerialPortData(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "串口参数获取失败：" + e.getLocalizedMessage());
    }
    return data;
    
  }
  /**
   * 通过序号获取串口
   * 
   * @param xuhao
   *          1-->COM1, 2-->COM2
   * @return 串口对象
   * @throws SerialPortParamFail
   * @throws NotASerialPort
   * @throws NoSuchPort
   * @throws PortInUse
   */
  public static SerialPort getPort(int xuhao) throws SerialPortParamFail, NotASerialPort, NoSuchPort, PortInUse {
    
    SerialPortData myPort = getPortByDB(xuhao);
    
    try {
      CommPortIdentifier comm = CommPortIdentifier.getPortIdentifier(myPort.getPortName()); // 通过端口名识别端口
      CommPort commPort = comm.open(myPort.getPortName(), 2000); // 打开端口，设置超时
      // 如果是串口
      if (commPort instanceof SerialPort) {
        SerialPort port = (SerialPort) commPort;
        try {
          port.setSerialPortParams(myPort.getBaudRate(), myPort.getDataBits(), myPort.getStopBits(), myPort.getParity());
        } catch (UnsupportedCommOperationException e) {
          throw new SerialPortParamFail();
        }
        return port;
      } else {
        throw new NotASerialPort(); // 不是串口
      }
    } catch (NoSuchPortException e) {
      throw new NoSuchPort();
    } catch (PortInUseException ex) {
      throw new PortInUse();
    }
  }

  /**
   * 重载获取串口方法
   * 
   * @param sPort
   *          串口号
   * @param baudRate
   *          波特率
   * @param dataBits
   *          数据位
   * @param stopBits
   *          停止位
   * @param parity
   *          校验位(1->奇校验，2->偶校验，0->无校验)
   * @return 当前串口
   * @throws SerialPortParamFail
   * @throws NotASerialPort
   * @throws NoSuchPort
   * @throws PortInUse
   */
  public static SerialPort getPort(String portName, int baudRate, int dataBits, int stopBits, int parity)
      throws SerialPortParamFail, NotASerialPort, NoSuchPort, PortInUse {

    try {
      CommPortIdentifier comm = CommPortIdentifier.getPortIdentifier(portName); // 通过端口名识别端口
      CommPort commPort = comm.open(portName, 2000); // 打开端口，设置超时
      // 如果是串口
      if (commPort instanceof SerialPort) {
        SerialPort port = (SerialPort) commPort;
        try {
          port.setSerialPortParams(baudRate, dataBits, stopBits, parity);
        } catch (UnsupportedCommOperationException e) {
          throw new SerialPortParamFail();
        }
        return port;
      } else {
        throw new NotASerialPort(); // 不是串口
      }
    } catch (NoSuchPortException e) {
      throw new NoSuchPort();
    } catch (PortInUseException ex) {
      throw new PortInUse();
    }
  }

  /**
   * 向串口发送数据
   * 
   * @param command
   *          待发送的命令
   * @return 如果成功返回true
   */
  public static boolean write(SerialPort sPort, String command) {
    try {
      writer = new PrintWriter(new OutputStreamWriter(sPort.getOutputStream(), "UTF-8"), true);
      writer.println(command);
      return true;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 从串口接收
   * 
   * @param sPort
   * @param callback
   * @param charset
   * @return 串口工具对象
   */
  private SerialPortTools read(SerialPort sPort, AbstractReadCallback callback, Charset charset) {
    try {
      // AbstractReadCallback cb = callback;
      this.callback = callback;
      reader = new BufferedReader(new InputStreamReader(sPort.getInputStream(), charset));
      is = new BufferedInputStream(sPort.getInputStream());
      sPort.addEventListener(this);
      sPort.notifyOnDataAvailable(true);
      return this;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TooManyListenersException e) {
      e.printStackTrace();
    }
    return this;
  }

  /**
   * 重载从串口接收方法
   * 
   * @param sPort
   * @return 获取到的值(String类型)
   */
  public static String read(SerialPort sPort) {
    CallBack cback = new CallBack();
    getSerialPortTool().read(sPort, cback, Charset.forName("UTF-8"));
    return cback.getResult();
  }
  /**
   * 获取十六进制整数的ASCII码字符
   * @param ascii
   * @return
   */
  public static char byteAsciiToChar(int ascii){
    char ch = (char)ascii;
    return ch;
  }
  /**
   * 16进制的字符串表示转成字节数组
   * 
   * @param hexString
   *          16进制格式的字符串
   * @return 转换后的字节数组
   **/
  public static byte[] toByteArray(String hexString) {

    hexString = hexString.replaceAll(" ", ""); // 去掉字符串中所有空格
    hexString = hexString.toLowerCase();
    int len = 0;
    if(hexString.length() % 2 == 0) {
      len = hexString.length() / 2;
    }
    else {
      len = hexString.length() /2 + 1;
    }
    byte[] byteArray = new byte[len];
    int k = 0;
    for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
      byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
      byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
      byteArray[i] = (byte)(high << 4 | low);
      k += 2;
    }
    return byteArray;
  }
  public static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(bytes[i] & 0xFF);
      if (hex.length() < 2) {
        sb.append(0);
      }
      sb.append(hex);
      sb.append(' ');
    }
    return sb.toString();
  }
  /**
   * 往串口发送数据
   * 
   * @param serialPort
   *          串口对象
   * @param order
   *          待发送数据
   * @throws SendToPortFail
   *           向串口发送数据失败
   * @throws OutputStreamCloseFail
   *           关闭串口对象的输出流出错
   */
  public static void write(SerialPort serialPort, byte[] order) throws SendToPortFail, OutputStreamCloseFail {

    OutputStream out = null;
    try {
      out = serialPort.getOutputStream();
      out.write(order);
      out.flush();
    } catch (IOException e) {
      throw new SendToPortFail();
    } finally {
      try {
        if (out != null) {
          out.close();
          out = null;
        }
      } catch (IOException e) {
        throw new OutputStreamCloseFail();
      }
    }

  }

  /**
   * 从串口读取数据
   * 
   * @param serialPort
   *          当前已建立连接的SerialPort对象
   * @return 读取到的数据
   * @throws ReadDataFromSerialFail
   *           从串口读取数据时出错
   * @throws InputStreamCloseFail
   *           关闭串口对象输入流出错
   */
  public static byte[] readByte(SerialPort serialPort) throws ReadDataFromSerialFail, InputStreamCloseFail {
    InputStream in = null;
    byte[] bytes = null;
    try {
      in = serialPort.getInputStream();
      int bufflenth = in.available(); // 获取buffer里的数据长度
      while (bufflenth != 0) {
        bytes = new byte[bufflenth]; // 初始化byte数组为buffer中数据的长度
        in.read(bytes);
        bufflenth = in.available();
      }
    } catch (IOException e) {
      throw new ReadDataFromSerialFail();
    } finally {
      try {
        if (in != null) {
          in.close();
          in = null;
        }
      } catch (IOException e) {
        throw new InputStreamCloseFail();
      }

    }
    return bytes;
  }
  public static byte read_byte(SerialPort port) throws ReadDataFromSerialFail, InputStreamCloseFail {
    InputStream in = null;
    byte data = 0;
    try {
      in = port.getInputStream();
      data = (byte) in.read();
    } catch (IOException e) {
      throw new ReadDataFromSerialFail();
    } finally {
      try {
        if (in != null) {
          in.close();
          in = null;
        }
      } catch (IOException e) {
        throw new InputStreamCloseFail();
      }
    }
    return data;
  }
  /**
   * 重载串口写操作
   * @param port 串口对象
   * @param datas 待发送字符数组
   * @throws SendToPortFail
   * @throws OutputStreamCloseFail
   */
  public static void write(SerialPort port, char[] datas) throws SendToPortFail, OutputStreamCloseFail {
    BufferedWriter buffWriter = null;
    try {
      buffWriter = new BufferedWriter(new OutputStreamWriter(port.getOutputStream(), Charset.forName("UTF-8")));
      buffWriter.write(datas);
      buffWriter.flush();
    } catch(IOException e) {
      throw new SendToPortFail();
    } finally {
      try {
        if(buffWriter != null) {
          buffWriter.close();
          buffWriter = null;
        }
      } catch(IOException e) {
        throw new OutputStreamCloseFail();
      }
    }
    
  }
  /**
   * 从串口接收字符数组
   * @param port 串口对象
   * @return 字符数组
   * @throws ReadDataFromSerialFail
   * @throws InputStreamCloseFail
   */
  public static char[] readChar(SerialPort port) throws ReadDataFromSerialFail, InputStreamCloseFail {
    BufferedReader buffReader = null;
    char[] result = null;
    try {
      buffReader = new BufferedReader(new InputStreamReader(port.getInputStream(), Charset.forName("UTF-8")));
      char[] temp = new char[1024];
      int length = buffReader.read(temp); 
      result = new char[length];
      for(int i = 0; i < length; i++) {
        result[i] = temp[i];
      }
      return result;
    } catch(IOException e) {
      throw new ReadDataFromSerialFail();
    } finally {
      try {
        if(buffReader != null) {
          buffReader.close();
          buffReader = null;
        }
      } catch(IOException e) {
        throw new InputStreamCloseFail();
      }
    }
  }

  @Override
  public void serialEvent(SerialPortEvent event) {
    switch (event.getEventType()) {
    case SerialPortEvent.BI:
    case SerialPortEvent.OE:
    case SerialPortEvent.FE:
    case SerialPortEvent.PE:
    case SerialPortEvent.CD:
    case SerialPortEvent.CTS:
    case SerialPortEvent.DSR:
    case SerialPortEvent.RI:
    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
      callback.onError(event);
      break;
    case SerialPortEvent.DATA_AVAILABLE:
      synchronized (reader) {
        // 数据到达，回调
        callback.call(reader, is);
      }
    }
  }
  public static void add(SerialPort port, SerialPortEventListener listener) throws TooManyListeners {

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
   * 关闭串口
   * 
   * @param serialPort
   *          串口对象
   */
  public static void closePort(SerialPort serialPort) {
    if (serialPort != null) {
      serialPort.close();
      serialPort = null;
    }
  }
}