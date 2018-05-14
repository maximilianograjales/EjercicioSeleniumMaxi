import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClienteBDTestingUy {

  private Connection connection;

  public ClienteBDTestingUy() throws SQLException, ClassNotFoundException {
    Class.forName("com.mysql.jdbc.Driver");
    connection = DriverManager.getConnection(
        "jdbc:mysql://10.1.2.111:3306/bdprueba", "root", "root");
  }

  public int getMagicNumber(int transactionNumber, String newData) throws Exception {
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt
          .executeQuery("select * from tabla where id = '" + transactionNumber + "'");

      connection.close();
      return rs.getInt(3);
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
