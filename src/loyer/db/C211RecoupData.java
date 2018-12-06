package loyer.db;

public class C211RecoupData {
  private double PULL1 = 0.0d;
  private double PULL2 = 0.0d;
  private double PULL3 = 0.0d;
  private double RES = 0.0d;
  private double VOL = 0.0d;
  private double STROKE1 = 0.0d;
  private double STROKE2 = 0.0d;
  private double STROKE3 = 0.0d;
  private String name = "";
  
  public C211RecoupData() {}
  public C211RecoupData(double pULL1, double pULL2, double pULL3, double res, 
      double vol, double stroke1, double stroke2, double stroke3, String names) {
    this.PULL1 = pULL1;
    this.PULL2 = pULL2;
    this.PULL3 = pULL3;
    this.RES = res;
    this.VOL = vol;
    this.STROKE1 = stroke1;
    this.STROKE2 = stroke2;
    this.STROKE3 = stroke3;
    this.name = names;
  }
  public double getPULL1() {
    return PULL1;
  }
  public void setPULL1(double pULL1) {
    this.PULL1 = pULL1;
  }
  public double getPULL2() {
    return PULL2;
  }
  public void setPULL2(double pULL2) {
    this.PULL2 = pULL2;
  }
  public double getPULL3() {
    return PULL3;
  }
  public void setPULL3(double pULL3) {
    this.PULL3 = pULL3;
  }
  public double getRES() {
    return RES;
  }
  public void setRES(double rES) {
    this.RES = rES;
  }
  public double getVOL() {
    return VOL;
  }
  public void setVOL(double vOL) {
    this.VOL = vOL;
  }
  public double getSTROKE1() {
    return STROKE1;
  }
  public void setSTROKE1(double sTROKE1) {
    this.STROKE1 = sTROKE1;
  }
  public double getSTROKE2() {
    return STROKE2;
  }
  public void setSTROKE2(double sTROKE2) {
    this.STROKE2 = sTROKE2;
  }
  public double getSTROKE3() {
    return STROKE3;
  }
  public void setSTROKE3(double sTROKE3) {
    this.STROKE3 = sTROKE3;
  }
  public void setName(String names) {
    this.name = names;
  }
  public String getName() {
    return name;
  }
   
}
