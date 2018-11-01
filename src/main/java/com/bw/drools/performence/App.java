package com.bw.drools.performence;

import org.apache.commons.lang3.time.StopWatch;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;

import demo.casemanagement.Chargeback;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {	
    	StopWatch watchLocal = new StopWatch();
    	watchLocal.start();
    	executeRulesLocally();
    	watchLocal.stop();    	
    	
    	StopWatch watchRemote = new StopWatch();
    	watchRemote.start();
    	executeRulesRemotely();
    	watchRemote.stop();
    	
    	System.out.println("---------------------------------------------------------------");
    	System.out.println("Time Elapsed during local rules execution: " + watchLocal.getTime() + " Milli seconds"); 
    	System.out.println("---------------------------------------------------------------");
    	System.out.println("Time Elapsed during Remote rules execution: " + watchRemote.getTime() + " Milli seconds");
    	System.out.println("---------------------------------------------------------------");
    }    
    
	public static KieContainer kieContainer() {
		return KieServices.Factory.get().getKieClasspathContainer();
	}
	
	public static void executeRulesLocally() {
		Chargeback chargeback =new Chargeback(1L,150,"01");		
		new CaseService(kieContainer()).getProductDiscount(chargeback);	  
	}
	
	public static void executeRulesRemotely() {
		Chargeback chargeback =new Chargeback(1L,150,"01");		
		new DecisionServerClient().executeCommands(chargeback);;
	}
}
