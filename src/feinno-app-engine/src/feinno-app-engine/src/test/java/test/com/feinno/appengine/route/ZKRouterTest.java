//package test.com.feinno.appengine.route;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import com.feinno.appengine.AppContext;
//import com.feinno.appengine.ContextUri;
//import com.feinno.appengine.route.ZKRouter;
//public class ZKRouterTest {
//
////	@Test 
//	public static void main(String[] args) throws IOException {
//
//		AppContext ctx = new AppContext(){
//
//			@Override
//			public ContextUri getContextUri() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public void decode(String uri, byte[] datas) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public Object getNamedValue(String id) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public void putNamedValue(String id, Object value) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public String getSiteName() {
//				// TODO Auto-generated method stub
//				return TestDataGen.testLocalSiteName;
//			}
//
//			@Override
//			public byte[] encode(int demands)
//			{
//				throw new UnsupportedOperationException("没实现呢");
//			}
//			
//		};
//		
//		final ZKRouter r = new ZKRouter("127.0.0.1:2181", 2 * 1000);
////		r.init();
////		r.go();
//		System.out.println("input cat-name:");
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				System.in));
//		try {
//			String cn = null;
//			while ((cn = reader.readLine()) != null) {
////				if (cn.equals("close")) {
////					System.out.println("closing");
////					r.testClose();
////				}
//				String url = r.getServiceUrl(cn, ctx);
//				System.out.println("url is : " + url);
//				System.out.flush();
//			}
//			System.out.println("EXIT");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//	}	
//}
