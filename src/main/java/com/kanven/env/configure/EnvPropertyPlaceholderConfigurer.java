package com.kanven.env.configure;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.kanven.env.ZKEnv;

public class EnvPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected Properties mergeProperties() throws IOException {
		Properties properties = new Properties();
		Properties merges = super.mergeProperties();
		
		return properties;
	}

	private Map<String, String> getValues() {
		ZKEnv zk = ZKEnv.getInstance();
		ZkConnection conn = new ZkConnection(zk.getZkServers(), zk.getSessionTimeout());
		ZkClient client = new ZkClient(conn, zk.getConnectionTimeout(), new SerializableSerializer(),
				zk.getOperationRetryTimeout());
		String node = zk.getNodePath();
		int len = node.length();
		Map<String, String> configs = new HashMap<String, String>();
		List<String> children = client.getChildren(node);
		for (String child : children) {
			String key = child.substring(len + 1);
			String value = client.readData(child);
			configs.put(key, value);
		}
		return configs;
	}

}
