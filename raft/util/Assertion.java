package raft.util;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Common assertion utils.
 *
 */
public class Assertion {

	/** Whether the page contains the text.  assertTrue(Assertion(driver, "My Account"));  
	 * @throws AssertionException */
	public static boolean hasText(WebDriver driver, String text){
		return driver.getPageSource().contains(text);
	
 
	}

	
	/** Whether the page contains the text with a regular expression.  
	 *	assertTrue(Assertion(driver, "My Account.*"));  
	 */
	public static boolean matchText(WebDriver driver, String regex) {
		return driver.getPageSource().matches(regex);
	}
	
	/** Whether the page contains the element.  by means a way to location the element.
	 *  assertTrue(Assertion(driver, By.name("q"))); 
	 */
	public static boolean hasElement(WebDriver driver, By by) {
		List<WebElement> allElements = driver.findElements(by);
		return allElements != null && allElements.size() != 0;
	}
	
}
