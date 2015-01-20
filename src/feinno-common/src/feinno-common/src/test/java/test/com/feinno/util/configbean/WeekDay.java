/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-1-24
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util.configbean;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public enum WeekDay {
	Mon, Tue, Wed, Thu, Fri, Sat, Sun;
	
	/**
	 * 此类型的方法必须声明为static
	 * @param day
	 * @return
	 */
	public static WeekDay getEnumVal(String day){
		if((day == "1") || (day.equalsIgnoreCase("mon")) || (day.equalsIgnoreCase("WeekDay.Mon")))
			return WeekDay.Mon;
		else if((day == "2") || (day.equalsIgnoreCase("tue")) || (day.equalsIgnoreCase("WeekDay.Tue")))
			return WeekDay.Tue;
		else if((day == "3") || (day.equalsIgnoreCase("wed")) || (day.equalsIgnoreCase("WeekDay.Wed")))
			return WeekDay.Wed;
		else if((day == "4") || (day.equalsIgnoreCase("thu")) || (day.equalsIgnoreCase("WeekDay.Thu")))
			return WeekDay.Thu;
		else if((day == "5") || (day.equalsIgnoreCase("fri")) || (day.equalsIgnoreCase("WeekDay.Fri")))
			return WeekDay.Fri;
		else if((day == "6") || (day.equalsIgnoreCase("sat")) || (day.equalsIgnoreCase("WeekDay.Sat")))
			return WeekDay.Sat;
		else if((day == "7") || (day.equalsIgnoreCase("sun")) || (day.equalsIgnoreCase("WeekDay.Sun")))
			return WeekDay.Sun;
		else
			return null;		
	}
}
