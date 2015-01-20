package test.com.feinno.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableKey;

public class CFGSiteConfigTableKey extends ConfigTableKey {

	@ConfigTableField(value="SiteName",isKeyField=true)
	public String SiteName;
	
	@ConfigTableField(value="SiteType",isKeyField=true)
	public String SiteType;
	
	@Override
	public int hashCode() {
		return SiteName.hashCode()^SiteType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		CFGSiteConfigTableKey key = (CFGSiteConfigTableKey)obj;
		return SiteName.equals(key.SiteName) && SiteType.equals(key.SiteType);
	}

}
