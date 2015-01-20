package test.com.feinno.util.configbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.Test;

import com.feinno.util.ConfigBean;
import com.feinno.util.PropertiesUtil;

public class TestConfigBean {

	@Test
	public void test() throws Exception {

		Properties props = null;
		props = new Properties();
		// 閫氱敤绫诲瀷娴嬭瘯
		props.setProperty("driver", "com.mysql.jdbc.Driver");
		props.setProperty("url", "jdbc:mysql://localhost:3306/mysql");
		props.setProperty("username", "root");
		props.setProperty("password", "root");
		// 鏋氫妇娴嬭瘯
		props.setProperty("day", "Mon");
		// 涓ょ骇娴嬭瘯
		props.setProperty("b.b1", "1");
		props.setProperty("b.b2", "aaa");
		// 涓夌骇娴嬭瘯
		props.setProperty("b.b3.c1", "2");
		props.setProperty("b.b3.c2", "bbb");
		props.setProperty("b.b3.c3", "Sat");

		props.setProperty("map{k2}.b1", "3");
		props.setProperty("map{k2}.b2", "map2");
		props.setProperty("map{k2}.b3.c1", "33");
		props.setProperty("map{k2}.b3.c2", "ccc");
		props.setProperty("map{k2}.b3.c3", "sat");
		// 涓ょ骇{}{}娴嬭瘯
		props.setProperty("map{k1}.b1", "3");
		props.setProperty("map{k1}.b2", "map1");
		props.setProperty("map{k1}.b3.c1", "33");
		props.setProperty("map{k1}.b3.c2", "ccc");
		props.setProperty("map{k1}.b3.c3", "wed");
		props.setProperty("map{k1}.b4{kc1}.c1", "333");
		props.setProperty("map{k1}.b4{kc1}.c2", "ccc");
		props.setProperty("map{k1}.b4{kc1}.c3", "wed");
		props.setProperty("map{k1}.b4{kc2}.c1", "3333");
		props.setProperty("map{k1}.b4{kc2}.c2", "cccccc");
		props.setProperty("map{k1}.b4{kc2}.c3", "mon");
		props.setProperty("servicePorts{1}.1", "111111");
		props.setProperty("servicePorts{2}.1", "111112");

		// props.setPropert// 娣锋惌娴嬭瘯
		props.setProperty("childs{c1}.sampleChild.sampleChild.level", "寰堟湁娣卞害");
		props.setProperty("childs{c1}.sampleChild.fjdskla", "fdsafdsa");
		props.setProperty("childs{c1}.sampleChild.childs{opq}.childs{uvwvc,xz,/.vxcslva}.sampleChild.level",
				"钘忚繖涔堟繁锛岃兘鍙戠幇鎴戝悧");

		ConfigBeanSample configBeanSample = ConfigBean.valueOf(props, ConfigBeanSample.class);
		System.out.println("==========================User Object==========================");
		System.out.println(configBeanSample.getChilds().get("c1").getSampleChild().getSampleChild().getLevel());
		System.out.println(configBeanSample.getChilds().get("c1").getSampleChild().getFieldValue("fjdskla"));
		System.out.println(configBeanSample.getChilds().get("c1").getSampleChild().getChilds().get("opq").getChilds()
				.get("uvwvc,xz,/.vxcslva").getSampleChild().getLevel());
		System.out.println("=========================Default Object========================");
		ConfigBeanTree configBeanTree = new ConfigBeanTree();
		configBeanTree.printTree(configBeanSample);

		System.out.println("=========================XML Object========================");

		// String xmlPath =
		// "C:\\Users\\Administrator\\git\\feinno-ha\\src\\feinno-ha\\logging.xml";
		String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><logging>	<!-- 鍏ㄥ眬鏃ュ織绛夌骇 -->	<level>DEBUG</level>	<!-- 鍏ㄥ眬鏃ュ織缂撳瓨澶勭悊鏂瑰紡 -->	<cache enabled=\"false\" lazyMs=\"100\" batchCount=\"200\" />	<!-- 鍏ㄥ眬鏃ュ織杩囨护鍣�浠讳綍鏈塱sMultiple灞炴�涓簍rue鐨勫悓鍚嶈妭鐐归兘鍙互涓哄涓�鍥犳鍙互鏈夊涓繃婊ゅ櫒 -->	<filter isMultiple=\"true\" class=\"com.feinno.appengine.logging.GlobalFilter\" />	<!-- 鍏蜂綋鏌愪竴涓寘鎴栫被鐨勫鐞嗙粏鑺�-->	<loggers>		<logger isMultiple=\"true\" key=\"com/feinno/logging/spi\" level=\"ERROR\">			<filter isMultiple=\"true\" class=\"com.feinno.appengine.logging.AppContextFilter\" />			<filter isMultiple=\"true\" class=\"com.feinno.appengine.logging.AppContextFilter\" level=\"info\" />		</logger>		<logger isMultiple=\"true\" key=\"com/feinno/imps/SmsClient\" level=\"ERROR\">			<filter isMultiple=\"true\" class=\"com.feinno.appengine.logging.AppContextFilter\" level=\"info\" />		</logger>	</loggers>	<!-- 杈撳嚭鏂瑰紡鐨勯厤缃�-->	<appenders>		<!-- 鎺у埗鍙扮殑杈撳嚭鏂瑰紡 -->		<console enabled=\"true\" />		<!-- 鏂囨湰鐨勮緭鍑烘柟寮�-->		<text enabled=\"true\" path=\"D://log//test\" />		<!-- 鏁版嵁搴撶殑杈撳嚭鏂瑰紡 -->		<database enabled=\"true\">			<url>jdbc:mysql://127.0.0.1:3306/test</url>			<driver>com.mysql.jdbc.Driver</driver>			<database>test</database>			<table>FAE_LOG</table>			<user>root</user>			<password>123456</password>		</database>	</appenders></logging>";
		Properties properties = PropertiesUtil.xmlToProperties(xmlBody);
		ConfigBean bean1 = ConfigBean.valueOf(properties, ConfigBean.class);
		configBeanTree.printTree(bean1);
		println(properties);
		// String content = FileUtil.read(xmlPath).replaceAll("\"", "\\\\\"");
		// System.out.println(content.replaceAll("\"", "\\\""));
	}

	public static void println(Properties props) {

		List<String> listTemp = new ArrayList<String>();
		for (Entry<Object, Object> entry : props.entrySet()) {
			listTemp.add(entry.getKey() + " = " + entry.getValue());
		}
		Collections.sort(listTemp);
		for (String str : listTemp) {
			System.out.println(str);
		}
	}
}