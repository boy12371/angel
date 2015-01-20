package com.feinno.ha.deployment;

import com.feinno.util.EnumInteger;

/**
 * 
 * <b>描述: </b>用于描述服务器运行状态的枚举类的工厂类，该工厂中包含了全部的运行状态的类型，
 * 其中包含WorkerEndpoint的以及MasterWorker的状态
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HAStatusEnum {

	public static enum WorkerEndpoint implements EnumInteger {

		STARTED(1), STOPPING(2), STOPPED(3);

		private int value = 0;

		WorkerEndpoint(int value) {
			this.value = value;
		}

		@Override
		public int intValue() {
			return value;
		}
	}

	public static enum MasterWorker implements EnumInteger {

		INITIAL(1), STANDBY(2), RUNNING(3), STOPPED(4), CORRUPTED(5), CRUSHED(6), DISCONNECTED(7), DELAY_STOPPED(8);

		private int value = 0;

		MasterWorker(int value) {
			this.value = value;
		}

		@Override
		public int intValue() {
			return value;
		}
	}

}
