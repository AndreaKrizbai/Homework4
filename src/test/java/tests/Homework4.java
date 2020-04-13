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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    @Test
    public void department_sort(){
        driver.get("https://www.amazon.com/");
        Assert.assertEquals(driver.findElement(By.className("nav-search-label")).getText(),"All");
        List<WebElement> list = new Select(driver.findElement(By.id("searchDropdownBox"))).getOptions();
        boolean notAlphabetOrder = false;
        for (int i = 0; i < list.size()-1; i++) {
            if(list.get(i).getText().compareTo(list.get(i+1).getText())>0){
                notAlphabetOrder=true;
                break;
            }
        }
        Assert.assertTrue(notAlphabetOrder);
    }

    @Test
    public void mainDeps(){
        driver.get("https://www.amazon.com/gp/site-directory");
        List<WebElement> mainDep= driver.findElements(By.tagName("h2"));
        List<WebElement>allDep= new Select(driver.findElement(By.id("searchDropdownBox"))).getOptions();
        Set<String> mainDepS = new HashSet<>();
        Set<String>allDepS = new HashSet<>();
        for(WebElement each : mainDep){
            mainDepS.add(each.getText());
        }
        for(WebElement each : allDep){
            allDepS.add(each.getText());
        }

        int count = 0;
        for(String each : mainDepS){
            if(!allDepS.contains(each)){
                System.out.println("This main department is not in All: " + each);
                count++;
            }
        }
        System.out.println();
        System.out.println(count + " out of " + mainDepS.size() +
                " main departments are not in All");
        Assert.assertTrue(allDepS.containsAll(mainDepS));

    }

    @Test
    public void links(){
        driver.get("https://www.w3schools.com/");
        List<WebElement>tagA = driver.findElements(By.tagName("a"));
        for(WebElement each : tagA){
            if(each.isDisplayed()){
                System.out.println(each.getText());
                System.out.println(each.getAttribute("href"));
            }
        }
    }

    @Test
    public void validLinks() {
        driver.get("https://www.selenium.dev/documentation/en/");
        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (int i = 0; i < links.size(); i++) {
            String href = links.get(i).getAttribute("href");
            try {
                URL url = new URL(href);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.connect();
                Assert.assertTrue(httpURLConnection.getResponseCode()==200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void cart() {
        driver.get("https://www.amazon.com/");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon");
        driver.findElement(By.xpath("//span[@id='nav-search-submit-text']//following-sibling::input")).click();
        List<WebElement>price = driver.findElements(By.xpath("//span[@class='a-price']/span[@class='a-offscreen']"));
        int x = new Random().nextInt(price.size());
        x = x==0 ? 1 : x;
        String originName = driver.findElement(By.xpath("(//span[@class='a-size-base-plus a-color-base a-text-normal'])["+x+"]")).getText();
        String originPrice = "$"+driver.findElement(By.xpath("(//span[@class='a-price']/span[2]/span[2])["+x+"]")).getText()+
                "."+driver.findElement(By.xpath("(//span[@class='a-price']/span[2]/span[3])[\"+x+\"]")).getText();

        driver.findElement(By.xpath("(//span[@class='a-price-fraction'])["+x+"]")).click();
        //Assert.assertEquals(driver.findElement(By.xpath("//span[@id='a-autoid-0-announce']/span[2]")).getText(),"1");
        Assert.assertEquals(driver.findElement(By.xpath("//span[text()='Qty:']/following-sibling::span")).getText(),"1");
        Assert.assertEquals(driver.findElement(By.id("productTitle")).getText(), originName);
        Assert.assertEquals(driver.findElement(By.id("priceblock_ourprice")).getText(), originPrice);
        Assert.assertTrue(driver.findElement(By.id("add-to-cart-button")).isDisplayed());
    }

    @Test
    public void prime(){
        driver.get("https://www.amazon.com/");
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon");
        driver.findElement(By.xpath("//span[@id='nav-search-submit-text']//following-sibling::input")).click();
        String firstPrimeName = driver.findElement(By.xpath("(//i[@aria-label='Amazon Prime']/../../../../../..//h2)[1]")).getText();
        driver.findElement(By.xpath("//i[@class='a-icon a-icon-prime a-icon-medium']/../div/label/i")).click();
        String newPrimeName = driver.findElement(By.xpath("(//i[@aria-label='Amazon Prime']/../../../../../..//h2)[1]")).getText();
        Assert.assertEquals(newPrimeName, firstPrimeName);
        driver.findElement(By.xpath("//div[@id='brandsRefinements']//ul/li[last()]//i")).click();
        String newBrandName = driver.findElement(By.xpath("(//i[@aria-label='Amazon Prime']/../../../../../..//h2)[1]")).getText();
        System.out.println(firstPrimeName);
        System.out.println(newPrimeName);
        System.out.println(newBrandName);
        Assert.assertNotEquals(newBrandName, firstPrimeName);
    }


    @AfterMethod
    public void teardown(){
        driver.quit();
    }
}

