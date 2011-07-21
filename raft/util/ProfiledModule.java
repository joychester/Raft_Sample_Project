package raft.util;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;


public class ProfiledModule extends AbstractModule {

	@Override
	public void configure() {
		// TODO Auto-generated method stub
		PerfTracing perftracing = new PerfTracing();
		requestInjection(perftracing);
		bindInterceptor(
				Matchers.any(),
	            Matchers.annotatedWith(Profiled.class),
	            perftracing);
	    }

	}