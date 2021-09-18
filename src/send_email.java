/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author srijan
 */
import javax.mail.*;
import javax.activation.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
public class send_email {
    
    String to,from,username,password,sub,body;  String path="";
    
    send_email(String t,String fr,String user,String pass, String s, String b, String p){
        to=t; from = fr; username=user;password=pass; sub=s;body=b; path=p;
    }
    
    public void sendEmail(){
        String host = "smtp.gmail.com";
       System.out.println(body);
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");
      
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

         // Set Subject: header field
         message.setSubject(sub);

         // Now set the actual message
         
         if(path!=""){
             BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
             MimeBodyPart file = new MimeBodyPart();
            file.attachFile(path);
             
             multipart.addBodyPart(file);

            // Send the complete message parts
            message.setContent(multipart);
             
         }
         else
             message.setText(body);
         
         

         // Send message
         Transport.send(message);
          
//         jLabel5.setText("Sent message successfully....");
//         jLabel5.paintImmediately(jLabel5.getVisibleRect());

      } catch (MessagingException ev) {
            throw new RuntimeException(ev);
      }
      }
      
    
      
    }
}
