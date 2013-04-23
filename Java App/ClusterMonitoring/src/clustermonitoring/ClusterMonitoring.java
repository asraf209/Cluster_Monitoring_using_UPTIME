/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clustermonitoring;

import java.util.Set;
import java.util.Iterator;
import javax.security.auth.login.LoginContext;

import org.ow2.bonita.util.AccessorUtil;
import org.ow2.bonita.util.BonitaConstants;
import org.ow2.bonita.util.SimpleCallbackHandler;

import org.ow2.bonita.facade.ManagementAPI;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.QueryDefinitionAPI;
import org.ow2.bonita.facade.RuntimeAPI;
import org.ow2.bonita.facade.def.element.BusinessArchive;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition;
import org.ow2.bonita.facade.runtime.ActivityState;
import org.ow2.bonita.facade.runtime.InstanceState;
import org.ow2.bonita.facade.runtime.TaskInstance;
import org.ow2.bonita.facade.runtime.ProcessInstance;
import org.ow2.bonita.facade.runtime.impl.ProcessInstanceImpl;
import org.ow2.bonita.facade.uuid.ProcessDefinitionUUID;
import org.ow2.bonita.facade.uuid.ProcessInstanceUUID;


public class ClusterMonitoring {

    private static final String LOGIN = "admin";
    private static final String PASSWORD = "bpm";    
    private static final String JAAS_FILE_PATH = "resources/jaas-standard.cfg";    

    static {
        System.setProperty(BonitaConstants.JAAS_PROPERTY, JAAS_FILE_PATH);        
        System.setProperty(BonitaConstants.API_TYPE_PROPERTY, "REST");
        //System.setProperty(BonitaConstants.REST_SERVER_ADDRESS_PROPERTY, "http://129.105.6.88:8080/bonita-server-rest/");
        System.setProperty(BonitaConstants.REST_SERVER_ADDRESS_PROPERTY, "http://guru.ece.northwestern.edu:8081/bonita-server-rest/");
    }
    
    
    public static void main(String[] args) throws Exception{
        //Thread.sleep(1000*60*2);
        final LoginContext loginContext = new LoginContext("BonitaStore", new SimpleCallbackHandler(LOGIN, PASSWORD));
        loginContext.login();        
        System.err.println("Login success..");
        
        try{
            final RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();   
            
            //ProcessDefinition definition = AccessorUtil.getQueryDefinitionAPI().getProcess("ClusterMonitoringProcess_on_Pulse", "1.1");            
            ProcessDefinition definition = AccessorUtil.getQueryDefinitionAPI().getProcess(args[0].toString(), args[1].toString());            
            Set<ProcessInstance> instances=AccessorUtil.getQueryRuntimeAPI().getProcessInstances(definition.getUUID());            
            System.out.println("No of Instances: "+instances.size());
            System.out.println("All Instances:"+"\n"+instances+"\n");
            
            ProcessInstance processInstance;            
            Iterator<ProcessInstance> it=instances.iterator();
            /*while(it.hasNext()){
                processInstance=it.next();                
                System.out.println(processInstance.getNb()+"\n"+processInstance);
            }*/
            long nb=1;                  //nb will be the highst numbered instance i.e. last instance
            if(instances.size()>1){
                while(it.hasNext()){
                    processInstance=it.next();
                    if(nb < processInstance.getNb())
                        nb=processInstance.getNb();
                }
                
                it=instances.iterator();
            
                while(it.hasNext()){
                    processInstance=it.next();
                    if(processInstance.getNb()!=nb){
                        runtimeAPI.deleteProcessInstance(processInstance.getUUID());
                        System.out.println("Deleted the process instance, "+processInstance.getUUID());
                    }
                }
            }
            
            //create a process instance
            ProcessInstanceUUID instanceUUID = runtimeAPI.instantiateProcess(definition.getUUID());
            System.out.println("New process instance created, "+instanceUUID);                        
            
            /*
            final RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();   
            ProcessDefinition definition = AccessorUtil.getQueryDefinitionAPI().getProcess("ClusterMonitoringProcess_on_Pulse", "1.1");            
            
            //create a process instance
            ProcessInstanceUUID instanceUUID = runtimeAPI.instantiateProcess(definition.getUUID());
            //print
            String instance = AccessorUtil.getQueryRuntimeAPI().getProcessInstance(instanceUUID).toString();
            System.out.println(instance);
            
            System.out.println("Going to sleep for 5 minutes....");
            Thread.sleep(1000*60*5);
            
            //cancel the process instance            
            runtimeAPI.cancelProcessInstance(instanceUUID);
            System.out.println("Canceled the process instance..");
            
            System.out.println("Going to sleep for 2 minutes....");
            Thread.sleep(1000*60*2);
             
            //delete the process instance            
            runtimeAPI.deleteProcessInstance(instanceUUID);
            System.out.println("Deleted the process instance..");
            */
        }catch(Exception ex){
            System.out.println("Error occurred: "+ex.toString());
        }finally{
            loginContext.logout();
        }        
        
    }
}
