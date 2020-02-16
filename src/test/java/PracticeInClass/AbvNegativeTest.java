package PracticeInClass;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class AbvNegativeTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.abv.bg/");
    }

    @Test
    public void negativeTest() {

        try {
            // Finding and Activating the Parent iframe.
            driver.switchTo().frame(driver.findElement(By.id("abv-GDPR-frame")));
            assertTrue(driver.findElement(By.id("cmp-faktor-io-parent")).isDisplayed());

            //Finding and Activating the iframe with shitty cookies
            driver.switchTo().frame(driver.findElement(By.id("cmp-faktor-io")));

            //Accept Cookies button - click
            WebElement acceptingButton = driver.findElement(By.id("acceptAll"));
            assertTrue(acceptingButton.isDisplayed());
            acceptingButton.click();

            //Going back to main page
            driver.switchTo().defaultContent();
            WebElement usernameBox = driver.findElement(By.id("username"));
            assertTrue(usernameBox.isDisplayed());

            //Typing some alien name for username and random password
            usernameBox.sendKeys("Bug Bunny");
            driver.findElement(By.id("password")).sendKeys("BugBunnyhere22!");

            //click on login button
            Actions loginButtonAction = new Actions(driver);

            WebElement loginBut = driver.findElement(By.cssSelector("#leftSide input#loginBut"));
            loginButtonAction.moveToElement(loginBut).click().perform();

            //Check if i am on a right place
            WebElement errorMessage = driver.findElement(By.id("form.errors"));
            assertTrue(errorMessage.isDisplayed());

            //Check if the error message is the right one
            assertEquals(errorMessage.getText(),
                    "Грешен потребител / парола.",
                    "----- Something is very wrong here ! ----");

        } catch (NoSuchElementException | NoSuchFrameException ef) {
            ef.printStackTrace();
            fail("====  Problem with finding an Element or Frame ! ====");

        }
    }

    @AfterMethod
    public void TearDown() throws InterruptedException {
        Thread.sleep(2000);
        driver.quit();
    }
}

