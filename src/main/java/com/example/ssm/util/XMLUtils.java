package com.example.ssm.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtils {
	/**
	 * map or list to xml string
	 */
	@SuppressWarnings("unchecked")
	public static String parseToXML(Object obj) {
		StringBuilder builder = new StringBuilder();
		if (obj != null) {
			if (obj instanceof Map) {
				Map<Object, Object> map = (Map<Object, Object>) obj;
				for (Entry<Object, Object> entry : map.entrySet()) {
					builder.append(
							String.format("<%s>%s</%s>", entry.getKey(), parseToXML(entry.getValue()), entry.getKey()));
				}
			} else if (obj instanceof List) {
				List<Object> list = (List<Object>) obj;
				for (Object object : list) {
					builder.append(parseToXML(object));
				}
			} else {
				builder.append(obj.toString());
			}
		}
		return builder.toString();
	}

	/**
	 * <h1>不会造成精度丢失，但有一定缺陷</h1>
	 * <p>
	 * <strong>1.单层若全为重复元素，则被转换为List</strong> <br>
	 * 如：&lt;root&gt;&lt;a&gt;1&lt;/a&gt;&lt;a&gt;2&lt;/a&gt;&lt;/root&gt;
	 * </p>
	 * <p>
	 * <strong>2.单层若全为不重复元素，则被转换为Map</strong> <br>
	 * 如：&lt;root&gt;&lt;a&gt;1&lt;/a&gt;&lt;b&gt;2&lt;/a&gt;&lt;/root&gt;
	 * </p>
	 * <p>
	 * <strong>3.单层若有重复元素也有不重复的元素，则被转换为List</strong> <br>
	 * 如：&lt;root&gt;&lt;a&gt;1&lt;/a&gt;&lt;a&gt;2&lt;/a&gt;&lt;b&gt;2&lt;/a&gt
	 * ;&lt;/root&gt;
	 * </p>
	 * 
	 * @param root
	 * @return
	 */
	public static Map<String, Object> parseToMap(String strxml) throws Exception {
		Document doc = parseToDoc(strxml);
		Element root = doc.getRootElement();
		Map<String, Object> m = elementToMapList(root);
		return m;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> elementToMapList(Element root) {
		List<Element> rc = root.elements();
		Map<String, Object> m = new LinkedHashMap<>();
		String k = root.getName();
		Object v = "";
		if (rc.isEmpty()) {
			v = root.getTextTrim();
		} else {
			Map<String, Object> map = new LinkedHashMap<>();
			List<Object> list = new ArrayList<Object>();
			for (Element ce : rc) {
				Map<String, Object> temp = elementToMapList(ce);
				list.add(temp);
				map.put(ce.getName(), temp.get(ce.getName()));
			}
			v = map.size() == list.size() ? map : list;
		}
		m.put(k, v);
		return m;
	}

	/**
	 * <h1>没有缺陷，不丢失精度，转为Document类</h1>
	 * 
	 * @param strxml
	 * @return
	 * @throws Exception
	 */
	public static Document parseToDoc(String strxml) throws Exception {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXReader reader = new SAXReader();
		Document doc = reader.read(in);
		in.close();
		return doc;
	}
}
