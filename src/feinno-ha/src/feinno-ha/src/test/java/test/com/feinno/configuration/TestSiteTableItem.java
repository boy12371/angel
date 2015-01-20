package test.com.feinno.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

public class TestSiteTableItem extends ConfigTableItem {

	public TestSiteTableItem() {
		super();
	}

	@ConfigTableField(value = "id", isKeyField = true)
	public String id;
}
