package com.feinno.appengine.resource.database;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.initialization.Initializer;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.util.StringUtils;

/**
 * 用来解析DAL 的配置。
 * 
 * @author
 *
 */
public class DatabaseDistributedCfgParser {
	private static Logger LOGGER = LoggerFactory
			.getLogger("DatabaseDistributedCfgParser");
	private static DatabaseDistributedCfgParser INSTANCE = null;
	private static DatabaseDistributedHostCfgs cfgMapping = null;

	@Initializer
	public void initialize() throws Exception {
		ConfigurationManager.loadText("dalclient.xml", null,
				new ConfigUpdateAction<String>() {
					@Override
					public void run(String dalcfg) throws Exception {
						updateDalCfgs(dalcfg);
					}
				});

	}

	private DatabaseDistributedCfgParser() throws Exception {
		initialize();
	}

	public synchronized static DatabaseDistributedCfgParser getInstance() {
		if (INSTANCE == null) {
			try {
				INSTANCE = new DatabaseDistributedCfgParser();
			} catch (Exception e) {
				LOGGER.error("init Error");
			}
		}
		return INSTANCE;
	}

	private synchronized void updateDalCfgs(String cfg) {
		if (cfg == null || cfg.trim().equals("")) {
			LOGGER.error("dal.properties is null please check it");
			return;
		}

		try {
			DatabaseDistributedHostCfgs cfgs = null;
			Document doc = DocumentHelper.parseText(cfg);
			if (doc != null) {
				Element root = doc.getRootElement();
				if (root != null) {
					List<Element> dbHostMappings = root.elements("mapping");
					for (Element el : dbHostMappings) {
						Attribute typeAtt = el.attribute("type");
						Attribute timeoutAtt = el.attribute("timeout");
						int timeout = DatabaseDistributedHostCfgs.DEFAULT_TIMEOUT_SECOND;
						if (timeoutAtt != null) {
						
							if (StringUtils.isNum(timeoutAtt.getText().trim())) {
								timeout = Integer.parseInt(timeoutAtt.getText()
										.trim());
							} else {
								LOGGER.warn(String
										.format("mapping timeout is null or Not NUM userDefault num=%s ",
												timeoutAtt.getText().trim()));
								
							}
						}
						

						Element hostEl = el.element("host");
						String host = "";
						if (hostEl != null) {
							host = hostEl.getTextTrim();
						} else {
							LOGGER.error("parser dalclient.properties   error  a host Element is lost ");
							throw new IllegalArgumentException(
									"parser dalclient.properties   error  a host Element is lost");
						}
						if (cfgs == null)
							cfgs = new DatabaseDistributedHostCfgs();

						if (typeAtt != null && typeAtt.getValue() != null
								&& typeAtt.getValue().equals("default")) {
							cfgs.setDefaultEp(parserEp(host));
							cfgs.setDefalutTimeout(timeout);
						} else {

							Element dbnamesEl = el.element("databases");
							String dbnames = "";
							if (dbnamesEl != null) {
								dbnames = dbnamesEl.getTextTrim();
							} else {
								LOGGER.error("parser dalclient.properties   error  a dbnames Element is lost ");
								throw new IllegalArgumentException(
										"parser dalclient.properties   error  a dbnames Element is lost");
							}

							RpcTcpEndpoint ep = parserEp(host);
							cfgs.setEndpoints(LOGGER, ep, dbnames.split(","));
							cfgs.setDefalutTimeout(timeout);

						}

					}
					if (cfgs != null)
						setCfgMapping(cfgs);
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format(
					"parser dal.properties to XML error  contents=%s", cfg), e);
		}

	}

	private static RpcTcpEndpoint parserEp(String host) {
		if (StringUtils.isNullOrEmpty(host))
			throw new IllegalArgumentException(
					"dalclient.xml dalHosts can not be null");

		RpcTcpEndpoint point = RpcTcpEndpoint.parse("tcp://" + host);

		return point;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static DatabaseDistributedHostCfgs getCfgMapping() {
		return cfgMapping;
	}

	private static void setCfgMapping(DatabaseDistributedHostCfgs _cfgMapping) {
		cfgMapping = _cfgMapping;
	}

	public RpcTcpEndpoint getRpcTcpEndpoint(String dbname) {
		if (cfgMapping != null)
			return cfgMapping.getRpcTcpEndpoint(dbname);
		else {
			throw new RuntimeException("DAL client CFGMpping is null");
		}
	}

	public int getTimeOut(String dbname) {
		if (cfgMapping != null)
			return cfgMapping.getTimeOut(dbname);
		else {
			throw new RuntimeException("DAL client CFGMpping is null");
		}
	}

}
