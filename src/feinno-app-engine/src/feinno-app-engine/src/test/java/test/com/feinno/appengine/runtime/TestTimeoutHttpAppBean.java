package test.com.feinno.appengine.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.context.SessionContext;
import com.feinno.appengine.http.HttpAppBean;
import com.feinno.appengine.http.HttpAppTx;

@AppName(category = "sample", name = "sample")
public class TestTimeoutHttpAppBean extends HttpAppBean<SessionContext>
{

	private static final Logger logger = LoggerFactory.getLogger(TestTimeoutHttpAppBean.class);

	@Override
	public void process(HttpAppTx<SessionContext> tx) throws Exception
	{
		logger.debug(">>>>>> TestTimeoutHttpAppBean");
		Thread.sleep(10 * 1000); // 现在这个已经不超时了吧？
		tx.getResponse().getWriter().println("Long long time ago, I am invoked.");
		tx.end();
	}

	@Override
	public void setup()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void load()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void unload()
	{
		// TODO Auto-generated method stub

	}
}
