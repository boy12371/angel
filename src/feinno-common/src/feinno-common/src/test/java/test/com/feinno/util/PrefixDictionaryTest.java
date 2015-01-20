package test.com.feinno.util;

import junit.framework.TestCase;

import com.feinno.initialization.InitialUtil;
import com.feinno.util.container.PrefixDictionary;


public class PrefixDictionaryTest extends TestCase{

	@Override
	protected void setUp() throws Exception {
		
		InitialUtil.init(PrefixDictionary.class);
	}

	public void testGet() {
		PrefixDictionary<String> prefix = new PrefixDictionary<String>();
		for (int i=0 ;i < 1000001; i ++) {
			String s = "go*.do/foo1";
			s = s.replace("*", String.valueOf(i));
			prefix.put(s, String.valueOf(i));
		}
		
		String target = "go1000000.do/foo1123123123";
		assertEquals("1000000", prefix.get(target));
	}
	
}
