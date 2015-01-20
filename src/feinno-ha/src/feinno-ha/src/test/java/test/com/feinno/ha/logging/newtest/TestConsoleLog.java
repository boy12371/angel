package test.com.feinno.ha.logging.newtest;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.logging.spi.LogManager;

public class TestConsoleLog {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConsoleLog.class);

	public static void main(String args[]) throws Exception {

		TestCounter.testLoggerCounter();

		Properties props = new Properties();
		props.load(new FileInputStream(TestConsoleLog.class.getResource("").getPath()+"\\logging.console.properties"));
		LogManager.loadSettings(props);
		
		for (int j = 0; j < 50; j++) {
			LOGGER.warn("第" + j + "圈完事了");
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread thread = new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < 20000000; i++) {
						TestLog2.writeLog();
						TestLog3.writeLog();
						TestLog4.writeLog();
						TestLog5.writeLog();
						try {
							Thread.sleep(1L);
							// if(i>0 && i<3000){
							// Thread.sleep(random.nextInt(4));
							// }else if(i>3000 && i<6000){
							// Thread.sleep(random.nextInt(55));
							// }else if(i>6000 && i<9000){
							// Thread.sleep(random.nextInt(5));
							// }else if(i>9000 && i<12000){
							// Thread.sleep(random.nextInt(30));
							// }else if(i>12000 && i<15000){
							// Thread.sleep(random.nextInt(6));
							// }else if(i>15000 && i<18000){
							// Thread.sleep(random.nextInt(25));
							// }else if(i>18000 && i<20000){
							// Thread.sleep(random.nextInt(4));
							// }
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.setName("test" + j);
			thread.start();
		}
		//
		// 运行中动态修改配置文件，检查原日志队列的监控线程是否会自动死掉，是否会启动新配置的监控队列与日志级别
		// Thread.sleep(10000L);
		// System.out.println("转文本记录");
		// Properties props = new Properties();
		// props.load(new FileInputStream("logging2.properties"));
		// LoggerContext.getInstance().loadSettings(props);
		//
		// Thread.sleep(10000L);
		// System.out.println("转数据库记录");
		// props.load(new FileInputStream("logging3.properties"));
		// LoggerContext.getInstance().loadSettings(props);
	}

}
