package loyer.serial;

import java.io.BufferedReader;
import java.io.InputStream;

import javax.swing.JOptionPane;

import gnu.io.SerialPortEvent;

public class CallBack extends AbstractReadCallback {

  @Override
  public void call(BufferedReader reader, InputStream is) {
    try {
      char[] buff = new char[1024];
      reader.read(buff);
      String str = new String(buff).trim();
      addResult(str);
      /**
        方式一：
        char[] buff=new char[1024];
        reader.read(buff);
        String c=new String(buff).trim();
        addResult(c);
       
        方式二：
        String result = reader.readLine();
        addResult(result.trim());
        
       
       */
      
      //方式三 对于一些乱码的情况，需要进行字符集转换
      
      /*String result=reader.readLine();
      result=new String(result.getBytes("GBK"),"GBK");
      addResult(result);*/
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onError(SerialPortEvent event) {
    JOptionPane.showMessageDialog(null, "出错了，出错类型是 '" + event.getEventType() + "'", "错误", JOptionPane.ERROR_MESSAGE);
  }
}
