package com.example.pratica4.model;

public enum Plano {
    BASICO("Básico"),
    PREMIUM("Premium");
    
    private final String nome;
    
    Plano(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }
}