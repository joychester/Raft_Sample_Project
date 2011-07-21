package raft.listener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import raft.engine.TestEngine;
import raft.util.LoadPara;
import raft.util.WebDriverBasic;
import raft.util.XmlUtil;

/**
 * ITestListener, to listen the test method's status.
 *
 */
public class TestMethodStatusListener extends TestListenerAdapter {
	/**
	 * 
	 * A listener that gets invoked before and after a method is invoked by TestNG.
	 * This listener will only be invoked for configuration and test methods.
	 *
	 */
	
	//one class, one map(xml para file data)
	static Map<Class<?>, Map<String, String>> classMapMapping = new HashMap<Class<?>, Map<String, String>>();
	//one method, one logger(PrintWriter)
	static Map<ITestNGMethod, PrintWriter> methodLoggerMapping = new HashMap<ITestNGMethod, PrintWriter>();
	//one method, one log file(File)
	static Map<ITestNGMethod, File> methodFileMapping = new HashMap<ITestNGMethod, File>();
	//to record which test methods are Error! 
	private Set<ITestResult> methodErrorSetting = new HashSet<ITestResult>();
	//to record screenshot picture's absolute path
	private Map<ITestResult,String> screenshotAddressMapping = new HashMap<ITestResult,String>();

	private String browserType = LoadPara.getGlobalParam("browser");
	
	public static Map<ITestNGMethod, PrintWriter> getMethodLoggerMapping() {
		return methodLoggerMapping;
	}
	
	public Set<ITestResult> getMethodErrorSetting() {
		return methodErrorSetting;
	}
	
	public Map<ITestResult, String> getScreenshotAddressMapping() {
		return screenshotAddressMapping;
	}

	public void setScreenshotAddressMapping(
			Map<ITestResult, String> screenshotAddressMapping) {
		this.screenshotAddressMapping = screenshotAddressMapping;
	}



	static Map<Method, WebDriverLoggingListener> methodWDLListenerMapping = new HashMap<Method, WebDriverLoggingListener>();
	public WebDriverLoggingListener getWebDriverLoggingListener(Method method) {  //called by GetWebDriverPlus.getLoggingWebDriver(), register this instance.
		return methodWDLListenerMapping.get(method);
	}
	
	/*
	 * Set up local parameter for each test class;
	 * onStart will be Invoked after the test class is instantiated and before any configuration method is called.
	 */
	public void onStart(ITestContext testContext){
		
		try{
			ITestNGMethod[] arr = testContext.getAllTestMethods();
			Class<?> clazz =  arr[0].getRealClass();

			//load one map for one test class
			if( classMapMapping.get(clazz) == null ) {
				String className = clazz.getName();
				String localParaDir = TestEngine.getClassesRootdir(); //if no package name, put .class, .xml files under the same folder
				if( className.indexOf(".") != -1 ) {
					String strArr[] = className.split("\\.");
					localParaDir = "";
					for(int i=0; i<strArr.length-2; i++)
						localParaDir += strArr[i] + File.separator;
					localParaDir = TestEngine.getClassesRootdir() + localParaDir + strArr[strArr.length-2] + "_para" + File.separator; //if has ".", at least be split two parts. 
				}
				String localParaFile = localParaDir + className.substring(className.lastIndexOf(".")+1, className.length()) + ".xml";
				
				if( !new File(localParaFile).exists() ) {System.out.println("No local parameter file: " + localParaFile + " found."); return ; }
				
				System.out.println("Loading local parameters(" + localParaFile + ") ...");
				
				classMapMapping.put(clazz, XmlUtil.readXmlToMap(localParaFile, "//var", "name")); //test class <--> xml map data mapping
			
		}

	}catch( Exception e ) { e.printStackTrace(); throw new RuntimeException(e); }
	}
	
	/**
	 * New a WebDriverLoggingListener instance when test method start and update ITestResult info.
	 * 
	 * createLogger, startDaemonThread, load local parameters(load once for every test class), ...
	 * 
	 */
	synchronized public void onTestStart(ITestResult result) {
		//Clean IE Browsers at first, if using IE as testing browser
		try {
			System.out.println("Test start: " + result);
			
			WebDriverLoggingListener wdlListener = new WebDriverLoggingListener(this); //create an instance for every test method
			result.setAttribute("wdlListener", wdlListener);
			methodWDLListenerMapping.put(result.getMethod().getMethod(), wdlListener); //method <--> WDLListener mapping

			createLogger(result.getMethod(), true);
			wdlListener.setLogger(methodLoggerMapping.get(result.getMethod())); //notify logger
			wdlListener.setTestResult(result);
			
		}
		catch( Exception e ) { e.printStackTrace(); throw new RuntimeException(e); }
	}
	
	//close the test method's logger, and kill browser if user defined.
	public void onTestSuccess(ITestResult tr) {
		java.io.PrintWriter logger = getMethodLoggerMapping().get(tr.getMethod());
		logger.println(WebDriverLoggingListener.logDateFormat.format(new java.util.Date()) + ": "+tr.getMethod().getRealClass().getName()+"."+tr.getMethod().getMethodName()+" Method Success!"); 
		try {
			finishTestMethod(tr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onTestSkipped(ITestResult tr) {
		
		java.io.PrintWriter logger = getMethodLoggerMapping().get(tr.getMethod());
		logger.println(tr.getMethod().getMethodName()+"SKIPES");
		
	}
	
	public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {
		
		try {
			finishTestMethod(tr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//take browser page shot if test failed or on exception.
	public void onTestFailure(ITestResult tr) {

		//System.out.println("Entry onTestFailure");

		//print out error messages into log file
		java.io.PrintWriter logger = getMethodLoggerMapping().get(tr.getMethod());
		if( tr.getThrowable() != null ) {
			logger.print(WebDriverLoggingListener.logDateFormat.format(new java.util.Date()) + " Test Failed or OnException: "); //(IsError(tr.getMethod())?":test method have errors:":": test method failed: ") ); 
			tr.getThrowable().printStackTrace(logger);
		}

		//Assertion fail
		if(((WebDriverLoggingListener)tr.getAttribute("wdlListener"))!= null) {
			System.out.println("Got WebDriverLoggingListener");
				try {
					finishLogger(tr.getMethod());
					
					if (((WebDriverLoggingListener)tr.getAttribute("wdlListener")).getDriver()!= null){
						System.out.println("Got Driver");
						WebDriver driver = ((WebDriverLoggingListener)tr.getAttribute("wdlListener")).getDriver();
						
						//take screen shot on test fail
						if (!(LoadPara.getGlobalParam("TakingScreenshot")).equalsIgnoreCase("false")&&!tr.getThrowable().getMessage().contains("time-out")) {
							System.out.println("Take Screen shot on Fail!");
							((WebDriverLoggingListener)tr.getAttribute("wdlListener")).takeScreenshot(driver, logger, tr.getMethod() + "_onTestFail",tr);
						}
						
						if (killBrowserIfExistByBrowserType(browserType)){
								System.out.println("Browser process(" + browserType + ") Killed by Raft on Test Failed!" );
								System.out.println("Test failed already due to TestNG exception/Assertion Fail!");
						}
						
					}
				
					//webDriver exception called, will set driver to null
					if (killBrowserIfExistByBrowserType(browserType)){
						System.out.println("Browser process(" + browserType + ") Killed by Raft on Test Error!" );
						System.out.println("Test Error already due to WebDriver onexception called!");
					}
				
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		}
				
			return ;
		}
		
		// TestNG Exception or WebDriver onException caught!
		else {
			try {
				System.out.println("No WebDriverLoggingListener Found");
				finishLogger(tr.getMethod());
			
				if (killBrowserIfExistByBrowserType(browserType)){
					System.out.println("Browser process(" + browserType + ") Killed by Raft on Test Finished!" );
					System.out.println("Test failed already due to TestNG exception/Assertion Fail!");
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * finishLogger, quit the driver ...
	 * 
	 * @param tr
	 * @throws Exception 
	 */
	public void finishTestMethod(ITestResult tr) {
	
					try {
						finishLogger(tr.getMethod());
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}

	
	//Invoked after all the tests have run and all their Configuration methods have been called. 
	public void onFinish(ITestContext testContext) {
		//To-do something on further demand		
		try {
			if (killBrowserIfExistByBrowserType(browserType)){
					System.out.println("Browser process(" + browserType + ") Killed by Raft on Test Finished!" );
					//System.out.println("Test Method: <" + tr.getMethod().getMethodName() + ">Success!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the image name by browser type string. 
	 * @param browser browser type string
	 * @return respond image name, null if not matched browser type string.
	 */
	public String getImageName(String browser) {
		WebDriverBasic driverAgent = new WebDriverBasic(browser);
		String imageName = null;
		if( driverAgent.isIE() ) imageName="iexplore.exe";
		else if( driverAgent.isFirefox() ) imageName="firefox.exe";
		else if( driverAgent.isChrome() ) imageName="chrome.exe";
		else if( driverAgent.isHtmlUnit() ) ; //"javaw.exe", ignore it.
		
		return imageName;
	}
	
	public boolean isBrowerProcessExist(String imageName) throws Exception {
		return callSystemCmd("TASKLIST /FI \"IMAGENAME eq " + imageName + "\"").toLowerCase().contains(imageName); 
	}

	public void killBrowserProcess(String imageName) throws Exception {
		callSystemCmd("TASKKILL /F /IM " + imageName + " /T");
	}
	
	public boolean killBrowserIfExistByBrowserType(String browser) throws Exception {
		boolean killed = false;
		String imageName = getImageName(browser); 
		if( imageName != null && isBrowerProcessExist(imageName) && "true".equalsIgnoreCase(LoadPara.getGlobalParam("autoBrowserKiller"))) {
			killBrowserProcess(imageName);
			killed = true;
		}
		return killed;
	}
	
	public String callSystemCmd(String cmdLine) throws Exception {
		return pToConsole(Runtime.getRuntime().exec(cmdLine));
	}
	
	/**
	 * Return the output stream of a java calling system commands.
	 * @param p calling process
	 * @return string of this process's output stream
	 * @throws Exception
	 */
	public String pToConsole(Process p) throws Exception {
		String line=null;
		BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuffer strBuf = new StringBuffer();
		while((line=br.readLine())!=null) {
			//System.out.println(line);
			strBuf.append(line);
			strBuf.append("\n");
		}
		br.close();
		
		return strBuf.toString();
	}
	
	
	/**
	 * Create a logger for every method.
	 * 
	 * @param method test method
	 * @param append append or overwrite
	 * @throws Exception
	 */
	public void createLogger(ITestNGMethod method, boolean append) throws Exception { //called by beforeInvocation, so don't know whether listener be initialized.
		File file = new File(TestEngine.getLoggerRootdir(), "log_" + method + ".txt");
		methodFileMapping.put(method, file);
		methodLoggerMapping.put(method, new PrintWriter(new FileOutputStream(file, append )) );
	}
	
	/**
	 * Flush and close the logger, if file is empty, delete it.
	 * 
	 * @param method test method
	 * @throws Exception
	 */
	public void finishLogger(ITestNGMethod method) throws Exception {
		//if test method status is skipped, won't call onTestStart(), so no logger created.
			if( methodLoggerMapping.get(method) != null ) {
				methodLoggerMapping.get(method).flush();
				methodLoggerMapping.get(method).close();
				if( methodFileMapping.get(method).length() == 0 )
					methodFileMapping.get(method).delete();
			}
	}

	/**
	 * Get parameter value via a className and parameter name
	 * @param className test class name
	 * @param name parameter name
	 * @return parameter value
	 */
    public static String getParam(String className, String name) {
    	Class<?> clazz = null;
    	try {
    		clazz = Class.forName(className);
		} catch(Exception e) { throw new RuntimeException(e); }
		
		return getParam(clazz, name);
    }
    
    /**
     * Get parameter value via a Class object and parameter name
     * @param clazz test class
     * @param name parameter name
     * @return parameter value
     */
	public static String getParam(Class<?> clazz, String name) {
		if(classMapMapping.get(clazz) == null) return "";
		else return classMapMapping.get(clazz).get(name);
	}
}
