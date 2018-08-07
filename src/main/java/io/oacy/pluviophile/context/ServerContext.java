package io.oacy.pluviophile.context;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端配置信息
 * 
 * @author Zephyr
 *
 */
public class ServerContext {
	// 服务端口
	public static int port;
	// 协议版本
	public static String protocol;
	// 线程池线程数量
	public static int threadPoolSum;
	// URI字符集
	public static String URIEncoding;

	/*
	 * Servlet与请求的映射关系 key:请求uri value:Servlet的名字
	 */
	private static Map<String, String> servletMappings = new HashMap<String, String>();

	static {
		init();
	}

	public static void init() {
		// 初始化服务端基础配置信息
		initServerConfig();
		// 初始化Servlet映射信息
		initServletMapping();
	}

	/**
	 * 初始化服务端基础配置信息
	 */
	private static void initServerConfig() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("conf/config.xml"));
			Element root = doc.getRootElement();
			Element con = root.element("Connector");

			port = Integer.parseInt(con.attributeValue("port"));
			protocol = con.attributeValue("protocol");
			threadPoolSum = Integer.parseInt(con.attributeValue("threadPool"));
			URIEncoding = con.attributeValue("URIEncoding");
			System.out.println(port + "," + protocol + "," + threadPoolSum + "," + URIEncoding);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化Servlet映射
	 */
	private static void initServletMapping() {
		/*
		 * 解析servlets.xml文件,将每个<servlet>标签 中的属性uri的值作为key,将属性class的值作为
		 * value存入到servletMappings中.
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("conf/servlets.xml"));
			// 获取根元素
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> list = root.elements();
			for (Element servletEle : list) {
				String uri = servletEle.attributeValue("uri");
				String classname = servletEle.attributeValue("class");
				servletMappings.put(uri, classname);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据给定的uri获取对应的Servlet的名字
	 * 
	 * @param uri
	 * @return
	 */
	public static String getServletNameByURI(String uri) {
		return servletMappings.get(uri);
	}

}
