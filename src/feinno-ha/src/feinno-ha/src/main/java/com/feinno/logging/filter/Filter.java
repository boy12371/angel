package com.feinno.logging.filter;

import com.feinno.logging.LogEvent;
import com.feinno.logging.LogLevel;

/**
 * 一个抽象的过滤器类，其中封装了过滤器链表和遍历这个链表最终取得过滤结果的方法，作为一个模板，子类需要实现模板的doFilter方法
 * 
 * @author Lv.Mingwei
 * 
 */
public abstract class Filter {

	/** 这个设置当前可以过滤的级�? */
	private LogLevel level = LogLevel.DEBUG;
	private Filter nextFilter;

	public FilterReply action(LogEvent event) {

		// 如果当前日志等于大于本过滤器可以过滤的等�?那么可以使用此过滤器过滤,否则不需过滤,直接赋予中立结果
		FilterReply reply = event.getLevel().intValue() >= level.intValue() ? doFilter(event) : FilterReply.NEUTRAL;
		if (reply == null) {
			reply = FilterReply.NEUTRAL;
		}
		// 如果当前过滤器得到拒绝接受的结果，那么这个结果直接作为最终结果返�?
		if (reply == FilterReply.DENY) {
			return reply;
		} else if (nextFilter != null) {
			FilterReply nextReply = nextFilter.action(event);
			// 否则将过滤得到的最严重的结果返回到最上层
			return reply.value() > nextReply.value() ? reply : nextReply;
		}
		return reply;
	}

	public void setNextFilter(Filter nextFilter) {
		this.nextFilter = nextFilter;
	}

	public final void setLevel(LogLevel level) {
		this.level = level;
	}

	protected abstract FilterReply doFilter(LogEvent event);

}
