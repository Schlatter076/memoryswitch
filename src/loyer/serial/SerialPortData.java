package loyer.serial;

public class SerialPortData {

  private int number;
  private String PortName;
  private int baudRate;
  private int dataBits;
  private int stopBits;
  private int parity;
  
  public SerialPortData() {
    super();
  }
  public SerialPortData(int number, String portName, int baudRate, int dataBits, int stopBits, int parity) {
    super();
    this.number = number;
    this.PortName = portName;
    this.baudRate = baudRate;
    this.dataBits = dataBits;
    this.stopBits = stopBits;
    this.parity = parity;
  }
  public int getNumber() {
    return number;
  }
  public void setNumber(int number) {
    this.number = number;
  }
  public String getPortName() {
    return PortName;
  }
  public void setPortName(String portName) {
    this.PortName = portName;
  }
  public int getBaudRate() {
    return baudRate;
  }
  public void setBaudRate(int baudRate) {
    this.baudRate = baudRate;
  }
  public int getDataBits() {
    return dataBits;
  }
  public void setDataBits(int dataBits) {
    this.dataBits = dataBits;
  }
  public int getStopBits() {
    return stopBits;
  }
  public void setStopBits(int stopBits) {
    this.stopBits = stopBits;
  }
  public int getParity() {
    return parity;
  }
  public void setParity(int parity) {
    this.parity = parity;
  }
  
}
