import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class ClienteTestingUy {

  private Properties properties;
  private String baseUri;
  private String serviceGet;
  private String servicePost;
  private ObjectMapper jsonMapper;


  public ClienteTestingUy() {
    this.baseUri = "http://backoffice.testinguy.com";
    this.serviceGet = "/probandoGet";
    this.servicePost = "/probandoPost";
    jsonMapper = new ObjectMapper();
    jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public ObjetoGet getService() throws IOException {
    String response = RestAssured.given().baseUri(baseUri)
        .contentType("application/json")
        .get(serviceGet).then().statusCode(200)
        .contentType(ContentType.JSON).extract().response().asString();
    return jsonMapper.readValue(response, ObjetoGet.class);
  }

  public String postService(int transaction, int magicNumber) throws Exception {
    String json = IOUtils
        .toString(ClienteTestingUy.class.getClassLoader().getResourceAsStream("PostJson.json"),
            "UTF-8");
    json = json.replace("{TRANSACCION}", String.valueOf(transaction));
    json = json.replace("{RESULTADO}", String.valueOf(magicNumber));

    try {
      return RestAssured.given().baseUri(baseUri).contentType("application/json").and().body(json)
          .post(servicePost).then().statusCode(200)
          .extract().response().asString();
    } catch (Exception ex) {
      throw new Exception(ex);
    }
  }

}
