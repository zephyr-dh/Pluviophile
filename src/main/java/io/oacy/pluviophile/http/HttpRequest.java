package io.oacy.pluviophile.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import io.oacy.pluviophile.context.HttpContext;
import io.oacy.pluviophile.context.ServerContext;
import io.oacy.pluviophile.exception.EmptyRequestException;

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

	// 具体的请求资源路径(?前的内容)
	private String requestURI;

	// 请求附带的参数部分(?后面的内容)
	private String queryString;

	// 存储所有参数
	private Map<String, String> parameters = new HashMap<String, String>();

	/*
	 * 消息头相关信息
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/**
	 * 实例化HttpRequest
	 * 
	 * @param in
	 * @throws EmptyRequestException
	 */
	public HttpRequest(InputStream in) throws EmptyRequestException {
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
		// 3
		parseContent();

		// 打桩
		System.out.println("HttpRequest:构造方法执行完毕");
	}

	/**
	 * 解析消息正文
	 */
	private void parseContent() {
		/*
		 * 查看消息头中是否有Content-Length.若没有 则表示没有消息正文部分.
		 */
		if (headers.containsKey(HttpContext.HEADER_CONTENT_LENGTH)) {
			try {
				System.out.println("开始解析消息正文");
				/*
				 * 判断是否为form表单数据
				 */
				String contentType = headers.get(HttpContext.HEADER_CONTENT_TYPE);
				if ("application/x-www-form-urlencoded".equals(contentType)) {
					// 开始处理form表单数据
					System.out.println("开始处理表单数据");
					// 读取正文中的所有字节
					int length = Integer.parseInt(headers.get(HttpContext.HEADER_CONTENT_LENGTH));
					byte[] data = new byte[length];
					in.read(data);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析请求行
	 * 
	 * @param in
	 * @throws EmptyRequestException
	 */
	private void parseRequestLine() throws EmptyRequestException {
		// 打桩
		System.out.println("开始解析请求行");
		// 读取请求行内容
		String requestLine = readLine();

		// 判断是否为空请求
		if (requestLine.length() == 0) {
			throw new EmptyRequestException("空请求");
		}

		/*
		 * 将请求行中的三部分分别存到method,url,protocl属性上 GET /index.html HTTP/1.1
		 */
		// 按照空格拆分
		String[] array = requestLine.split("\\s");
		method = array[0];
		url = array[1];
		protocol = array[2];

		// 详细解析请求地址部分
		parseUrl();
		// 打桩
		System.out.println("请求行:" + requestLine);
	}

	/**
	 * 解析请求行中地址部分
	 */
	private void parseUrl() {
		/*
		 * 请求行中地址部分(this.url)有两种情况: 1直接请求资源: 例如:/myweb/reg.html 2请求功能并附带参数:
		 * 例如:/myweb/reg?username=zhangsan&passowrd=123456...
		 * 
		 * 该功能要实现: 1:将url中"?"(若有"?")之前的内容设置到属性requestURI中
		 * 2:将url中"?"(若有"?")之后的内容设置到属性queryString中 3:解析queryString,将每个参数取出,将参数名做为key
		 * 参数值作为value存入属性parameters这个map中.
		 */
		// 首先判断是否在地址中存在"?"
		int index = this.url.indexOf("?");
		if (index == -1) {
			this.requestURI = this.url;
		} else {
			/*
			 * 按照?拆分为两部分
			 */
			this.requestURI = this.url.substring(0, index);
			this.queryString = this.url.substring(index + 1);

			/*
			 * 将queryString转码
			 * 
			 * URLDecoder可以对浏览器地址栏发送过来的内容 进行转码 HTTP协议要求地址栏传递的参数只能符合ISO8859-1编码
			 * 内容,所以像中文这样的字符是不能直接通过地址栏传递 的.现在的解决办法是将中文以UTF-8编码转换为字节,
			 * 再将字节以16进制形式表示,前面以%开始.这样每个中文 是三个字节,地址栏表示形式类似:%E8%8C%83%
			 * 注:两位16进制可以表示一个8位2进制(一个字节).
			 * 
			 * URLDecoder的decode方法可以对含有上述的字符按照 指定的字符集转码.
			 */
			try {
				queryString = URLDecoder.decode(queryString, ServerContext.URIEncoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			/*
			 * 拆分所有参数
			 */
			// 1按照"&"拆分出每个参数
			String[] paraArray = queryString.split("&");
			for (String para : paraArray) {
				// 2按照"="拆分出参数名与参数值
				String[] array = para.split("=");
				if (array.length == 2) {
					this.parameters.put(array[0], array[1]);
				} else {
					this.parameters.put(array[0], "");
				}
			}
		}

		System.out.println("requestURI:" + this.requestURI);
		System.out.println("queryString:" + this.queryString);
		System.out.println("parameters:" + this.parameters);

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
				if (c1 == HttpContext.CR && c2 == HttpContext.LF) {
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

	public String getRequestURI() {
		return this.requestURI;
	}

	public String getQueryString() {
		return this.queryString;
	}

	public String getParameter(String name) {
		return this.parameters.get(name);
	}

}