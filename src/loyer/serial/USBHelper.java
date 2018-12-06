package loyer.serial;

import visatype.VisatypeLibrary;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.testng.Assert.*;
import jvisa.JVisa;
import jvisa.JVisaException;
import jvisa.JVisaReturnNumber;
import jvisa.JVisaReturnString;
import visa.VisaLibrary;

/**
 * 通过USB连接仪器的一些操作方法
 * @author hw076
 *
 */
public class USBHelper {
  static JVisa instance;
  long viSession = 0;
  long viInstrument = 0;
  long timeOut = 2000;
  String read;
 
  protected static final String CLASS_NAME = USBHelper.class.getName();
 
  public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
  
  static final String LOG_PATH = "log/usblog.txt";
  
  static File logFile; 
  
  /**
   * 获取默认资源
   * @throws Exception
   */
  public static void setUpClass() throws Exception{
    instance = new JVisa();
    assertEquals(instance.openDefaultResourceManager(), VisatypeLibrary.VI_SUCCESS);
    logFile = new File(LOG_PATH);
  }
  /**
   * 设置日志文件
   */
  public void setLogFile() {
    if(logFile.exists()) {
      logFile.delete();
    }
    instance.setLogFile(LOG_PATH);
    JVisa.logFileHandler.setFormatter(new SimpleFormatter());
    JVisa.LOGGER.setLevel(Level.FINE);
  }
  /**
   * 获取visa资源处理
   */
  public void getVisaResourceManagerHandle() {
    viSession = instance.getResourceManagerHandle();
    assertTrue(viSession > 0L, String.format("Resource manager handle is 0x%08X", viSession));
  }
  /**
   * 通过资源名称打开仪器连接
   * @param instrument 资源名称
   */
  public void openInstrument(String instrument) {
    try {
      long expResult = VisatypeLibrary.VI_SUCCESS;
      long result = instance.openInstrument(instrument);
      assertEquals(result, expResult);
      viInstrument = instance.getVisaInstrumentHandle();
      
    } catch(Exception e) {
      LOGGER.log(Level.SEVERE, null, e);
    }
    
  }
  /**
   *清除资源
   */
  public void visaClear() {
    try {
      long expResult = 0L;
      assertEquals(instance.clear(), expResult);
    } catch(Exception e) {
      LOGGER.log(Level.SEVERE, null, e);
    }
  }
  /**
   * 设置超时
   */
  public void setTimeout() {
    JVisaReturnNumber timeoutReturn = new JVisaReturnNumber((int) 0);
    int timeoutOriginal, timeoutNew = 5123;
    long expResult = VisatypeLibrary.VI_SUCCESS;

    long result = instance.getAttribute(VisaLibrary.VI_ATTR_TMO_VALUE, timeoutReturn, viInstrument);
    assertEquals(result, expResult);
    timeoutOriginal = timeoutReturn.returnNumber.intValue();
    assertEquals(timeoutOriginal, timeOut);

    result = instance.setAttribute(VisaLibrary.VI_ATTR_TMO_VALUE, timeoutNew, viInstrument);
    assertEquals(result, expResult);

    result = instance.getAttribute(VisaLibrary.VI_ATTR_TMO_VALUE, timeoutReturn, viInstrument);
    assertEquals(result, expResult);
    assertEquals(timeoutReturn.returnNumber.intValue(), timeoutNew);

    result = instance.setAttribute(VisaLibrary.VI_ATTR_TMO_VALUE, timeoutOriginal, viInstrument);
    assertEquals(result, expResult);
  }
  /**
   * 获取资源处理
   */
  public void getVisaInstrumentHandle() {
    assertTrue(instance.getVisaInstrumentHandle() > 0L);
  }
  /**
   * 发送指令
   * @param command  指令
   */
  public void visaWrite(String command) {
    long expResult = VisatypeLibrary.VI_SUCCESS;
    try {
      assertEquals(instance.write(command), expResult);
    } catch(JVisaException e) {
      LOGGER.log(Level.SEVERE, null, e);
    }
  }
  /**
   * 读取缓冲区的值
   * @return
   */
  public String visaRead() {
    try {
      JVisaReturnString response = new JVisaReturnString();
      long expResult = VisatypeLibrary.VI_SUCCESS;
      long result = instance.read(response);
      assertEquals(result, expResult);
      read = response.returnString;
    } catch(JVisaException e) {
      LOGGER.log(Level.SEVERE, null, e);
      fail(e.getMessage());
    }
    return read;
  }
  
}