package com.feinno.appengine;

import com.feinno.diagnostic.perfmon.PerformanceCounter;
import com.feinno.diagnostic.perfmon.PerformanceCounterCategory;
import com.feinno.diagnostic.perfmon.PerformanceCounterType;
import com.feinno.diagnostic.perfmon.SmartCounter;

@PerformanceCounterCategory("appbean-session")
public class AppBeanSessionCouters
{
	@PerformanceCounter(name = "CurrentSession", type = PerformanceCounterType.NUMBER)
	private SmartCounter currentSession;
	@PerformanceCounter(name = "SessionQuequeLength", type = PerformanceCounterType.NUMBER)
	private SmartCounter sessionQuequeLength;
	@PerformanceCounter(name = "SessionTimeoutCount", type = PerformanceCounterType.NUMBER)
	private SmartCounter sessionTimeoutCount;
	@PerformanceCounter(name = "SessionRejectCount", type = PerformanceCounterType.NUMBER)
	private SmartCounter sessionRejectCount;
	@PerformanceCounter(name = "sessionShrinkCount", type = PerformanceCounterType.NUMBER)
	private SmartCounter sessionShrinkCount;
	public void setCurrentSession(SmartCounter currentSession) {
		this.currentSession = currentSession;
	}
	public SmartCounter getCurrentSession() {
		return currentSession;
	}
	public void setSessionQuequeLength(SmartCounter sessionQuequeLength) {
		this.sessionQuequeLength = sessionQuequeLength;
	}
	public SmartCounter getSessionQuequeLength() {
		return sessionQuequeLength;
	}
	public void setSessionTimeoutCount(SmartCounter sessionTimeoutCount) {
		this.sessionTimeoutCount = sessionTimeoutCount;
	}
	public SmartCounter getSessionTimeoutCount() {
		return sessionTimeoutCount;
	}
	public void setSessionRejectCount(SmartCounter sessionRejectCount) {
		this.sessionRejectCount = sessionRejectCount;
	}
	public SmartCounter getSessionRejectCount() {
		return sessionRejectCount;
	}
	public void setSessionShrinkCount(SmartCounter sessionShrinkCount) {
		this.sessionShrinkCount = sessionShrinkCount;
	}
	public SmartCounter getSessionShrinkCount() {
		return sessionShrinkCount;
	}
}