package loyer.db;

public class NL3BData {
  private String pdxuhao;
  private String testitem;
  private String maxvalue;
  private String minvalue;
  private String testvalue;
  private String danwei;
  private String testresult;
  
  public NL3BData() {}
  public NL3BData(String pdxuhao, String testitem, String maxvalue, String minvalue, String testvalue, String danwei, String testresult) {
    this.pdxuhao = pdxuhao;
    this.testitem = testitem;
    this.maxvalue = maxvalue;
    this.minvalue = minvalue;
    this.testvalue = testvalue;
    this.danwei = danwei;
    this.testresult = testresult;
  }
  
  public String getPdxuhao() {
    return pdxuhao;
  }
  public void setPdxuhao(String pdxuhao) {
    this.pdxuhao = pdxuhao;
  }
  public String getTestitem() {
    return testitem;
  }
  public void setTestitem(String testitem) {
    this.testitem = testitem;
  }
  public String getMaxvalue() {
    return maxvalue;
  }
  public void setMaxvalue(String maxvalue) {
    this.maxvalue = maxvalue;
  }
  public String getMinvalue() {
    return minvalue;
  }
  public void setMinvalue(String minvalue) {
    this.minvalue = minvalue;
  }
  public String getTestvalue() {
    return testvalue;
  }
  public void setTestvalue(String testvalue) {
    this.testvalue = testvalue;
  }
  public String getDanwei() {
    return danwei;
  }
  public void setDanwei(String danwei) {
    this.danwei = danwei;
  }
  public String getTestresult() {
    return testresult;
  }
  public void setTestresult(String testresult) {
    this.testresult = testresult;
  }
}
