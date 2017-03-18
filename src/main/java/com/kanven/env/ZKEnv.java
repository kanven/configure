package com.kanven.env;

import org.apache.commons.lang3.StringUtils;

public final class ZKEnv {

	private final String zkServers;

	private final int sessionTimeout;

	private final int connectionTimeout;

	private final long operationRetryTimeout;

	private final String nodePath;

	{
		zkServers = getVal("zk.servers");
		sessionTimeout = Integer.parseInt(getVal("zk.sessionTimeout"));
		connectionTimeout = Integer.parseInt(getVal("zk.connectionTimeout"));
		operationRetryTimeout = Long.parseLong(getVal("zk.retryTimeout"));
		nodePath = getVal("zk.node");
	}

	private String getVal(String key) {
		String val = System.getenv(key);
		if (StringUtils.isBlank(val)) {
			val = System.getProperty(key);
		}
		if (StringUtils.isBlank(val)) {
			throw new NullPointerException("获取" + key + "键值失败！");
		}
		return val;
	}

	private ZKEnv() {
		// 防止反射破坏单例
		if (null != EnvironmentHolder.INSTANCE) {
			throw new IllegalStateException("实例已经被创建！");
		}
	}

	public static ZKEnv getInstance() {
		return EnvironmentHolder.INSTANCE;
	}

	private static class EnvironmentHolder {
		private static final ZKEnv INSTANCE = new ZKEnv();
	}

	/**
	 * 防止序列化破坏单例
	 * 
	 * @return
	 */
	private Object readResolve() {
		return EnvironmentHolder.INSTANCE;
	}

	public String getZkServers() {
		return zkServers;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public long getOperationRetryTimeout() {
		return operationRetryTimeout;
	}

	public String getNodePath() {
		return nodePath;
	}

	@Override
	public String toString() {
		return "Environment [zkServers=" + zkServers + ", sessionTimeout=" + sessionTimeout + ", connectionTimeout="
				+ connectionTimeout + ", operationRetryTimeout=" + operationRetryTimeout + ", nodePath=" + nodePath
				+ "]";
	}
}
