package com.feinno.diagnostic.perfmon.monitor.rpc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportEntity;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.diagnostic.observation.ObserverReportRowBean;
import com.feinno.diagnostic.perfmon.monitor.PullManager;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.util.TimeSpan;

/**
 * RpcMonitor实现类;
 * 
 * @author jingmiao
 * 
 */
public class MonitorServiceImpl implements MonitorService
{
	/**
	 * 获取Category列表
	 */
	@Override
	public MonitorCategoryEntity[] getCategoryList()
	{
		List<MonitorCategoryEntity> mCategoryEntityList = new ArrayList<MonitorCategoryEntity>();
		// 一条信息;
		MonitorCategoryEntity mCategoryEntity = null;
		for (Observable ob : ObserverManager.getAllObserverItems())
		{
			mCategoryEntity = new MonitorCategoryEntity();
			mCategoryEntity.setName(ob.getObserverName());
			mCategoryEntity.setInstance(ob.getObserverUnits().size());
			// ObserverReportColumn继承自ProtoEntity支持序列化;
			mCategoryEntity.setColumns(ob.getObserverColumns());
			mCategoryEntityList.add(mCategoryEntity);
		}
		return mCategoryEntityList.toArray(new MonitorCategoryEntity[mCategoryEntityList.size()]);
	}

	@Override
	public ObserverReportEntity[] pull(ProtoString pCookie)
	{
		String cookie = pCookie.getValue();
		PullManager mgr = PullManager.getInstance(cookie, false);
		if (mgr == null)
		{
			throw new IllegalArgumentException("cookieId not exist");
		}
		List<ObserverReport> oReportList = mgr.pull();
		// 可序列化的返回List;
		List<ObserverReportEntity> oReportEntityList = new ArrayList<ObserverReportEntity>();
		ObserverReportEntity oReportEntity = null;

		for (ObserverReport or : oReportList)
		{
			oReportEntity = new ObserverReportEntity();
			oReportEntity.setCategory(or.getCategory());
			oReportEntity.setTime(new Date(or.getTime().getTime()));
			oReportEntity.setColumns(or.getColumns());

			List<ObserverReportRowBean> rows = new ArrayList<ObserverReportRowBean>();
			ObserverReportRowBean rpRow = null;
			// ObserverReportRow非可序列化,将其内容赋值给可序列化的ObserverReportRowBean,并存入List<ObserverReportRowBean>;
			List<ObserverReportRow> observerRow = or.getRows();
			for (ObserverReportRow orw : observerRow)
			{
				rpRow = new ObserverReportRowBean();
				rpRow.setInstance(orw.getInstanceName());
				rpRow.setData(orw.getData());
				rows.add(rpRow);
			}
			oReportEntity.setRows(rows);
			oReportEntityList.add(oReportEntity);
		}
		return oReportEntityList.toArray(new ObserverReportEntity[oReportEntityList.size()]);
	}

	@Override
	public int subscribe(MonitorRequestArgs args)
	{
		String category = args.getCategory();
		String instance = args.getInstance();
		int interval = args.getInvterval();
		String cookie = args.getCookie();
		Observable ob = ObserverManager.getObserverItem(category);
		ObserverReportMode mode = ObserverReportMode.valueOf(instance.toUpperCase());
		if (ob == null)
		{
			// 404
			return 404;
		}
		final PullManager manager = PullManager.getInstance(cookie, true);
		TimeSpan span = new TimeSpan(interval * TimeSpan.SECOND_MILLIS);
		ObserverManager.addInspector(ob, mode, span, new ReportCallback()
		{
			@Override
			public boolean handle(ObserverReport report)
			{
				if (!manager.isActive())
				{
					return false;
				}
				else
				{
					manager.enqueueReport(report);
					return true;
				}
			}
		});
		// 200
		return 200;
	}

	@Override
	public int unsubscribe(MonitorRequestArgs args)
	{
		return 404;
	}

}
