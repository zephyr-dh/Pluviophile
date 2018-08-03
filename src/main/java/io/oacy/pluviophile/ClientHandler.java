package io.oacy.pluviophile;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import io.oacy.pluviophile.http.HttpRequest;
import io.oacy.pluviophile.http.HttpResponse;

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

			File file = new File("webapps" + request.getUrl());
			if (file.exists()) {
				response.setEntity(file);
				response.flush();
			}

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
