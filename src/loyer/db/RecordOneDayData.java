package loyer.db;


/**
 * recordoneday表的实体
 * @author Loyer
 * @coding utf8
 */
public class RecordOneDayData {
  
  private String recordname;
  private String recordtimes;
  private String testitem;
  private String maxvalue;
  private String minvalue;
  private String testvalue;
  private String danwei;
  private String testresult;
  private String recorddate;
  private String remark;
  
  /**
   * 构造器
   */
  public RecordOneDayData() {}
  public RecordOneDayData(String recordname, String recordtimes, String testitem, String maxvalue, String minvalue, String testvalue, 
                          String danwei, String testresult, String recorddate, String remark) {
    this.recordname = recordname;
    this.recordtimes = recordtimes;
    this.testitem = testitem;
    this.maxvalue = maxvalue;
    this.minvalue = minvalue;
    this.testvalue = testvalue;
    this.danwei = danwei;
    this.testresult = testresult;
    this.recorddate = recorddate;
    this.remark = remark;
  }
  public void setRecordname(String recordname) {
    this.recordname = recordname;
  }
  public String getRecordname() {
    return recordname;
  }
  public void setRecordtimes(String recordtimes) {
    this.recordtimes = recordtimes;
  }
  public String getRecordtimes() {
    return recordtimes;
  }
  public void setTestitem(String testitem) {
    this.testitem = testitem;
  }
  public String getTestitem() {
    return testitem;
  }
  public void setMaxvalue(String maxvalue) {
    this.maxvalue = maxvalue;
  }
  public String getMaxvalue() {
    return maxvalue;
  }
  public void setMinvalue(String minvalue) {
    this.minvalue = minvalue;
  }
  public String getMinvalue() {
    return minvalue;
  }
  public void setTestvalue(String testvalue) {
    this.testvalue = testvalue;
  }
  public String getTestvalue() {
    return testvalue;
  }
  public void setDanwei(String danwei) {
    this.danwei = danwei;
  }
  public String getDanwei() {
    return danwei;
  }
  public void setTestresult(String testresult) {
    this.testresult = testresult;
  }
  public String getTestresult() {
    return testresult;
  }
  public void setRecorddata(String recorddate) {
    this.recorddate = recorddate;
  }
  public String getRecorddate() {
    return recorddate;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getRemark() {
    return remark;
  }
   
}
