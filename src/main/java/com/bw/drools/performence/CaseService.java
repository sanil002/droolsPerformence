package com.bw.drools.performence;

import java.util.Collection;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import demo.casemanagement.CaseResponce;
import demo.casemanagement.Chargeback;

public class CaseService {

	private KieContainer kieContainer;
	
	public CaseService() {}

	public CaseService(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}

	public void getProductDiscount(Chargeback chargeback) {
		CaseResponce caseResponse = new CaseResponce();
		KieSession kieSession = kieContainer.newKieSession("session2");
		kieSession.insert(chargeback);
		int rulesCount = kieSession.fireAllRules();
		System.out.println("Number of rules executed :==> " + rulesCount);

		Collection<FactHandle> allHandles = kieSession.getFactHandles();

		for (FactHandle handle : allHandles) {
			if (kieSession.getObject(handle) instanceof CaseResponce) {
				caseResponse = (CaseResponce) kieSession.getObject(handle);
			}
		}
		kieSession.dispose();
		System.out.println(caseResponse.getState());
	}
}
