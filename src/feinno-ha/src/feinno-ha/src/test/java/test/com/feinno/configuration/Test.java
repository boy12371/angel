/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import com.ibm.icu.util.Calendar;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class Test {

	@org.junit.Test
	public void test() {
		// /*
		// * Properties p = new Properties(); p.getProperty(new InputStream());
		// *
		// * DatabaseManager.getOperation(p);
		// */
		//
		// Random random = new Random(17);
		//
		// int centerNumber = 3;
		// int workerNumber = 100;
		//
		// Map<String, Integer> map = new HashMap<String, Integer>();
		// for (int i = 0; i < workerNumber; i++) {
		// Integer randomInteger = random.nextInt(centerNumber);
		// if (map.containsKey(Integer.toString(randomInteger))) {
		// map.put(Integer.toString(randomInteger),
		// map.get(Integer.toString(randomInteger)) + 1);
		// } else {
		// map.put(Integer.toString(randomInteger), 1);
		// }
		// // Integer integer = map.get(Integer.toString(i));
		// // if(integer == null){
		// // }
		// }
		//
		// System.out.println(map);
		// System.out.println(UUID.randomUUID().toString());
		// Calendar nowCalendar = Calendar.getInstance();
		// Calendar afterClaendar = Calendar.getInstance();
		// afterClaendar.add(Calendar.HOUR, 1);
		// afterClaendar.set(Calendar.MINUTE, 0);
		// afterClaendar.set(Calendar.MILLISECOND, 0);
		// long waitTime = afterClaendar.getTimeInMillis() -
		// nowCalendar.getTimeInMillis();
		// System.out.println(waitTime);
	}

	public static void main(String args[]) throws Exception {
		// Integer i = new Integer(1);
		// JsonObject jsonObject = new JsonObject();
		// jsonObject.addProperty("age", i);
		// System.out.println(jsonObject);
		// JsonArray dataArray = new JsonArray();
		// JsonPrimitive jsonPrimitive = new JsonPrimitive("aaa");
		// dataArray.add(jsonPrimitive);
		// dataArray.add(jsonPrimitive);
		// dataArray.add(jsonPrimitive);
		// jsonObject.add("dataArray", dataArray);
		// System.out.println(jsonObject);
		// System.out.println(dataArray.get(0));
		//
		// System.out.println(HexUtil.toHexString(ProtoManager.toByteArray("ANDR030037497042")));

		// System.out.println(HexUtil.toHexString(ProtoManager.toByteArray("327911017")));
		// System.out.println(HexUtil.toHexString(ProtoManager.toByteArray("266 M")));
		// System.out.println(HexUtil.toHexString(ProtoManager.toByteArray("258 M")));

		// ConfigParams reg = new
		// ConfigParams("app=(\\-GetContactInfo|\\-AddChatFriendInfo|\\-QueryScheduledTask|\\-UpdateScheduledTask|\\-CreateScheduledTask|\\-DeleteScheduledTask|\\-GetPartUserInfo|\\-DeleteMeetingByChangeMobileNo|\\-IfSendMeetingSmsLogic|\\-SaveSystemMsgText|\\-SendSystemMsgBatch|\\-AddRobotBuddy|\\-WatsReceiveContactList|\\-WatsSyncUserInfo|\\-WatsReceivePresenceChanged|\\-InternalMarketingManager|\\-MarketingManager|\\-MarketingDataNotify|\\-GetMarketingData|\\-ReverseDeleteBuddy|\\-GetBackDeleteBuddy|\\-AddBuddyGreeting|\\-AddBuddyRequest|\\-GetAddBuddyEventCount|\\-SystemNotifyV4|\\-DeleteBuddyRequest|\\-AddBuddyResponse|\\-GetUserInfo)$");
		// // ConfigParams reg = new
		// ConfigParams("app=(\\-GetContactInfo|\\-AddChatFriendInfo)$");
		// ConfigParams params1 = new ConfigParams("app=user-GetContactInfo");
		// System.out.println(params1.matchRegex(reg));

		// String str1 = "app=(\\-GetContactInfo|\\-AddChatFriendInfo)$";
		// String str2 =
		// "app=(\\-SaveSystemMsgText|\\-SendSystemMsgBatch|\\-AddRobotBuddy|\\-WatsReceiveContactList|\\-WatsSyncUserInfo|\\-WatsReceivePresenceChanged|\\-InternalMarketingManager|\\-MarketingManager|\\-MarketingDataNotify)$";
		// String str3 =
		// "app=(\\-GetMarketingData|\\-ReverseDeleteBuddy|\\-GetBackDeleteBuddy|\\-AddBuddyGreeting|\\-AddBuddyRequest|\\-GetAddBuddyEventCount|\\-SystemNotifyV4|\\-DeleteBuddyRequest|\\-AddBuddyResponse|\\-GetUserInfo)$";
		// ConfigParams reg = new ConfigParams(str1);
		// ConfigParams params1 = new ConfigParams("user-GetContactInfo");
		// System.out.println(params1.matchRegex(reg));

		// ConfigParams reg = new ConfigParams("service=^m.*");
		// ConfigParams params1 = new
		// ConfigParams("service=usmer-GetContactInfo");
		// System.out.println(params1.matchRegex(reg));

		// TimeZone tz = TimeZone.getTimeZone("GMT+08:00");// 获取中国北京时区
		// TimeZone.setDefault(tz);// 设置中国北京时区为默认时区
		//
		// String dateStr = "1900-01-01 00:00:00";
		//
		// Date date = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
		// long time = date.getTime();
		// System.out.println(date.getTime());
		// System.out.println((date.getTime() +
		// TimeZone.getDefault().getOffset(date.getTime())));
		// System.out.println((date.getTime() +
		// TimeZone.getDefault().getOffset(date.getTime())) * 10000);
		// ProtoManager.toByteArray(new Date());
		// System.out.println(date);
		// System.out.println(ProtoManager.parseFrom(new Date(),
		// ProtoManager.toByteArray(date)));

//		Date nowTime = Calendar.getInstance().getTime();
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowTime));
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.HOUR, 1);
//		Date newTime = calendar.getTime();
//		System.out.println("nowTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowTime));
//		System.out.println("newTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newTime));
//		System.out.println("nowTime.after(newTime) = " + nowTime.after(newTime));
		
		String address = "8613699223301";
		if (address.startsWith("86")) {
			address = address.substring(2);
		}else if(address.startsWith("+86")) {
			address = address.substring(3);
		}else if(address.startsWith("12520")) {
			address = address.substring(5);
		}
		System.out.println(address);
	}
}
