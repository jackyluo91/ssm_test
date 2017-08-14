package com.example.ssm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class URLHelper {
	private ArrayList<NameValuePair> parameters;
	private HashMap<String, String> map;
	private String host;

	public URLHelper() {
		parameters = new ArrayList<>();
		map = new HashMap<>();
	}

	public URLHelper(String host) {
		this();
		this.host = host;
	}

	class NameValuePair2 implements NameValuePair {
		private String name;
		private String value;

		public NameValuePair2(String name, String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getValue() {
			return value;
		}

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void put(String key, String value) {
		parameters.add(new NameValuePair2(key, value));
		map.put(key, value);
	}

	public String get(String key) {
		return map.get(key);
	}

	public void clear() {
		parameters.clear();
	}

	public String getUrl(String charset) {
		StringBuffer url = null;
		if (host != null) {
			url = new StringBuffer(host + "?");
		} else {
			url = new StringBuffer();
		}
		url.append(URLEncodedUtils.format(parameters, charset));
		String urlString = url.toString();
		urlString = urlString.replaceAll("\\+", "%20"); // 处理空格
		return urlString;
	}

	public String getUrlSource(String charset) {
		String url = getUrl(charset);
		try {
			return URLDecoder.decode(url, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getQueryMap(String queryString) {
		List<NameValuePair> list = URLEncodedUtils.parse(queryString, Charset.forName("UTF-8"));
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		for (NameValuePair nameValuePair : list) {
			Object obj = map.get(nameValuePair.getName());
			if (obj == null) {
				map.put(nameValuePair.getName(), nameValuePair.getValue());
			} else {
				if (obj instanceof String) {
					List<Object> listObj = new ArrayList<>();
					listObj.add(obj);
					listObj.add(nameValuePair.getValue());
					map.put(nameValuePair.getName(), listObj);
				} else if (obj instanceof List) {
					((List<Object>) obj).add(nameValuePair.getValue());
				}
			}
		}
		return map;
	}

	public static List<NameValuePair> getQueryList(String queryString) {
		List<NameValuePair> list = URLEncodedUtils.parse(queryString, Charset.forName("UTF-8"));
		return list;
	}
}
