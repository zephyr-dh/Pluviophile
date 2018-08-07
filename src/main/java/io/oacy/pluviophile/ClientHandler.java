package io.oacy.pluviophile;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import io.oacy.pluviophile.context.HttpContext;
import io.oacy.pluviophile.context.ServerContext;
import io.oacy.pluviophile.exception.EmptyRequestException;
import io.oacy.pluviophile.http.HttpRequest;
import io.oacy.pluviophile.http.HttpResponse;
import io.oacy.pluviophile.servlet.HttpServlet;

/**
 * 该线程任务供WebServer使用.负责处理指定客户端的交互.
 * 
 * @author Zephyr
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// 解析请求信息
			HttpRequest request = new HttpRequest(socket.getInputStream());
			// 创建响应对象
			HttpResponse response = new HttpResponse(socket.getOutputStream());

			// 获取请求路径
			String requestURI = request.getRequestURI();

			// 获取URI对应的Servlet名字
			String servletName = ServerContext.getServletNameByURI(requestURI);
			if (servletName != null) {
//				RegServlet servlet = new RegServlet();
//				servlet.service(request, response);
				/*
				 * 利用反射机制,通过servletName加载该类并实例化 然后调用其service方法.
				 */
				Class<?> cls = Class.forName(servletName);
//				Object obj = cls.newInstance();
//				Method method = cls.getDeclaredMethod("service", new Class[]{HttpRequest.class,HttpResponse.class});
//				method.invoke(obj, new Object[]{request,response});

				HttpServlet servlet = (HttpServlet) cls.newInstance();
				servlet.service(request, response);

			} else {
				File file = new File("webapps" + requestURI);
				if (file.exists()) {
					String name = file.getName();
					System.out.println("fileName:" + name);
					// 获取该文件后缀
					String extension = name.substring(name.lastIndexOf(".") + 1);
					System.out.println("extension:" + extension);
					// 根据后缀获取Content-Type对应的值
					String contentType = HttpContext.getMimeType(extension);
					System.out.println("content-type:" + contentType);

					// 设置响应头
					response.setContentType(contentType);
					response.setContentLength(file.length());

					response.setEntity(file);
					response.flush();
				} else {
					System.out.println("文件不存在!");
				}
			}
		} catch (EmptyRequestException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
