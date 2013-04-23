import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

import asraf.lib.EmailSender;


public class Email_Notificator_without_Log extends ProcessConnector {

	String output="";
	// DO NOT REMOVE NOR RENAME THIS FIELD
	private java.lang.String msg_body;
	// DO NOT REMOVE NOR RENAME THIS FIELD
	private java.lang.String to_address;		//',' seperated email addressess
	// DO NOT REMOVE NOR RENAME THIS FIELD
	private java.lang.String email_subject;
	
	@Override
	protected void executeConnector() throws Exception {
		try{		
			String[] toAddressList=getToAddressList();
			
			EmailSender sender=new EmailSender();
			
			sender.setHost("smtp.gmail.com");
			sender.setFromAddress("bos.process@gmail.com");
			sender.setFromPass("bosadmin123");        
			sender.setToAddressList(toAddressList);
					
			/*
			sender.setMsgSubject("Done: Wall Downloading Process");
			sender.setMsgBody(
					"All Steps Successfully executed."+"\n\n"        		
			);
			*/
			
			sender.setMsgSubject(email_subject);
			sender.setMsgBody(msg_body);
			
			sender.setSMTPProperties();
		
		//try{        	        	
			sender.sendMsgOnly();        	                    
			output="Successfully sent email notification";
		}catch(Exception ex){
			output="Failed.."+ex.toString();
		}


	}

	String[] getToAddressList()
	{
		String[] list=to_address.split(",");
		return list;
	}
	
	@Override
	protected List<ConnectorError> validateValues() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Getter for output argument 'connectorOutput'
	 * DO NOT REMOVE NOR RENAME THIS GETTER, unless you also change the related entry in the XML descriptor file
	 */
	public java.lang.String getConnectorOutput() {
		// TODO Add return value for the output here
		return output;
	}

	/**
	 * Setter for input argument 'msg_body'
	 * DO NOT REMOVE NOR RENAME THIS SETTER, unless you also change the related entry in the XML descriptor file
	 */
	public void setMsg_body(java.lang.String msg_body) {
		this.msg_body = msg_body;
	}

	/**
	 * Setter for input argument 'to_address'
	 * DO NOT REMOVE NOR RENAME THIS SETTER, unless you also change the related entry in the XML descriptor file
	 */
	public void setTo_address(java.lang.String to_address) {
		this.to_address = to_address;
	}

	/**
	 * Setter for input argument 'email_subject'
	 * DO NOT REMOVE NOR RENAME THIS SETTER, unless you also change the related entry in the XML descriptor file
	 */
	public void setEmail_subject(java.lang.String email_subject) {
		this.email_subject = email_subject;
	}

}
