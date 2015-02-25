import java.io.*;
import java.net.*;
import java.util.*;

public class MultiUserServer implements Runnable {
	
	public static class Person{
		String name;
		String number;
		Person next = null;
		
		Person(String nameInput, String numberInput){
			name = nameInput;
			number = numberInput;
			next = null;
		}
		
		public String getName(){
			return name;
		}
		
		public String getNumber(){
			return number;
		}
		
		public Person getNext(){
			return next;
		}
	}
	
	static int clientNum = 0;
	int port = 0;
	static LinkedList<Person> database = new LinkedList<Person>();
	public final static String filename = "numbers.txt";
	public final static String encoding = "UTF-8";
	static LinkedList<Socket> clientsList = new LinkedList<Socket>();
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static LinkedList<String> clientInfoServerDisplay = new LinkedList<String>(); 
	
	MultiUserServer(int port) throws IOException{
		this.port = port;
		
	}
	
	public static void writeToFile() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(filename, encoding);
		
		for (int counter = 0; counter < database.size(); counter++){
			writer.println(database.get(counter).getName() + ";" + database.get(counter).getNumber() + "#");
		}
		
		writer.close();
	}
	
	public static void addPerson(String name, String number, int clientNumber){
		Person person = new Person(name, number);
		database.addLast(person);
		
		if (clientNumber > -1) { // clients server msg
			System.out.println(clientDisplay(clientNumber) + "Confirmation: " + name + " added to contacts.");
		}
		else if (clientNumber == -1) {
			System.out.println("Server: Confirmation: " + name + " added to contacts.");
		}
		//A change was made in the database
		//Executing persistence -> writing data to file.
		try {
			writeToFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void removePerson(String name, int clientNumber) throws IOException{
		for (int counter = 0; counter < database.size(); counter++){
			if ((database.get(counter).getName()).equals(name)){
				database.remove(counter);
				
				System.out.println(clientDisplay(clientNumber) + "Removing a contact..."); //server msg
				
				if (clientNumber > -1) {
					echoToClient(ANSI_YELLOW + "\nConfirmation: " + name + " has been removed from contacts.\n" + ANSI_RESET, clientNumber);
				}

				System.out.println(clientDisplay(clientNumber) +"Confirmation: " + name + " has been removed from contacts.");
			
				//A change was made in the database
				//Executing persistence -> writing data to file.
				try {
					writeToFile();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		System.out.println(clientDisplay(clientNumber) + "Error: " + name + " could not be found in contacts so was not removed.");
		echoToClient(ANSI_RED + "\nError: " + name + " could not be found in contacts and therefore was not removed.\n" + ANSI_RESET, clientNumber);
	}
	
	public static int search(String name, int clientNumber) throws IOException{
		for (int counter = 0; counter < database.size(); counter++){
			if ((database.get(counter).getName()).equals(name)){
				
				
				if (clientNumber > -1) {
					echoToClient(ANSI_YELLOW + "\nSearch Successful:", clientNumber);
					echoToClient("===========================================", clientNumber);
					echoToClient("Name: " + "\t\t" + name, clientNumber);
					echoToClient("Number: " + '\t' + database.get(counter).getNumber(), clientNumber);
					echoToClient("===========================================", clientNumber);
				} 
				else if (clientNumber == -1) //server
				{
					System.out.println(clientDisplay(clientNumber));
					System.out.println("===========================================");
					System.out.println("Name: " + "\t\t" + name);
					System.out.println("Number: " + '\t' + database.get(counter).getNumber());
					System.out.println("===========================================");
				}
				
				return 0;
			}
		}
		System.out.println(clientDisplay(clientNumber) + "Error: " + name + " could not be found in contacts.");
		echoToClient(ANSI_RED + "\nError: " + name + " could not be found in contacts.", clientNumber);
		return 1;
	}
	
	
	public static void edit(String name, String newNumber, int clientNumber){
		for (int counter = 0; counter < database.size(); counter++){
			if ((database.get(counter).getName()).equals(name)){
				database.get(counter).number = newNumber;
				
				if (clientNumber > -1) { // clients server msg
					System.out.println(clientDisplay(clientNumber) + "Confirmation: Contact " + name + " has been updated.");
				}
				else if (clientNumber == -1) {
					System.out.println("Server: Confirmation: Contact " + name + " has been updated.");
				}
				
				//A change was made in the database
				//Executing persistence -> writing data to file.
				try {
					writeToFile();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static int searchEdit(String name, int clientNumber) throws IOException{
		for (int counter = 0; counter < database.size(); counter++){
			if ((database.get(counter).getName()).equals(name)){
				
				if (clientNumber > -1) {
					echoToClient(ANSI_YELLOW + "===========================================", clientNumber);
					echoToClient("Name: " + "\t\t" + name, clientNumber);
					echoToClient("Number: " + '\t' + database.get(counter).getNumber(), clientNumber);
					echoToClient("===========================================\n" + ANSI_RESET, clientNumber);
				} 
				return 0;
			}
		}
		
		System.out.println(clientDisplay(clientNumber) + "Error: " + name + " could not be found and was not changed.");
		echoToClient(ANSI_RED + "\nError: " + name + "  could not be found and was not changed.\n" + ANSI_RESET, clientNumber);
		return 1;
	}
	
	public static void list(int clientNumber) throws IOException{
		if (clientNumber > -1){
			echoToClient(ANSI_YELLOW + "===================================================", clientNumber);
			echoToClient("NAME" + "\t\t\t\t" + "NUMBER", clientNumber);
			for (int count = 0; count < database.size(); count++){
				echoToClient(database.get(count).getName() + "\t\t\t" + database.get(count).getNumber(), clientNumber);
			}
			echoToClient("===================================================" + ANSI_RESET, clientNumber);
		}
		else{ //Server Output
			System.out.println(clientDisplay(clientNumber));
			System.out.println("===================================================");
			System.out.println("NAME" + "\t\t\t\t" + "NUMBER");
			for (int count = 0; count < database.size(); count++){
				System.out.println(database.get(count).getName() + "\t\t\t" + database.get(count).getNumber());
			}
			System.out.println("===================================================");
		}
	}
	
	public void populate() throws IOException{
		String line = null;
		try{
			String name;
			String number;
			
			System.out.println("\n-----------------------------------------------------------");
			System.out.println("Server populating database...");
			
			FileReader fileReader = new FileReader(filename);
			BufferedReader buff = new BufferedReader(fileReader);
			
			while((line=buff.readLine()) != null){
				name = line.substring(0, line.indexOf(";"));
				number = line.substring(line.indexOf(";") + 1, line.indexOf("#"));
				addPerson(name, number, -1);
			}
			buff.close();
			System.out.println("Database has been populated... ");
			System.out.println("-----------------------------------------------------------\n");
			
		}
		catch(FileNotFoundException e){
			System.out.println("Error: No file found.");//Simple logging message.
			//System will create a new file if file does not exist when writing database data to file.
			//If a file already existed, it will be overwritten.
		}
		
	}
	
	synchronized static void echoToClient(String message, int N) throws IOException  {
		PrintWriter print;
		Socket soc = new Socket();
		soc = clientsList.get(N);
		print = new PrintWriter(soc.getOutputStream(), true);
		print.println(message);
	}
	
	synchronized static void addClient(int clientN, Socket c) {
		clientsList.add(clientN, c);
		clientNum++;
		System.out.println("Client Connected: #" + clientN + " : " + c.getPort());
	}
	
	public static String clientDisplay(int c)
	{
		return (clientInfoServerDisplay.get(c));
	}
	
	
	public void run() {
		try{
			ServerSocket listener = new ServerSocket(port);
			System.out.println("Server running on Port: " + port);
			ClientHandler ch;
			int ln;
			while (true){
				Socket client = listener.accept();
				ch = new ClientHandler(client, clientNum);
				ch.start();
				
				System.out.println("\n-----------------------------------------------------------");
				System.out.println("Client #"+ clientNum + " requesting connection...");
				ln = clientNum;
				clientInfoServerDisplay.add(ln, "Client #" + clientNum + " : " + client.getPort() + " :: ");
				addClient(clientNum, client);
				System.out.println("-----------------------------------------------------------\n");
			}
		}
		catch(Exception e){
			//No error to display yet.
		}
	}
	
		
	public static void main(String args[]){
		try{
			
			System.out.println("Server Starting...");
			
			int port = 8080; //default port
			if (args.length > 0)
				port = Integer.parseInt(args[0]);
			
			MultiUserServer server;
			server = new MultiUserServer(port);
			Thread thread = new Thread(server);
			
			thread.start();
			
			Scanner scan = new Scanner(System.in);
			
			server.populate();
			
			while(true){
				String line = scan.nextLine();

				if (line.equals("Add")){
					System.out.print("New Contact Name: ");
					String newCName = scan.nextLine();
					System.out.print("New Contact Number: ");
					String newCNumber = scan.nextLine();
					server.addPerson(newCName, newCNumber, -1);
				}
				else if (line.equals("Search")){
					System.out.print("Contact Name: ");
					String contact = scan.nextLine();
					server.search(contact,-1);
				}
				else if (line.equals("Delete")){
					System.out.println("Warning: Contact will be deleted perminantly and cannot be regained.");
					System.out.print("Continue [Y/N]: ");
					String answer = scan.nextLine();
					if (answer.equals("Y") || answer.equals("y")){
						System.out.print("Contact Name: ");
						answer = scan.nextLine();
						server.removePerson(answer, -1);
					}
					else{
						System.out.println("Confirmation: Deletion operation terminated.");
						return;
					}
					
				}
				else if (line.equals("Edit")){
					System.out.print("Contact Name: ");
					String person = scan.nextLine();
					System.out.print("New Number for " + person + " : ");
					String newNumber = scan.nextLine();
					server.edit(person, newNumber, -1);
				}
				else if (line.equals("List")){
					server.list(-1);
				}
				else if(line.equals("quit") || line.equals("Quit")){
					System.out.println("Confirmation: A user has left the server.");
					break;
				}
				else{
					System.out.println("Command was not recognized. Available commands will be listed:");
					System.out.println("\t[Add]\t\tAdds a new contact to the contact book database.");
					System.out.println("\t[Delete]\tPerminantly deletes a contact from the contact book database.");
					System.out.println("\t[Search]\tSearches for a specific contact in the contact book database.");
					System.out.println("\t[Edit]\t\tEdits an existing contact in the contact book database.");
					System.out.println("\t[List]\t\tLists all available contacts in the contact book database.");
				}
				line = "";
			}
			
			thread.interrupt();
			System.out.println("Server Quiting");
			System.exit(1);
		}
		catch(Exception e){
			
		}
		
	}
}
