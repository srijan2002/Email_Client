package ms.services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author srijan
 */
import java.util.Properties;
import java.util.*;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.InternetAddress;
import java.io.*;

import javax.mail.Part;
import java.util.regex.Pattern;
import ms.msg;
 
public class get_email {
    public Properties getServerProperties(String protocol, String host,
            String port) {
        Properties properties = new Properties();
 
        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);
 
        // SSL setting
        properties.setProperty(
                String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(
                String.format("mail.%s.socketFactory.fallback", protocol),
                "false");
        properties.setProperty(
                String.format("mail.%s.socketFactory.port", protocol),
                String.valueOf(port));
        properties.setProperty("mail.imaps.partialfetch", "false");
 
        return properties;
    }
 
    /**
     * Downloads new messages and fetches details for each message.
     * @param protocol
     * @param host
     * @param port
     * @param userName
     * @param password
     */
     public String[] getMail(String protocol, String host, String port,
            String userName, String password,int index) {
         String result[] = new String[2];
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
 
        try {
             
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);
               
            // opens the inbox folder
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages(); 
            msg.mes = messages;
             int n = messages.length;
            int c=-1;
            for (int i = n-1; i>=n-30; i--) {
                c++;
                if(c==index){
                    Message msg = messages[i];
                    
                    
                    Object content = msg.getContent();
if (content instanceof Multipart) {
    Multipart mp = (Multipart) content;
    for (int j = 0; j < mp.getCount(); j++) {
        BodyPart bp = mp.getBodyPart(j);
        if (Pattern
                .compile(Pattern.quote("text/html"),
                        Pattern.CASE_INSENSITIVE)
                .matcher(bp.getContentType()).find()) {
            // found html part
             result[1]=((String) bp.getContent());
//             System.out.println(result[1]);
        } else {
            // some other bodypart...
        }
    }
}
                    
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                String from = sender.getAddress();
                        String body="";
                        String subject = msg.getSubject();  
                  if (msg.isMimeType("text/plain")&& result[1]==null) {
                      result[1] = msg.getContent().toString();
                      System.out.println("plain");
                  
                    } 
                  if (msg.isMimeType("multipart/*")) {
                      System.out.println("multipart");
                       try{
                   MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
//                   getTextFromMimeMultipart(mimeMultipart);
                       }catch(IOException e){}
                       }
                    result[0]=subject;
                    break;
                }
            }
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex){
        }
        
       return result;
    }
     
    
    public String[] downloadEmails(String protocol, String host, String port,
            String userName, String password) {
         String result[] = new String[30];
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties); 
        try {
          
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);
            // opens the inbox folder
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages(); 
             int n = messages.length;
            
            for (int i = n-1; i>=n-30; i--) {
                Message msg = messages[i];
                
//                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
//                String from = sender.getAddress();
                result[n-i-1]=msg.getSubject();
               
            }
            
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
        return result;
    }
 
    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    public String parseAddresses(Address[] address) {
        String listAddress = "";
 
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }
 
        return listAddress;
    }
    public String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException{
    String result = "";
    int count = mimeMultipart.getCount();
  try{
        for (int i = 0; i < count; i++) {
        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
       
        if (bodyPart.isMimeType("text/plain")) {
            
            result = result  + bodyPart.getContent();
            System.out.println(result);
            break; // without break same text appears twice in my tests
        } else if (bodyPart.isMimeType("text/html")) {
             System.out.println("text html");
            String html = (String) bodyPart.getContent();
            System.out.println(html);
            result = result + org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart){
             System.out.println("none");
            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
             System.out.println(result);
        }
    }
  }catch(IOException e){}
    return result;
}
    
      public void getAttach(String protocol, String host, String port,
            String userName, String password,int index) {
          String save = "/home/shruti/Downloads/";
          String result[] = new String[2];
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
 
        try {
             
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);
               
            // opens the inbox folder
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages(); 
            msg.mes = messages;
             int n = messages.length;
       
                    Message msg = messages[n-1-index];
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                String from = sender.getAddress();
                          
                  if (msg.isMimeType("text/plain")) {
                       
                  
                    } 
                  if (msg.isMimeType("multipart/*")) {
                       try{
                   MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                   for (int j = 0; j < mimeMultipart.getCount(); j++) {
                         MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(j);
                          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                  
                           part.saveFile(save + part.getFileName());
                             System.out.println("Saved");
                         }
                   } 
                   
                       }catch(IOException e){}
                       }
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }  
       
       
    }
       public boolean attachName(String protocol, String host, String port,
            String userName, String password,int index) {
          String save = "/home/srijan/Downloads/";
          String res ="";
      
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);
 
        try {
             
            // connects to the message store
            Store store = session.getStore(protocol);
            store.connect(userName, password);
               
            // opens the inbox folder
            Folder folderInbox = store.getFolder("[Gmail]/All Mail");
            folderInbox.open(Folder.READ_WRITE);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages(); 
            msg.mes = messages;
             int n = messages.length;
       
                    Message msg = messages[n-1-index];
                InternetAddress sender = (InternetAddress) msg.getFrom()[0];
                String from = sender.getAddress();
                          
                  if (msg.isMimeType("text/plain")) {
                       
                  
                    } 
                  if (msg.isMimeType("multipart/*")) {
                       try{
                   MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                   for (int j = 0; j < mimeMultipart.getCount(); j++) {
                         MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(j);
                          if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                  
                            
                         }
                          res=part.getFileName();
                   } 
                   
                       }catch(IOException e){}
                       }
            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }  
       if(res==null)
       return false;
       else
           return true;
    }
     
}
