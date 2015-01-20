/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.configuration.ConfigTableKey;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigurationFailedException;
import com.feinno.configuration.ConfigurationNotFoundException;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.AnnotationHelper;
import com.feinno.util.EnumParser;
import com.feinno.util.Flags;
import com.feinno.util.ObjectHelper;

/**
 * 保存ConfigTable数据的序列化缓冲类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAConfigTableBuffer extends ProtoEntity
{
	@ProtoMember(1)
	private String tableName;

	@ProtoMember(2)
	private Date version;

	@ProtoMember(3)
	private List<String> columns = new ArrayList<String>();

	@ProtoMember(4)
	private List<HAConfigTableRow> rows = new ArrayList<HAConfigTableRow>();

	@ProtoMember(value = 5, required = false)
	private long dynamicTableVersion;

	public long getDynamicTableVersion()
	{
		return dynamicTableVersion;
	}

	public void setDynamicTableVersion(long dynamicTableVersion)
	{
		this.dynamicTableVersion = dynamicTableVersion;
	}

	public Date getVersion()
	{
		return version;
	}

	public void setVersion(Date version)
	{
		this.version = version;
	}

	public List<String> getColumns()
	{
		return columns;
	}

	public void setColumns(List<String> columns)
	{
		this.columns = columns;
	}

	public List<HAConfigTableRow> getRows()
	{
		return rows;
	}

	public void setRows(List<HAConfigTableRow> rows)
	{
		this.rows = rows;
	}

	public int rowCount()
	{
		return rows.size();
	}

	public HAConfigTableRow getRow(int i)
	{
		return rows.get(i);
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * 
	 * 将缓冲区转换为最终可用的Hashtable
	 * 
	 * @param <K>
	 * @param <V>
	 * @param keyType
	 * @param valueType
	 * @return
	 * @throws ConfigurationNotFoundException
	 * @throws ConfigurationFailedException
	 */
	@SuppressWarnings("unchecked")
	public <K, V extends ConfigTableItem> ConfigTable<K, V> toTable(Class<K> keyType, Class<V> valueType) throws ConfigurationNotFoundException, ConfigurationFailedException
	{
		int columnCount = this.getColumns().size();
		// int keyCount = keyType.getDeclaredFields().length;
		Hashtable<K, V> innerTable = new Hashtable<K, V>();
		boolean simpleKey = !(keyType.getSuperclass().equals(ConfigTableKey.class));

		if (simpleKey) {
			// TODO 增加简单类型检查
			// if (!keyType.isPrimitive() && !keyType.equals(String.class)) {
			// throw new
			// IllegalArgumentException("No-simple type must extends from ConfigTableKey:"
			// + keyType.getName());
			// }
		}
		Field[] valueFields = new Field[columnCount];
		ConfigTableField[] valueAttrs = new ConfigTableField[columnCount];

		ConfigTableField[] keyAttrs = new ConfigTableField[columnCount];
		Field[] keyFields = new Field[columnCount];

		Field[] fields = valueType.getDeclaredFields();
		for (Field field : fields) {
			ConfigTableField attr = AnnotationHelper.tryGetAnnotation(ConfigTableField.class, field);
			// boolean find = false;
			if (attr != null) {
				for (int i = 0; i < columnCount; i++) {
					if (this.getColumns().get(i).equals(attr.value())) {
						// find = true;
						valueFields[i] = field;
						valueAttrs[i] = attr;
						if (simpleKey && attr.isKeyField()) {
							keyFields[i] = field;
						}
						break;
					}
				}
			}
			/*
			 * if (!find && attr.required()) { throw new
			 * ConfigurationNotFoundException(getTableName(), "", attr.value());
			 * }
			 */
		}
		if (!simpleKey) {
			fields = keyType.getDeclaredFields();
			for (Field field : fields) {
				ConfigTableField attr = AnnotationHelper.tryGetAnnotation(ConfigTableField.class, field);
				boolean find = false;
				if (attr != null) {
					for (int i = 0; i < columnCount; i++) {
						if (this.getColumns().get(i).equals(attr.value())) {
							keyFields[i] = field;
							keyAttrs[i] = attr;
							find = true;
							break;
						}
					}
					if (!find) {
						throw new ConfigurationNotFoundException(ConfigType.TABLE, getTableName(), attr.value());
					}
				}
			}
		}
		if (this.getRows() != null) // 这都不能兼容，小心
		{
			for (HAConfigTableRow row : this.getRows()) {
				try {
					Object key = null;
					Object value;

					value = valueType.newInstance();

					if (!simpleKey)
						key = keyType.newInstance();

					for (int i = 0; i < columnCount; i++) {
						if (valueFields[i] == null)
							continue;

						String valStr = row.getValue(i);
						if (valueAttrs[i].trim() && valStr != null)
							valStr = valStr.trim();
						Class<?> clazz = valueFields[i].getType();
						Object fieldValue = null;
						if (clazz.equals(Flags.class)) {
							try {
								// int n = Integer.parseInt(valStr);
								fieldValue = EnumParser.parseFlags(clazz, valStr, true);
							} catch (Exception e) {
								ParameterizedType pt = (ParameterizedType) valueFields[i].getGenericType();
								Type[] aType = pt.getActualTypeArguments();
								String s = aType[0].toString().split(" ")[1];
								Class<?> genericClass = Class.forName(s);
								fieldValue = EnumParser.parseFlags(genericClass, valStr, true);
							}
						} else
							fieldValue = ObjectHelper.convertTo(valStr, clazz);
						// Object fieldValue = null;
						valueFields[i].setAccessible(true);
						valueFields[i].set(value, fieldValue);
						if (keyFields[i] != null) {
							if (simpleKey) {
								key = fieldValue;
							} else {
								keyFields[i].setAccessible(true);
								keyFields[i].set(key, fieldValue);
							}
						}
					}
					K k1 = (K) key;
					V v1 = (V) value;
					innerTable.put(k1, v1);
				} catch (Exception ex) {
					throw new ConfigurationFailedException(ConfigType.TABLE, this.getTableName(), ex);
				}
			}
		}

		ConfigTable<K, V> table = new ConfigTable<K, V>(this.getTableName(), innerTable, this.getVersion());
		try {
			table.runAfterLoad();
		} catch (Exception e) {
			throw new ConfigurationFailedException(ConfigType.TABLE,"runAfterLoad",e);
		}
		return table;
	}
}
