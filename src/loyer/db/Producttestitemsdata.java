package loyer.db;

/**
 * C211表的实例
 * @author hw076
 *
 */
public class Producttestitemsdata {
  String pdxuhao;
  String testitem;
  String maxvalue;
  String minvalue;
  String testvalue;
  String danwei;
  String testresult;
  
  public Producttestitemsdata() {}
  public Producttestitemsdata(String pdxuhao, String testitem, String maxvalue, String minvalue, String testvalue, String danwei, String testresult) {
    this.pdxuhao = pdxuhao;
    this.testitem = testitem;
    this.maxvalue = maxvalue;
    this.minvalue = minvalue;
    this.testvalue = testvalue;
    this.danwei = danwei;
    this.testresult = testresult;
  }
  
  public void setPdxuhaoi(String pdxuhao) {
    this.pdxuhao = pdxuhao;
  }
  public String getPdxuhao() {
    return pdxuhao;
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
  
}










