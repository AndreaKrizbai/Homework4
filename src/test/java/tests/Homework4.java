package tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;
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
        List<WebElement> days = driver.findElements(By.cssSelector(".gwt-CheckBox>label"));
        List<WebElement> checkboxes = driver.findElements(By.cssSelector(".gwt-CheckBox>input"));
        Random random = new Random();
        int count = 0;
        while(count<3){
            int index = random.nextInt(days.size());
            if(checkboxes.get(index).isEnabled()){
                days.get(index).click();
                if(days.get(index).getText().equals("Friday")){
                    count++;
                }
                System.out.println(days.get(index).getText());
                days.get(index).click();
            }
        }
    }

    @Test
    public void todaysDate(){
        driver.get("http://practice.cybertekschool.com/dropdown");
        WebElement year = driver.findElement(By.id("year"));
        WebElement month = driver.findElement(By.id("month"));
        WebElement day = driver.findElement(By.id("day"));
        Select y = new Select(year);
        Select m = new Select(month);
        Select d = new Select(day);
        String year_value = y.getFirstSelectedOption().getText();
        String month_value = m.getFirstSelectedOption().getText();
        String day_value = d.getFirstSelectedOption().getText();
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMMMdd");
        Assert.assertEquals(year_value+month_value+day_value, date.format(new Date()));
    }

    @Test
    public void years_months_days(){
        driver.get("http://practice.cybertekschool.com/dropdown");
        WebElement year = driver.findElement(By.id("year"));
        WebElement month = driver.findElement(By.id("month"));
        WebElement day = driver.findElement(By.id("day"));
        Select y = new Select(year);
        Select m = new Select(month);
        Select d = new Select(day);

        Random r = new Random();
        int index = r.nextInt(y.getOptions().size());
        y.selectByIndex(index);
        //y.selectByIndex(new Random().nextInt(y.getOptions().size()));

        List<String>months31 = new ArrayList<>(Arrays.asList(new String[]{"January", "March", "May", "July", "August", "October", "December"}));

        int febDays;
       // febDays = Integer.parseInt(y.getFirstSelectedOption().getText())%4==0 ? 29 : 28;
        int yearValue = Integer.parseInt(y.getFirstSelectedOption().getText());
        if(yearValue%400==0 || yearValue%4==0 && yearValue%100!=0){
            febDays=29;
        }else{
            febDays=28;
        }

        for (int i = 0; i < 12; i++) {
            m.selectByIndex(i);
            if(months31.contains(m.getFirstSelectedOption().getText())){
                Assert.assertEquals(d.getOptions().size(), 31);
            }else if(m.getFirstSelectedOption().getText().equals("February")){
                Assert.assertEquals(d.getOptions().size(), febDays);
            }else{
                Assert.assertEquals(d.getOptions().size(), 30);
            }
        }

    }



    @AfterMethod
    public void teardown(){
        driver.quit();
    }
}

