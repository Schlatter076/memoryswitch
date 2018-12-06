package loyer.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * 产品型号工具类
 * @author hw076
 *
 */
public class ProductTools {

  private ProductTools() {}  //不允许其他类创建本类实例
  /**
   * 从数据库加载所有的产品型号
   * @return
   */
  public static ArrayList<ProductData> getAllbyDB() {
    ArrayList<ProductData> list = new ArrayList<>();
    String sql = "select * from product_type";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      while(rs.next()) {
        int num = rs.getInt(1);
        String name = rs.getString(2);
        
        list.add(new ProductData(num, name));
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "产品型号加载失败:" + e.getLocalizedMessage(), "警告", JOptionPane.ERROR_MESSAGE);
    } finally {
      DBHelper.close();
    }
    return list;
  }
  /**
   * 通过序号获取产品型号
   * @param num
   * @return
   */
  public static ProductData getByNum(int num) {
    ProductData data = null;
    String sql = "select * from product_type where num='" + num + "'";
    ResultSet rs = DBHelper.search(sql, null);
    try {
      if(rs.next()) {
        String name = rs.getString(2);  
        data = new ProductData(num, name);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "产品型号加载失败:" + e.getLocalizedMessage(), "警告", JOptionPane.ERROR_MESSAGE);
    } finally {
      DBHelper.close();
    }
    return data;
  }
}
