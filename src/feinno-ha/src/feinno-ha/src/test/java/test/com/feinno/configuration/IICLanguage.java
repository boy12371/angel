package test.com.feinno.configuration;

public enum IICLanguage {
	
		zhCN(0x0804),
		zhHK(0x0C04),
		enUS(0x0409);
		
		private int value;
	    
	    private IICLanguage(int value){
			this.value = value;
		}
	    
	    public int value(){
	    	return value;
	    }
}
