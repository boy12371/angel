package com.feinno.logging.filter;

/**
 * 一个简单的枚举类,用于描述过滤器的三种过滤结果<br>
 * 1. ACCEPT:代表接受,如果过滤器返回此结果,那么无论日志级别是否允许输出,均输出该日志<br>
 * 2. NEUTRAL:代表中立，如果过滤器返回此结果，那么继续根据日志级别进行判断决定是否输出<br>
 * 3. DENY:代表拒绝，如果过滤器返回此结果，那么不管日志级别如何，均过滤掉此消息<br>
 * 
 * @author Lv.Mingwei
 * 
 */
public enum FilterReply {
	ACCEPT(1), NEUTRAL(2), DENY(3);

	private int value = 0;

	private FilterReply(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
