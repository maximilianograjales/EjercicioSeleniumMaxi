import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EjercicioSelenium {

  @Test
  public void testSumar() {
    System.out.println("Sumar test");
    int a = 1;
    int b = 2;
    int resultado = a + b;

    Assert.assertEquals(resultado, 3, "Falla la suma en assertequals");
    Assert.assertTrue(resultado == 3, "Falla la suma en asserttrue");
    Assert.assertNull(resultado, "Falla la suma en assertNull");
    Assert.assertFalse(resultado == 3, "Falla la suma en assertfalse");
    Assert.assertNotEquals(resultado, 2, "Falla la suma en assertNotEquals");
  }

  @Test(testName = "Prueba de selenium", description = "Esto es una prueba para levantar el browser", groups = "SMOKE")
  public void testDePrueba() {
    System.setProperty("webdriver.chrome.driver",
        "/aftersale-hopper-resources/drivers/mac/chromedriver");

    WebDriver driver = new ChromeDriver();
    driver.get("http://www.google.com");

    WebElement textFieldGoogle = driver.findElement(By.id("q"));
    textFieldGoogle.sendKeys("Selenium");
    WebElement lupa = driver.findElement(By.xpath("//*[@class='sbico-c']//button"));
    lupa.click();
    WebElement resultados = driver.findElement(By.id("resultStats"));
    String textoResultados = resultados.getText();
    Assert.assertTrue(textoResultados.contains("Cerca de"));

    driver.close();
  }


}
