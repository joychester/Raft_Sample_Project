package raft.listener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.reporters.util.StackTraceTools;
import org.testng.xml.XmlSuite;

import raft.engine.TestEngine;
/**
 * User-define TestNG's report.
 * 
 * @author james.deng
 *
 */
public class TestReportListener implements IReporter {
	PrintWriter reporter;
	private TestMethodStatusListener testMethodStatusListener;
	
	public TestMethodStatusListener getTestMethodStatusListener() {
		return testMethodStatusListener;
	}

	public void setTestMethodStatusListener(TestMethodStatusListener testMethodStatusListener) {
		this.testMethodStatusListener = testMethodStatusListener;
	}
	public TestReportListener(TestMethodStatusListener testMethodStatusListener)
	{
		this.testMethodStatusListener = testMethodStatusListener;
	}
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		try {
			reporter = new PrintWriter(new FileOutputStream(new File(outputDirectory, "testReport.html")));
			reporter.println("<html>");
			createHead(reporter, "");			
			reporter.println("<body>");
			for(ISuite suite: suites) {
				createSummary(suite);  //one suite one table
				createDetail(suite);
				reporter.println("<br/><br/>");
			}
			reporter.println("<script type='text/javascript'>");
			reporter.println("var currRef;");
			reporter.println("function toggleException(ref) {");
			reporter.println("	if (currRef == ref) {");
			reporter.println("		// Hide");
			reporter.println("		currRef = null;");
			reporter.println("		document.getElementById('exception-display').style.display = 'none';");
			reporter.println("	} else {");
					// Show");
			reporter.println("		currRef = ref;");
			reporter.println("		document.getElementById('exception-display').style.display = 'block';");
			reporter.println("		document.getElementById('exception-display').innerHTML = ref.parentNode.getElementsByTagName('div').item(0).innerHTML;");
			reporter.println("	}");
			reporter.println("}");
			reporter.println("</script>");

			reporter.println("</body>");
			reporter.println("</html>");
			reporter.flush();
			reporter.close();
		} catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void createHead(PrintWriter reporter, String title) {
		reporter.println("<head>");
		
		//title
		if( title != null ) {
			reporter.println("<title>" + title + "</title>");
		}
		//style
		reporter.println("<style type=\"text/css\">");
		reporter.println(".invocation-failed,  .test-failed  { background-color: #DD0000; }");
		reporter.println(".invocation-percent, .test-percent { background-color: #006600; }");
		reporter.println(".invocation-passed,  .test-passed  { background-color: #00AA00; }");
		reporter.println(".invocation-skipped, .test-skipped { background-color: #CCCC00; }");	
		reporter.println("body {");
		reporter.println("	background:#EFEFEF;");
		reporter.println("	font-family: Arial, Helvetica, sans-serif;");
		reporter.println("}");
		reporter.println("h1,");
		reporter.println("h2 {");
		reporter.println("	text-align: center;");
		reporter.println("}");
		reporter.println("h2 span {");
		reporter.println("	color: #666;");
		reporter.println("}");
		reporter.println("hr {");
		reporter.println("	width: 98%");
		reporter.println("}");
		reporter.println(".report-border {");
		reporter.println("	border: #999 solid 1px;");
		reporter.println("	background: #FFF;");
		reporter.println("	padding: 0px 10px 0px;");
		reporter.println("}");
		reporter.println(".Pass {");
		reporter.println("	background: #BAFFA6;");
		reporter.println("}");
		reporter.println(".Error {");
		reporter.println("	background: #FF7D7D;");
		reporter.println("}");
		reporter.println(".Skip {");
		reporter.println("	background: #CCC;");
		reporter.println("}");
		reporter.println(".Fail {");
		reporter.println("	background: red;");
		reporter.println("}");
		reporter.println(".error-row {");
		reporter.println("	color: #FF7D7D;");
		reporter.println("}");
		reporter.println(".skip-row {");
		reporter.println("	color: #CCC;");
		reporter.println("}");
		reporter.println(".fail-row {");
		reporter.println("	color: red;");
		reporter.println("}");
		reporter.println(".overview-table {");
		reporter.println("	width: 600px;");
		reporter.println("	border-collapse: collapse;");
		reporter.println("}");
		reporter.println(".overview-table span {");
		reporter.println("	font-size: 12px;");
		reporter.println("	color: #333;");
		reporter.println("	font-weight: normal;");
		reporter.println("}");
		reporter.println(".overview-table td {");
		reporter.println("	text-align: center;");
		reporter.println("	font-size: 13px;");
		reporter.println("	font-weight: bold;");
		reporter.println("}");
		reporter.println(".total-cases {");
		reporter.println("	text-align: center;");
		reporter.println("	font-weight: bold;");
		reporter.println("	margin-bottom: 0px;");
		reporter.println("}");
		reporter.println(".total-cases span {");
		reporter.println("	font-weight: normal;");
		reporter.println("}");
		reporter.println(".total-time {");
		reporter.println("	text-align: center;");
		reporter.println("	font-size: 13px;");
		reporter.println("	color: #333;");
		reporter.println("	margin-top: 0px;");
		reporter.println("}");
		reporter.println(".total-time span {");
		reporter.println("	color: #666;");
		reporter.println("}");
		reporter.println(".details-table {");
		reporter.println("	border-collapse: collapse;");
		reporter.println("	border: #999 solid 1px;");
		reporter.println("	font-size: 13px;");
		reporter.println("}");
		reporter.println(".details-table th {");
		reporter.println("	background: #EBEBEB;");
		reporter.println("	text-align: left;");
		reporter.println("}");
		reporter.println(".details-table th,");
		reporter.println(".details-table td {");
		reporter.println("	border-right: #999 solid 1px;");
		reporter.println("	border-bottom: #999 solid 1px;");
		reporter.println("	padding: 2px 4px 2px;");
		reporter.println("}");
		reporter.println(".details-table span {");
		reporter.println("	font-size: 11px;");
		reporter.println("}");
		reporter.println(".exception-info {");
		reporter.println("	display: none;");
		reporter.println("}");
		reporter.println("#exception-display {");
		reporter.println("	font-family: 'Courier New', Courier, monospace;");
		reporter.println("	font-size: 12px;");
		reporter.println("	border: #333 solid 1px;");
		reporter.println("	background: #F2F2F2;");
		reporter.println("	padding: 10px;");
		reporter.println("	margin: 10px;");
		reporter.println("	display: none;");
		reporter.println("}");
		reporter.println("#exception-display p {");
		reporter.println("	margin-top: 0px;");
		reporter.println("}");
		reporter.println("</style>");	
		reporter.println("</head>");
	}
	
	//one suite, one summary.
	private void createSummary(ISuite suite) {
		int passCount = 0;
		int failCount = 0;
		int skipCount = 0;
		int errorCount =0;
		long totalTime = 0;
		int totalCount = 0;
		int passprc;
		int failprc;
		int errorprc;
		int skipprc;
		for(ISuiteResult sr : suite.getResults().values()) { //<test> tags
			ITestContext testContext = sr.getTestContext();
			passCount += testContext.getPassedTests().size();
			failCount += testContext.getFailedTests().size();
			skipCount += testContext.getSkippedTests().size();
			totalTime += testContext.getEndDate().getTime()- testContext.getStartDate().getTime();
			totalCount = passCount+failCount+skipCount+errorCount;
		}
		errorCount = testMethodStatusListener.getMethodErrorSetting().size();
		System.out.println("errorCount:" + errorCount);
		System.out.println("failCount:" + failCount);
		failCount = failCount - errorCount;
		passprc = passCount*100/totalCount;
		failprc = failCount*100/totalCount;
		errorprc =errorCount*100/totalCount;
		skipprc = skipCount*100/totalCount;
		reporter.println("<div class='report-border'>");
		reporter.println("  <h1>Report</h1>");
		reporter.println("  <h2>Test Suite Name: <span>"+suite.getName()+"</span></h2>");
		reporter.println("  <hr />");
		reporter.println("  <h3>Overview</h3>");
		reporter.println(" <table class='overview-table' align='center'>");
		reporter.println("  <tr>");
		reporter.println("    <td class='pass' width='"+passprc+"%'>"+passprc+"%<span>("+passCount+")</span></td>");
		reporter.println("      <td class='fail' width='"+failprc+"%'>"+failprc+"%<span>("+failCount+")</span></td>");
		reporter.println("      <td class='error' width='"+errorprc+"%'>"+errorprc+"%<span>("+errorCount+")</span></td>");
		reporter.println("      <td class='skip' width='"+skipprc+"%'>"+skipprc+"%<span>("+skipCount+")</span></td>");
		reporter.println(" 		</tr>");
		reporter.println("    <tr>");
		reporter.println("      <td>Pass</td>");
		reporter.println("      <td>Fail</td>");
		reporter.println("      <td>Error</td>");
		reporter.println("      <td>Skip</td>");
		reporter.println("    </tr>");
		reporter.println("  </table>");
		reporter.println("  <p class='total-cases'>Total Cases: <span>"+totalCount+"</span></p>");
		reporter.println("  <p class='total-time'>Total Time: <span>("+TestEngine.outputTimeConsumption(totalTime, 0)+")</span></p>");
		reporter.println("  <hr />");
	}
	//one suite, one detail.
	private void createDetail(ISuite suite) {
		reporter.println("<h3>Details</h3>");
		reporter.println("  <table class='details-table' align='center'>");
		reporter.println("    <tr>");
		reporter.println("      <th>Test Name</th>");
		reporter.println("      <th>Test Case</th>");
		reporter.println("      <th>Test Class</th>");
		reporter.println("      <th>Start Time</th>");
		reporter.println("      <th>Duration (mm:ss)</th>");
		reporter.println("      <th>Result</th>");
		reporter.println("      <th>ScreenShot</th>");
		reporter.println("      <th>Exception</th>");
		reporter.println("    </tr>");
		for(ISuiteResult sr : suite.getResults().values()) { //<test> tags
			ITestContext testContext = sr.getTestContext();
			//sr.getTestContext().getAllTestMethods();
			List<ITestResult> allTestMethodsResultList = new ArrayList<ITestResult>();
			allTestMethodsResultList.addAll(testContext.getPassedTests().getAllResults());
			allTestMethodsResultList.addAll(testContext.getFailedTests().getAllResults());
			allTestMethodsResultList.addAll(testContext.getSkippedTests().getAllResults());
			
			Collections.sort(allTestMethodsResultList, new ITestMethodComparator(ITestMethodComparator.STARTTIME));
			//reporter.println("<tr><td>Test Name: " + testContext.getName() + "</td><td></td><td></td><td></td><td></td><td></td></tr>");

			reporter.println("    <tr>");
			reporter.println("      <td rowspan='"+allTestMethodsResultList.size()+"'>"+testContext.getName()+"</td>");
			for(ITestResult tr : allTestMethodsResultList) {
				String classstr = "fail-row";
				String statusstr = "Fail";
				if(tr.getStatus() == ITestResult.SUCCESS) {
					classstr = "";
					statusstr = "Pass";
				}
				else if(tr.getStatus() == ITestResult.SKIP)
				{
					classstr = "skip-row";
					statusstr = "Skip";
				}
				else if(tr.getStatus() == ITestResult.FAILURE) 
				{
						classstr = (IsError(tr)?"error-row":"fail-row");
						statusstr = IsError(tr)?"Error":"Fail";
				}
				else if(tr.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) 
				{
					classstr = "";
					statusstr = "percent";
				}
				reporter.println("      <td class='"+classstr+"'>"+(new File(TestEngine.getReportRootdir()+getLogAddress(tr)).exists()?"<a href='"+getLogAddress(tr)+"'>"+tr.getMethod().getMethodName()+"</a>":tr.getMethod().getMethodName())+"</td>");
				reporter.println("      <td class='"+classstr+"'>"+tr.getMethod().getRealClass().getName()+"</td>");
				reporter.println("      <td class='"+classstr+"'>"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new java.util.Date(tr.getStartMillis()))+"</td>");
				reporter.println("      <td align='right' class='"+classstr+"'>"+ duration(tr.getEndMillis(), tr.getStartMillis())+"</td>");
				reporter.println("      <td class='"+statusstr+"'>"+statusstr+"</td>");
				reporter.println("      <td class='"+classstr+"'>"+(testMethodStatusListener.getScreenshotAddressMapping().get(tr)!=null?"<a href='"+getScreenshotAddress(tr)+"'>Screenshot</a>":"")+"</td>");
				reporter.println("      <td class='"+classstr+"'>");
				if(tr.getThrowable()!=null)
				{
					
					reporter.println("        <a href='javascript:;' onclick='toggleException(this);'>" +
							"Exception</a>");
					createException(tr.getMethod().toString(), 
							tr.getMethod().toString(), tr.getThrowable(), tr.getMethod());
				}
				reporter.println("</tr><tr>");	
			} 
			
		} //all <test> tags
		reporter.println("</tr></table><div id='exception-display'></div></div>");		  
		
	}
	
	/**
	 * Output the testMethod duration. 
	 * @param end end millisecond
	 * @param start start millisecond
	 * @return duration statistics string
	 */
	public static String duration(long end ,long start)
	{
		long diff = end - start;
		long minute = diff/60000;
		long second = diff/1000 - minute*60;
		String min = minute<10?"0"+minute:minute+"";
		String sec = second<10?"0"+second:second+"";
		String dur = min+":"+sec;
		return dur;
	}
	
	private void createException(String anchorName, String showName, Throwable exception, ITestNGMethod method) {
		generateExceptionReport(exception, method);
	}
	
	protected void generateExceptionReport(Throwable exception,ITestNGMethod method) {
		generateExceptionReport(exception, method, exception.getLocalizedMessage());
	}
	
	private void generateExceptionReport(Throwable exception,ITestNGMethod method,String title) {
		reporter.println("<div class='exception-info'>");
		reporter.println("<p>" + escape(title) + "</p>");
	    StackTraceElement[] s1= exception.getStackTrace();
	    Throwable t2= exception.getCause();
	    if(t2 == exception) {
	      t2= null;
	    }
	    int maxlines= Math.min(100,StackTraceTools.getTestRoot(s1, method));
	    for(int x= 0; x <= maxlines; x++) {
	    	reporter.println((x>0 ? "<br/>at " : "") + escape(s1[x].toString()));
	    }
	    if(maxlines < s1.length) {
	    	reporter.println("<br/>" + (s1.length-maxlines) + " lines not shown");
	    }
	    if(t2 != null) {
	      generateExceptionReport(t2, method, "Caused by " + t2.getLocalizedMessage());
	    }
	    reporter.println("</div></td>");
	}
	private String getLogAddress(ITestResult tr)
	{
		return "logs/log_"+tr.getMethod().getRealClass().getName()+"."+tr.getMethod().getMethodName()+"().txt";
	}
	private String getScreenshotAddress(ITestResult tr)
	{
		return "file:///"+testMethodStatusListener.getScreenshotAddressMapping().get(tr);
	}
	private boolean IsError(ITestResult tr)
	{
		boolean bool = false;
		for(ITestResult trs:testMethodStatusListener.getMethodErrorSetting())
		{
			
			if(trs.equals(tr))
			{
				bool = true;
				break;
			}	
		}
		return bool;
	}
	private static String escape(String string) {
	    if(null == string) return string;
	    return string.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
		  
	
	
	/** Sorts ITestNGMethod by Class name. */
	class ITestMethodComparator implements Comparator<ITestResult> {
		public final String sortBy;
		public final static String METHODNAME = "methodName"; //default
		public final static String STARTTIME = "startTime";
		
		public ITestMethodComparator(String sortBy) {
			this.sortBy = sortBy;
		}
		
		public int compare(ITestResult element1, ITestResult element2) {
			if(STARTTIME.equals(sortBy)) {
					return String.valueOf(element1.getStartMillis()).compareTo(String.valueOf(element2.getStartMillis()));
			}
			
			//System.out.println("methods sort by method name");
			return element1.getMethod().getMethodName().compareTo(element2.getMethod().getMethodName());
		}
	}
	
}



