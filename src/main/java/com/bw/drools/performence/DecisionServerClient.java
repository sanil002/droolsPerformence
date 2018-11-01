package com.bw.drools.performence;

import java.util.Arrays;
import java.util.List;

import org.drools.core.fluent.impl.NewContextCommand;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

import demo.casemanagement.Chargeback;

public class DecisionServerClient {

	private static final String URL = "https://kie.gp.bitwise-digital.info/kie-server/services/rest/server";
	private static final String USER = "admin";
	private static final String PASSWORD = "admin";

	private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

	private KieServicesConfiguration conf;
	private KieServicesClient kieServicesClient;

	public void initialize() {
		conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
		conf.setMarshallingFormat(FORMAT);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
	}

	public void executeCommands(Chargeback chargeback) {
		initialize();
		System.out.println("== Sending commands to the server ==>");
		RuleServicesClient rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
		KieCommands commandsFactory = KieServices.Factory.get().getCommands();
		Command<?> insert = commandsFactory.newInsert(chargeback,"cr");
		Command<?> fireAllRules = commandsFactory.newFireAllRules();
		Command<?> batchCommand = commandsFactory.newBatchExecution(Arrays.asList(insert, fireAllRules),"session1");
		ServiceResponse<String> executeResponse = rulesClient.executeCommands("container", batchCommand);
		if (executeResponse.getType() == ResponseType.SUCCESS) {
			System.out.println("Commands executed with success! Response: ");
			System.out.println(executeResponse.getResult());
		} else {
			System.out.println("Error executing rules. Message: ");
			System.out.println(executeResponse.getMsg());
		}
	}
	public void listContainers() {  
		initialize();
	    KieContainerResourceList containersList = kieServicesClient.listContainers().getResult();  
	    List<KieContainerResource> kieContainers = containersList.getContainers();  
	    System.out.println("Available containers: ");  
	    for (KieContainerResource container : kieContainers) {  
	        System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");  
	    } 
	}
}