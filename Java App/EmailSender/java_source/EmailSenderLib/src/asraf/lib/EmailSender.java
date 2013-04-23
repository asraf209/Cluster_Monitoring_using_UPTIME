/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asraf.lib;

/**
 *
 * @author MOON MOON
 */

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.util.regex.Matcher;  
import java.util.regex.Pattern; 

public class EmailSender {
    
    String host, fromAddress, fromPass, msgSubject, msgBody;
    String toAddressList[]=null;
    
    Properties props;
    
    public EmailSender()    {}
    
    public void setHost(String h)
    {
        host=h;  
    }    
    String getHost()
    {
        return host;
    }
    
    public void setFromAddress(String from)
    {
        fromAddress=from;  
    }    
    String getFromAddress()
    {
        return fromAddress;  
    }    
    
    public void setFromPass(String pass)
    {
        fromPass=pass;  
    }    
    String getFromPass()
    {
        return fromPass;  
    }    
    
    public void setToAddressList(String[] to)
    {
        String[] temp=null;
        int validCount=0;
        
        for(int i=0; i<to.length; i++)        
            if(isValidEmailAddress(to[i].trim()))  validCount++;
        
        if(validCount>0){
            temp=new String[validCount];
            for(int i=0, j=0; i<to.length; i++)
            {
                if(isValidEmailAddress(to[i].trim())){
                    temp[j]=to[i].trim();
                    j++;
                }
            }
        }
        else    System.out.println("No valid Email ID found");
        
        toAddressList=temp;  
    }    
    String[] getToAddressList()
    {
        return toAddressList;  
    }    
    
    public void setMsgSubject(String s)
    {
        msgSubject=s;
    }
    String getMsgSubject()
    {
        return msgSubject;
    }
    
    public void setMsgBody(String body)
    {
        msgBody=body;
    }
    String getMsgBody()
    {
        return msgBody;
    }
    
    boolean isValidEmailAddress(String emailAddress)
    {  
        String  expression="^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$";  
        CharSequence inputStr = emailAddress;  
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(inputStr);  
        return matcher.matches();    
    }  
    
    public void setSMTPProperties()
    {
        props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.host", getHost());
        props.put("mail.smtp.user", getFromAddress());
        props.put("mail.smtp.password", getFromPass());
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");                
    }
    
    public void sendMsgOnly() throws Exception              //Only Msg, no Attachment
    {
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(getFromAddress()));

        InternetAddress[] toAddress = new InternetAddress[toAddressList.length];
        
        for( int i=0; i < toAddressList.length; i++ )
        { 
            toAddress[i] = new InternetAddress(toAddressList[i]);
        }        
        for( int i=0; i < toAddress.length; i++)
        { 
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }

        // For only msg....No attachment               
        message.setSubject(getMsgSubject());   
        message.setText(getMsgBody()); 

        Transport transport = session.getTransport("smtp");
        transport.connect(getHost(), getFromAddress(), getFromPass());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();            
        
    }
    
    
    public void sendMsgWithAttachment(String fileAbsPath) throws Exception
    {
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(getFromAddress()));

        InternetAddress[] toAddress = new InternetAddress[toAddressList.length];
        
        for( int i=0; i < toAddressList.length; i++ )
        { 
            toAddress[i] = new InternetAddress(toAddressList[i]);
        }        
        for( int i=0; i < toAddress.length; i++)
        { 
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }

        // Msg with Attachment
        message.setSubject(getMsgSubject()); 
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message body
        messageBodyPart.setText(getMsgBody()); 

        Multipart multipart = new MimeMultipart(); // Create a multipart message        
        multipart.addBodyPart(messageBodyPart);    // Set text message part

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        String filename = fileAbsPath;              //"F:\\EmailList.txt";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        
        //fileName format is like: logs/output_xxxxx.log
        //messageBodyPart.setFileName(filename);        
        if(filename.contains("/"))                      //For UNIX
            messageBodyPart.setFileName(filename.substring( filename.lastIndexOf('/') + 1 ));   
        else                                            //For Windows
            messageBodyPart.setFileName(filename.substring( filename.lastIndexOf('\\') + 1 ));   
                
        multipart.addBodyPart(messageBodyPart);

        // Send the complete message parts
        message.setContent(multipart);
        
        //Transport.send(message);
        Transport transport = session.getTransport("smtp");
        transport.connect(getHost(), getFromAddress(), getFromPass());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();                   
         
    }
}
