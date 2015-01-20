package com.feinno.logging.filter;


import org.slf4j.Marker;

import com.feinno.logging.LogEvent;

public class CaptureFilter extends Filter {


	@Override
	protected FilterReply doFilter(LogEvent event) {
		if (event.getUserIdentify() == null || event.getUserIdentify().trim().isEmpty())
			return FilterReply.NEUTRAL; 
		if (FilterManager.GetCapture().contains(event.getUserIdentify()))
			return FilterReply.ACCEPT;

		return FilterReply.NEUTRAL;
		
	}

}
