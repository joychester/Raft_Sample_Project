package raft.util;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
/*
 * Two usage examples in your test code:
 * MyWaiter myWaiter = new MyWaiter(driver);
   WebElement search = myWaiter.waitForMe(By.name("btnG"), 10);
   or
   if(!myWaiter.waitForMe(By.name("btnG"), 1, 10)) return;
   or if (!myWaiter.waitForMeDisappear(By.name("btnG"), 10)) return;
 */
public class MyWaiter {

	private WebDriver driver;
	
	public MyWaiter(WebDriver driver){
		this.driver = driver;
		
	}
	public WebElement waitForMe(By locatorname, int timeout){
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		return  wait.until(MyWaiter.presenceOfElementLocated(locatorname));
		
	}
	
	//Given certain number of web element to see if it is found within timeout
	public Boolean waitForMe(By locatorname, int count, int timeout) throws InterruptedException{
		long ctime = System.currentTimeMillis();
		
		while ((timeout*1000 > System.currentTimeMillis()- ctime)){
			List<WebElement> elementList = driver.findElements(locatorname);
			if ((elementList.size()< count)){
				Thread.sleep(300);
			}
			//element is found within timeout 
			else
				return true;
			
		}

		// otherwise element is not found within timeout
		return false;
	}
	
	//Given certain number of web element to see if it is disappear within timeout
	public Boolean waitForMeDisappear(By locatorname, int timeout) throws InterruptedException{
		long ctime = System.currentTimeMillis();
		
		while ((timeout*1000 > System.currentTimeMillis()- ctime)){
			List<WebElement> elementList = driver.findElements(locatorname);
			if ((elementList.size()!= 0)){
				Thread.sleep(300);
			}
			//element is Disappear within timeout 
			else
				return true;
			
		}

		// otherwise element is still show up within timeout
		return false;
	}
	
	public static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
		// TODO Auto-generated method stub
		return new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				if (driver.findElement(locator)!= null){
					return driver.findElement(locator);
				}
				else return null;
			}
		};
	}

}