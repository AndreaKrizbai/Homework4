package tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Homework4 {
    protected static WebDriver driver;

    @BeforeMethod
    public void setup(){
        WebDriverManager.chromedriver().version("79").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    //Randomly select a checkbox. As soon as you check any day, print the name of the day and uncheck
    // immediately. After you check and uncheck Friday for the third time, exit the program.

    @Test
    public void daysTask(){
        driver.get("http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox");
        List<WebElement> l1 = driver.findElements(By.cssSelector(".gwt-CheckBox>label"));
        Random random = new Random();
        int count = 0;
        while(count<3){
            int index = random.nextInt(l1.size());
            if(l1.get(index).isEnabled()){
                l1.get(index).click();
                if(l1.get(index).getText().equals("Friday")){
                    count++;
                }
                System.out.println(l1.get(index).getText());
                l1.get(index).click();
            }
        }
    }

    @AfterMethod
    public void teardown(){
        driver.quit();
    }
}

