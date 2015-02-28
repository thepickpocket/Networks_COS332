import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


public class Server implements Runnable{
	int port = 8080;
	int clientNum;
	static LinkedList<Socket> clientsList = new LinkedList<Socket>();
	
	public Server(int chosenPort){
		this.port = chosenPort;
	}
	
	public static void main(){
		
	}

	public void run() {
		try{
			ServerSocket listener = new ServerSocket(port);
			System.out.println("Server running on Port: " + port);
			//ClientHandler ch;
			int ln;
			while (true){
				Socket client = listener.accept();
				//ch = new ClientHandler(client, clientNum);
				//ch.start();
				
				System.out.println("\n-----------------------------------------------------------");
				System.out.println("Client #"+ clientNum + " requesting connection...");
				ln = clientNum;
				//clientInfoServerDisplay.add(ln, "Client #" + clientNum + " : " + client.getPort() + " :: ");
				//addClient(clientNum, client);
				System.out.println("-----------------------------------------------------------\n");
			}
		}
		catch(Exception e){
			//No error to display yet.
		}
	}
}
