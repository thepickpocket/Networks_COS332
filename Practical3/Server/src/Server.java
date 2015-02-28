import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{
	
	private static volatile boolean serverStatus = true;
	private ServerSocket serverSocket;
	StringBuffer request = new StringBuffer(2048);
	String statusMessage;
	String docRoot;
	int port;
	
	public synchronized void startServer(int port, String docRoot){
		//setOwner();
		this.port = port;
		setDocRoot(docRoot);
		String message = new String();
		message = "Starting server on port " + Integer.toString(port);
		try{
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		}
		catch(IOException e){
			e.printStackTrace(); System.exit(1);
		}
		
		this.serverStatus = true;
		serverNotify();
	}
	
	private synchronized void serverNotify() {
		notify();
	}
	
	public void stopServer(){
		try{
			serverStatus = false;
			serverSocket.close();
		}
		catch(IOException e){
			e.printStackTrace(); System.exit(1);
		}
	}

	public static void main(String args[]) throws Exception{

	}
	
	public void setDocRoot(String theRoot){
		docRoot = theRoot;
	}
	
	public String getDocRoot(){
		return docRoot;
	}
	
	public void setStatusMessage(String message){
		statusMessage = message;
	}
	
	public void run(){
		while (true){
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try{
				synchronized(this){
					while (serverStatus == false){
						wait();
					}
				}
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			
			try{
				socket = serverSocket.accept();
				
				if (serverStatus == true){
					input = socket.getInputStream();
					output = socket.getOutputStream();
					HttpRequests request = new HttpRequests(input);
					int parseCode = request.parseEntireRequest();
					
					if (parseCode > 0){
						this.setStatusMessage(request.getErrorMessage() + '\n');
					}
					else{
						this.setStatusMessage(request.getHttpRequest() +  '\n');
						HttpResponse response = new HttpResponse(output, this);
						if (request.getMethod().compareTo("GET") == 0){
							response.setRequest(request);
						}
						else{
							response.returnError(501); //Server only handles GET method.
						}
					}
				}
				
				socket.close();
			}
			catch(Exception e){
				if (serverStatus == false){
					System.out.println(e.getMessage());
				}
				else{
					e.printStackTrace();
				}
				continue;
			}
		}
	}
}