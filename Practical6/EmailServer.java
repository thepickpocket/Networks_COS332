/**
 * Creators for this server.
 * @author Jason Richard Evans u13032608
 * @author Vivian Laura-Lee Venter u13238435
 */

//USES
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class EmailServer {
    //CONSTANTS
    static final String HOST = "mail.cs.up.ac.za";
    static final int PORT = 25;
    static String sReturns;

    @Deprecated
    public static void sendEmail(String sMessage){
        Socket smtpSocket = null;
        DataOutputStream os = null;
        DataInputStream is = null;

        Date dDate = new Date();
        DateFormat dFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);

        try{
            //Opening port to the server
            smtpSocket = new Socket(HOST, PORT);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());
            if (smtpSocket != null && os != null && is != null){
                //CODE TO SEND THE EMAIL...
                sReturns = is.readLine();
                System.out.println("RESPONSE: " + sReturns);
                try{
                    os.writeBytes("HELO\r\n");
                    sReturns = is.readLine();
                    System.out.println("RESPONSE: " + sReturns);
                    os.writeBytes("MAIL FROM:<jasonevans@tuks.co.za>\r\n"); //SENDER
                    sReturns = is.readLine();
                    System.out.println("RESPONSE: " + sReturns);
                    os.writeBytes("RCPT TO:<u13032608@tuks.co.za>\r\n"); //RECEIVER
                    sReturns = is.readLine();
                    System.out.println("RESPONSE: " + sReturns);
                    //os.writeBytes("RCPT CC: <u13238435@tuks.co.za>\r\n");

                    //Writing the message header
                    os.writeBytes("DATA\r\n");
                    sReturns = is.readLine();
                    System.out.println("RESPONSE: " + sReturns);

                    os.writeBytes("Subject: Home Alarm Raised\r\n\r\n"); //Two newlines to conform to RFC 882
                    os.writeBytes(sMessage + "\r\n\r\nThe Practic Alarm Team." + "\r\n");
                    os.writeBytes(".\r\n"); //to end off a message
                    sReturns = is.readLine();
                    System.out.println("RESPONSE: " + sReturns);
                    os.writeBytes("QUIT\r\n");

                    String responseLine;
                    while((responseLine = is.readLine()) != null){
                        if (responseLine.indexOf("ok") != -1)
                            break;
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
