package loyer.db;

/**
 * 机种名存储类，表product_type的实体
 * @author hw076
 *
 */
public class ProductData {

  private int num;
  private String productName;
  
  public ProductData() {
    super();
  }
  
  public ProductData(int num, String productName) {
    super();
    this.num = num;
    this.productName = productName;
  }

  public int getNum() {
    return num;
  }
  public void setNum(int num) {
    this.num = num;
  }
  public String getProductName() {
    return productName;
  }
  public void setProductName(String productName) {
    this.productName = productName;
  }
  
  
}
