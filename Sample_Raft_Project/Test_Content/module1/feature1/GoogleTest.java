package module1.feature1;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import pagefactory.GooglePages.GoogleLandingPage;

import raft.util.LoadPara;
import raft.util.WebDriverPlus;

public class GoogleTest {

	@Test(invocationCount = 1)
	public void Test1(){
		
		WebDriverPlus driver = WebDriverPlus.newWebDriverPlusInstance(LoadPara.getGlobalParam("browser"));
		
		GoogleLandingPage landingPage = PageFactory.initElements(driver, GoogleLandingPage.class);
		
		landingPage.googleSearch(LoadPara.getLocalParam("querystring1"));
			
		driver.quit();
		
	}
	
}
