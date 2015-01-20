/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-2-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import test.com.feinno.serialization.entity.Entity;
import test.com.feinno.serialization.entity.TestEnum;
import test.com.feinno.serialization.entity.User;
import test.com.feinno.serialization.entity.UserInfo;

import com.feinno.serialization.Serializer;
import com.feinno.util.Guid;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class TestEntity {
	
	@Test
	public void testGuid(){
		byte[] by = {4,5,6,7,8,9,10,11};
		Guid g = new Guid();
		g.setData1(by);
		g.setData2(by);
//		g.setData3((short)3);
//		g.setData4(by);
		
		try {
			byte[] b = Serializer.encode(g);
			System.out.println(b);
			
			Guid a = Serializer.decode(Guid.class, b);
			System.out.println(a);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void testEntity() {
		Entity e = new Entity();
		e.setId(0);
		e.setName("name");
		e.setPwd("pwd");

		int[] n = new int[] { 1, 2, 3, 4, 5 };
		boolean[] bol = new boolean[] { true, false, true, false };
		e.setBolArray(bol);
		e.setIntArray(n);
		List<User> userList = new ArrayList<User>();

		for (int i = 0; i < 2; i++) {
			User user = new User();
			user.setAnIntTest(i + 1);
			user.setStringTest("Test" + (i + 1));
			userList.add(user);
			List<UserInfo> userInfoList = new ArrayList<UserInfo>();
			for (int j = 0; j < 2; j++) {
				UserInfo ui = new UserInfo();
				ui.setAddress("address" + (i + 1) + j);
				ui.setTel("tel" + (i + 1) + j);
				userInfoList.add(ui);
			}
			user.setUserInfo(userInfoList);
		}
		e.setUserList(userList);

		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		for (int i = 0; i < 2; i++) {
			UserInfo uiEntity = new UserInfo();
			uiEntity.setAddress(i + "askldjflsjadf");
			uiEntity.setTel("17234979823784");
			uiEntity.setTestEnum(TestEnum.TESTTWO);
			userInfoList.add(uiEntity);

		}
		e.setUserInfoList(userInfoList);

		UserInfo uInfo = new UserInfo();
		uInfo.setAddress("address");
		e.setUserInfo(uInfo);
		Entity ee = null;
		try {
			byte[] by = Serializer.encode(e);
			System.out.println("Entity = " + by);

			ee = Serializer.decode(Entity.class, by);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println(ee.getId() + "   " + ee.getName() + "   "
				+ ee.getPwd());
		for (User user : ee.getUserList()) {
			System.out.println("userList:" + user.getAnIntTest() + "  "
					+ user.getStringTest());

			for (UserInfo ui : user.getUserInfo()) {
				System.out.println("userInfoList:" + ui.getAddress() + "   "
						+ ui.getTel());
			}
		}

	}
}
