package pop3Client_Prac7;

/**
 * Creators for this Pop3Server.
 * @author Vivian Laura-Lee Venter u13238435
 * @author Jason Richard Evans u13032608
 */

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Pop3Server {

    static String popHostServer;
    static final int PORT = 110;
    static String sReturns;

    static Socket popSocket = null;
    static DataOutputStream os = null;
    static DataInputStream is = null;

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Pop3Server(String server) {
        System.out.println(ANSI_CYAN + "Connecting to server " + server  + "..." + ANSI_WHITE);
        popHostServer = server;
        try {
            popSocket = new Socket(popHostServer, PORT);
            os = new DataOutputStream(popSocket.getOutputStream());
            is = new DataInputStream(popSocket.getInputStream());
            sReturns = is.readLine();
            System.out.println(ANSI_GREEN + "RESPONSE SERVER: " + sReturns + ANSI_WHITE);
            System.out.println(ANSI_CYAN + "Server Connected... " + popSocket.toString() + ANSI_WHITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Email> listEmails() throws IOException {
        LinkedList<Email> emailList = new LinkedList<>();
        LinkedList<Integer>  EmailNrs = new LinkedList<>();

        System.out.println(ANSI_CYAN + "Listing Emails..."  +  ANSI_WHITE);
        os = new DataOutputStream(popSocket.getOutputStream());
        is = new DataInputStream(popSocket.getInputStream());
        int bytes;
        String lineHolder;
        os.writeBytes("LIST\r\n");
        sReturns = is.readLine();
        lineHolder = sReturns.substring(4, sReturns.length() - 10);
        System.out.println(ANSI_GREEN + lineHolder + ANSI_WHITE);
        bytes = Integer.parseInt(lineHolder);
        System.out.println(ANSI_GREEN + "RESPONSE LIST: " + sReturns + "   " + bytes + ANSI_WHITE);

        for (int i = 0; i < bytes; i++) {
            sReturns = is.readLine();
            int emailNr = Integer.parseInt(sReturns.substring(0, sReturns.indexOf(" ")));
            System.out.println(ANSI_GREEN +  sReturns + ANSI_WHITE);
            EmailNrs.add(emailNr);
        }

        sReturns = is.readLine();

        for (int i = 0; i < bytes; i++) {

            Email e = retrieveEmails(EmailNrs.get(i), os, is);

            if (e != null) {
                emailList.add(e);
            }
        }

        return emailList;
    }

    private static Email retrieveEmails(int i, DataOutputStream os, DataInputStream is) throws IOException {
        System.out.println(ANSI_CYAN + "Message " + i + ANSI_WHITE);

        String sender = null;
        String subject = null;
        String date = null;
        Double size, tempSize;

        os.writeBytes("RETR " + i + "\r\n");
        sReturns = is.readLine();

        if (sReturns.contains("+OK")) {
            int index = sReturns.indexOf('o');
            tempSize = Double.parseDouble(sReturns.substring(4,index));
            size = tempSize/1024;

            while (sReturns != null) {

                if (sReturns.contains("From: ")) {

                    sender = sReturns.substring(6, sReturns.length());
                    System.out.println(ANSI_CYAN + "Length: " + sender.length() + ANSI_WHITE);
                    System.out.println(ANSI_CYAN + sender + ANSI_WHITE);

                } else if (sReturns.contains("Subject: ")) {

                    subject = sReturns.substring(9, sReturns.length());
                    System.out.println(ANSI_CYAN + "Length: " + subject.length() + ANSI_WHITE);
                    System.out.println(ANSI_CYAN + subject + ANSI_WHITE);

                } else if (sReturns.contains("Date: ")) {

                    date = sReturns.substring(6, sReturns.length());
                    System.out.println(ANSI_CYAN + "Length: " + date.length() + ANSI_WHITE);
                    System.out.println(ANSI_CYAN + subject + ANSI_WHITE);
                }

                if (sReturns.equals(".")) {
                    break;
                }

                sReturns = is.readLine();
            }
        }
        else {
            System.out.println(ANSI_GREEN + sReturns + ANSI_WHITE);
            return null;
        }

        System.out.println(ANSI_CYAN + "Message Done................................................................" + ANSI_WHITE);
        Email email = new Email(i,sender,subject,date,size);

        return email;
    }

    public static boolean login(String username, String password) throws IOException {
        System.out.println(ANSI_CYAN + "Trying to log user in: " + popSocket.toString() + ANSI_WHITE);
        os = new DataOutputStream(popSocket.getOutputStream());
        is = new DataInputStream(popSocket.getInputStream());

         os.writeBytes("USER " + username + "\r\n");
         sReturns = is.readLine();
         System.out.println(ANSI_GREEN + "RESPONSE USER: " + sReturns + ANSI_WHITE);

         os.writeBytes("PASS " + password + "\r\n");
         sReturns = is.readLine();
         System.out.println(ANSI_GREEN + "RESPONSE PASS: " + sReturns + ANSI_WHITE);

         System.out.println(ANSI_CYAN + "User logged in..." + ANSI_WHITE);

         if (sReturns.contains("+OK")) {
             return true;
         }
         else {
             return false;
         }
    }

    public static void deleteEmails(int i) throws IOException {
        System.out.println(ANSI_CYAN + "Deleting Email: " + i + ANSI_WHITE);

        os = new DataOutputStream(popSocket.getOutputStream());
        is = new DataInputStream(popSocket.getInputStream());

        os.writeBytes("DELE " + i + "\r\n");
        sReturns = is.readLine();
        System.out.println(ANSI_GREEN + "RESPONSE DELE: " + sReturns + ANSI_WHITE);
    }

    public static void quit() throws IOException {
        System.out.println(ANSI_CYAN + "QUIT from server... " + ANSI_WHITE);

        os = new DataOutputStream(popSocket.getOutputStream());
        is = new DataInputStream(popSocket.getInputStream());

        os.writeBytes("QUIT\r\n");
        sReturns = is.readLine();
        System.out.println(ANSI_GREEN + "RESPONSE QUIT: " + sReturns + ANSI_WHITE);

        popSocket.close();
        System.out.println(ANSI_CYAN + "Server connection closed... " + ANSI_WHITE);
    }

    public static class Email {
        private String Sender;
        private String Subject;
        private String Date;
        private Double Size;
        private int emailNR;

        public Email( int nr, String sender, String subject, String date, Double size) {
            setEmailNR(nr);
            setSender(sender);
            setSubject(subject);
            setDate(date);
            setSize(size);
        }

        public int getEmailNR() {
            return emailNR;
        }

        public void setEmailNR(int emailNR) {
            this.emailNR = emailNR;
        }


        public String getSender() {
            return Sender;
        }

        public void setSender(String sender) {
            Sender = sender;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String subject) {
           Subject = subject;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public Double getSize() {
            return Size;
        }

        public void setSize(Double size) {
            Size = size;
        }


    }


}
