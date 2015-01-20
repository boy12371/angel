package test.com.feinno.appengine.runtime;

import com.feinno.appengine.runtime.AppEngineServiceComponent;
import com.feinno.appengine.server.AppEngineServerComponent;
import com.feinno.ha.ServiceSettings;

public class AppEngineServiceComponentTest
{
	public static void main(String[] args) throws Throwable
	{
		//初始化 service settings
		//   ConfigurationManager的Loader会被自动set, 无需再显式设置
		//    LogManager的设置，也会被自动触发，无需显式设置 
		//     （约定配置文件名称为运行时目录下的 logging.properties）
		ServiceSettings.init();
		
		AppEngineServerComponent asc = new AppEngineServerComponent();
		asc.start();		
	}
}
