package raft.util;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import raft.listener.WebDriverLoggingListener;

public class MyLogger {
	
	public static PrintWriter grabLogger(){
		
		Method method = WebDriverPlus.getTestMethodInsideStackTrace(Thread.currentThread().getStackTrace());
		WebDriverLoggingListener wdlListener1 = WebDriverLoggingListener.getTmsl().getWebDriverLoggingListener(method);
		
		return wdlListener1.getLogger();
		
	}

}
