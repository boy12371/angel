//package test.com.feinno.configuration.dynamic;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import test.com.feinno.configuration.TestHaConfigurator;
//
//public class TestDynamicTableClient {
//
//	@Before
//	public void setUp() {
//		TestHaConfigurator thc = new TestHaConfigurator();
//		thc.registerService();
//	}
//
//	@Test
//	public void testDynamicTableManager() throws Exception {
//
////		DynamicTableManager.setLoader(new HAConfigurationLoader("worker01",
////				"test", "tcp://127.0.0.1:11211"));
////		DynamicTableManager.processData(String.class, TestDynamicTable.class,
////				"testtable",
////				new DynamicTableAction<String, TestDynamicTable>() {
////
////					@Override
////					public boolean process(
////							ConfigTable<String, TestDynamicTable> table) {
////						Assert.assertTrue(table.getTableName().equals(
////								"testtable"));
////						return true;
////					}
////
////				});
//	}
//
//}
