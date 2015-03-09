/*
 * Vivian Venter (13238435) & Jason Evans (13032608)
 * COS 332 - Practical 4
 * Collaboration
 */
import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

public class HTTPServer extends Thread{
	
	static String displayLine = "";
	static String displayAnswer = "0";
	
	Socket connectionClient = null;
	static String method = ""; //Describes what we will be doing with data entered CRUD
	static LinkedList<String> contactNames = new LinkedList<String>(); //Saves all currently stored Contact names.
	static LinkedList<String> contactNumbers = new LinkedList<String>(); //Saves all currently stored Contact contactNames
	
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;
	
	public HTTPServer(Socket client) {
		connectionClient = client;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		try{
			readFromFile();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		try { 
				System.out.println("----------------------------------------------------------------------------------------");
				System.out.println( "Client connected: "+ connectionClient.getInetAddress() + " :: " + connectionClient.getPort());
	
				inFromClient = new BufferedReader(new InputStreamReader (connectionClient.getInputStream()));
				outToClient = new DataOutputStream(connectionClient.getOutputStream());
	
				String requestString = inFromClient.readLine();
				System.out.println("REQUEST:  "+requestString);
				
				String tokenizerString = requestString;
				if (!tokenizerString.equals("")){
				StringTokenizer tokenizer = new StringTokenizer(tokenizerString);
				String Method = tokenizer.nextToken();
				String Query = tokenizer.nextToken();
	
				if (Method.equals("GET")) {  // GET Method used for http protocol
					if (Query.equals("/")) {  // The default home page
						resetRootFile();
						String fileName = Query.replaceFirst("/", "index.html");
						fileName = URLDecoder.decode(fileName);
						requestedFile(fileName);
					} 
					else if (Query.substring(0, 8).equals("/method=")) { // what will we be doing -> CRUD
						
						method = Query.substring(8, 9);
						/*
							method = d : DELETE
							method = i : INSERT
							method = e : EDIT
							method = s : SEARCH
						*/
						
						System.out.println("Method is : " + method);
						if (method.equals("d")){
							//String delName = Query.substring(,);
						}
						else if (method.equals("i")){
							String theName = "";
							String theNumber = "";
							
							int lastIndex = 14;
							theName = Query.substring(lastIndex + 1, Query.indexOf('&'));
							theName = theName.replace('+', ' ');
							lastIndex = (Query.indexOf('&') + 7);
							theNumber = Query.substring(lastIndex + 1);
							theNumber = theNumber.replace('+', ' ');
							//System.out.println(theName + " == " + theNumber);
							if (insertNewContact(theName, theNumber) == true)
								System.out.println("Contact has been added successfully!");
						}
						else if (method.equals("e")){
							
						}
						else if (method.equals("s")){
							
						}
						else{
							//Random stuff that need to be done
						}
						

						String newQ = "index.html";
						String fileName = newQ;
						fileName = URLDecoder.decode(fileName);

						/*Read in strings to update display and then write everything to a html file called index.html*/
						
						String c1 = constructTopPart("Temporary");
						String c2 = constructBottomPart();
						
						String fileContent = c1 + c2;
						
						FileWriter out = new FileWriter(fileName, false);
						BufferedWriter b = new BufferedWriter(out);
						b.write(fileContent);
						b.close();
						
						requestedFile(fileName);
					}
					else { // will handle any other types of files to be fetched for example my images to display
						String fileName = Query.replaceFirst("/", "");
						fileName = URLDecoder.decode(fileName);
						
						if (new File(fileName).isFile()){
							sendResponse(200, fileName, true);
						}
						else {
							sendResponse(404, "<b>HTTP_Server could not resolve request... <br> The Requested resource not found ...." +
									"Usage: http://127.0.0.1:8080 </b>", false); // page is not found error
						}
					}
				}
				else 
					sendResponse(404, "<b>HTTP_Server could not resolve request... <br> The Requested HTTP Method not supported .... </b>", false);
				}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("----------------------------------------------------------------------------------------\n");
	}

	public void sendResponse (int statusCode, String response, boolean isFile) throws Exception {

		String statusCODE = "";
		String fileName = "";
		String type = "Content-Type: text/html" + "\r\n";
		FileInputStream file_istream = null;
		String length = "";
		
		if (statusCode == 200)
			statusCODE = "HTTP/1.1 200 OK" + "\r\n";
		else
			statusCODE = "HTTP/1.1 404 Not Found" + "\r\n";

		if (isFile) {
		fileName = response;
		file_istream = new FileInputStream(fileName);
		length = "Content-Length: " + Integer.toString(file_istream.available()) + "\r\n";
		
		if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))
			type = "Content-Type: \r\n";
		}
		else {
			length = "Content-Length: " + response.length() + "\r\n";
		}

		outToClient.writeBytes(statusCODE);
		outToClient.writeBytes(type);
		outToClient.writeBytes(length);
		outToClient.writeBytes("\r\n");

		if (isFile) 
			sendFile(file_istream, outToClient);
		else 
			outToClient.writeBytes(response);

		outToClient.close();
	}

	public void sendFile (FileInputStream file_istream, DataOutputStream out) throws Exception {
		byte[] buffer = new byte[1024] ;
		int bytesRead;
	
		while ((bytesRead = file_istream.read(buffer)) != -1 ) {
			out.write(buffer, 0, bytesRead);
		}
		
		file_istream.close();
	}
	
	public static String constructTopPart(String list) {
		String theList = getList();
		String content = 
		" <!DOCTYPE html> " + 
		"<html>" +
		"<head> <meta charset=\"utf-8\"> <title>Personal Contact Book Database</title> <link href=\"styling.css\" rel=\"stylesheet\" type=\"text/css\"> "+
		"<script type=\"text/javascript\" src=\"jquery-1.11.2.min.js\"></script>" +
		"<script type=\"text/javascript\" src=\"jquery-ui-1.11.3.custom/jquery-ui.min.js\"></script>" +
		"<script type=\"text/javascript\" src=\"scripting.js\"></script>"+
		"</head>"  +
		"<body>" +
		"<div class=\"ApplicationNameBacking\">"+
		"<h1>Personal Contact Book Database</h1>" +
		"</div><br />"+
		"<div class=\"Main\">" +
		"<div class=\"functionality\">"+
		"<!--add buttons to throw splash of input forms for CRUD-->"+
		"<button id=\"search\">Search</button>"+
		"<button id=\"edit\">Edit</button>"+
		"<button id=\"insert\">Insert</button>"+
		"<button id=\"delete\">Delete</button>"+
		"</div><div class=\"UserInteraction\">"+
		"<textarea rows=\"30\" cols=\"60\" style=\"text-align: center;\"readonly>"+
		theList + //THIS IS WHERE THE LIST OF CONTACTS WILL BE
		"</textarea></div></div>";
	
		return content;
	}
	
	public static String constructBottomPart(){
		String content = "<div id=\"popupForSearch\" class=\"popups\">" +
		"<form method=\"GET\" action=\"http://127.0.0.1:8080/method=s\">" +
		"<h1>Searching for a contact</h1>"+
		"<h3>Name of Contact: <input name=\"name\" type=\"text\" placeholder=\"Jane Doe\"></input></h3><br />"+
		"<input type=\"submit\" value=\"Submit now\" />"+
		"<input type=\"reset\" value=\"Clear Now\" />" +
		"</form></div>" +
		"<div id=\"popupForEdit\" class=\"popups\">" +
		"<form method=\"GET\" action=\"http://127.0.0.1:8080/method=e\">" +
		"<h1>Editing a Contact</h1>" +
		"<h3>Name of Contact: <input name=\"name\" type=\"text\" placeholder=\"Jane Doe\"></input></h3><br /> " +
		"<h3>New Contact Number: <input name=\"number\" type=\"text\" placeholder=\"012 345 6789\"></input></h3><br /> " +
		"<input type=\"submit\" value=\"Submit now\" />" +
		"<input type=\"reset\" value=\"Clear Now\" />" +
		"</form></div>" +
		"<div id=\"popupForInsert\" class=\"popups\">" +
		"<form method=\"GET\" action=\"http://127.0.0.1:8080/method=i\">" +
		"<h1>Inserting a new contact</h1>" +
		"<h3>Name of New Contact: <input name=\"name\" type=\"text\" placeholder=\"Jane Doe\"></input></h3><br /> " +
		"<h3>New Contact Number: <input name=\"number\" type=\"text\" placeholder=\"012 345 6789\"></input></h3><br /> " +
		"<input type=\"submit\" value=\"Submit now\" />" +
		"<input type=\"reset\" value=\"Clear Now\" />" +
		"</form></div>" +
		"<div id=\"popupForDelete\" class=\"popups\">" +
		"<form method=\"GET\" action=\"http://127.0.0.1:8080/method=d\">" +
		"<h1>Deleting a contact</h1>" +
		"<h3>Contact name to delete: <input name=\"name\" type=\"text\" placeholder=\"Jane Doe\"></input></h3>" +
		"<input type=\"submit\" value=\"Submit now\" /> "+ 
		"<input type=\"reset\" value=\"Clear Now\" />" +
		"</form></div></body></html>";
		
		return content;
	}
	
	private static String getList(){
		String compiling = "NAME:					NUMBER:\n====================================================\n";
		
		//Testing to see if there are any contacts in our database.
		if (contactNames.isEmpty() == true){
			compiling = compiling + "There are currently no contacts in your database.";
		}
		else{
			for (int i = 0; i < contactNames.size(); i++){
				compiling += contactNames.get(i) + "\t\t\t" + contactNumbers.get(i);
				if ((i < contactNames.size()))
					compiling += '\n'; //Adds a newline to all strings except the last line in the list
			}
		}
		
		return compiling;
	}
	
	private static boolean insertNewContact(String newName, String newNumber){
		//Up to this point, we are only thinking about saving the data locally.
		//No mode of persistance has yet been added.
		if (contactNumbers.contains(newNumber)){//There already exists a contact with that number.
			System.out.println("There already exist a contact with that number.");
			return false;
		}
		else
		{
			contactNames.add(newName);
			contactNumbers.add(newNumber);
			System.out.println("Contact Number has been added successfully.");
			return true;
		}
	}
	
	private static String searchContact(String searchName){
		String searchResults = "";
		if (contactNames.contains(searchName)){
			//This search method will only get the first instance of that name.
			//Meaning we will have a problem with multiple equal names with different numbers.
			searchResults += contactNames.get(contactNames.indexOf(searchName)) + "\t\t\t" + contactNumbers.get(contactNames.indexOf(searchName));
		}
		else
			searchResults = "No contact with the name " + searchName + " could be found.";
		return searchResults;
	}
	
	private static boolean deleteContact(String delName){
		if (contactNames.contains(delName)){ //check if contact actually exist before deleteing
			int indexAt = contactNames.indexOf(delName);
			contactNames.remove(indexAt);
			contactNumbers.remove(indexAt);
			return true;
		}
		else
			return false;
	}
	
	private static boolean editContact(String contactName, String newNumber){
		//The functionality of this function is very limited. It can only update/edit the contact number, not the contact name itself.
		if (contactNames.contains(contactName)){
			int index = contactNames.indexOf(contactName);
			contactNumbers.set(index, newNumber);
			return true;
		}
		else
			return false;
	}
	
	private static void completePersistance(){
		String toFile = "";
		for (int i = 0; i < contactNames.size(); i++){
			toFile += contactNames.get(i) + ";" + contactNumbers.get(i) + "#\n";
		}
		
		try{
			PrintWriter output = new PrintWriter("numbers.txt");
			output.println(toFile);
			output.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void readFromFile() throws IOException{
		//This function simply populates the linked list with already existing contacts.
		File theFile = new File("numbers.txt");
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		String aLine = "";
		
		try{
			contactNames.clear();
			contactNumbers.clear();
			fis = new FileInputStream(theFile);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			
			while (dis.available() != 0){
				aLine = dis.readLine();
				//now we use the data to populate...
				String name = aLine.substring(0, aLine.indexOf(';'));
				contactNames.add(name);
				String number = aLine.substring(aLine.indexOf(';') + 1, aLine.indexOf('#'));
				contactNumbers.add(number);
			}
			
			fis.close();
			bis.close();
			dis.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static void resetRootFile() throws IOException {
		contactNames.clear();
		contactNumbers.clear();
		displayAnswer = "0";
		displayLine = "";
		String c1 = constructTopPart("---");
		String c2 = constructBottomPart();
		String fileContent = c1 + c2;
		FileWriter out = new FileWriter("index.html", false);
		BufferedWriter b = new BufferedWriter(out);
		b.write(fileContent);
		b.close();
	}
	
	private void requestedFile(String fn) throws Exception {
		File requestedFile = new File(fn);
		if (requestedFile.isFile()){
			sendResponse(200, fn, true);  //page is ok and loaded
		}
		else {
			sendResponse(404, "<b>HTTP_Server could not resolve request... <br> The Requested resource not found ...." +
					"Usage: http://127.0.0.1:8080 </b>", false); // page is not found error
		}
	}
	
	public static void main (String args[]) throws Exception {

		ServerSocket Server = new ServerSocket (8080, 10, InetAddress.getByName("127.0.0.1"));
		System.out.println ("HTTP_Server Waiting for a client on port 8080...");
		resetRootFile();
		while(true) {
			Socket connected = Server.accept();
			HTTPServer server = new HTTPServer(connected);
		    server.start();
		}
	}
}
