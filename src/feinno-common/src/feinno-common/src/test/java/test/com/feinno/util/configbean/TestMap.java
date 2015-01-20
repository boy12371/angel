package test.com.feinno.util.configbean;

import java.util.Properties;

import com.feinno.util.ConfigBean;

public class TestMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		// props.setProperty("serverPorts{1}.1", "111");
		// props.setProperty("serverPorts{2}.1", "222");
		// props.setProperty("serverPorts{1.1}.1", "111");
		// props.setProperty("serverPorts{2}", "222");
		// props.setProperty("serverPorts{1.1}.1", "111");
		ConfigBean configBean = ConfigBean.valueOf(props, ConfigBean.class);
		// System.out.println(configBean);
		props.clear();
		// props.setProperty("metas.serverPorts{1.1}.a", "111");
		// props.setProperty("metas.serverPorts{1.1}", "111");
		// props.setProperty("metas.serverPorts{1.2}", "111");
		// metas.meta{appengine}.isMultiple=true,
		// metas.meta{appengine1}.isMultiple=true,
		// metas.meta{appengine1}.key=appengine1,
		// metas.meta{appengine}.key=appengine,
		// metas.meta{appengine1}=
		// metas.meta{appengine}=
		props.setProperty("meta{appengine2}", "00000000000");
		// props.setProperty("metas.meta{appengine}.isMultiple", "true");
		// props.setProperty("metas.meta{appengine}.key", "appengine");
		props.setProperty("meta{appengine1}", "11111111111");
		// props.setProperty("metas.meta{appengine1}.isMultiple", "true");
		// props.setProperty("metas.meta{appengine1}.key", "appengine1");
		configBean = ConfigBean.valueOf(props, ConfigBean.class);
		System.out.println(configBean.getChild("meta").getFieldValue("appengine1"));
		System.out.println(configBean.getChild("meta").getFieldValue("appengine2"));
		// if (configBean.getChild("metas") != null &&
		// configBean.getChild("metas").getChild("meta") != null) {
		// for (String key :
		// configBean.getChild("metas").getChild("meta").childKeySet()) {
		// String body =
		// configBean.getChild("metas").getChild("meta").getFieldValue(key);
		// System.out.println(body);
		// }
		// }
	}
}
