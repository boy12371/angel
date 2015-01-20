/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2010-11-26
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package test.com.feinno.util;

import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import test.com.feinno.util.bean.City;

import com.feinno.util.Func;
import com.feinno.util.SearchIndex;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public class SearchIndexTest extends TestCase{
	
	/**
	 * {在这里补充功能说明}
	 * @param value
	 * @return
	 */
	private SearchIndex<City> searchIndex;
	String indexFields[] = {"name","province","areadesc"};	//按照这三个字段建立索引
	@Before
	public void setUp() throws Exception {
		super.setUp();
		List<City> citys = new ArrayList<City>();		
		City city = null;
		//list中添加6个City对象
		for(int j=0;j<2;j++){
			for(int i=0;i<3;i++){
				city = new City();
				city.setID(i);
				city.setName("石家庄" + i);
				city.setProvince("河北省" + i);
				city.setAreacode("130130" + i);
				city.setAreadesc("环北京大省" + i);
				city.setHistory(Calendar.getInstance().getTime());		
				citys.add(city);
			}
		}
		for(int i=3;i<5;i++){
			city = new City();
			city.setID(i);
			city.setName("石家庄" + i);
			city.setProvince("河北省" + i);
			city.setAreacode("130130" + i);
			city.setAreadesc("环北京大省" + i);
			city.setHistory(Calendar.getInstance().getTime());		
			citys.add(city);
		}
		for(int i=5;i<7;i++){
			city = new City();
			city.setID(i);
			city.setName("石家庄" + i);
			city.setProvince("河北省" + i);
			city.setAreacode("130130" + i);
			city.setAreadesc("环北京大省" + i);
			city.setHistory(Calendar.getInstance().getTime());		
			citys.add(city);
		}
		//以上添加10个City对象，查看SearchIndex是否能自动排序
		setSearchIndex(new SearchIndex<City>(City.class,citys,indexFields));
	}
	
	@Test
	public void testNullList() throws SecurityException, IllegalArgumentException, IllegalAccessException, InvalidParameterSpecException, NoSuchFieldException{
		SearchIndex<City> searchIndex = new SearchIndex<City>(City.class,null,indexFields);
		
		List<City> citys = new ArrayList<City>();		
		City city = null;
		//list中添加6个City对象
		for(int j=0;j<2;j++){
			for(int i=0;i<3;i++){
				city = new City();
				city.setID(i);
				city.setName("石家庄" + i);
				city.setProvince("河北省" + i);
				city.setAreacode("130130" + i);
				city.setAreadesc("环北京大省" + i);
				city.setHistory(Calendar.getInstance().getTime());		
				citys.add(city);
			}
		}		
		searchIndex.build(citys);
		
		Assert.assertEquals(2, searchIndex.find("石家庄2","河北省2").size());
	}
	
	@Test
	public void testFind() throws InvalidParameterSpecException {
		List<City> list = null;
		
		list= searchIndex.find("石家庄3","河北省3");		//参数顺序对应建立SearchIndex对象时传入的参数，此时是{"name","province","areadesc"};
		Assert.assertEquals(1, list.size());
		
		list= searchIndex.find("石家庄1",null,"环北京大省1");	//中间参数为空则忽略null后的参数，此时只有"石家庄1"这个参数起作用
		Assert.assertEquals(2, list.size());
		
		try{
			list= searchIndex.find("石家庄3","河北省3","环北京大省3","多余参数");		//测试参数过多异常
		}catch(IllegalArgumentException e){
			Assert.assertNull(null);
		}
		
		list= searchIndex.find("","","");	//测试空参数情况
		Assert.assertEquals(0, list.size());
	}
	
	@Test
	public void testFindFirst() throws Exception{
		City city = null;

		city = searchIndex.findFirst("石家庄3","河北省3");
		Assert.assertNotNull(city);
		Assert.assertEquals("石家庄3", city.getName());
		
		city = null;
		city = searchIndex.findFirst("石家庄100","河北省100");
		Assert.assertNull(city);
		
		try{
			city = searchIndex.findFirst("石家庄2","河北省2");
		}catch(Exception e){
			Assert.assertNull(null);
		}
	}
	
	//测试类似委托的功能
	@Test
	public void testFindKeys() throws InvalidParameterSpecException{
		List<City> list = null;

		Func func= new Func<City,Boolean>() {
			public Boolean exec(City obj) {
				System.out.println("aaa");
				System.out.println(((City)obj).toString());
				Boolean obj2 = new Boolean(true);
				return obj2;
			}
		};
		list = searchIndex.findKeys(3, func, "石家庄2","河北省2");
		Assert.assertNotNull(list);
		Assert.assertEquals("石家庄2", list.get(0).getName());
		
		list = searchIndex.findKeys(3, "石家庄2","河北省2");		//测试第二个参数为空的searchIndex方法
		Assert.assertNotNull(list);
		
		try{
			list= searchIndex.findKeys(3,func,"石家庄2","河北省2","环北京大省2","多余参数");		//测试参数过多异常
		}catch(IllegalArgumentException e){
			Assert.assertNull(null);
		}
		try{
			list= searchIndex.findKeys(4,func,"石家庄2","河北省2","环北京大省2");		//测试keyFieldCount大于索引字段个数异常
		}catch(IllegalArgumentException e){
			Assert.assertNull(null);
		}
		try{
			list= searchIndex.findKeys(1,func,"石家庄2","河北省2","环北京大省2");		//测试keyFieldCount小于索引条件参数个数异常
		}catch(IllegalArgumentException e){
			Assert.assertNull(null);
		}
	}
	
	@Test
	public void testException() throws SecurityException,IllegalArgumentException,IllegalAccessException{
		List<City> citys = new ArrayList<City>();		
		City city = new City();
		city.setID(1);
		city.setName("石家庄");
		city.setProvince("河北省");
		city.setAreacode("130130");
		city.setAreadesc("环北京大省");
		city.setHistory(Calendar.getInstance().getTime());		
		citys.add(city);

//		try {
//			SearchIndex<City> searchIndex = new SearchIndex<City>(City.class,citys,"name","province","aaaaaaaaaaaaaaa");
//			Assert.assertFalse(true);
//		} catch (NoSuchFieldException e) {
//			Assert.assertNull(null);
//		}
	}
	
	public void setSearchIndex(SearchIndex<City> searchIndex) {
		this.searchIndex = searchIndex;
	}

	public SearchIndex<City> getSearchIndex() {
		return searchIndex;		
	}

	//测试类似委托的功能
	class FuncA implements Func<City,Boolean>{
		@Override
		public Boolean exec(City obj) {
			System.out.println("aaa");
			System.out.println(((City)obj).toString());
			Boolean obj2 = new Boolean(true);
			return obj2;
		}
	}
	
}
