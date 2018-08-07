package io.oacy.pluviophile.context;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Http协议相关信息定义
 * 
 * @author Zephyr
 *
 */
public class HttpContext {
	public static final int CR = 13;
	public static final int LF = 10;
	
	/*
	 * Http协议中头信息定义
	 */
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	
	/*
	 * 介质类型映射信息
	 */
	private static Map<String,String> mimeTypeMapping = new HashMap<String,String>();
	
	static{
		//初始化静态资源
		init();
	}
	/**
	 * 初始化所有静态资源
	 */
	private static void init(){
		/*
		 * 读取conf/web.xml文档
		 * 
		 * 将跟标签<web-app>下所有名字为<mime-mapping>的
		 * 子标签解析出来
		 * 
		 * 将每个<mime-mapping>标签中的子标签:
		 * <extension>中间的文本作为key
		 * <mime-type>中间的文本作为value
		 * 存入到mimeTypeMapping中.
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> mimeList = root.elements("mime-mapping");
			for(Element mimeEle : mimeList){
				String ex = mimeEle.elementText("extension");
				String type = mimeEle.elementText("mime-type");
				mimeTypeMapping.put(ex, type);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据资源后缀获取HTTP协议规定对应的介质类型值
	 * @param extension
	 * @return
	 */
	public static String getMimeType(String extension){
		return mimeTypeMapping.get(extension);
	}
	
	

}
