package loyer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;


public class C211DataTools {
  Connection connection = null;
  PreparedStatement pstmt = null;
  ResultSet res = null;
  
  /**
   * 提供c211表插入方法
   * @param str
   * @return
   * @throws Exception
   */
  public static int c211Insert(String str[]) throws Exception{
    String sql = "insert into c211 values(?, ?, ?, ?, ?, ?, ?)";
    return DBHelper.AddU(sql, str);
  }
  /**
   * 提供从c211表中查询所有内容方法
   * @return
   */
  public static List<C211Data> getAllbyDb() {
    List<C211Data> list = new ArrayList<C211Data>();
    try {
      String sql = "select * from c211";
      ResultSet res = DBHelper.search(sql, null);
      while(res.next()) {
        String pdxuhao = res.getString(1);
        String testitem = res.getString(2);
        String maxvalue = res.getString(3);
        String minvalue = res.getString(4);
        String testvalue = res.getString(5);
        String danwei = res.getString(6);
        String testresult = res.getString(7);
       
        
        list.add(new C211Data(pdxuhao, testitem, maxvalue, minvalue, testvalue, danwei, testresult)); 
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return list;
  }
  
  
}