package io.oacy.pluviophile.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 当前类的每个实例用于表示一个具体的客户端发送过来的HTTP 请求
 * 
 * @author Zephyr
 *
 */

public class HttpRequest {
	/*
	 * 用于读取客户端发送过来数据的输入流.该流应该是 通过Socket获取的输入流.
	 */
	private InputStream in;

	/*
	 * 请求行相关信息
	 */
	// 请求方式
	private String method;

	// 请求资源的路径
	private String url;

	// 请求使用的HTTP协议版本
	private String protocol;

	/*
	 * 消息头相关信息
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/**
	 * 实例化HttpRequest
	 * 
	 * @param in
	 */
	public HttpRequest(InputStream in) {
		// 打桩
		System.out.println("HttpRequest:构造方法执行");
		this.in = in;
		/*
		 * 每个请求包含三部分解析: 1:解析请求行 2:解析消息头 3:解析消息正文
		 */
		// 1
		parseRequestLine();
		// 2
		parseHeaders();

		// 打桩
		System.out.println("HttpRequest:构造方法执行完毕");
	}

	/**
	 * 解析请求行
	 * 
	 * @param in
	 */
	private void parseRequestLine() {
		// 打桩
		System.out.println("开始解析请求行");
		// 读取请求行内容
		String requestLine = readLine();
		/*
		 * 将请求行中的三部分分别存到method,url,protocl属性上 GET /index.html HTTP/1.1
		 */
		// 按照空格拆分
		String[] array = requestLine.split("\\s");
		method = array[0];
		url = array[1];
		protocol = array[2];
		// 打桩
		System.out.println("请求行:" + requestLine);
	}

	/**
	 * 解析消息头
	 */
	private void parseHeaders() {
		/*
		 * 循环读取若干行,每一行是一个消息头 将消息头":"左面的内容作为key,右面内容 作为value存入到headers这个属性中保存.
		 * 当读取一行时返回的是空字符串,说明只读取 到了一个CRLF,这标志着请求中的消息头部分 读取完毕了.
		 */
		// 打桩
		System.out.println("开始解析消息头");
		while (true) {
			String line = readLine();
			if ("".equals(line)) {
				break;
			}
			int index = line.indexOf(":");
			String name = line.substring(0, index).trim();
			String value = line.substring(index + 1).trim();
			headers.put(name, value);
		}
		System.out.println("解析消息头完毕.headers:" + headers);
	}

	/**
	 * 读取一行字符串(以CRLF作为结尾),返回的字符串 中不包含最后的CRLF
	 * 
	 * @return
	 */
	private String readLine() {
		StringBuilder builder = new StringBuilder();
		try {
			int d = -1;// 保存每次读取的字节
			// c1保存上次读到的字符,c2为本次读到的字符
			char c1 = 'a';
			char c2 = 'a';
			while ((d = in.read()) != -1) {
				c2 = (char) d;
				// CR编码为13 LF编码为10
				if (c1 == 13 && c2 == 10) {
					break;
				}
				builder.append(c2);
				c1 = c2;
			}
			return builder.toString().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHeader(String name) {
		return headers.get(name);
	}

}
