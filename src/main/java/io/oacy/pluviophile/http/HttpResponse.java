package io.oacy.pluviophile.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * 该类表示一个具体的Http响应信息 一个标准的Http响应应该包含三部分: 1:状态行 2:响应头 3:响应正文
 * 
 * @author Zephyr
 *
 */
public class HttpResponse {
	/*
	 * 通过这个输出流将响应信息发送给客户端
	 */
	private OutputStream out;
	/*
	 * 响应实体 实际给客户端发送的文件
	 */
	private File entity;

	public HttpResponse(OutputStream out) {
		this.out = out;
	}

	/**
	 * 回复客户端
	 */
	public void flush() {
		/*
		 * 1:发送状态行 2:发送响应头 3:发送响应正文
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
	}

	/**
	 * 发送状态行
	 */
	private void sendStatusLine() {
		try {
			System.out.println("HttpResponse:发送状态行");
			String line = "HTTP/1.1 200 OK";
			println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送响应头
	 */
	private void sendHeaders() {
		System.out.println("HttpResponse:发送响应头");
		try {
			String line = "Content-Type:text/html";
			System.out.println("header:" + line);
			println(line);

			line = "Content-Length:" + entity.length();
			System.out.println("header:" + line);
			println(line);

			// 单独发送CRLF
			println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送响应正文
	 */
	private void sendContent() {
		System.out.println("HttpResponse:发送响应正文");
		try (FileInputStream fis = new FileInputStream(entity);
				BufferedInputStream bis = new BufferedInputStream(fis);) {
			byte[] buf = new byte[1024 * 10];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向客户端发送一行字符串,发送该字符串后会自动发送 CRLF.
	 * 
	 * @param line 给定的字符串末尾不需要CRLF
	 */
	private void println(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);// written CR
			out.write(10);// written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getEntity() {
		return entity;
	}

	public void setEntity(File entity) {
		this.entity = entity;
	}

}