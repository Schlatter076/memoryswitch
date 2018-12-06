package loyer.db;

import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 * 用户工具类
 * @author hw076
 *
 */
public class UserTools {

 
  private UserTools() {}  //不允许其他类创建本类实例
  /**
   * 传入对应id号，获取相应用户
   * @param id
   * @return 用户
   */
  public static UserData getUserByID(int id) {

    UserData user = null;
    try {
      String sql = "select * from switchdetection.user where id='" + id + "'";
      ResultSet rs = DBHelper.search(sql, null);
      if(rs.next()) {
        int num = rs.getInt(1);
        String userName = rs.getString(2);
        String pwd = rs.getString(3);
        user = new UserData(num, userName, pwd);
      }      
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "用户数据读取失败：" + e.getLocalizedMessage());
    } finally {
      DBHelper.close();
    }
    return user;
  }
}
