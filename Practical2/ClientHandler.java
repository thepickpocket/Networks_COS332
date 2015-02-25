import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
	private PrintWriter out;
	private BufferedReader in;
	private Socket connectionToClient;
	private int clientN;
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	
	public ClientHandler(Socket socket,int clientNum){
		connectionToClient = socket;
		clientN = clientNum;
	}
	
	public String clientDisplay()
	{
		return (MultiUserServer.clientInfoServerDisplay.get(clientN));
	}
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(connectionToClient.getInputStream()));
			out = new PrintWriter(this.connectionToClient.getOutputStream(), true);
			
			MultiUserServer.echoToClient(MultiUserServer.clientInfoServerDisplay.get(clientN), clientN);
			MultiUserServer.echoToClient(ANSI_GREEN + "\n----------------------------------------------------------------------------------------------------", clientN);
			MultiUserServer.echoToClient("\t\t\t\t\tWelcome to the Database", clientN);
			MultiUserServer.echoToClient("----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
			
			while (true){
				String command = in.readLine().trim();
				
				if (command.equals("Add")){
					
					System.out.println("\n-----------------------------------------------------------"); //server msg
					System.out.println(clientDisplay() + "Adding a contact..."); //server msg
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------" + ANSI_RESET, clientN);
					
					MultiUserServer.echoToClient(ANSI_GREEN + "New Contact Name:" + ANSI_RESET, clientN);
					String newCName = in.readLine().trim();
					MultiUserServer.echoToClient(ANSI_GREEN + "New Contact Number: " + ANSI_RESET , clientN);
					String newCNumber = in.readLine().trim();
					
					MultiUserServer.echoToClient("\n", clientN);
					
					MultiUserServer.addPerson(newCName, newCNumber, clientN);
					MultiUserServer.echoToClient(ANSI_YELLOW + "Confirmation: New person added to contacts.", clientN);
					MultiUserServer.search(newCName,clientN);
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
					System.out.println("-----------------------------------------------------------\n"); //server msg
				}
				else if (command.equals("Search")) {
					
					System.out.println("\n-----------------------------------------------------------"); //server msg
					System.out.println(clientDisplay() + "Searching for a contact..."); //server msg
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------" + ANSI_RESET, clientN);
					
					MultiUserServer.echoToClient(ANSI_GREEN + "Contact Name: " + ANSI_RESET, clientN);
					String contact = in.readLine().trim();
					
					MultiUserServer.echoToClient(ANSI_YELLOW, clientN);
					int k = MultiUserServer.search(contact, clientN);
					out.println(ANSI_RESET);
					
					if (k == 0) {
						System.out.println(clientDisplay() + contact + " has been found.\nClient search successful..."); //server msg
					} 
					else if (k == 1) {
						System.out.println(clientDisplay() + "Client search unsuccessful...\n"); //server msg
					}
					
					System.out.println("-----------------------------------------------------------\n"); //server msg
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
				}
				else if (command.equals("Delete")) {
					
					System.out.println("\n-----------------------------------------------------------"); //server msg
					System.out.println(clientDisplay() + "Request to remove a contact..."); //server msg
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------" + ANSI_RESET, clientN);
					
					MultiUserServer.echoToClient(ANSI_RED + "Warning: Contact will be deleted permanently and cannot be regained." + ANSI_GREEN, clientN);
					MultiUserServer.echoToClient("Continue [Y/N]: \n" + ANSI_RESET, clientN);
					String answer = in.readLine().trim();
					
					if (answer.equals("Y") || answer.equals("y")){
						System.out.println(clientDisplay() + "Trying to remove a contact..."); //server msg
						MultiUserServer.echoToClient(ANSI_GREEN + "Contact Name: " + ANSI_RESET, clientN);
						answer = in.readLine().trim();
						MultiUserServer.removePerson(answer, clientN);
					}
					else if (answer.equals("N") || answer.equals("n")) {
						System.out.println( clientDisplay() + "Request to remove a contact aborted..."); //server msg
						MultiUserServer.echoToClient(ANSI_YELLOW +"Confirmation: Deletion operation terminated.\n"  + ANSI_RESET, clientN);
					}
					else {
						System.out.println(clientDisplay() + "Request to remove a contact aborted..."); //server msg
						MultiUserServer.echoToClient(ANSI_RED + "Input not recognized: Deletion operation terminated.\n"  + ANSI_RESET, clientN);
					}
					
					System.out.println("-----------------------------------------------------------\n"); //server msg
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
				}
				else if (command.equals("Edit")){
					
					System.out.println("\n-----------------------------------------------------------"); //server msg
					System.out.println(clientDisplay() + "Request to edit a contact..."); //server msg
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------" + ANSI_RESET, clientN);
					
					MultiUserServer.echoToClient(ANSI_GREEN + "Contact Name: " + ANSI_RESET, clientN);
					String person = in.readLine().trim();
					
					int k = MultiUserServer.searchEdit(person, clientN);
		
					if (k == 0) {
						System.out.println(clientDisplay() + "Contact found: Editing " + person + " now..."); //server msg
						MultiUserServer.echoToClient(ANSI_GREEN + "New Number for " + person + " :" + ANSI_RESET, clientN);
						String newNumber = in.readLine().trim();
						MultiUserServer.edit(person, newNumber, clientN);
						
						MultiUserServer.echoToClient(ANSI_YELLOW + "\n\nContact " + person + " changed successful.\n", clientN);
						MultiUserServer.searchEdit(person, clientN);
						out.println(ANSI_RESET);
						
						System.out.println(clientDisplay() + "Edit operation completed..."); //server msg
						System.out.println("-----------------------------------------------------------\n"); //server msg
					}
					else if (k == 1){
						MultiUserServer.echoToClient(ANSI_RED + "Edit operation terminated.\n" + ANSI_RESET, clientN);
						System.out.println(clientDisplay() + "Edit operation aborted..."); //server msg
						System.out.println("-----------------------------------------------------------\n"); //server msg
					}
					
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
				}
				else if (command.equals("List")){
					System.out.println(clientDisplay() + "Requesting to list all contacts..."); //server msg
					MultiUserServer.list(clientN);
				}
				else if(command.equals("quit") || command.equals("Quit")){
					MultiUserServer.echoToClient("Confirmation: User #" + clientN + " closed the connection to the server.", clientN);
					System.out.println(clientDisplay() + "Confirmation: User #" + clientN + " closed the connection to the server.");
					break;
				}
				else if (command.equals("Clear")) {
					out.println("\033[2J");
					MultiUserServer.echoToClient(MultiUserServer.clientInfoServerDisplay.get(clientN), clientN);
					MultiUserServer.echoToClient(ANSI_GREEN + "\n----------------------------------------------------------------------------------------------------", clientN);
					MultiUserServer.echoToClient("\t\t\t\t\tWelcome to the Database", clientN);
					MultiUserServer.echoToClient("----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
				}
				else{
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------" + ANSI_RESET, clientN);
					out.println(ANSI_GREEN + "Command was not recognized. Available commands will be listed:");
					out.println("\t[Add]\t\tAdds a new contact to the contact book database.");
					out.println("\t[Delete]\tPerminantly deletes a contact from the contact book database.");
					out.println("\t[Search]\tSearches for a specific contact in the contact book database.");
					out.println("\t[Edit]\t\tEdits an existing contact in the contact book database.");
					out.println("\t[List]\t\tLists all available contacts in the contact book database.\n");
					out.println("\t[Clear]\t\tTo clear the screen.\n" + ANSI_RESET);
					MultiUserServer.echoToClient(ANSI_CYAN + "----------------------------------------------------------------------------------------------------\n" + ANSI_RESET, clientN);
				}
				command = "";
			}
			connectionToClient.close();
		}
		catch(Exception e){
			
		}
	}

}
