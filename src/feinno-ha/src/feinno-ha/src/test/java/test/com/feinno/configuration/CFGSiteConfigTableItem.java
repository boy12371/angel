package test.com.feinno.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

public class CFGSiteConfigTableItem extends ConfigTableItem{
	
	@ConfigTableField(value="SiteName",isKeyField=true)
	public String SiteName;
	
	@ConfigTableField("SiteType")
	public String siteType;
	
	@ConfigTableField("Gateway")
	public String Gateway;

}
