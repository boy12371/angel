package test.com.feinno.appengine.http.server;

import java.io.File;

import test.com.feinno.appengine.runtime.TestFaultyHttpAppBean;
import test.com.feinno.appengine.runtime.TestHttpAppBean;
import test.com.feinno.appengine.runtime.TestTimeoutHttpAppBean;

import com.feinno.appengine.http.server.JettyServer;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;

public class TestServer {
	public static void main(String args[] ) throws Exception {
		ConfigurationManager.setConfigurator(new LocalConfigurator());
		JettyServer server = new JettyServer(); 
		server.registerBean("test-cat", new TestHttpAppBean());
		server.registerBean("test-faulty", new TestFaultyHttpAppBean());
		server.registerBean("test-timeout", new TestTimeoutHttpAppBean());
		server.registerWar(new File("./test1.war").getAbsolutePath());
		server.start();
	}
}