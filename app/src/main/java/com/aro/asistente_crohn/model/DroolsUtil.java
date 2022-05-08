package com.aro.asistente_crohn.model;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class DroolsUtil {

    private static final KieServices kieServices = KieServices.Factory.get();

    private static KieContainer healthKieContainer;

    static {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("../java/com/aro/asistente_crohn/model/health_rules.drl"));
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        healthKieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
    }

    public static KieSession getNewKieSessionForHealth() {
        return healthKieContainer.newKieSession();
    }

    public static Health validateHealth(Health health) {
        KieSession newKieSessionForHealth = getNewKieSessionForHealth();
        newKieSessionForHealth.insert(health);
        newKieSessionForHealth.fireAllRules();
        newKieSessionForHealth.dispose();
        return health;
    }

}
