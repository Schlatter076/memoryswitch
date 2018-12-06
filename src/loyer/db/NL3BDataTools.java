package loyer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NL3BDataTools {
  Connection connection = null;
  PreparedStatement pstmt = null;
  ResultSet res = null;
  
  /**
   * 提供nl3b表插入方法
   * @param str
   * @return
   * @throws Exception
   */
  public static int nl3bInsert(String str[]) throws Exception{
    String sql = "insert into nl3b values(?, ?, ?, ?, ?, ?, ?)";
    return DBHelper.AddU(sql, str);
  }
  /**
   * 提供从nl3b表中查询所有内容方法
   * @return
   */
  public static List<NL3BData> getAllbyDb() {
    List<NL3BData> list = new ArrayList<NL3BData>();
    try {
      
      String sql = "select * from nl3b";
      ResultSet res = DBHelper.search(sql, null);
      while(res.next()) {
        String pdxuhao = res.getString(1);
        String testitem = res.getString(2);
        String maxvalue = res.getString(3);
        String minvalue = res.getString(4);
        String testvalue = res.getString(5);
        String danwei = res.getString(6);
        String testresult = res.getString(7);
       
        
        list.add(new NL3BData(pdxuhao, testitem, maxvalue, minvalue, testvalue, danwei, testresult)); 
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return list;
  }
  
}
