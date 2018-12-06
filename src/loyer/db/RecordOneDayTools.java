package loyer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


/**
 * recordtdoneday表工具类
 * @author Loyer
 * @coding utf8
 */
public class RecordOneDayTools {
  
  Connection connection = null;
  PreparedStatement pstmt = null;
  ResultSet res = null;
  /**
   * 清除recordtdoneday表
   * @return
   */
  public static int truncateRecordoneday() throws Exception {
    String sql = "truncate table recordtdoneday";
    return DBHelper.AddU(sql, null);
  }
  /**
   * 提供recordtdoneday表插入方法
   * @param sql
   * @param str
   * @return
   * @throws Exception
   */
  public static int recordonedayInsert(String str[]) throws Exception{
    String sql = "insert into recordtdoneday values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    return DBHelper.AddU(sql, str);
  }
  /**
   * 查询recordtdoneday表中的所有数据
   * @return
   */
  public static List<RecordOneDayData> getAllbyDb() {
    List<RecordOneDayData> list = new ArrayList<RecordOneDayData>();
    try {
      
      String sql = "select * from recordtdoneday";
      ResultSet res = DBHelper.search(sql, null);
      while(res.next()) {
        String recordname = res.getString(1);
        String recordtimes = res.getString(2);
        String testitem = res.getString(3);
        String maxvalue = res.getString(4);
        String minvalue = res.getString(5);
        String testvalue = res.getString(6);
        String danwei = res.getString(7);
        String testresult = res.getString(8);
        String testdate = res.getString(9);
        String remark = res.getString(10);
        
        list.add(new RecordOneDayData(recordname, recordtimes, testitem, maxvalue, minvalue,
                 testvalue, danwei, testresult, testdate, remark)); 
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    return list;
  }
  /**
   * 导出到excel
   */
  public static void outFile() {
    try {
      WritableWorkbook wwb = null;
      //创建可写入的Excel工作簿
      String fileName = "D://test089.xls";
      File file = new File(fileName);
      if(!file.exists()) {
        file.createNewFile();
      }
      //以fileName为文件名来创建一个Workbook
      wwb = Workbook.createWorkbook(file);
      
      //创建工作表
      WritableSheet ws = wwb.createSheet("recordoneday", 0);
      
      //查询数据库中所有的数据
      List<RecordOneDayData> list = getAllbyDb();
      //要插入到的excl表格的行号，默认从0开始
      Label labelRecordname = new Label(0, 0, "recordname");
      Label labelRecortimes = new Label(1, 0, "recordtimes");
      Label labelTestitem = new Label(2, 0, "testitem");
      Label labelMaxvalue = new Label(3, 0, "maxvalue");
      Label labelMinvalue = new Label(4, 0, "minvalue");
      Label labelTestvalue = new Label(5, 0, "testvalue");
      Label labelDanwei = new Label(6, 0, "danwei");
      Label labelTestResult = new Label(7, 0, "testresult");
      Label labelRecordDate = new Label(8, 0, "testdate");
      Label labelRemark = new Label(9, 0, "remark");
      ws.addCell(labelRecordname);
      ws.addCell(labelRecortimes);
      ws.addCell(labelTestitem);
      ws.addCell(labelMaxvalue);
      ws.addCell(labelMinvalue);
      ws.addCell(labelTestvalue);
      ws.addCell(labelDanwei);
      ws.addCell(labelTestResult);
      ws.addCell(labelRecordDate);
      ws.addCell(labelRemark);
      for(int i = 0; i < list.size(); i++) {
        Label labelRecordname_i = new Label(0, i+1, list.get(i).getRecordname() + "");
        Label labelRecordtimes_i = new Label(1, i+1, list.get(i).getRecordtimes());
        Label labelTestitem_i = new Label(2, i+1, list.get(i).getTestitem());
        Label labelMaxvalue_i = new Label(3, i+1, list.get(i).getMaxvalue());
        Label labelMinvalue_i = new Label(4, i+1, list.get(i).getMinvalue());
        Label labelTestvalue_i = new Label(5, i+1, list.get(i).getTestvalue());
        Label labelDanwei_i = new Label(6, i+1, list.get(i).getDanwei());
        Label labelTestResult_i = new Label(7, i+1, list.get(i).getTestresult());
        Label labelRecordDate_i = new Label(8, i+1, list.get(i).getRecorddate());
        Label labelRemark_i = new Label(9, i+1, list.get(i).getRemark() + "");
        ws.addCell(labelRecordname_i);
        ws.addCell(labelRecordtimes_i);
        ws.addCell(labelTestitem_i);
        ws.addCell(labelMaxvalue_i);
        ws.addCell(labelMinvalue_i);
        ws.addCell(labelTestvalue_i);
        ws.addCell(labelDanwei_i);
        ws.addCell(labelTestResult_i);
        ws.addCell(labelRecordDate_i);
        ws.addCell(labelRemark_i);
      }
      //写进文档
      wwb.write();
      //关闭Excel工作簿对象
      wwb.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
}