package com.feinno.ha.service.activity.node;

public abstract class AbstractActivityNode {

	public abstract void onExpired();

	public abstract boolean equals(Object obj);

	public abstract int hashCode();

}
