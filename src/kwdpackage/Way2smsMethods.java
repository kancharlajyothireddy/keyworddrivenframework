package kwdpackage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Way2smsMethods
{
	public WebDriver driver;
	public WebDriverWait wait;
	public String launch(String e,String d,String c) throws Exception
	{
		if(e.equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver","chromedriver.exe");
			driver=new ChromeDriver();			
		}
		else if(e.equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver","geckodriver.exe");
			driver=new FirefoxDriver();
		}
		else if(e.equalsIgnoreCase("edge"))
		{
			System.setProperty("webdriver.edge.driver","MicrosoftWebDriver.exe");
			driver=new EdgeDriver();
		}
		else if(e.equalsIgnoreCase("opera"))
		{
			OperaOptions o=new OperaOptions();
			o.setBinary("C:\\Users\\gattu\\AppData\\Local\\Programs\\Opera\\58.0.3135.53\\opera.exe");
			System.setProperty("webdriver.opera.driver","operadriver.exe");
			OperaDriver driver=new OperaDriver(o);
		}
		else
		{
			return("Unknown browser");
		}
		driver.get(d);
		wait=new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("mobileNo")));
		driver.manage().window().maximize();
		return("Done");
	}
	public String fill(String e,String d,String c) throws Exception
	{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(e)));
		driver.findElement(By.xpath(e)).sendKeys(d);
		return("Done");
	}
	public String click(String e,String d,String c) throws Exception
	{
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(e)));
		driver.findElement(By.xpath(e)).click();
		return("Done");
	}
	public String validatelogin(String e,String d,String c) throws Exception
	{
		Thread.sleep(5000);
		try
		{
			if(c.equalsIgnoreCase("all_valid") && driver.findElement(By.xpath("//*[text()='Send SMS']")).isDisplayed())
			{
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[contains(@class,'logout')])[2]")));
				driver.findElement(By.xpath("(//*[contains(@class,'logout')])[2]")).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("mobileNo")));
				return("Valid data test passed");
			}
			else if(c.equalsIgnoreCase("mbno_blank") && driver.findElement(By.xpath("//*[text()='Enter your mobile number']")).isDisplayed())
			{
				return("Blank mobile number test passed");
			}
			else if(c.equalsIgnoreCase("mbno_wrong_size") && driver.findElement(By.xpath("//*[text()='Enter valid mobile number']")).isDisplayed())
			{
				return("Wrong size mobile number test passed");
			}
			else if(c.equalsIgnoreCase("mbno_invalid") && driver.findElement(By.xpath("//*[text()='Invalid Mobile Number']")).isDisplayed())
			{
				return("Invalid mobile number test passed");
			}
			else if(c.equalsIgnoreCase("pwd_blank") && driver.findElement(By.xpath("(//*[text()='Enter password'])[2]")).isDisplayed())
			{
				return("Blank password test passed");
			}
			else if(c.equalsIgnoreCase("pwd_invalid") && driver.findElement(By.xpath("(//*[contains(text(),'Try Again')])[1]")).isDisplayed())
			{
				return("Invalid password test passed");
			}
			else
			{
				String temp=this.screenshot();
				return("Test failed and go to "+temp);
			}
		}
		catch(Exception ex)
		{
			String temp=this.screenshot();
			return("Test interrupted and go to "+temp);
		}
	}
	public String closesite(String e,String d,String c) throws Exception
	{
		driver.close();
		return("Done");
	}
	public String screenshot() throws Exception
	{
		SimpleDateFormat sf=new SimpleDateFormat("dd-MM-yy-hh-mm-ss");
		Date dt=new Date();
		String ssname=sf.format(dt)+".png";
		File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File dest=new File(ssname);
		FileHandler.copy(src,dest);
		return("Done");
	}
}
