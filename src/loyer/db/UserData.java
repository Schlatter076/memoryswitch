package loyer.db;

/**
 * 用户数据库表user的实体
 * @author hw076
 *
 */
public class UserData {

  private int id;
  private String userName;
  private String password;
  
  public UserData() {}
  public UserData(int id, String usrName, String pwd) {
    this.id = id;
    this.userName = usrName;
    this.password = pwd;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  
}
