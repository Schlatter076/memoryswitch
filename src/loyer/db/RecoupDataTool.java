package loyer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RecoupDataTool {
  protected Connection connection = null;
  protected PreparedStatement pstmt = null;
  protected ResultSet res = null;
  
  /**
   * 获取c211-recoup表中所有数据
   * @return
   */
  public static List<C211RecoupData> getC211RecoupData() {
    List<C211RecoupData> list = new ArrayList<>();
    try {
      String sql = "select * from c211_recoup";
      ResultSet res = DBHelper.search(sql, null);
      while(res.next()) {
        double puLL1 = res.getDouble(1);
        double puLL2 = res.getDouble(2);
        double puLL3 = res.getDouble(3);
        double resistance = res.getDouble(4);
        double vol = res.getDouble(5);
        double stroke1 = res.getDouble(6);
        double stroke2 = res.getDouble(7);
        double stroke3 = res.getDouble(8);
        String name = res.getString(9);
        
        list.add(new C211RecoupData(puLL1, puLL2, puLL3, resistance, vol, stroke1, stroke2, stroke3, name));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return list;
  }
  /**
   * 提供c211-recoup表插入方法
   * @param datas
   * @return
   * @throws Exception
   */
  public static int insertC211_recoup(String[] datas) throws Exception {
    String sql = "insert into c211_recoup values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    return DBHelper.AddU(sql, datas); 
  }
  /**
   * 提供c211-recoup表更新方法
   * @param data
   * @throws Exception
   */
  public static void updateC211_recoup(double[] data) throws Exception {
    String sql = "update c211_recoup set pull_1='"+ data[0] +"',pull_2='"+data[1]+"',pull_3='"+data[2]+"',resistance='"+data[3]+"',"
        + "voltage='"+data[4]+"',stroke_1='"+data[5]+"',stroke_2='"+data[6]+"',stroke_3='"+data[7]+"',name='C211'";
    ECTESTSYSTools.updateRecord(sql);
  }
  
  /**
   * 获取nl3b-recoup表中所有数据
   * @return
   */
  public static List<NL3BRecoupData> getNl3bRecoupData() {
    List<NL3BRecoupData> list = new ArrayList<>();
    try {
     
      String sql = "select * from nl3b_recoup";
      ResultSet res = DBHelper.search(sql, null);
      while(res.next()) {
        double puLL1 = res.getDouble(1);
        double puLL2 = res.getDouble(2);
        double puLL3 = res.getDouble(3);
        double resistance = res.getDouble(4);
        double vol = res.getDouble(5);
        double stroke1 = res.getDouble(6);
        double stroke2 = res.getDouble(7);
        double stroke3 = res.getDouble(8);
        String name = res.getString(9);
        
        list.add(new NL3BRecoupData(puLL1, puLL2, puLL3, resistance, vol, stroke1, stroke2, stroke3, name));
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return list;
  }
  /**
   * 提供c211-recoup表插入方法
   * @param datas
   * @return
   * @throws Exception
   */
  public static int insertNl3b_recoup(String[] datas) throws Exception {
    String sql = "insert into nl3b_recoup values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    return DBHelper.AddU(sql, datas); 
  }
  /**
   * 提供nl3b-recoup表更新方法
   * @param data
   * @throws Exception
   */
  public static void updateNl3b_recoup(double[] data) throws Exception {
    String sql = "update nl3b_recoup set pull_1='"+ data[0] +"',pull_2='"+data[1]+"',pull_3='"+data[2]+"',resistance='"+data[3]+"',"
        + "voltage='"+data[4]+"',stroke_1='"+data[5]+"',stroke_2='"+data[6]+"',stroke_3='"+data[7]+"',name='C211'";
    ECTESTSYSTools.updateRecord(sql);
  }
}
