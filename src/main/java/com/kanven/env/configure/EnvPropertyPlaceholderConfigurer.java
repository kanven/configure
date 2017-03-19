package com.kanven.env.configure;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.kanven.env.ZKEnv;

public class EnvPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected Properties mergeProperties() throws IOException {
		Properties properties = new Properties(super.mergeProperties());
		Map<String, String> entries = getValues();
		if (entries.size() > 0) {
			for (Entry<String, String> entry : entries.entrySet()) {
				String key = entry.getKey();
				if (properties.containsKey(key)) {
					continue;
				}
				properties.setProperty(key, entry.getValue());
			}
		}
		return properties;
	}

	private Map<String, String> getValues() {
		ZKEnv zk = ZKEnv.getInstance();
		ZkConnection conn = new ZkConnection(zk.getZkServers(), zk.getSessionTimeout());
		ZkClient client = new ZkClient(conn, zk.getConnectionTimeout(), new SerializableSerializer(),
				zk.getOperationRetryTimeout());
		String node = zk.getNodePath();
		Map<String, String> configs = new HashMap<String, String>();
		List<String> children = client.getChildren(node);
		if (children != null && children.size() > 0) {
			for (String child : children) {
				String path = node + "/" + child;
				String value = client.readData(path);
				configs.put(child, value);
			}
		}
		return configs;
	}

}
