package test.com.feinno.serialization.protobuf;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;
import sun.tools.jps.Jps;

public class TestJPS {

	public static void printJVM() throws Exception {
		boolean assertionsDisabled = true;
		boolean showLongPaths = true;
		String hostname = null;
		HostIdentifier localHostIdentifier = new HostIdentifier(hostname);
		MonitoredHost localObject1 = MonitoredHost.getMonitoredHost(localHostIdentifier);
		System.out.println(((MonitoredHost) localObject1).activeVms());
		Set<Integer> localSet = ((MonitoredHost) localObject1).activeVms();
		Iterator<Integer> localIterator = null;
		for (localIterator = localSet.iterator(); localIterator.hasNext();) {
			StringBuilder localStringBuilder = new StringBuilder();
			Object localObject2 = null;

			int i = ((Integer) localIterator.next()).intValue();

			localStringBuilder.append(String.valueOf(i));

			// if (arguments.isQuiet()) {
			// System.out.println(localStringBuilder);
			// continue;
			// }

			MonitoredVm localMonitoredVm = null;
			String str1 = "//" + i + "?mode=r";
			try {
				VmIdentifier localVmIdentifier = new VmIdentifier(str1);
				localMonitoredVm = ((MonitoredHost) localObject1).getMonitoredVm(localVmIdentifier, 0);
			} catch (URISyntaxException localURISyntaxException) {
				localObject2 = localURISyntaxException;
				if (!assertionsDisabled)
					throw new AssertionError();
			} catch (Exception localException) {
				localObject2 = localException;
			} finally {
				if (localMonitoredVm == null) {
					localStringBuilder.append(" -- process information unavailable");
					// if ((arguments.isDebug()) && (localObject2 != null) &&
					// (localObject2.getMessage() != null)) {
					// localStringBuilder.append("\n\t");
					// localStringBuilder.append(localObject2.getMessage());
					// }
					//
					// System.out.println(localStringBuilder);
					// if (arguments.printStackTrace()) {
					// localObject2.printStackTrace();
					// continue;
					// }
				}

			}

			localStringBuilder.append(" ");
			localStringBuilder.append(MonitoredVmUtil.mainClass(localMonitoredVm, showLongPaths));
			String str2;
			// if (arguments.showMainArgs()) {
			// str2 = MonitoredVmUtil.mainArgs(localMonitoredVm);
			// if ((str2 != null) && (str2.length() > 0)) {
			// localStringBuilder.append(" ").append(str2);
			// }
			// }
			// if (arguments.showVmArgs()) {
			// str2 = MonitoredVmUtil.jvmArgs(localMonitoredVm);
			// if ((str2 != null) && (str2.length() > 0)) {
			// localStringBuilder.append(" ").append(str2);
			// }
			// }
			// if (arguments.showVmFlags()) {
			// str2 = MonitoredVmUtil.jvmFlags(localMonitoredVm);
			// if ((str2 != null) && (str2.length() > 0)) {
			// localStringBuilder.append(" ").append(str2);
			// }
			// }

			System.out.println(localStringBuilder);

			((MonitoredHost) localObject1).detach(localMonitoredVm);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// printJVM();
		System.out.println(getRunningJVMPID());
		System.out.println(111);
	}

	public static Set<Integer> getRunningJVMPID() {
		try {
			String hostname = null;
			HostIdentifier localHostIdentifier = new HostIdentifier(hostname);
			MonitoredHost monitoredHost = MonitoredHost.getMonitoredHost(localHostIdentifier);
			return ((MonitoredHost) monitoredHost).activeVms();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashSet<Integer>();
	}

}
