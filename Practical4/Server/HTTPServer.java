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
	static LinkedList<Integer> contactNames = new LinkedList<Integer>(); //Saves all currently stored Contact names.
	static LinkedList<String> contactNumbers = new LinkedList<String>(); //Saves all currently stored Contact contactNames
	
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;
	
	public HTTPServer(Socket client) {
		connectionClient = client;
	}

	@SuppressWarnings("deprecation")
	public void run() {

		try { 
				System.out.println("----------------------------------------------------------------------------------------");
				System.out.println( "Client connected: "+ connectionClient.getInetAddress() + " :: " + connectionClient.getPort());
	
				inFromClient = new BufferedReader(new InputStreamReader (connectionClient.getInputStream()));
				outToClient = new DataOutputStream(connectionClient.getOutputStream());
	
				String requestString = inFromClient.readLine();
				System.out.println("REQUEST:  "+requestString);
				
				String tokenizerString = requestString;
	
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
					else if (Query.substring(0, 7).equals("/method=")) { // what will we be doing -> CRUD
						
						method = Query.substring(8, 9);
						/*
							method = d : DELETE
							method = i : INSERT
							method = e : EDIT
							method = s : SEARCH
						*/
						
						System.out.println(method);

						String newQ = "index.html";
						String fileName = newQ;
						fileName = URLDecoder.decode(fileName);

						/*Read in strings to update display and then write everything to a html file called index.html*/
						
						String d_line = getDisplayLine();
						String d_ans = getAnswer();
						String c1 = constructTopPart("Temporary");
						String c2 = constructBottomPart();
						
						String fileContent = c1 + c2;
						
						//System.out.println(fileContent);
						
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
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("----------------------------------------------------------------------------------------\n");
	}

	private void computePreliminary() {
		compute();
	}

	private void compute() {
		int v1 = 0, v2 = 0, ans = 0;
		if (contactNumbers.get(0).equals("+")) {
			
			//System.out.println("IN METHOD compute: " + contactNumbers.get(0));
			v1 = contactNames.pop();
			v2 = contactNames.pop();
			
			//System.out.println("IN METHOD: compute" + v1 + "    " + v2);
			
			ans = v1 + v2;
			
			//System.out.println("IN METHOD: compute" + ans);
			
			contactNumbers.remove(0);
			setAnswer(ans);
		}
		else if (contactNumbers.get(0).equals("-")) {
			v1 = contactNames.pop();
			v2 = contactNames.pop();
			
			ans = v1 - v2;
			contactNumbers.remove(0);
			setAnswer(ans);
		}
		else if (contactNumbers.get(0).equals("x")) {
			v1 = contactNames.pop();
			v2 = contactNames.pop();
			
			ans = (v1 * v2);
			contactNumbers.remove(0);
			setAnswer(ans);
		}
		else if (contactNumbers.get(0).equals("/")) {
			v1 = contactNames.pop();
			v2 = contactNames.pop();
			
			ans = (v1 / v2);
			contactNumbers.remove(0);
			setAnswer(ans);
		}
		else if (contactNumbers.get(0).equals("=")) {
			v1 = contactNames.pop();

			ans = v1;
			contactNumbers.remove(0);
			setAnswer(ans);
		}
	}

	private void setAnswer(int ans) {
		contactNames.addLast(ans);
		displayAnswer = Integer.toString(ans);
		//System.out.println("IN METHOD: setAnswer" + getAnswer());
	}

	private void setDisplayLine(String value) {
		String dummy = displayLine + value;
		this.displayLine = dummy;
	}

	private String getAnswer() {
		return displayAnswer;
	}
	
	private boolean evaluate(String operator) {
		
		//System.out.println("IN METHOD: evaluate" + contactNames.size());
		
		if (operator.equals("equal"))
		{
			return true;
		}
		
		if (contactNames.size() != 2) {  //only one operand available do not compute answer
			return false;
		}
		else if (contactNames.size() == 2) { // two operand available can compute answer
			return true;
		}
		
		return false;
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
		String content = 
		" <!DOCTYPE html> " + 
		"<html>" +
		"<head> <meta charset=\"utf-8\"> <title>Personal Contact Book Database</title> <link href=\"styling.css\" rel=\"stylesheet\" type=\"text/css\"> "+
		"<script type=\"text/javascript\" src=\"jquery-1.11.2.min.js\"><script>" +
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
		list + //THIS IS WHERE THE LIST OF CONTACTS WILL BE
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

	private String getDisplayLine() {
		return displayLine;
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
