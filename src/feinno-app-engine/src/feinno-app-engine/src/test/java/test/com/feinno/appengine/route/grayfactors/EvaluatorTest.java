package test.com.feinno.appengine.route.grayfactors;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.ContextUri;
import com.feinno.appengine.route.gray.CondBuilder;
import com.feinno.appengine.route.gray.ParserException;
import com.feinno.appengine.route.gray.conds.Cond;

public class EvaluatorTest {
	/*
	 * random(0.1)	 类型无关	 随机括号中的比率进行随机匹配, random应用在任何field当中都是一样的.
equals(bj)	 值类型	 取值与括号中的值完全相等, 可以按照toString()后进行匹配
between(12, 20)	 值类型	 按照取值范围进行匹配, 字符串按照字符串顺序
in(100,200,300)	 值类型	 值包含在括号中
starts(cn.)	 字符串类型	 以括号中的值起始
ends(cn.)	 字符串类型	 以括号中的值结束
contains(cn.)	 字符串类型	 包含括号中的值
package(in.txt)	 值类型	 取值包含在取值包当中
	 */

	abstract class BaseContext extends AppContext {

		@Override
		public ContextUri getContextUri() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void decode(String uri, byte[] datas) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public byte[] encode(int demands) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void putNamedValue(String id, Object value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getSiteName() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Test
	public final void basic() throws ParserException {
		
		BaseContext testCtx = new BaseContext() {

			@Override
			public String getNamedValue(String key) {
				System.out.println("getting data from context, field " + key);
//				Assert.assertEquals("fieldname match", "field1", key);
				return "123456789";
			}
		};
		
		Cond cond = CondBuilder.parse("field1.contains(\"456\")");
		
		boolean res = cond.apply(testCtx);
		
		Assert.assertTrue("contains 456 ", res);
		
		Cond cond2 = CondBuilder.parse("field1.contains(456) and !field2.contains(123)");
		
		boolean res2 = cond2.apply(testCtx);
		
		Assert.assertTrue("contains 456 and contains 123 ", !res2);

//		Cond cond3 = Evaluator.eval("field3.contains(456) or field4.eq(999)");
		Cond cond3 = CondBuilder.parse("field4.equals(999) or field3.contains(\"456\")");
		
		boolean res3 = cond3.apply(testCtx);
		
		Assert.assertTrue("field4.eq(999) or field3.contains(\"456\")", res3);
		
		
	}
	
	@Test
	public final void parseFail(){
		
		try {
			CondBuilder.parse("field1.contains(123, \n" +
					"\"456\")");
			Assert.fail("should got an parser exception");
		} catch (ParserException e) {
			System.out.println(e.getMessage());
			Assert.assertTrue(e.getLocalizedMessage(), true);
		}
		
		try {
			CondBuilder.parse("field1.contains(\"456\", 123)");
			Assert.fail("should got an parser exception");
		} catch (ParserException e) {
			System.out.println(e.getMessage());
			Assert.assertTrue(e.getLocalizedMessage(), true);
		}		
		
		try {
			CondBuilder.parse("field1.contains(123.23, 123)");
			Assert.fail("should got an parser exception");
		} catch (ParserException e) {
			System.out.println(e.getMessage());
			Assert.assertTrue(e.getLocalizedMessage(), true);
		}		
	}
	
	@Test
	public final void equals(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "123";
			}
		};
		try {
			Assert.assertTrue(
					"int, 123 eq 123",
			CondBuilder.parse("field1.equals(123, 123)").apply(ctx)
			);

		} catch (ParserException e) {
			Assert.fail("int, 123 eq 123");
		}		
	}
	
	@Test
	public final void string(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "\"123::\t";
			}
		};
		try {
			String expStr = "field.equals(\"\\\"123::\\t\")";
			
			Assert.assertTrue(
					"String, 123 equals 123",
					CondBuilder.parse(expStr).apply(ctx)
			);

		} catch (ParserException e) {
			Assert.fail("String, 123 equals 123");
		}		
	}	
	
//	register("eq", new Equals());
	@Test
	public final void eqInt(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "123";
			}
		};
		try {
			String expInt = "field.equals(123)";
			
			Assert.assertTrue(
					"Int, 123 equals 123",
					CondBuilder.parse(expInt).apply(ctx)
			);
			
			String expStr = "field.equals(\"123\")";
			
			Assert.assertTrue(
					"Str, 123 equals 123",
					CondBuilder.parse(expStr).apply(ctx)
			);			

		} catch (ParserException e) {
			Assert.fail("equals");
		}		
	}	
//	register("contains", new Contains());
	@Test
	public final void contains(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "12345";
			}
		};
		try {
			String expInt = "field.contains(123)";
			
			Assert.assertFalse(
					"Int, 12345 contains 123",
					CondBuilder.parse(expInt).apply(ctx)
			);
			
			String expStr = "field.contains(\"123\")";
			
			Assert.assertTrue(
					"Str, 12345 contains 123",
					CondBuilder.parse(expStr).apply(ctx)
			);			

		} catch (ParserException e) {
			Assert.fail("contains");
		}		
	}		
//	register("ends", new Ends());
	@Test
	public final void ends(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "12345";
			}
		};
		try {
			String expInt = "field.ends(45)";
			
			Assert.assertFalse(
					"Int, 12345 ends 45",
					CondBuilder.parse(expInt).apply(ctx)
			);
			
			String expStr = "field.ends(\"45\")";
			
			Assert.assertTrue(
					"Str, 12345 ends 45",
					CondBuilder.parse(expStr).apply(ctx)
			);			

		} catch (ParserException e) {
			Assert.fail("ends");
		}		
	}		
//	register("starts", new Starts());
	@Test
	public final void starts(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "12345";
			}
		};
		try {
			String expInt = "field.starts(123)";
			
			Assert.assertFalse(
					"Int, 12345 starts 123",
					CondBuilder.parse(expInt).apply(ctx)
			);
			
			String expStr = "field.starts(\"123\")";
			
			Assert.assertTrue(
					"Str, 12345 starts 123",
					CondBuilder.parse(expStr).apply(ctx)
			);			

		} catch (ParserException e) {
			Assert.fail("starts");
		}		
	}		
//	register("in", new In());
	@Test
	public final void in(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "12345";
			}
		};
		try {
			String expInt = "field.in(123, 12345, 67890)";
			
			Assert.assertTrue(
					"Int, 12345 in 123, 12345, 67890",
					CondBuilder.parse(expInt).apply(ctx)
			);
			
			String expStr = "field.in(\"123\",\"12345\", \"67890\")";
			
			Assert.assertTrue(
					"Str, 12345 in 123, 12345, 67890",
					CondBuilder.parse(expStr).apply(ctx)
			);			

		} catch (ParserException e) {
			Assert.fail("in");
		}		
	}	
//	register("between", new Between());
	
	@Test
	public final void between(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				return "12345";
			}
		};
		try {
			String expInt = "field.between(12344, 12346)";
			
			Assert.assertTrue(
					"Int, 12345 between 12344, 12346",
					CondBuilder.parse(expInt).apply(ctx)
			);
			
			String expFloat = "field.between(123.44, 12346.00)";
			
			Assert.assertTrue(
					"float, field.between(123.44, 12346.00)",
					CondBuilder.parse(expFloat).apply(ctx)
			);			
			
			String expStr = "field.between(\"1\", \"a\")";
			
			Assert.assertTrue(
					"Str, 12345 between 123, 12345, 67890",
					CondBuilder.parse(expStr).apply(ctx)
			);		
			
			String faulty = "field.between(\"1\")";
			
			Assert.assertFalse(
					"faulty, single argument",
					CondBuilder.parse(faulty).apply(ctx)
			);				

		} catch (ParserException e) {
			Assert.fail("in");
		}		
	}		
//	register("random", new Random());	
	@Test
	public final void random(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				Assert.fail("should not be called");
				return null;
			}
		};
		try {
			String rand = "random(0.1)";
			int t = 0;
			Cond cond = CondBuilder.parse(rand);
			for(int i = 0; i < 100000; i++) {
			 if (cond.apply(ctx)){
				 t++;
			 }
			}
			
			float ration = t / 100000f;
			float diff = Math.abs(ration - 0.1f);
			System.out.println("diff is " + diff);
			Assert.assertTrue("about 0.1", diff < 0.01);
			
			t = 0;
			for(int i = 0; i < 100000; i++) {
			 if (CondBuilder.parse(rand).apply(ctx)){
				 t++;
			 }
			}
			float ration1 = t / 100000f;	
			float diff1 = Math.abs(ration1 - 0.1f);
			System.out.println("diff1 is " + diff1);
			Assert.assertTrue("about 0.1", diff1 < 0.01);			
		} catch (ParserException e) {
			Assert.fail("random");
		}		
	}	
	
	@Test
	public final void testVersion(){
		BaseContext ctx = new BaseContext() {
			@Override
			public String getNamedValue(String key) {
				//Assert.fail("should not be called");
				return "4.5.1.1130";
			}
		};
		try {
			String v = "version.equals(\"4.5.1.1130\")";
			Cond cond = CondBuilder.parse(v);
			int t = 0;
			for(int i = 0; i < 100000; i++) {
				 if (cond.apply(ctx)){
					 t++;
				 }
				}
			float ration = t / 100000f;
			float diff = Math.abs(ration - 0.1f);
			System.out.println("diff is " + diff);
			Assert.assertTrue("about 0.1", diff < 0.01);
			
			
		}catch(Exception ex)
		{
			
			ex.printStackTrace();
			Assert.fail("testVersion");
		}
	}
			
}
