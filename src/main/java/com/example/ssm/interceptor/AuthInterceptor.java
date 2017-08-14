package com.example.ssm.interceptor;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.example.ssm.annotation.AuthPassport;
import com.example.ssm.util.URLHelper;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	static Logger logger = Logger.getLogger(AuthInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Class<?> clazz = handlerMethod.getBeanType();
				Method method = handlerMethod.getMethod();
				AuthPassport annotationT = clazz.getAnnotation(AuthPassport.class);
				AuthPassport annotationM = method.getAnnotation(AuthPassport.class);
				// 在类或者方法上标记注解
				if (annotationT != null || annotationM != null) {
					Map<String, Object> map = URLHelper.getQueryMap(request.getQueryString());
					response.setCharacterEncoding("UTF-8");
					String appId = (String) map.get("appId");
					String sign = (String) map.get("sign");
					logger.debug("AuthInterceptor Class >>>>>>>>>>>>>>>>>> " + clazz.getName());
					logger.debug("AuthInterceptor Method >>>>>>>>>>>>>>>>> " + method.getName());
					logger.debug("AuthInterceptor Content >>>>>>>>>>>>>>>> " + JSON.toJSONString(map, true));
//					if (!StringUtils.isEmpty(sign)) {
//						String token = apiApplicationService.getToken(appId);
//						if (!StringUtils.isEmpty(token)) {
//							map.remove("sign");
//							String content = getSignContent(map) + token;
//							String realSign = new EncryptUtil().SHA256(content);
//							if (realSign.equalsIgnoreCase(sign)) {
//								return true;
//							} else {
//								response.setStatus(HttpStatus.BAD_REQUEST.value());
//								response.getWriter().append("签名错误");
//							}
//						} else {
//							response.setStatus(HttpStatus.BAD_REQUEST.value());
//							response.getWriter().append("非法APP ID：" + appId);
//						}
//					} else {
//						response.setStatus(HttpStatus.BAD_REQUEST.value());
//						response.getWriter().append("缺少签名");
//					}
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.error("AuthInterceptor", e);
		}
		return false;
	}

	public String eccrypt(String info) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("SHA-256");
		md5.update(info.getBytes());
		byte[] resultBytes = md5.digest();
		StringBuffer strHexString = new StringBuffer();
		// 遍历 byte buffer
		for (int i = 0; i < resultBytes.length; i++) {
			String hex = Integer.toHexString(0xff & resultBytes[i]);
			if (hex.length() == 1) {
				strHexString.append('0');
			}
			strHexString.append(hex);
		}
		// 得到返回結果
		return strHexString.toString();
	}

	public static String getSignContent(Map<String, Object> sortedParams) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			Object value = sortedParams.get(key);
			if (value instanceof String) {
				if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
					content.append((index == 0 ? "" : "&") + key + "=" + value);
					index++;
				}
			} else {
				throw new RuntimeException("有重复的KEY: " + key);
			}
		}
		return content.toString();
	}

}
