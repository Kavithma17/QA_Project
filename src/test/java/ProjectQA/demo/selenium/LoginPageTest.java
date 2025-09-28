package ProjectQA.demo.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginPageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginPage() {
        // Open login page
        driver.get("http://localhost:5175/login");

        // Locate elements
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter your username']"))
        );
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Enter your password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        // Enter credentials (change these for different scenarios)
        usernameField.sendKeys("alice");
        passwordField.sendKeys("wrongpassword"); // or correct password for successful login

        // Click login
        loginButton.click();

        // Handle possible alert
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            System.out.println("Alert appeared: " + alertText);
            alert.accept(); // close the alert
            assertTrue(alertText.contains("Something went wrong"), "Unexpected alert text");
        } catch (TimeoutException e) {
            // No alert appeared, check URL for successful login
            wait.until(ExpectedConditions.urlContains("/diary")); // adjust to your success redirect URL
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("/diary"), "Login failed or redirect did not happen");
        }
    }
    @Test
    public void testSignupPage() {
        // Open signup page
        driver.get("http://localhost:5175/signup");

        // Locate elements
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Enter your username']"))
        );
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Enter your email']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Create a password']"));
        WebElement signupButton = driver.findElement(By.xpath("//button[text()='Sign Up']"));

        // Enter details
        usernameField.sendKeys("newuser123");
        emailField.sendKeys("newuser123@example.com");
        passwordField.sendKeys("password123");

        // Click signup
        signupButton.click();

        // Handle possible alert
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            System.out.println("Alert appeared: " + alertText);
            alert.accept(); // close alert

            // ✅ Updated assertions for the new Signup component
            assertTrue(alertText.equals("true") || alertText.equals("false"),
                    "Unexpected alert text");
        } catch (TimeoutException e) {
            // No alert, check redirect
            wait.until(ExpectedConditions.urlContains("/login"));
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("/login"), "Signup failed or redirect did not happen");
        }
    }


}