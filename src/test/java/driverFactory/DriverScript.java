package driverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunction.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
	String inputpath = "./FileInput\\DataEngine.xlsx";
	String outputpath = "./FileOutput/HybridResults.xlsx";
	ExtentReports report;
	ExtentTest logger;
	String sheet = "MasterTestCases";
	WebDriver driver;
	@Test
	public void starttest() throws Throwable
	
	{
		String ModuleStatus ="";
		ExcelFileUtil xl = new ExcelFileUtil(inputpath);
		
		//iterate all in the sheet
		for (int i=1;i<=xl.rowCount(sheet);i++)
		{
			if(xl.getCellData(sheet, i, 2).equalsIgnoreCase("y"))
			{
				String TCModule = xl.getCellData(sheet, i, 1);
				report = new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.generateDate()+".html");
				logger = report.startTest(TCModule);
				logger.assignAuthor("Undavalli SaiAil");
				
				for(int j=1;j<=xl.rowCount(TCModule);j++)
				{
					//read each cell from TCModule
					String Description = xl.getCellData(TCModule, j, 0);
					String Object_Type = xl.getCellData(TCModule, j, 1);
					String Locator_Type =xl.getCellData(TCModule, j, 2);
					String Locator_Value = xl.getCellData(TCModule, j, 3);
					String Test_Data = xl.getCellData(TCModule, j, 4);
					try {
						if (Object_Type.equalsIgnoreCase("startBrowser"))
						{
							FunctionLibrary.startBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if (Object_Type.equalsIgnoreCase("openUrl"))
						{
							FunctionLibrary.openUrl();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("waitForElement"))
						{
							FunctionLibrary.waitForElement(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("validateTitle"))
						{
							FunctionLibrary.validateTitle(Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("closeBrowser"))
						{
							FunctionLibrary.closeBrowser();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("dropDownAction"))
						{
							FunctionLibrary.dropDownAction(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						
						if(Object_Type.equalsIgnoreCase("captureSock"))
						{
							FunctionLibrary.captureSock(Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("stockTable"))
						{
							FunctionLibrary.stockTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureSup"))
						{
							FunctionLibrary.captureSup(Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("supplerTable"))
						{
							FunctionLibrary.supplerTable();
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureCus"))
						{
							FunctionLibrary.captureCus(Locator_Value, ModuleStatus);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("customerTable"))
						{
							FunctionLibrary.customerTable();
							logger.log(LogStatus.INFO, Description);
						}
						
						
						xl.setCellData(TCModule, j, 5, "Pass", outputpath);
						logger.log(LogStatus.PASS, Description);
						ModuleStatus = "True";						
					} catch (Exception e) {
						System.out.println(e.getMessage());
						xl.setCellData(TCModule, j, 5, "Fail", outputpath);
						logger.log(LogStatus.FAIL, Description);
						ModuleStatus = "False";
					}
					if(ModuleStatus.equalsIgnoreCase("true"))
					{
						xl.setCellData(sheet, i, 3, "Pass", outputpath);
					}
					if(ModuleStatus.equalsIgnoreCase("False"))
					{
						//write as Fail into Sheet status cell
						xl.setCellData(sheet, i, 3, "Fail", outputpath);
					}
					report.endTest(logger);
					report.flush();
				}
				
			}
			else
			{
				//write as blocked into status cell for Testcases flag to N
				xl.setCellData(sheet, i, 3, "Blocked", outputpath);
			}
			
		}
		
		
		
	}
}
