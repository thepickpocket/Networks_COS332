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
	static String WIN_Notification = "";

	Socket connectionClient = null;
	BufferedReader inFromClient = null;
	DataOutputStream outToClient = null;

	BattleShip game = null;
	private HashMap<Character,Integer> map_Indexes = new HashMap();

	public HTTPServer(Socket client) {
		connectionClient = client;
		game = new BattleShip();

		map_Indexes.put('A',0);
		map_Indexes.put('B',1);
		map_Indexes.put('C',2);
		map_Indexes.put('D',3);
		map_Indexes.put('E',4);
		map_Indexes.put('F',5);
		map_Indexes.put('G',6);
		map_Indexes.put('H',7);
		map_Indexes.put('I', 8);
		map_Indexes.put('J',9);
		map_Indexes.put('L',10);
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
					else if ( /*Query.equals("/g")*/ Query.contains("/?Grid")) {  //Grid init (format : /?Grid=6x6&Game=Game+2)
						game.setGridSize(Integer.parseInt(Query.substring(7, Query.indexOf('x'))));
						game.setPuzzleNr(Integer.parseInt(Query.substring(Query.indexOf('+')+1,Query.length())));

						System.out.println("Server setting the size of the grid to " + game.getGridSize() + " by " + game.getGridSize());
						System.out.println("Puzzle number is: " + game.getPuzzleNr());

						game.setGrid();
						game.initializeGrid();

						String fileName = "game.html";
						fileName = URLDecoder.decode(fileName);
						requestedFile(fileName);
					}
					else if (Query.contains("/shoot")) { //Shoot a block (format: /shoot=0+5) means shoot block ar row 0 column 5
						char rowC = Query.charAt(7);
						int row = map_Indexes.get(rowC);
						int col = Integer.parseInt(Query.substring(8,Query.length()));

						if (row >= game.getGridSize() || row < 0) {
							System.out.println("Incorrect row! Out of bounds...");
							return;
						}

						if (col >= game.getGridSize() || col < 0) {
							System.out.println("Incorrect col! Out of bounds...");
							return;
						}

						System.out.println("Block [" + row + "][" + col + "] was shot!");

						if (game.getGrid() != null) {
							char block = game.getBlock(row,col);

							if (block == '0') {
								System.out.println("No Ship was shot!");
								GLOBAL_Notification = "No Ship was shot! Try Again...";

							} else {

								//if the user shoots a block that is occupied by a block of ship A
								if (block == 'A') {
									GLOBAL_Notification = game.shipAShot();
								}
								//if the user shoots a block that is occupied by a block of ship B
								else if (block == 'B') {
									GLOBAL_Notification = game.shipBShot();
								}
								//if the user shoots a block that is occupied by a block of ship C
								else if (block == 'C') {
									GLOBAL_Notification = game.shipCShot();
								}
								//if the user shoots a block that is occupied by a block of ship D
								else if (block == 'D') {
									GLOBAL_Notification = game.shipDShot();
								}
								//if the user shoots a block that is occupied by a block of ship E
								else if (block == 'E') {
									GLOBAL_Notification = game.shipEShot();
								}

							}

							if (game.getTotalNumberOfBlocks() == 0) {
								WIN_Notification = "You have sunk all the ships! Congratulations!";
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
