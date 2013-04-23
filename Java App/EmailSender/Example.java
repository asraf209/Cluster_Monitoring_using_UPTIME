import asraf.lib.EmailSender;

public class EmailSendingWithLib {

    public static void main(String[] args) {
        
        String toAddress[]={"asraf.alom02@gmail.com", "md.asrafulalom@yahoo.com"};
        
        EmailSender sender=new EmailSender();
        
        sender.setHost("smtp.gmail.com");
        sender.setFromAddress("bos.process@gmail.com");
        sender.setFromPass("bosadmin123");        
        sender.setToAddressList(toAddress);
        sender.setMsgSubject("Test Email Notification");
        sender.setMsgBody("Hi Ashraf....This is a test email notification");
        
        sender.setSMTPProperties();
        
        try{
		//sender.sendMsgOnly();
            sender.sendMsgWithAttachment("F:\\EmailList.txt");
            System.out.println("Successfully sent email notification");
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
}