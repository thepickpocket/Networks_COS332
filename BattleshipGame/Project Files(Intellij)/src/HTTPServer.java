/*
 * Vivian Venter (13238435) & Jason Evans (13032608)
 * COS 332 - Practical 9
 * Battleship Game
 * Collaboration
 */
import java.io.*;
import java.net.*;
import java.util.*;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

public class HTTPServer extends Thread{

	static String GLOBAL_Notification = "";

	Socket connectionClient = null;
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;


	private char grid[][];
	private static int gridSize = 0;
	private static int puzzleNr = 0;
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
				if (!tokenizerString.equals("")){
				StringTokenizer tokenizer = new StringTokenizer(tokenizerString);
				String Method = tokenizer.nextToken();
				String Query = tokenizer.nextToken();
				System.out.println("Query:  "+ 	Query);

				if (Method.equals("GET")) {  // GET Method used for http protocol
					if (Query.equals("/") || Query.equals("/index.html") ) {  // The default home page
						String fileName = Query.replaceFirst("/", "index.html");
						fileName = URLDecoder.decode(fileName);
						requestedFile(fileName);
					}
					else if ( /*Query.equals("/g")*/ Query.contains("/grid")) {  //Grid init (format : /grid=6x6&p=2)
						gridSize = Integer.parseInt(Query.substring(6, Query.indexOf('x')));
						puzzleNr = Integer.parseInt(Query.substring(Query.indexOf('p')+2,Query.length()));

						System.out.println("Server setting the size of the grid to " + gridSize + " by " + gridSize);
						System.out.println("Puzzle number is: " + puzzleNr);

						grid = new char[gridSize][gridSize];
						initGrid(gridSize,puzzleNr);
						//init grid
						//display page with the grid of the correct size...
					}
					else if (Query.contains("/shoot")) { //Shoot a block (format: /shoot=0+5) means shoot block ar row 0 column 5
						int row = Integer.parseInt(Query.substring(7,Query.indexOf('+')));
						int col = Integer.parseInt(Query.substring(Query.indexOf('+')+1,Query.length()));

						if (row >= gridSize || row < 0) {
							System.out.println("Incorrect row! Out of bounds...");
							return;
						}

						if (col >= gridSize || col < 0) {
							System.out.println("Incorrect col! Out of bounds...");
							return;
						}

						System.out.println("Block [" + row + "][" + col + "] was shot!");

						if (grid != null) {
							char block = grid[row][col];

							if (block == '0') {
								System.out.println("No Ship was shot!");
								GLOBAL_Notification = "No Ship was shot! Try Again...";
							} else { //this depends in what we will use to represent the ships.

							}
						}
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

	private void initGrid(int size, int gridNr) throws IOException {
		// textfile will have naming convention: grid#+* where # is the grid size and * is the puzzle number
		// for example: grid6_2 will be puzzle 2 for a grid of size 6x6

		String line;
		char ch;
		int rowC = 0;
		String gridFilename = "grid"+size+"_"+gridNr+".txt";
		File gridFile = new File(gridFilename);
		FileInputStream in = new FileInputStream(gridFile);
		DataInputStream dis = new DataInputStream(in);

		while (dis.available() != 0) {
			line = dis.readLine();

			for (int k = 0; k < size; k++) {
				ch = line.charAt(k);
				grid[rowC][k] = ch;
			}
			rowC++;
		}

		printGrid();

	}

	private void printGrid() {
		System.out.println("GRID: ");
		for (int k = 0; k < gridSize; k++) {
			for (int i = 0; i < gridSize; i++) {
				if (i == (gridSize-1)) {
					System.out.print(grid[k][i]);
				}
				else {
					System.out.print(grid[k][i] + "	");
				}
			}
			System.out.println();
		}
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
	
	public static String constructTopPart(String str) {
		String content = "";

		return content;
	}
	
	private void requestedFile(String fn) throws Exception {
		System.out.println(fn);
		File requestedFile = new File(fn);
		if (requestedFile.isFile()){
			sendResponse(200, fn, true);  //page is ok and loaded
			System.out.println("Success");
		}
		else {
			System.out.println("Failed");
			sendResponse(404, "<b>HTTP_Server could not resolve request... <br> The Requested resource not found ...." +
					"Usage: http://127.0.0.1:8080 </b>", false); // page is not found error
		}
	}
	
	public static void main (String args[]) throws Exception {

		ServerSocket Server = new ServerSocket (8080, 10, InetAddress.getByName("127.0.0.1"));
		System.out.println("HTTP_Server Waiting for a client on port 8080...");
		while(true) {
			Socket connected = Server.accept();
			HTTPServer server = new HTTPServer(connected);
		    server.start();
		}
	}
}
