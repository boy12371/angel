package com.feinno.logging.filter;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Marker;

public class FilterManager {

	/** 存放所有的用于匹配的标识 */
	private static CopyOnWriteArrayList<Marker> markerToMatchList = new CopyOnWriteArrayList<Marker>();

	private static CopyOnWriteArrayList<Integer> sessionIdList = new CopyOnWriteArrayList<Integer>();


	private static HashSet<String> CaputreMatchList = new HashSet<String>();
	private FilterManager() {
	}

	/**
	 * 添加一个用于匹配的标记，使用它和日志传递进来的Marker进行匹配
	 * 
	 * @param marker
	 */
	public static void addMarker(Marker marker) {
		if (!markerToMatchList.contains(marker)) {
			markerToMatchList.add(marker);
		}
	}

	/**
	 * 清理全部的Marker标记
	 */
	public static void clearMarker() {
		markerToMatchList.clear();
	}

	/**
	 * 清除掉某一个Marker
	 * 
	 * @param marker
	 * @return
	 */
	public static boolean removeMarker(Marker marker) {
		return markerToMatchList.remove(marker);
	}

	/**
	 * 返回所有的用于匹配的标识
	 * 
	 * @return
	 */
	public static CopyOnWriteArrayList<Marker> getMarker() {
		return markerToMatchList;
	}
	
	public static void addCapture(String capture) {
		if (!CaputreMatchList.contains(capture)) {
			CaputreMatchList.add(capture);
		}
	}

	/**
	 * 清理全部的Capture标记
	 */
	public static void clearCapture() {
		CaputreMatchList.clear();
	}
	
	public static HashSet<String> GetCapture()
	{
		return CaputreMatchList;
	}

	/**
	 * 添加一个用于匹配的标记，使用它和日志传递进来的Marker进行匹配
	 * 
	 * @param marker
	 */
	public static void addSessionId(Integer sessionId) {
		if (!sessionIdList.contains(sessionId)) {
			sessionIdList.add(sessionId);
		}
	}

	/**
	 * 返回所有的用于匹配的标识
	 * 
	 * @return
	 */
	public static CopyOnWriteArrayList<Integer> getSessionIdList() {
		return sessionIdList;
	}
}
