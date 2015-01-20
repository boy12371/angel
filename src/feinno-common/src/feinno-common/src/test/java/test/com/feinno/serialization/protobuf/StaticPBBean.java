package test.com.feinno.serialization.protobuf;

import java.util.Arrays;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.mysql.jdbc.Buffer;

public class StaticPBBean extends ProtoEntity {

	@ProtoMember(1)
	private String serverName;

	@ProtoMember(2)
	private static String serviceName;

	@ProtoMember(3)
	private String[] services;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String[] getServices() {
		return services;
	}

	public void setServices(String[] services) {
		this.services = services;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StaticPBBean test = new StaticPBBean();
		test.setServerName("ServerName1");
		test.setServiceName("ServiceName2");
		test.setServices(new String[] { "1", "2", "3" });
		byte[] buffer = test.toByteArray();
		StaticPBBean result = new StaticPBBean();
		result.parseFrom(buffer);
		System.out.println(result.getServerName());
		System.out.println(result.getServiceName());
		System.out.println(Arrays.toString(result.getServices()));
	}

}
