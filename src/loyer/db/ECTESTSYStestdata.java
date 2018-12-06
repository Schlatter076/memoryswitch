package loyer.db;

/**
 * ECTESTSYStestdata表的实体
 * @author hw076
 *
 */
public class ECTESTSYStestdata {
  private String buzhou;
  private String buzhouname;
  private String testdata;
  private String min;
  private String max;
  private String result;
  private String condition;
  private String date;
  /**
   * 构造器
   */
  public ECTESTSYStestdata() {};
  public ECTESTSYStestdata(String buzhou, String buzhouname, String testdata, String min, String max, String result, String condition, String date) {
    this.buzhou = buzhou;
    this.buzhouname = buzhouname;
    this.testdata = testdata;
    this.min = min;
    this.max = max;
    this.result = result;
    this.condition = condition;
    this.date = date;    
  }
  public void setBuzhou(String buzhou) {
    this.buzhou = buzhou;
  }
  public String getBuzhou() {
    return buzhou;
  }
  public void setBuzhouname(String buzhouname) {
    this.buzhouname = buzhouname;
  }
  public String getBuzhouname() {
    return buzhouname;
  }
  public void setTestdata(String testdata) {
    this.testdata = testdata;
  }
  public String getTestdata() {
    return testdata;
  }
  public void setMin(String min) {
    this.min = min;
  }
  public String getMin() {
    return min;
  }
  public void setMax(String max) {
    this.max = max;
  }
  public String getMax() {
    return max;
  }
  public void setResult(String result) {
    this.result = result;
  }
  public String getResult() {
    return result;
  }
  public void setCondition(String condition) {
    this.condition = condition;
  }
  public String getCondition() {
    return condition;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getDate() {
    return date;
  }
  
}