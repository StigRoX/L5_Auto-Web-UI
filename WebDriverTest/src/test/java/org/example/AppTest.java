package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class AppTest {
    WebDriver driver;

    private final static String SITE_BASE = "https://www.cifrus.ru";

    @BeforeAll
    static void registerDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupBrowser() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    @DisplayName("Добавление товара в корзину")
    void cartTest() {
        driver.get(SITE_BASE);
        WebElement product = driver.findElement(By.className("product-thumb"));
        String name = product.findElement(By.className("caption")).findElement(By.className("name")).getText();
        System.out.println("Найден продукт: " + name);
        product.findElement(By.xpath("//button[contains(text(), 'В корзину')]")).click();
        driver.get("https://www.cifrus.ru/basket.php");
        WebElement cartProduct = driver
                .findElement(By.className("simplecheckout-cart"))
                .findElement(By.tagName("tbody"))
                .findElement(By.tagName("tr"));
        String cartProductName = cartProduct.findElement(By.className("name")).getText();
        Assertions.assertEquals(cartProductName, name);
    }

    @Test
    @DisplayName("Авторизация на сайте")
    void loginTest() {
        driver.manage().window().maximize();
        driver.get(SITE_BASE);
        WebElement authButton = driver.findElement(By.xpath("//*[contains(text(), 'Личный')]"));
        authButton.click();
        driver.findElement(By.xpath("//*[contains(text(), 'Авторизация')]")).click();
        driver.findElement(By.name("email")).sendKeys("vexeae@mailto.plus");
        driver.findElement(By.name("password")).sendKeys("8520go");
        driver.findElement(By.xpath("//a[contains(text(), 'Войти')]")).click();
        String url = driver.getCurrentUrl();
        System.out.println(url);
        Assertions.assertEquals(url, "https://www.cifrus.ru/account/profile");
    }

    @Test
    @DisplayName("Поиск конкретного товара")
    void searchIphone() {
        driver.manage().window().maximize();
        driver.get(SITE_BASE);
        driver.findElement(By.id("search_box")).sendKeys("iphone 13");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        WebElement el = driver.findElement(By.className("product-thumb")).findElement(By.className("caption")).findElement(By.className("name"));
        String text = el.getText();
        Assertions.assertTrue(el.getText().toLowerCase().contains("iphone 13"));
    }

    @AfterEach
    void quitBrowser() {
        driver.quit();
    }
}
