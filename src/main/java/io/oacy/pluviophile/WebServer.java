package io.oacy.pluviophile;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer web服务端主类
 * 
 * @author Zephyr
 *
 */
public class WebServer {
	/*
	 * 负责与客户端(浏览器)进行TCP连接的ServerSocket
	 */
	private ServerSocket server;

	/**
	 * 构造方法,用来初始化服务端
	 */
	public WebServer() {
		try {
			server = new ServerSocket(8088);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 服务端开始工作的方法
	 */
	public void start() {
		try {
			while (true) {
				Socket socket = server.accept();
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}

}