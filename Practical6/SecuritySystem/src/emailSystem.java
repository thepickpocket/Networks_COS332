import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class emailSystem {
	public void sendEmail(String alarmMessage){
		  // Recipient's email ID needs to be mentioned.
	      String to = "jasonevans@tuks.co.za";
	      String cc = "u13238435@tuks.co.za";

	      // Sender's email ID needs to be mentioned
	      String from = "cos332demo@gmail.com";
	      final String username = "cos332demo";
	      final String password = "Thisisademo";

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "smtp.gmail.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "587");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	         }
	      });

	      try {
	         // Create a default MimeMessage object.
	         Message message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.setRecipients(Message.RecipientType.TO,
	         InternetAddress.parse(to));
	         message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));

	         // Set Subject: header field
	         message.setSubject("Alarm!");

	         // Now set the actual message
	         message.setText("The following alarm has been activated: \n\n"
	            + alarmMessage + "\n\nRegards,\nPractic Alarm Systems.");

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	            throw new RuntimeException(e);
	      }
	   }
}
