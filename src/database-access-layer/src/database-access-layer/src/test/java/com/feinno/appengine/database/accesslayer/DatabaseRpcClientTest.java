package com.feinno.appengine.database.accesslayer;

import java.io.IOException;
import java.sql.SQLException;

import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.database.DatabaseProxy;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.configuration.ConfigurationNotFoundException;
import com.feinno.database.DataReader;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.ha.ServiceSettings;
import com.feinno.imps.user.context.UserContext;
import com.feinno.imps.user.entity.UserInfo;
import com.feinno.imps.user.entity.enums.BuddyChangeMask;

public class DatabaseRpcClientTest {

	public static void main(String[] args) {
		try {
			ServiceSettings.init();
			AppEngineManager.INSTANCE.initialize();
			DatabaseProxy dbProxy = ResourceFactory.getDatabaseProxy("IICUPDB");

			testSpExcuteTable(dbProxy);
			testTinyInt(dbProxy);
			testNextResult(dbProxy);
			testGetByColumnLabel(dbProxy);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void testSpExcuteReader(DatabaseProxy dbProxy) throws IOException, SQLException {
		String spName = "USP_SetBuddyInfo";
		String[] spParams = { "@OwnerId", "@BuddyId", "@LocalName", "@OnlineNotify", "@BuddyLists",
		        "@FeikeReadVersion", "@RelationStatus", "@Permission", "@UpdateContactListVersion" };
		Object[] spValues = { 200034727, 200034848, "test3", null, null, null, null, null, true };

		DataReader drResult = null;
		UserContext context = new UserContext(560048162, 200034727, 6014);
		drResult = dbProxy.spExecuteReader(context, spName, spParams, spValues);
		int version = 0;
		if (drResult.read())
			version = drResult.getInt(1);

		System.out.println(version);
	}

	public static void testSpExcuteTable(DatabaseProxy dbProxy) throws IOException, SQLException {
		String spName = "USP_GetUserInfo";
		String[] spParams = { "@UserId", "@NeedUserEx", "@NeedServiceEx", "@NeedConfigEx" };
		Object[] spValues = { 200034727, false, false, false };

		UserContext context = new UserContext(250316634, 200034727, 6014);
		DataTable dtResult = dbProxy.spExecuteTable(context, spName, spParams, spValues);

		System.out.println(dtResult.getRowCount());
		System.out.println(dtResult.getRow(0).getObject(1));

	}

	public static void testTinyInt(DatabaseProxy dbProxy) throws IOException, SQLException,
	        ConfigurationNotFoundException {

		String spName = "USP_GetUserInfo";
		String[] spParams = { "@UserId", "@NeedUserEx", "@NeedServiceEx", "@NeedConfigEx" };
		Object[] spValues = { 200034727, false, false, false };

		UserContext context = new UserContext(250316634, 200034727, 6014);
		// DataTable table = dbProxy.spExecuteTable(context, spName, spParams,
		// spValues);

		DataReader drResult = null;
		drResult = dbProxy.spExecuteReader(context, spName, spParams, spValues);

		UserInfo userInfo = new UserInfo();
		if (drResult.read())
			userInfo.parserDataReader(drResult);
		else
			System.out.println("error : null");
	}

	public static void testNextResult(DatabaseProxy dbProxy) throws IOException, SQLException {
		// USP_GetPermission

		String spName = "USP_GetPermission";
		String[] spParams = { "@UserId", "@ContactId" };
		Object[] spValues = { 200034727, 200584280 };
		UserContext context = new UserContext(250316634, 200034727, 6014);
		// Object[] spValues = {
		// 200034819,
		// 200034701};
		// UserContext context = new UserContext(560048255,200034819,6401);
		DataReader drResult = null;

		drResult = dbProxy.spExecuteReader(context, spName, spParams, spValues);

		if (drResult.read())
			System.out.println(drResult.getObject(1));

		drResult.nextResult();
		if (drResult.read())
			System.out.println(drResult.getObject(1));
	}

	public static void testGetByColumnLabel(DatabaseProxy dbProxy) throws IOException, SQLException {
		// USP_GetPermission

		String spName = "USP_CheckQuota";
		String[] spParams = { "@UserId", "@QuotaType", "@DayLimit", "@MonthLimit", "@Increment" };
		// SiteC
		// Object[] spValues = {200034727,1,100,3000,1};
		//
		// UserContext context = new UserContext(250316634,200034727,6014);

		// SiteA
		Object[] spValues = { 200034819, 1, 100, 3000, 1 };
		UserContext context = new UserContext(560048255, 200034819, 6401);

		DataReader drResult = null;

		drResult = dbProxy.spExecuteReader(context, spName, spParams, spValues);

		if (drResult.read()) {
			System.out.println(drResult.getBoolean("Successed"));
			System.out.println(drResult.getInt("DayCount"));
		}

	}

}
