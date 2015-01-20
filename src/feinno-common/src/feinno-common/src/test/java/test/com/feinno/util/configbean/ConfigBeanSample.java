package test.com.feinno.util.configbean;

import java.util.Map;

import com.feinno.util.ConfigBean;
import com.feinno.util.ConfigPath;
@ConfigPath("")
public class ConfigBeanSample extends ConfigBean {

	private String name;

	private Map<String, ConfigBeanSampleChild> childs;

	private ConfigBeanSampleChild sampleChild;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, ConfigBeanSampleChild> getChilds() {
		return childs;
	}

	public void setChilds(Map<String, ConfigBeanSampleChild> childs) {
		this.childs = childs;
	}

	public ConfigBeanSampleChild getSampleChild() {
		return sampleChild;
	}

	public void setSampleChild(ConfigBeanSampleChild sampleChild) {
		this.sampleChild = sampleChild;
	}
	

}
