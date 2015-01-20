/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-6
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.observation;

import java.util.List;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface ObserverReportUnit
{
	void outputReport(ObserverReportRow row);
	
	ObserverReportUnit summaryAll(List<ObserverReportUnit> items);
}
