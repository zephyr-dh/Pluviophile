package io.oacy.pluviophile.servlet;

import java.io.File;

import io.oacy.pluviophile.http.HttpRequest;
import io.oacy.pluviophile.http.HttpResponse;

/**
 * 用来处理请求的所有Servlet的父类
 * 
 * @author Zephgyr
 *
 */
public class HttpServlet {

	public void service(HttpRequest request, HttpResponse response) {
		/*
		 * 实际在使用Tomcat时的HttpServlet类的service 会根据请求方式:"GET","POST"来调用另外的两个
		 * 方法:doGet,doPost 现在常用做法是直接继承HttpServlet并重写service 处理业务逻辑了.
		 */
	}

	/**
	 * 跳转指定页面
	 * 
	 * @param uri
	 * @param request
	 * @param response
	 */
	public void forward(String uri, HttpRequest request, HttpResponse response) {
		File page = new File("webapps" + uri);
		response.setContentLength(page.length());
		response.setContentType("text/html");
		response.setEntity(page);
		response.flush();
	}
}