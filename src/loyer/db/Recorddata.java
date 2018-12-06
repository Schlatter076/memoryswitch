package loyer.db;

/**
 * recordtd表的实体
 * @author hw076
 *
 */
public class Recorddata {
  private String recordname;
  private String recordsum;
  private String recordok;
  private String recordng;
  private String recordts;
  private String recordtime;
  /**
   * 构造器
   */
  public Recorddata() {}
  public Recorddata(String recordname, String recordsum, String recordok, String recordng, String recordts, String recordtime) {
    this.recordname = recordname;
    this.recordsum = recordsum;
    this.recordok = recordok;
    this.recordng = recordng;
    this.recordts = recordts;
    this.recordtime = recordtime;
  }
  public void setRecordname(String recordname) {
    this.recordname = recordname;
  }
  public String getRecordname() {
    return recordname;
  }
  public void setRecordsum(String recordsum) {
    this.recordsum = recordsum;
  }
  public String getRecordsum() {
    return recordsum;
  }
  public void setRecordok(String recordok) {
    this.recordok = recordok;
  }
  public String getRecordok() {
    return recordok;
  }
  public void setRecordng(String recordng) {
    this.recordng = recordng;
  }
  public String getRecordng() {
    return recordng;
  }
  public void setRecordts(String recordts) {
    this.recordts = recordts;
  }
  public String getRecordts() {
    return recordts;
  }
  public void setRecordtime(String recordtime) {
    this.recordtime = recordtime;
  }
  public String getRecordtime() {
    return recordtime;
  }
  
}