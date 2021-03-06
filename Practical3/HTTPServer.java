/*
 * Vivian Venter (13238435) & Jason Evans (13032608)
 * COS 332 - Practical 3
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
	static LinkedList<Integer> numbers = new LinkedList<Integer>();
	static LinkedList<String> operators = new LinkedList<String>();
	
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
	
				//System.out.println("METHOD:  "+Method);
				//System.out.println("QUERYSTRING:  "+Query);
				//System.out.println("QUERYSTRING two :  "+ Query.substring(0, 5));
				
	
				if (Method.equals("GET")) {  // GET Method used for http protocol
					
					if (Query.equals("/")) {  // The default home page which is calculator
						resetRootFile();
						String fileName = Query.replaceFirst("/", "index.html");
						fileName = URLDecoder.decode(fileName);
						requestedFile(fileName);
					} 
					else if (Query.substring(0, 5).equals("/val=")) { // a digit was inserted
						
						String value = Query.substring(5, 6);
						int val = Integer.parseInt(value);
						numbers.addLast(val); // put value in stack for operation
						
						if (numbers.size() == 2)
						{
							computePreliminary();
						}
						
						setDisplayLine(value); // set value in display line
						
						//System.out.println("-------------------------------------------"+
						//		"127.0.0.1:8080/val= --------- " + (val+1) + " ------------------------------------");	
						
						String newQ = "index.html";
						String fileName = newQ;
						fileName = URLDecoder.decode(fileName);

						/*Read in strings to update display and then write everything to a html file called index.html*/
						
						String d_line = getDisplayLine();
						String d_ans = getAnswer();
						String c1 = constructTopPart(d_line,d_ans);
						String c2 = constructBottomPart();
						
						String fileContent = c1 + c2;
						
						//System.out.println(fileContent);
						
						FileWriter out = new FileWriter(fileName, false);
						BufferedWriter b = new BufferedWriter(out);
						b.write(fileContent);
						b.close();
						
						requestedFile(fileName);
					}
					else if (Query.substring(0, 4).equals("/op=")) {

						Boolean canEvaluate = false;
						String newQ = "index.html";
						String fileName = newQ;
						fileName = URLDecoder.decode(fileName);
						
						String operator = Query.substring(4, Query.length());
						
						//System.out.println("-------------------------------------------"+
						//		"127.0.0.1:8080/op= --------- " + operator  + " ------------------------------------");

						if (operator.equals("+")) { // operator is plus
							//System.out.println("IN METHOD----------------");
							setDisplayLine(operator);
							operators.addLast(operator);
							//System.out.println("IN METHOD: " + operators.getLast());
							canEvaluate = evaluate(operator);
						}
						else if (operator.equals("-")) { // operator is minus
							setDisplayLine(operator);
							operators.addLast(operator);
							canEvaluate = evaluate(operator);
						}
						else if (operator.equals("divide")) { // operator is divide
							setDisplayLine("/");
							operators.addLast("/");
							canEvaluate = evaluate(operator);
						}
						else if (operator.equals("x")) { // operator is multiply
							setDisplayLine(operator);
							operators.addLast(operator);
							canEvaluate = evaluate(operator);
						}
						else if (operator.equals("equal")) { // operator is equal
							operators.addLast("=");
							canEvaluate = evaluate(operator);
						}
						
						if (canEvaluate == true) {
							compute();
						}
						
						String d_line = getDisplayLine();
						String d_ans = getAnswer();
						String c1 = constructTopPart(d_line,d_ans);
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
		if (operators.get(0).equals("+")) {
			
			//System.out.println("IN METHOD compute: " + operators.get(0));
			v1 = numbers.pop();
			v2 = numbers.pop();
			
			//System.out.println("IN METHOD: compute" + v1 + "    " + v2);
			
			ans = v1 + v2;
			
			//System.out.println("IN METHOD: compute" + ans);
			
			operators.remove(0);
			setAnswer(ans);
		}
		else if (operators.get(0).equals("-")) {
			v1 = numbers.pop();
			v2 = numbers.pop();
			
			ans = v1 - v2;
			operators.remove(0);
			setAnswer(ans);
		}
		else if (operators.get(0).equals("x")) {
			v1 = numbers.pop();
			v2 = numbers.pop();
			
			ans = (v1 * v2);
			operators.remove(0);
			setAnswer(ans);
		}
		else if (operators.get(0).equals("/")) {
			v1 = numbers.pop();
			v2 = numbers.pop();
			
			ans = (v1 / v2);
			operators.remove(0);
			setAnswer(ans);
		}
		else if (operators.get(0).equals("=")) {
			v1 = numbers.pop();

			ans = v1;
			operators.remove(0);
			setAnswer(ans);
		}
	}

	private void setAnswer(int ans) {
		numbers.addLast(ans);
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
		
		//System.out.println("IN METHOD: evaluate" + numbers.size());
		
		if (operator.equals("equal"))
		{
			return true;
		}
		
		if (numbers.size() != 2) {  //only one operand available do not compute answer
			return false;
		}
		else if (numbers.size() == 2) { // two operand available can compute answer
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
	
	public static String constructTopPart(String GLOBAL_STRING, String GLOBAL_ANSWER) {
		String content = 
		" <!DOCTYPE html> " + 
		"<html>" +
		"<head> <meta charset=\"utf-8\"> <title>Basic Calculator</title> <link href=\"styleCalc.css\" rel=\"stylesheet\" type=\"text/css\"> </head>"  +
		"<body>" +
		"<div class=\"mainDiv\">" +
			"<div class=\"Digit_Display\">" + GLOBAL_STRING +
				"<div class=\"Calculator_Display\">" + GLOBAL_ANSWER + "</div>" +
			"</div>"
			;
			
			return content;
	}
	
	public static String constructBottomPart(){
		String content = "<button class=\"num1div\" onClick=\"location.href='http://127.0.0.1:8080/val=1'\" type=\"submit\" formmethod=\"get\"> 1 </button> " +
        "<button class=\"num2div\" onClick=\"location.href='http://127.0.0.1:8080/val=2'\" type=\"submit\" formmethod=\"get\"> 2 </button>" +
        "<button class=\"num3div\" onClick=\"location.href='http://127.0.0.1:8080/val=3'\" type=\"submit\" formmethod=\"get\"> 3 </button>" +
        "<button class=\"plusdiv\" onClick=\"location.href='http://127.0.0.1:8080/op=+'\" type=\"submit\" formmethod=\"get\"> + </button>" +
        "<br>" +
		"<button class=\"num4div\" onClick=\"location.href='http://127.0.0.1:8080/val=4'\" type=\"submit\" formmethod=\"get\"> 4 </button>" +
        "<button class=\"num5div\" onClick=\"location.href='http://127.0.0.1:8080/val=5'\" type=\"submit\" formmethod=\"get\"> 5 </button>" +
        "<button class=\"num6div\" onClick=\"location.href='http://127.0.0.1:8080/val=6'\" type=\"submit\" formmethod=\"get\"> 6 </button>" +
        "<button class=\"minusdiv\" onClick=\"location.href='http://127.0.0.1:8080/op=-'\" type=\"submit\" formmethod=\"get\"> - </button>" +
        "<br>" +
		"<button class=\"num7div\" onClick=\"location.href='http://127.0.0.1:8080/val=7'\" type=\"submit\" formmethod=\"get\"> 7 </button>" +
        "<button class=\"num8div\" onClick=\"location.href='http://127.0.0.1:8080/val=8'\" type=\"submit\" formmethod=\"get\"> 8 </button>" +
        "<button class=\"num9div\" onClick=\"location.href='http://127.0.0.1:8080/val=9'\" type=\"submit\" formmethod=\"get\"> 9 </button>" +
        "<button class=\"multdiv\" onClick=\"location.href='http://127.0.0.1:8080/op=x'\" type=\"submit\" formmethod=\"get\"> x </button>" +
        "<br>" +
		"<button class=\"num0div\" onClick=\"location.href='http://127.0.0.1:8080/val=0'\" type=\"submit\" formmethod=\"get\"> 0 </button>" +
        "<button class=\"equaldiv\" onClick=\"location.href='http://127.0.0.1:8080/op=equal'\" type=\"submit\" formmethod=\"get\"> = </button> " +
		"<button class=\"clrdiv\" onClick=\"location.href='http://127.0.0.1:8080'\" type=\"submit\" formmethod=\"get\"> CLR </button> " +
        "<button class=\"dividediv\" onClick=\"location.href='http://127.0.0.1:8080/op=divide'\" type=\"submit\" formmethod=\"get\"> &divide; </button>" +
		"</div>" +
		"</body>" +
		"</html>";
		
		return content;
	}

	private String getDisplayLine() {
		return displayLine;
	}
	
	private static void resetRootFile() throws IOException {
		numbers.clear();
		operators.clear();
		displayAnswer = "0";
		displayLine = "";
		String c1 = constructTopPart("---","0");
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
