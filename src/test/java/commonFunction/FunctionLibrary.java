package commonFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class FunctionLibrary {
	public static WebDriver driver;
	public static Properties conpro;
	
	
	//method for launching browser
	public static WebDriver startBrowser() throws Throwable
	{
		conpro = new Properties();
		conpro.load(new FileInputStream("./PropertyFiles\\Envinorment.properties"));
		if(conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else 
		{
			Reporter.log("Browser valuse is not matching",true);
		}
		return driver;
	}
	//Method for launching URL
	public static void openUrl()
	{
		driver.get(conpro.getProperty("Url"));
		
	}
	//method for to wait any web element
	public static void waitForElement( String LocatorType,String LocatorValue,String TestData)
	{
		
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("name"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
			
	}
	//method for to write text boxes
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}

	}
	
	//method foe to click any button,check boxes,links & images
	public static void clickAction(String LocatorType,String LocatorValue)
	
	{
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);		
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
	}
	//method to validate title
	public static void validateTitle(String ExpTitle)
	{
		
		String Act_title = driver.getTitle();
		try {
		Assert.assertEquals(Act_title, ExpTitle, "Title is not matching");
		}
		catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	
		
	}
	//method to close browser
	public static void closeBrowser()
	{
		driver.quit();
	}
	
	//method for date generate
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd hh-mm");
		return df.format(date);
	}
	
	//method for list boxes
	public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}

		if(LocatorType.equalsIgnoreCase("name"))
		{
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}

		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}
	}
	
	//method for capturing stock number into notepad
	public static void captureSock(String LocatorType,String LocatorValue,String TestData) throws Throwable
	{
		String stock_Num="";
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stock_Num=driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stock_Num=driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stock_Num=driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		
		FileWriter fw = new FileWriter("./CaptureData/stocknumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stock_Num);
		bw.flush();
		bw.close();
		
	}
	
	//method for stock table validation
	public static void stockTable() throws Throwable
	{
		//read data from notepad
		FileReader fr = new FileReader("./CaptureData/stocknumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_data = br.readLine();
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
		
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_data =  driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log("Act_data:--->"+Act_data+"==================="+"Exp_data:---->"+Exp_data,true);
		try {
		Assert.assertEquals(Act_data, Exp_data, "Stock Number is not matching");
		}catch (AssertionError e) {
			System.out.println(e.getMessage());		
			}
		
	}
	
	//method for capture supplier number into notepad
	public static void captureSup(String LocatorType,String LocatorValue) throws Throwable
	{
		String SupplierName="";
		
		if (LocatorType.equalsIgnoreCase("xpath"))
		{
			SupplierName = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if (LocatorType.equalsIgnoreCase("id"))
		{
			SupplierName = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		if (LocatorType.equalsIgnoreCase("name"))
		{
			SupplierName = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		//Write Suppler number into notepad
		FileWriter fw = new FileWriter("./CaptureData/SupplierNUmber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(SupplierName);
		bw.flush();
		bw.close();
		
	}
	
	//method for supplier table
	public static void supplerTable() throws Throwable
	{
		//read data from notepad
				FileReader fr = new FileReader("./CaptureData/SupplierNUmber.txt");
				BufferedReader br = new BufferedReader(fr);
				String Exp_data = br.readLine();
				
				if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
					
					driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
					driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
					driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
					driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
					Thread.sleep(3000);
					
					String Act_data =  driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
					Reporter.log("Act_data:--->"+Act_data+"==================="+"Exp_data:---->"+Exp_data,true);
					try {
					Assert.assertEquals(Act_data, Exp_data, "Supplier Number is not matching not matching");
					}catch (AssertionError e) {
						System.out.println(e.getMessage());		
						}
					
	}
	
	//method for capture Customer number into notepad
		public static void captureCus(String LocatorType,String LocatorValue) throws Throwable
		{
			String CustomerName="";
			
			if (LocatorType.equalsIgnoreCase("xpath"))
			{
				CustomerName = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
			}
			if (LocatorType.equalsIgnoreCase("id"))
			{
				CustomerName = driver.findElement(By.id(LocatorValue)).getAttribute("value");
			}
			if (LocatorType.equalsIgnoreCase("name"))
			{
				CustomerName = driver.findElement(By.name(LocatorValue)).getAttribute("value");
			}
			//Write Cutsomer number into notepad
			FileWriter fw = new FileWriter("./CaptureData/SupplierNUmber.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(CustomerName);
			bw.flush();
			bw.close();
			
		}
		
		//method for supplier table
		public static void customerTable() throws Throwable
		{
			//read data from notepad
					FileReader fr = new FileReader("./CaptureData/SupplierNUmber.txt");
					BufferedReader br = new BufferedReader(fr);
					String Exp_data = br.readLine();
					
					if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed())
						
						driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
						driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
						driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_data);
						driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
						Thread.sleep(3000);
						
						String Act_data =  driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
						Reporter.log("Act_data:--->"+Act_data+"==================="+"Exp_data:---->"+Exp_data,true);
						try {
						Assert.assertEquals(Act_data, Exp_data, "Supplier Number is not matching not matching");
						}catch (AssertionError e) {
							System.out.println(e.getMessage());		
							}
						
		}
		
	
	
	
	
	




	
	
	
	
	
	

}
