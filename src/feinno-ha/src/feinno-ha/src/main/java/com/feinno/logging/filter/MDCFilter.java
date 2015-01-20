package com.feinno.logging.filter;

import com.feinno.logging.LogEvent;
import com.feinno.threading.ThreadContext;

/**
 * 这是一个MDC类型的过滤器，还有待完善
 * 
 * @author Lv.Mingwei
 * 
 */
public class MDCFilter extends Filter {

	@Override
	protected FilterReply doFilter(LogEvent event) {

		int sessionId = -1;
//		if (ThreadContext.getCurrent() != null && ThreadContext.getCurrent().getSession() != null) {
//			sessionId = ThreadContext.getCurrent().getSession().getSessionId();
//		} else {
//			return FilterReply.NEUTRAL;
//		}
//
//		for (int sessionIdTemp : FilterManager.getSessionIdList()) {
//			// 当前SessionId只要命中了一个匹配标记，就可以放行
//			if (sessionIdTemp == sessionId) {
//				return FilterReply.ACCEPT;
//			}
//		}
		return FilterReply.NEUTRAL;
	}
}
