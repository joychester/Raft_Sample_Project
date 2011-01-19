package raft.util;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import raft.engine.TestEngine;
import raft.listener.WebDriverLoggingListener;

/**
 * A wrap instance of event firing WebDriver. Do some logging work for user.
 * 
 *
 */
public class WebDriverPlus extends EventFiringWebDriver {

	//private boolean isQuit;
	private WebDriver driver;
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
	
	
	private WebDriverPlus(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * Portal. Give a browser type string, return a  WebDriverPlus(which contains logging function) instance.
	 * @param browser browser type string (now, be "IE", "Firefox", "Chrome", "HtmlUnit") 
	 * @return
	 */
	public static WebDriverPlus newWebDriverPlusInstance(String browser) {
		return getLoggingWebDriver(new WebDriverBasic(browser).getWebDriver(), null);
	}

	//if user has multiple overload methods, please pass a Method object
	public static WebDriverPlus newWebDriverPlusInstance(String browser, Method method) {
		return getLoggingWebDriver(new WebDriverBasic(browser).getWebDriver(), method);
	}

	//support load profile if it is a FirefoxDriver
	public static WebDriverPlus newWebDriverPlusInstance(String browser, FirefoxProfile profile){
		return getLoggingWebDriver(new WebDriverBasic(browser).getWebDriver(profile), null);
	}
	
	public WebDriver getBrowserType() {
		return driver;
	}
	
	/**
	 * Return a logging WebDriver, registered a logging listener.
	 * When call this method, logging instance already exist.
	 * 
	 * precondition: TestEngine is running.
	 * 
	 */
	private static WebDriverPlus getLoggingWebDriver(WebDriver driver, Method method) {
		WebDriverPlus webDriverPlus = new WebDriverPlus(driver);
		webDriverPlus.driver = driver;
		parseBrowser(driver, webDriverPlus);
		
		if(method == null) {
			method = getTestMethodInsideStackTrace(Thread.currentThread().getStackTrace());
			if(method == null) throw new RuntimeException("You are not using our default test method writing regular. Sorry your request will not be handled.");
		}
		
		//WebDriver logging listener, instantiated when onTestStart(). this getLoggingWebDriver() action is called after onTestStart().
		WebDriverLoggingListener webDriverLoggingListener = (TestEngine.getTestMethodStatusListener()).getWebDriverLoggingListener(method); 
		
		/* //tell WebDriverLoggingListener the WebDriverPlus instance. and use webDriverPlus'
		//getWrappedDriver() to take screen shot, use webDriverPlus' override quit method to quit, both failed, I don't know why.
		webDriverLoggingListener.setDriverPlus(webDriverPlus);  
		*/
		
		webDriverLoggingListener.setDriverPlus(webDriverPlus);  //can use now. when test method failed, call can its quit() method.
		webDriverLoggingListener.setDriver(driver);
		webDriverPlus.register(webDriverLoggingListener);
		
		return webDriverPlus;  //super type is EventFiringWebDriver which wrapped WebDriver
	}	
	
	//if user do not pass a Method object, mean user abide our default rule: all 'test methods' under one 
	//class have different names and no parameters.
	//this method will return the right calling test method's Method object. (the first occurred @Test inside the stack trace)
	public static Method getTestMethodInsideStackTrace(StackTraceElement[] testElements) {
		Method method = null;
		for(StackTraceElement element : testElements) {
			Method innerMethod = null;
			try {
				innerMethod = Class.forName(element.getClassName()).getDeclaredMethod(
						element.getMethodName(), new Class[]{});
			} catch(Exception e) {} //ignore if method(which without paras) not found  
			 
			if( innerMethod != null && innerMethod.isAnnotationPresent(org.testng.annotations.Test.class) ) {
				method = innerMethod;
				break;
			}
		}
		
		//System.out.println("test method is: " + method);
		return method;
	}
	
	
	private static void parseBrowser(WebDriver driver, WebDriverPlus driverPlus) {
		if( driver instanceof InternetExplorerDriver ) {
			driverPlus.isIE = true;
		} else if( driver instanceof FirefoxDriver ) {
			driverPlus.isFirefox = true;
		} else if( driver instanceof ChromeDriver ) {
			driverPlus.isChrome = true;
		} else if( driver instanceof HtmlUnitDriver ) {
			driverPlus.isHtmlUnit = true;
		} else {
			System.out.println("Your WebDriver's type is none of IE,Firefox,Chrome,HtmlUnit.");
		}
	}


}
