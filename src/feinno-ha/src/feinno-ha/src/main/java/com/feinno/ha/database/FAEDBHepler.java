package com.feinno.ha.database;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FAEDBHepler {

	private static Logger LOGGER = LoggerFactory.getLogger(FAEDBHepler.class);

	/** 这是查询与FAE_Application表关联的有效的FAE_Appbean的SQL语句 */
	private static final String SQL_SELECT_FAE_APPBEAN_EFFECTIVE = "select b.BeanId,a.AppCategory,a.AppName,b.PackageId, b.GrayFactors from FAE_AppBean a left join FAE_Application b on a.AppCategory = b.AppCategory and a.AppName = b.AppName %s group by a.AppName, a.AppCategory, b.GrayFactors";

	private static final String SQL_SELECT_WORKE_PACKAGE_NAME_BY_PACKAGEID = "select PackageFile from HA_WorkerPackage where PackageId = ? ";

	/**
	 * 获得符合category名称的Appbeans内容，如果category为空，则表示所有有效的Appbeans
	 * 
	 * @param category
	 *            如果category为空，则表示所有有效的Appbeans
	 * @return
	 */
	public static JsonArray getAppbeans(String category) throws SQLException {
		LOGGER.info("Get appbeans. category = {}", category);
		JsonArray result = new JsonArray();
		category = category == null || category.length() == 0 ? "" : (" and a.AppCategory = " + category);
		String sql = String.format(SQL_SELECT_FAE_APPBEAN_EFFECTIVE, category);
		Database database = DatabaseManager.getDatabase("FAEDB");
		DataTable datatable = database.executeTable(sql);
		if (datatable.getRowCount() > 0) {
			for (DataRow row : datatable.getRows()) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("BeanId", row.getInt("BeanId"));
				jsonObject.addProperty("Category", row.getString("AppCategory"));
				jsonObject.addProperty("Name", row.getString("AppName"));
				String packageFile = getPackageFile(row.getInt("PackageId"));
				jsonObject.addProperty("PackageName", packageFile.split("name=")[0]);
				jsonObject.addProperty("GrayFactors", row.getString("GrayFactors"));
				result.add(jsonObject);
			}
		}
		return result;
	}

	/**
	 * 通过packageId获取packageFile路径
	 * 
	 * @param packageId
	 * @return
	 * @throws SQLException
	 */
	public static String getPackageFile(int packageId) throws SQLException {
		LOGGER.info("Get packageFile. packageId = {}", packageId);
		Database database = HADatabaseFactory.getHADatabase();
		DataTable datatable = database.executeTable(SQL_SELECT_WORKE_PACKAGE_NAME_BY_PACKAGEID,
				new Object[] { packageId });
		if (datatable.getRowCount() > 0) {
			return datatable.getRow(0).getString("PackageFile");
		}
		return null;
	}
}
