package com.example.ssm.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class BaseController {

	@ModelAttribute
	public void setDefaultCharacterEncoding() {
		getResponse().setCharacterEncoding("utf8");
	}

	public HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	public HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}

	public ServletRequestAttributes getRequestAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	}

	public ServletContext getContext() {
		return ContextLoader.getCurrentWebApplicationContext().getServletContext();
	}

	/**
	 * 返回200
	 */
	public Object ok(Object obj) {
		HttpServletResponse response = getResponse();
		response.setStatus(HttpStatus.OK.value());
		try {
			response.getWriter().write(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回400
	 */
	public Object badRequest(String str) {
		HttpServletResponse response = getResponse();
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回500
	 */
	public Object internalServerError(String str) {
		HttpServletResponse response = getResponse();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		try {
			response.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回302
	 */
	public String redirect(String url) {
		HttpServletResponse response = getResponse();
		response.setStatus(HttpStatus.FOUND.value());
		try {
			response.getWriter().write(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public String getRealIp() {
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

}
