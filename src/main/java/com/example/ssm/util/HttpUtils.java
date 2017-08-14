package com.example.ssm.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {
	public static HttpClient client;
	private static String charset = "utf-8";
	static {
		client = HttpClientBuilder.create().build();

	}

	public static String post(String url, Map<String, String> params, Map<String, String> header, String charset)
			throws Exception {
		// 处理请求地址
		URI uri = new URI(url);
		HttpPost post = new HttpPost(uri);
		// 添加参数
		if (params != null) {
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			for (String str : params.keySet()) {
				paramsList.add(new BasicNameValuePair(str, params.get(str)));
			}
			post.setEntity(new UrlEncodedFormEntity(paramsList, charset));
		}
		// 添加header
		if (header != null) {
			for (Entry<String, String> entry : header.entrySet()) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			// 处理请求结果
			return IOUtils.toString(response.getEntity().getContent(), charset);
		}
		return null;
	}

	public static String post(String url, String content, Map<String, String> header, String charset) throws Exception {
		// 处理请求地址
		URI uri = new URI(url);
		HttpPost post = new HttpPost(uri);
		// 添加参数
		if (StringUtils.isNotBlank(content)) {
			post.setEntity(new StringEntity(content, charset));
		}
		// 添加header
		if (header != null) {
			for (Entry<String, String> entry : header.entrySet()) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			// 处理请求结果
			return IOUtils.toString(response.getEntity().getContent(), charset);
		}
		return null;
	}

	public static String get(String url, Map<String, String> params, Map<String, String> header, String charset)
			throws Exception {
		URIBuilder builder = new URIBuilder(url);
		// 添加参数
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				builder.addParameter(entry.getKey(), entry.getValue());
			}
		}
		URI uri = builder.build();
		HttpGet get = new HttpGet(uri);
		// 添加header
		if (params != null) {
			for (Entry<String, String> entry : header.entrySet()) {
				get.addHeader(entry.getKey(), entry.getValue());
			}
		}
		HttpResponse response = client.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			return IOUtils.toString(response.getEntity().getContent(), charset);
		}
		return null;
	}

	public static String post(String url, Map<String, String> params, String charset) throws Exception {
		return post(url, params, null, charset);
	}

	public static String post(String url, Map<String, String> params) throws Exception {
		return post(url, params, null, charset);
	}

	public static String post(String url, String content, String charset) throws Exception {
		return post(url, content, null, charset);
	}

	public static String post(String url, String content) throws Exception {
		return post(url, content, null, charset);
	}

	public static String get(String url) throws Exception {
		return get(url, null, null, charset);
	}
}
