package raft.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.base.Function;
/*
 * Usage example in your test code:
 * MyWaiter myWaiter = new MyWaiter(driver);
   WebElement search = myWaiter.waitForMe(By.name("btnG"), 10);
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