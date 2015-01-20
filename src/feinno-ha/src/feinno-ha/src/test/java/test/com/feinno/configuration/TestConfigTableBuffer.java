/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-4-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTableRow;
import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class TestConfigTableBuffer {
	@SuppressWarnings("unchecked")
	@Test
	public void testBuffer() {
		HAConfigTableBuffer tb = new HAConfigTableBuffer();
		// tb.setTableName("config");
		// tb.setVersion(new Date());

		// List<String> s = new ArrayList();
		// for(int i = 0; i < 2; i++){
		// s.add(i + "a");
		// }

		List<HAConfigTableRow> tl = new ArrayList();
		for (int i = 0; i < 2; i++) {
			HAConfigTableRow t = new HAConfigTableRow();
			List<String> ts = new ArrayList();
			for (int j = 0; j < 2; j++) {
				ts.add(i + "" + j + "b");
			}
			t.setValues(ts);
			tl.add(t);
		}

		tb.setRows(tl);

		try {
			byte[] by = Serializer.encode(tb);
			System.out.println(by);

			HAConfigTableBuffer byT = Serializer.decode(
					HAConfigTableBuffer.class, by);
			System.out.println(byT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
