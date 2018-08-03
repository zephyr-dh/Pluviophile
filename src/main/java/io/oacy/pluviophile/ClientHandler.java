package io.oacy.pluviophile;

import java.net.Socket;

/**
 * 该线程任务供WebServer使用.负责处理指定客户端的交互.
 * @author Zeohyr
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.setSocket(socket);
	}
	public void run() {
		System.out.println("一个客户端连接了!");
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
