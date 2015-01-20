package org.slf4j.helpers;

import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;

/**
 * slf4j的BasicMarkerFactory创建出来的Marker就会一直持有不释放，占用内存过高，此处继承接口，
 * 实现了一个简单的BarkerFactory
 * 
 * @author Lv.Mingwei
 * 
 */
public class DefaultMarkerFactory implements IMarkerFactory {

	public DefaultMarkerFactory() {
	}

	public synchronized Marker getMarker(String name) {
		if (name == null)
			throw new IllegalArgumentException("Marker name cannot be null");
		return new BasicMarker(name);
	}

	public synchronized boolean exists(String name) {
		return false;
	}

	public boolean detachMarker(String name) {
		return true;
	}

	public Marker getDetachedMarker(String name) {
		return new BasicMarker(name);
	}
}
