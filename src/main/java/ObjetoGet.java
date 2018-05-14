public class ObjetoGet {
  String newData;
  boolean check;

  public ObjetoGet() {
  }

  public ObjetoGet(String newData, boolean check) {
    this.newData = newData;
    this.check = check;
  }

  public String getNewData() {
    return newData;
  }

  public void setNewData(String newData) {
    this.newData = newData;
  }

  public boolean isCheck() {
    return check;
  }

  public void setCheck(boolean check) {
    this.check = check;
  }
}
