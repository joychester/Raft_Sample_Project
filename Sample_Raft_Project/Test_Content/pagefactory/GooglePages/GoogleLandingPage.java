/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pagefactory.GooglePages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

/**
 *
 * @author cheng.chi
 */

// To store Common elements and methods across all pages in Common page, to reduce the maintance work
public class GoogleLandingPage {

	private WebDriver driver;
	
    @FindBy(how = How.NAME, using = "q")
    WebElement searchtext;
    
    @FindBy(how = How.NAME, using = "btnG")
    WebElement searchbutton;
    
    public GoogleLandingPage(WebDriver driver){
    	this.driver = driver;
    	driver.get("http://www.google.com/");
    }
    

    public void googleSearch(String querytext){
    	searchtext.sendKeys(querytext);
    	searchbutton.click();
    }

}
