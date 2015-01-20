package test.com.feinno.util.configbean;

import java.util.Map;

import com.feinno.util.ConfigBean;

public class ConfigBeanSampleChild extends ConfigBean {

	private ConfigBeanSampleChild sampleChild;

	private Map<String, ConfigBeanSampleChild> childs;

	private String level;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public final ConfigBeanSampleChild getSampleChild() {
		return sampleChild;
	}

	public final void setSampleChild(ConfigBeanSampleChild sampleChild) {
		this.sampleChild = sampleChild;
	}

	public final Map<String, ConfigBeanSampleChild> getChilds() {
		return childs;
	}

	public final void setChilds(Map<String, ConfigBeanSampleChild> childs) {
		this.childs = childs;
	}

}
