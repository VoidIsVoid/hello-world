package ServerContainer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Server {
	static ServletRequest req = null;
	static ServletResponse res = null;
	
	public static void main(String args[]) throws Exception{
		final int port = 3333; 
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			println("服务器正在监视端口： " + serverSocket.getLocalPort());
			
			while(true){
				Socket clientSocket = serverSocket.accept();
				println("建立了与客户的一个新的TCP连接，该客户的地址为：" + clientSocket.getInetAddress() + ": " + clientSocket.getPort());
				service(clientSocket);
				clientSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void service(Socket socket) throws Exception{
		try {
			InputStream socketIn = socket.getInputStream();
			//Thread.sleep(500);
			req = new MyServletRequest(socketIn);
			println("正在创建ServletRequest对象");
			OutputStream socketOut = socket.getOutputStream();
			res = new MyServletResponse(socketOut);
			println("正在创建ServletResponse对象");
			String uri = ((MyServletRequest)req).getURI();
			
			ServletProcessor.processServletRequest((MyServletRequest)req, (MyServletResponse)res); 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
                    + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"
                    + "\r\n" + "<h1>Resource Not Found</h1>";
			res.getWriter().println(errorMessage);
			//e.printStackTrace();
		}
		
		
	}
	
	public static void println(String content){
		System.out.println(content);
	}
}
