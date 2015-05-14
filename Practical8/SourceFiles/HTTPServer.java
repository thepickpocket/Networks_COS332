/*
 * Vivian Venter (13238435) & Jason Evans (13032608)
 * COS 332 - Practical 8
 * Collaboration
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class HTTPServer extends Thread{
	
	static String displayLine = "";
	static String GLOBAL_Notification = " ";
	
	Socket connectionClient = null;
	static LinkedList<String> contactNames = new LinkedList<>(); //Saves all currently stored Contact names.
	static LinkedList<String> contactNumbers = new LinkedList<>(); //Saves all currently stored Contact contactNames
	
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;
	
	public HTTPServer(Socket client) {
		connectionClient = client;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		try {
				String tempName, name = "", tempNum, num = "";
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

				System.out.println("QUERY: " + Query);

				if (Method.equals("GET")) {  // GET Method used for http protocol
					if (Query.equals("/")) {  // The default home page
						resetRootFile();
						String fileName = Query.replaceFirst("/", "index.wml");
						fileName = URLDecoder.decode(fileName);
						requestedFile(fileName);
					}
					else if (Query.substring(0,2).equals("/i") && (Query.length()>=12)) { // inserting
						System.out.println("action: INSERT");
						tempName = Query.substring(17, Query.indexOf('&'));
						tempNum = Query.substring(Query.indexOf('&')+5,Query.length());

						name = trimName(tempName);
						System.out.println("Name to add is : " + name);

						num = trimNum(tempNum);

						System.out.println("Number to add is : " + num);

						if (insertNewContact(name,num)){
							completePersistence();
							System.out.println("Contact, " + name + " has been added successfully!");
							GLOBAL_Notification = "<p>\n <b>Notification:</b>\n <br></br>\n New Contact, " + name + " added successfully!\n </p>\n";
						}

						writeFileAndSendFile("insert.wml",'i',true);

					}
					else if (Query.substring(0,2).equals("/e") && (Query.length()>=10)) { // edit
						System.out.println("action: EDIT");

						tempName = Query.substring(15, Query.indexOf('&'));
						tempNum = Query.substring(Query.indexOf('&')+5,Query.length());
						
						name = trimName(tempName);
						System.out.println("Name to edit is : " + name);

						num = trimNum(tempNum);
						System.out.println("Number to edit is : " + num);

						if (editContact(name,num)){
							completePersistence();
							System.out.println("Contact, " + name + " has been updated successfully!");
							GLOBAL_Notification = "<p>\n <b>Notification:</b>\n <br></br>\n Contact, " + name + " has been updated successfully!\n </p>\n";
						}
						else{
							System.out.println("Contact, " + name + " could not be found or updated.");
							GLOBAL_Notification = "<p>\n <b>Notification:</b>\n <br></br>\n Contact, " + name + " could not be found or updated.\n </p>\n";
						}

						writeFileAndSendFile("edit.wml",'e',true);

					}
					else if (Query.substring(0,2).equals("/d") && (Query.length()>=12)) { // delete
						System.out.println("action: DELETE");
						tempName = Query.substring(17, Query.length());

						name = trimName(tempName);
						System.out.println("Name to delete is : " + name);

						if (deleteContact(name)){
							completePersistence();
							System.out.println("Contact. " + name + " has been removed successfully!");
							GLOBAL_Notification = "<p>\n <b>Notification:</b>\n <br></br>\n Contact, " + name + " removed successfully!\n </p>\n";
						}
						else {
							GLOBAL_Notification = "<p>\n <b>Notification:</b>\n <br></br>\n Contact, " + name + " could not be found.\n </p>\n";
						}

						writeFileAndSendFile("delete.wml",'d',true);

					}
					else if (Query.substring(0,2).equals("/s") && (Query.length()>=12)) { // searching
						System.out.println("action: SEARCH");
						tempName = Query.substring(17, Query.length());

						name = trimName(tempName);
						System.out.println("Name to search is : " + name);

						if (contactNames.contains(name)) {
							GLOBAL_Notification = "<p>\n <b>Result:</b> <br></br>" + name + " ===>" + contactNumbers.get(contactNames.indexOf(name)) + "\n" + "</p>\n";
						} else {
							GLOBAL_Notification = "<p>\n <b>Result:</b> <br></br>\n Could not find any search results. <br></br>\n Contact not Found!\n </p>\n";
						}

						writeFileAndSendFile("search.wml",'s',true);
					}
					else if (Query.substring(0,3).equals("/in")) { // index -> back
						System.out.println("action: BACK");

						resetRootFile();
						String fileName = Query.replaceFirst("/", "");
						fileName = URLDecoder.decode(fileName);
						requestedFile(fileName);

						clearFiles();
					}
					else { // will handle any other types of files to be fetched
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

	private String trimNum(String tempNum) {
		//Trimming Number
		String num = "";
		for (int k = 0; k < tempNum.length(); k++) {
			if (tempNum.charAt(k) == '%') {
				num += " ";
				k += 2;
			}
			else {
				num += tempNum.charAt(k);
			}
		}
		return num;
	}

	private String trimName(String tempName) {
		//Trimming Name
		String name = "";
		for (int k = 0; k < tempName.length(); k++) {
			if (tempName.charAt(k) != '%' && tempName.charAt(k) != '2' && tempName.charAt(k) != '0') {
				name += tempName.charAt(k);
			}

			if (tempName.charAt(k) == '0') {
				name += " ";
			}
		}

		return name;
	}

	@SuppressWarnings("deprecation")
	private void writeFileAndSendFile(String fileName, char method, boolean send) throws Exception {
		fileName = URLDecoder.decode(fileName);

		String fileContent = " ";
		switch(method) {
			case 's':	fileContent = constructSearchFile();
						break;
			case 'd':	fileContent = constructDeleteFile();
						break;
			case 'i':	fileContent = constructInsertFile();
						break;
			case 'e':	fileContent = constructEditFile();
						break;
			case 'S':	fileContent = clearSearchFile();
						break;
			case 'D':	fileContent = clearDeleteFile();
						break;
			case 'I':	fileContent = clearInsertFile();
						break;
			case 'E':	fileContent = clearEditFile();
						break;
		}

		FileWriter out = new FileWriter(fileName, false);
		BufferedWriter b = new BufferedWriter(out);
		b.write(fileContent);
		b.close();

		if (send) {
			requestedFile(fileName);
		}
	}

	public void sendResponse (int statusCode, String response, boolean isFile) throws Exception {
		String statusCODE;
		String fileName;
		String type = "Content-Type: text/vnd.wap.wml" + "\r\n";
		FileInputStream file_istream = null;
		String length;
		
		if (statusCode == 200)
			statusCODE = "HTTP/1.1 200 OK" + "\r\n";
		else
			statusCODE = "HTTP/1.1 404 Not Found" + "\r\n";

		if (isFile) {
		fileName = response;
		file_istream = new FileInputStream(fileName);
		length = "Content-Length: " + Integer.toString(file_istream.available()) + "\r\n";
		
		if (!fileName.endsWith(".wml"))
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
	
	public static String constructIndexFile(String action) {
		String theList;
		if (action.equals("root")) {
			theList = getList();
		}
		else {
			theList = " ";
		}
		String content =
				"<?xml version=\"1.0\"?>\n" +
						"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
						"\"http://www.wapforum.org/DTD/wml12.dtd\">" +
						"<wml>\n" +
						"    <card id=\"one\" title=\"PhoneBook\">\n" +
						"        <p>\n" +
						"            <big><b>Personal Contact Book Database</b></big>\n" +
						"            <br></br>\n" +
						"            <a href=\"search.wml\">Search</a>\n" +
						"            <br></br>\n" +
						"            <a href=\"edit.wml\">Edit</a>\n" +
						"            <br></br>\n" +
						"            <a href=\"delete.wml\">Delete</a>\n" +
						"            <br></br>\n" +
						"            <a href=\"insert.wml\">Insert</a>\n" +
						"        </p>\n" +
									theList +
						"    </card>\n" +
						"</wml>";
		return content;
	}

	private String constructSearchFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
						"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
						"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
						"<wml>\n" +
						"    <card id=\"one\" title=\"Search Page\">\n" +
						"        <p>\n" +
						"            <big><b>Search Page</b></big>\n" +
						"                Name: <input name=\"name\" size=\"12\"/>\n" +
						"                <br></br>\n" +
						"                <anchor>\n" +
						"                    <go method=\"get\" href=\"\">\n" +
						"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
						"                    </go>\n" +
						"                    Search\n" +
						"                </anchor>\n" +
						"            <br></br>\n" +
						"            <a href=\"index.wml\">Back</a>\n" +
						"        </p>\n" +
								GLOBAL_Notification +
						"    </card>\n" +
						"</wml>";
		return content;
	}

	private String clearSearchFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
						"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
						"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
						"<wml>\n" +
						"    <card id=\"one\" title=\"Search Page\">\n" +
						"        <p>\n" +
						"            <big><b>Search Page</b></big>\n" +
						"                Name: <input name=\"name\" size=\"12\" value=\"\"/>\n" +
						"                <br></br>\n" +
						"                <anchor>\n" +
						"                    <go method=\"get\" href=\"\">\n" +
						"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
						"                    </go>\n" +
						"                    Search\n" +
						"                </anchor>\n" +
						"            <br></br>\n" +
						"            <a href=\"index.wml\">Back</a>\n" +
						"        </p>\n" +
						"    </card>\n" +
						"</wml>";
		return content;
	}

	private String constructInsertFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
						"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
						"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
						"<wml>\n" +
						"    <card id=\"one\" title=\"Insert Page\">\n" +
						"        <p>\n" +
						"            <big><b>Insert Page</b></big>\n" +
						"                Name: <input name=\"name\" size=\"12\" value=\"\"/>\n" +
						"                <br></br>\n" +
						"                Telephone Number :  <input name=\"num\" size=\"12\" value=\"\"/>\n" +
						"                <br></br>\n" +
						"                <anchor>\n" +
						"                    <go method=\"get\" href=\"\">\n" +
						"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
						"                        <postfield name=\"num\" value=\"$(num)\"/>\n" +
						"                    </go>\n" +
						"                    Add\n" +
						"                </anchor>\n" +
						"            <br></br>\n" +
						"            <a href=\"index.wml\">Back</a>\n" +
						"        </p>\n" +
								GLOBAL_Notification +
						"    </card>\n" +
						"</wml>";
		return content;
	}

	private String clearInsertFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
				"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
				"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
				"<wml>\n" +
				"    <card id=\"one\" title=\"Insert Page\">\n" +
				"        <p>\n" +
				"            <big><b>Insert Page</b></big>\n" +
				"                Name: <input name=\"name\" size=\"12\" value=\"\"/>\n" +
				"                <br></br>\n" +
				"                Telephone Number :  <input name=\"num\" size=\"12\" value=\"\"/>\n" +
				"                <br></br>\n" +
				"                <anchor>\n" +
				"                    <go method=\"get\" href=\"\">\n" +
				"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
				"                        <postfield name=\"num\" value=\"$(num)\"/>\n" +
				"                    </go>\n" +
				"                    Add\n" +
				"                </anchor>\n" +
				"            <br></br>\n" +
				"            <a href=\"index.wml\">Back</a>\n" +
				"        </p>\n" +
				"    </card>\n" +
				"</wml>";
		return content;
	}

	private String constructEditFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
						"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
						"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
						"<wml>\n" +
						"    <card id=\"one\" title=\"Edit Page\">\n" +
						"        <p>\n" +
						"            <big><b>Edit Page</b></big>\n" +
						"                Name: <input name=\"name\" size=\"12\"/>\n" +
						"                <br></br>\n" +
						"                New Telephone Number :  <input name=\"num\" size=\"12\"/>\n" +
						"                <br></br>\n" +
						"                <anchor>\n" +
						"                    <go method=\"get\" href=\"\">\n" +
						"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
						"                        <postfield name=\"num\" value=\"$(num)\"/>\n" +
						"                    </go>\n" +
						"                    Update\n" +
						"                </anchor>\n" +
						"            <br></br>\n" +
						"            <a href=\"index.wml\">Back</a>\n" +
						"        </p>\n" +
								GLOBAL_Notification +
						"    </card>\n" +
						"</wml>";
		return content;
	}

	private  String clearEditFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
				"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
				"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
				"<wml>\n" +
				"    <card id=\"one\" title=\"Edit Page\">\n" +
				"        <p>\n" +
				"            <big><b>Edit Page</b></big>\n" +
				"                Name: <input name=\"name\" size=\"12\" value=\"\"/>\n" +
				"                <br></br>\n" +
				"                New Telephone Number :  <input name=\"num\" size=\"12\" value=\"\"/>\n" +
				"                <br></br>\n" +
				"                <anchor>\n" +
				"                    <go method=\"get\" href=\"\">\n" +
				"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
				"                        <postfield name=\"num\" value=\"$(num)\"/>\n" +
				"                    </go>\n" +
				"                    Update\n" +
				"                </anchor>\n" +
				"            <br></br>\n" +
				"            <a href=\"index.wml\">Back</a>\n" +
				"        </p>\n" +
				"    </card>\n" +
				"</wml>";

		return content;
	}

	private String constructDeleteFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
						"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
						"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
						"<wml>\n" +
						"    <card id=\"one\" title=\"Delete Page\">\n" +
						"        <p>\n" +
						"            <big><b>Delete Page</b></big>\n" +
						"                Name: <input name=\"name\" size=\"12\"/>\n" +
						"                <br></br>\n" +
						"                <anchor>\n" +
						"                    <go method=\"get\" href=\"\">\n" +
						"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
						"                    </go>\n" +
						"                    Delete\n" +
						"                </anchor>\n" +
						"            <br></br>\n" +
						"            <a href=\"index.wml\">Back</a>\n" +
						"        </p>\n" +
								GLOBAL_Notification +
						"    </card>\n" +
						"</wml>";
		return content;
	}

	private String clearDeleteFile() {
		String content =
				"<?xml version=\"1.0\"?>\n" +
				"<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
				"        \"http://www.wapforum.org/DTD/wml12.dtd\">\n" +
				"<wml>\n" +
				"    <card id=\"one\" title=\"Delete Page\">\n" +
				"        <p>\n" +
				"            <big><b>Delete Page</b></big>\n" +
				"                Name: <input name=\"name\" size=\"12\" value=\"\"/>\n" +
				"                <br></br>\n" +
				"                <anchor>\n" +
				"                    <go method=\"get\" href=\"\">\n" +
				"                        <postfield name=\"name\" value=\"$(name)\"/>\n" +
				"                    </go>\n" +
				"                    Delete\n" +
				"                </anchor>\n" +
				"            <br></br>\n" +
				"            <a href=\"index.wml\">Back</a>\n" +
				"        </p>\n" +
				"    </card>\n" +
				"</wml>";

		return content;
	}

	private void clearFiles() throws Exception {
		//Clear Search Page
		writeFileAndSendFile("search.wml",'S',false);

		//Clear Insert Page
		writeFileAndSendFile("insert.wml", 'I', false);

		//Clear Delete Page
		writeFileAndSendFile("delete.wml", 'D', false);

		//Clear Edit Page
		writeFileAndSendFile("edit.wml",'E',false);
	}

	private static String getList(){
		String compiling =
				"		<p>\n" +
				"		<br></br>\n"+
				"			<big><b>Database: </b></big>"+
				"			<table columns=\"2\" align=\"CC\">\n";
		
		//Testing to see if there are any contacts in our database.
		if (contactNames.isEmpty() == true){
			compiling = compiling + "There are currently no contacts in your database.";
		}
		else{
			for (int i = 0; i < contactNames.size(); i++){
				compiling += "				<tr>\n";
				compiling += "					<td>" + contactNames.get(i) + "</td>";
				compiling += "					<td>" + contactNumbers.get(i) + "</td>" ;

				if ((i < contactNames.size())) {
					compiling += "				</tr>\n";
				}
			}
		}
		compiling += "			</table>\n" +
				 	 "       </p>\n";
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
	
	private static void completePersistence(){
		String toFile = "";
		for (int i = 0; i < contactNames.size(); i++){
			toFile += contactNames.get(i) + ";" + contactNumbers.get(i) + "#";
			if (i < contactNames.size()-1){
				toFile += "\n";
			}
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
		FileInputStream fis;
		BufferedInputStream bis;
		DataInputStream dis;
		String aLine;
		
		try{
			contactNames.clear();
			contactNumbers.clear();
			fis = new FileInputStream(theFile);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			
			while (dis.available() != 0){
				aLine = dis.readLine();
				//System.out.println(aLine);
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

		displayLine = "";
		try{
			readFromFile();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		String fileContent = constructIndexFile("root");
		FileWriter out = new FileWriter("index.wml", false);
		BufferedWriter b = new BufferedWriter(out);
		b.write(fileContent);
		b.close();
	}
	
	private void requestedFile(String fn) throws Exception {
		File requestedFile = new File(fn);
		if (requestedFile.isFile()){
			System.out.println("SUCCEED");
			sendResponse(200, fn, true);  //page is ok and loaded
		}
		else {
			System.out.println("ERROR");
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
