package test.com.feinno.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

public class CFG_Carrier extends ConfigTableItem {
	
	public static final String NULL = "NIL";

	public static final int LITE_CARRIERID = 0;
	
	//[IICCodeTableField("CarrierId")]
		@ConfigTableField("CarrierId")
		private int carrierId;

		//[IICCodeTableField("CarrierName", IsKeyField = true)]
		@ConfigTableField(value = "CarrierName",isKeyField = true)
		private String carrierName = "";

		//[IICCodeTableField("ParentCarrier")]
		@ConfigTableField("ParentCarrier")
		private String parentCarrier = "";

		//[IICCodeTableField("CarrierNameResId")]
		@ConfigTableField("CarrierNameResId")
		private String carrierNameResId = "";

		//[IICCodeTableField("DefaultLanguage")]
		@ConfigTableField("DefaultLanguage")
		private IICLanguage defaultLanguage = IICLanguage.zhCN;

		public int getCarrierId() {
			return carrierId;
		}

		public String getCarrierName() {
			return carrierName;
		}

		public IICLanguage getDefaultLanguage() {
			return defaultLanguage;
		}
		/*public CFG_Carrier ParentCarrier
		{
			get { return string.IsNullOrEmpty(_parentCarrier) ? null : ServiceHelper.GetCarrier(_parentCarrier); }
		}*/
	/*	public CFG_Carrier getParentCarrier() throws ConfigurationNotFoundException {
			return (parentCarrier == null || parentCarrier.length() == 0) ? null : ServiceHelper.getCarrier(parentCarrier);
		}*/
		
		/*public string GetCarrierName(IICLanguage lang)
		{
			return ServiceHelper.GetGlobalizationText(_carrierNameResId, lang);
		}*/
		/*public String getCarrierNameResId(IICLanguage lang) throws ConfigurationNotFoundException {
			return ServiceHelper.getGlobalizationText(carrierNameResId, lang);
		}*/

		

		
		

	

		
}
