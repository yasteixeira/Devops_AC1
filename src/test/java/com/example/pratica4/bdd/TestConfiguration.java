package com.example.pratica4.bdd;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class TestConfiguration {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Iniciando cenário: " + scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("Cenário falhou: " + scenario.getName());
        } else {
            System.out.println("Cenário passou: " + scenario.getName());
        }
    }
}