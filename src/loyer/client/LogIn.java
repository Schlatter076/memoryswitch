package loyer.client;
import java.awt.EventQueue;

import loyer.db.ProductData;
import loyer.db.ProductTools;
import loyer.gui.LogInFrame;

public class LogIn extends LogInFrame {
  
  private static ProductData c211;
  private static ProductData nl3b;
  
  
  static { //加载本类时先加载产品型号
    c211 = ProductTools.getByNum(1);
    nl3b = ProductTools.getByNum(2);
  }
  public LogIn() {
    super();
    textField.setText(c211.getProductName());
  }

  @Override
  public void logInEvent() {
    if(!isDataView) {
      if(textField.getText().equals(c211.getProductName())) {
        isDataView = true;
        this.frame.dispose();
        C211DataView.dataView();
      } else if(textField.getText().equals(nl3b.getProductName())) {
        isDataView = true;
        this.frame.dispose();
        NL3BDataView.dataView();
      } else {
        isDataView = false;
      }
    }
  }

  @Override
  public void chooseEvent() {
    if(textField.getText().equals(c211.getProductName())) {
      textField.setText(nl3b.getProductName());
    }
    else
      textField.setText(c211.getProductName());
  }

  public static void main(String[] args) {

    EventQueue.invokeLater(new Runnable() {
      
      @Override
      public void run() {
        LogIn window = new LogIn();
        window.frame.setVisible(true);
      }
    });
  }

}
