package raft.util;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * Handle browser type. If user gives a browser type string, return a relevant real browser webdriver instance. 
 * 
 *
 */
public class WebDriverBasic {

	private boolean isIE;
	private boolean isFirefox;
	private boolean isChrome;
	private boolean isHtmlUnit;
	
	public boolean isIE() {
		return isIE;
	}
	public boolean isFirefox() {
		return isFirefox;
	}
	public boolean isChrome() {
		return isChrome;
	}
	public boolean isHtmlUnit() {
		return isHtmlUnit;
	}

	
	public WebDriverBasic(String browser) {
		parseBrowser(browser);
	}

	/**
	 * Portal. Give a browser type string, return a  WebDriver instance.
	 * @param browser browser type string (now, be "IE", "Firefox", "Chrome", "HtmlUnit") 
	 * @return WebDriver instance
	 */
	//return a new instance of real browser: IE, Firefox, ...
	public static WebDriver newWebDriverInstance(String browser) {
		return new WebDriverBasic(browser).getWebDriver();
	}

	//support Profile for FirefoxDriver
	public static WebDriver newWebDriverInstance(String browser, FirefoxProfile profile) {
		return new WebDriverBasic(browser).getWebDriver(profile);
	}
	
	/**
	 * Get a WebDriver instance by browser type string.
	 * @param browser browser type string, such as "Firefox"
	 * @return WebDriver instance, or null if not matched browser type string.
	 */
	public WebDriver getWebDriver() {
		WebDriver driver = null;
		if(isIE)
			driver = new InternetExplorerDriver(); 
		else if(isFirefox)
			driver = new FirefoxDriver();
		else if(isChrome)
			driver = new ChromeDriver();
		else if(isHtmlUnit)
			driver = new HtmlUnitDriver();
		
		return driver;
	}
	
	// if FirefoxProfile provided, load profile to FirefoxDriver only
	public WebDriver getWebDriver(FirefoxProfile profile) {
		WebDriver driver = null;
		if(isIE)
			driver = new InternetExplorerDriver(); 
		else if(isFirefox)
			driver = new FirefoxDriver(profile);
		else if(isChrome)
			driver = new ChromeDriver();
		else if(isHtmlUnit)
			driver = new HtmlUnitDriver();
		
		return driver;
	}
	
	/**
	 * Parse browser type.
	 * @param browser
	 */
	private void parseBrowser(String browser) {
		if("IE".equals(browser))
			isIE = true; 
		else if("Firefox".equals(browser))
			isFirefox = true; 
		else if("Chrome".equals(browser))
			isChrome = true; 
		else if("HtmlUnit".equals(browser))
			isHtmlUnit = true; 
		else 
			System.out.println("Your input browser type is : " + browser + 
					" . Now only support : IE, Firefox, Chrome, HtmlUnit.");
	}

}
