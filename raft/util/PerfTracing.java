package raft.util;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PerfTracing implements MethodInterceptor{

	private Logger logger = LoggerFactory.getLogger("PerfLog");
	
	@Override
	public Object invoke( MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		long start = System.nanoTime();

        try {
            return invocation.proceed();
        } 
        finally {
        	if(logger.isDebugEnabled()){
        		Object[] paramArray = {	invocation.getMethod().getName(), 
        								Arrays.toString(invocation.getArguments()), 
        								(System.nanoTime() - start) / 1000000};
            	logger.debug("Invocation of method: {} with parameters: {} took: {} ms." , paramArray);
        	}
        	
        }

	}

}
