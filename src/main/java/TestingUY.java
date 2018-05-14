import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


public class TestingUY {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestingUY.class.getName());
  private CsvWriter csvOutput;

  @Test(testName = "Probando", description = "Caso de prueba probando Selenium")
  public void testDePrueba() throws InterruptedException, IOException {
    //Creamos el archivo CSV
    csvOutput = new CsvWriter(new FileWriter("Resultado.csv", true), ',');

    //Seteamos driver
    System.setProperty("webdriver.chrome.driver",
        "/Descargas/chromedriver");
    WebDriver driver = new ChromeDriver();

    //Accedemos al sistema
    driver.get("http://www.backofficedeprueba.com");
    LOGGER.info("Se accede al backoffice");

    //Primer pantalla (LOGIN)
    WebElement textFieldUsername = driver.findElement(By.id("usernamefield"));
    WebElement textFieldPassword = driver.findElement(By.id("passwordfield"));
    WebElement buttonLogin = driver.findElement(By.id("buttonlogin"));
    textFieldUsername.sendKeys("testinguy@gmail.com");
    textFieldPassword.sendKeys("probando123");
    buttonLogin.click();
    Thread.sleep(5000);
    LOGGER.info("Se loguea al usuario");

    //Se localiza el excel y se carga
    URL path = this.getClass().getResource("TestingUyExcel.xlsx");
    FileInputStream file = new FileInputStream(new File(path.getFile()));
    XSSFWorkbook worbook = new XSSFWorkbook(file);
    XSSFSheet sheet = worbook.getSheetAt(0);
    Row row;

    //Se obtiene el iterador
    Iterator<Row> rowIterator = sheet.iterator();
    LOGGER.info("Se lee el excel");

    //Bucle del excel
    while (rowIterator.hasNext()) {
      try {

        row = rowIterator.next();
        LOGGER.info("Fila del excel : " + row.getRowNum());

        //se obtiene las celdas por fila
        int transactionNumber = new Double(row.getCell(0).getNumericCellValue()).intValue();
        String action = row.getCell(1).getStringCellValue();
        String optionToClick = row.getCell(2).getStringCellValue();
        LOGGER.info("Numero de transaccion : " + transactionNumber);
        LOGGER.info("Tipo de accion : " + action);
        LOGGER.info("Opcion a seleccionar : " + optionToClick);
        csvOutput.write(String.valueOf(transactionNumber));
        csvOutput.write(action);
        csvOutput.write(optionToClick);

        //Se obtienen mas datos por Servicio REST
        ClienteTestingUy clienteTestingUy = new ClienteTestingUy();
        ObjetoGet objetoGet = clienteTestingUy.getService();
        LOGGER.info("Dato nuevo : " + objetoGet.getNewData());
        LOGGER.info("Tildar checkbox?: " + objetoGet.isCheck());
        csvOutput.write(objetoGet.getNewData());
        csvOutput.write(String.valueOf(objetoGet.isCheck()));

        //Segunda pantalla
        LOGGER.info("Se busca la transaccion...");
        WebElement textFieldTrNumber = driver.findElement(By.id("transactionfield"));
        WebElement findButton = driver.findElement(By.id("findButton"));
        textFieldTrNumber.sendKeys(String.valueOf(transactionNumber));
        findButton.click();
        Thread.sleep(5000);
        LOGGER.info("Transaccion encontrada");

        //Tercer pantalla
        WebElement newDataField = driver.findElement(By.id("newData"));
        WebElement activateCheckBox = driver.findElement(By.id("checkbox"));
        WebElement action1Radio = driver.findElement(By.id("radiobutton1"));
        WebElement action2Radio = driver.findElement(By.id("radiobutton2"));
        WebElement modifyDataButton = driver.findElement(By.id("modifyButton"));

        LOGGER.info("Se carga pantalla de modificacion de datos...");
        newDataField.sendKeys(objetoGet.getNewData());
        LOGGER.info("Nuevo dato ingresado");

        if (objetoGet.isCheck()) {
          activateCheckBox.click();
          LOGGER.info("Se tilda el checkbox");
        }

        switch (action) {
          case "Action 1":
            action1Radio.click();
            break;
          case "Action 2":
            action2Radio.click();
            break;
        }
        LOGGER.info("Accion seleccionada correctamente");

        modifyDataButton.click();
        Thread.sleep(5000);
        LOGGER.info("Boton de modificar datos fue clickeado. Cargando siguiente pantalla...");

        //Cuarta pantalla
        WebElement collectAgainButton = driver.findElement(By.id("collectAgain"));
        WebElement cancelTransactionButton = driver.findElement(By.id("cancelTransactionButton"));

        switch (optionToClick) {
          case "Cobrar":
            collectAgainButton.click();
            break;
          case "Cancelar":
            cancelTransactionButton.click();
            break;
        }
        LOGGER.info("Se clickea en la opción correctamente!");
        Thread.sleep(5000);

        //Se obtiene por BD otro dato
        ClienteBDTestingUy bd = new ClienteBDTestingUy();
        int magicNumber = bd.getMagicNumber(transactionNumber, objetoGet.getNewData());
        LOGGER.info("El numero magico es : " + magicNumber);

        //Se llama a un POST de un servicio para guardar un dato
        clienteTestingUy.postService(transactionNumber, magicNumber);
        LOGGER.info(
            "El servicio fue posteado correctamente. Caso finalizado. Siguiente transaccion...");
        csvOutput.write("Exitoso");
      } catch (Exception e) {
        LOGGER.error(e.getMessage());
        csvOutput.write("Fallido");
      } finally {
        //Se guarda el registro en el csv
        csvOutput.endRecord();
      }
    }
    //Se cierra el csv
    csvOutput.close();
  }


}
