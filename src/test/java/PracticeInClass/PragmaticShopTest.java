package PracticeInClass;

import PracticeInClass.utils.WaitTool;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PragmaticShopTest {
        WebDriver driver;

        @BeforeMethod
        public void setUp() {
            System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver_win32\\chromedriver.exe");
            driver = new ChromeDriver();

            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            driver.manage().window().maximize();
            driver.get("http://shop.pragmatic.bg/");
        }

        @Test
        public void searchAndAddToCard() throws InterruptedException {
            //Search Bar
            driver.findElement(By.xpath("//div[@id='search']/input[@name='search']")).sendKeys("blabla");
            //Click on Search Button
            driver.findElement(By.cssSelector("div#search button")).click();

            //Assertion we are on the right page
            //With error message
            assertEquals(driver.findElement(By.xpath("//input[@id='button-search']/following-sibling::p")).getText(),
                    "Your shopping cart is empty!",
                    "String are not equals!");

            //In the new page
            //Find search bar and clear old search
            //And type "iPhone"
            WebElement searchBar = driver.findElement(By.cssSelector("#input-search"));
            searchBar.clear();
            searchBar.sendKeys("iPhone");

            //Finding drop down and choose Phones & PDAs category
            Select dropDown = new Select(driver.findElement(By.name("category_id")));
            List<WebElement> allOptions = dropDown.getOptions();

            for (WebElement element :allOptions) {
                if(element.getText().equals("Phones & PDAs")){
                    element.click();
                }

            }

            //OR other ways is by value
            //First way
            dropDown.selectByVisibleText("Phones & PDAs");
            //Second way
            dropDown.selectByValue("24");

            //Search in subcategories checkbox
            //And check the box
            WebElement categoriesCheckBox = driver.findElement(By.xpath("//*[@name='sub_category']"));
            assertFalse(categoriesCheckBox.isSelected());

            if(categoriesCheckBox.isEnabled())
                categoriesCheckBox.click();

            assertTrue(categoriesCheckBox.isSelected());

            //Search in product descriptions
            //And check the box
            WebElement descriptionElement = driver.findElement(By.id("description"));
            assertFalse(descriptionElement.isSelected());

            if(descriptionElement.isEnabled())
                descriptionElement.click();

            assertTrue(descriptionElement.isSelected());

            //Click on search button
            driver.findElement(By.id("button-search")).click();

            //Check if the product is displayed
            assertTrue(driver.findElement(By.linkText("iPhone")).isDisplayed());

            //Check if the cart is empty
            assertEquals(driver.findElement(By.cssSelector("span#cart-total")).getText(),
                    "0 item(s) - $0.00");

            //OR other way is:
            WebElement cartButton = driver.findElement(By.xpath("//*[@id='cart']/button"));
            cartButton.click();
            assertEquals(driver.findElement(By.xpath("//*[@class='dropdown-menu pull-right']/li/p")).getText(),
                    "Your shopping cart is empty!");


            //Click on add to cart button
            driver.findElement(By.xpath("//div[@class='button-group']/button[1]")).click();

            //Check cart if the element is there
            //Actions needed for moving to element
            //Wait needed for element to be clickable
            Actions  builder = new Actions(driver);
            builder.moveToElement(cartButton).click(cartButton).perform();

            WebDriverWait wait = new WebDriverWait(driver,10);
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#cart button")));

            //Click and check the cart have a item in it
            cartButton.click();
            assertEquals(driver.findElement(By.xpath("//*[@class='table table-striped']//td[3]")).getText(),
                    "x 1");

            //Proceed to checkout by clicking on the checkout button
            driver.findElement(By.cssSelector("p.text-right a:nth-of-type(2)")).click();

            WebDriverWait waitCheckOut = new WebDriverWait(driver,10);
            waitCheckOut.until(new ExpectedCondition<WebElement>() {
                @NullableDecl
                @Override
                public WebElement apply(@NullableDecl WebDriver webDriver) {
                    return driver.findElement(By.linkText("Step 1: Checkout Options"));
                }
            });

            //Assertion about that we are on the right page
            assertTrue(driver.findElement(By.cssSelector("#content h1")).isDisplayed());
            //OR
            assertEquals(driver.findElement(By.cssSelector("#content h1")).getText(),"Checkout");




            Thread.sleep(4000);
        }

        @AfterMethod
        public void tearDown() {
            driver.quit();
        }
    }

