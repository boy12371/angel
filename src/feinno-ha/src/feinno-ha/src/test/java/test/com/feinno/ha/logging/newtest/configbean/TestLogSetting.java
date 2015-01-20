package test.com.feinno.ha.logging.newtest.configbean;

import java.io.File;

import org.junit.Test;

import com.feinno.logging.configuration.LogSettings;
import com.feinno.util.ConfigBean;
import com.feinno.util.PropertiesUtil;

public class TestLogSetting {

	@Test
	public void test() {
		File xmlFile = new File("logging.xml");
		LogSettings setting = ConfigBean.valueOf(PropertiesUtil.xmlToProperties(xmlFile), LogSettings.class);
		ConfigBeanTree tree = new ConfigBeanTree();
		tree.printTree(setting);
		System.out.println(setting.getFilter().get("0").getFieldValue("class"));
		// tree.printTree(setting.getChild("loggers"));
		// Iterable<ConfigBean> iterable =
		// setting.getChild("loggers").getChild("logger").childValues();
		// for (ConfigBean bean : iterable) {
		// System.out.println();
		// tree.printTree(bean);
		// }
		// System.out.println(setting.getChild("filter").getChild("0").getFieldValue("isMultiple"));
		// tree.printTree(setting.getChild("filter").getChild("0"));

	}

}
