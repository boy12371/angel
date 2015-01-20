package com.feinno.logging.filter;

import org.slf4j.Marker;

import com.feinno.logging.LogEvent;

/**
 * 这是一个Marker类型的过滤器，还有待完善
 * 
 * @author Lv.Mingwei
 * 
 */
public class MarkerFilter extends Filter {

	@Override
	protected FilterReply doFilter(LogEvent event) {

		if (event.getMarker() == null) {
			return FilterReply.NEUTRAL;
		}

		for (Marker marker : FilterManager.getMarker()) {
			// 当前Marker只要命中了一个匹配标记，就可以放行
			if (marker != null && marker.contains(event.getMarker())) {
				return FilterReply.ACCEPT;
			}
		}
		return FilterReply.NEUTRAL;
	}
}
