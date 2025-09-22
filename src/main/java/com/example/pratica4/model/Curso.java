package com.example.pratica4.model;

public class Curso {
    private String nome;
    private double media;
    private boolean concluido;
    
    public Curso(String nome) {
        this.nome = nome;
        this.concluido = false;
        this.media = 0.0;
    }
    
    public void concluir(double media) {
        // Marca como concluído e armazena a média
        this.media = media;
        this.concluido = true;
    }
    
    public String getNome() {
        return nome;
    }
    
    public double getMedia() {
        return this.media;
    }
    
    public boolean isConcluido() {
        return this.concluido;
    }
    
    public boolean isAprovado() {
        // Considera aprovado quando concluído e média >= 7.0
        return this.concluido && this.media >= 7.0;
    }
}