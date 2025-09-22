package com.example.pratica4.model;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    private String nome;
    private Plano plano;
    private List<Curso> cursos;
    private boolean beneficiosPremiumAtivos;
    
    public Aluno(String nome) {
        this.nome = nome;
        this.plano = Plano.BASICO;
        this.cursos = new ArrayList<>();
        this.beneficiosPremiumAtivos = false;
    }
    
    public void adicionarCurso(Curso curso) {
        this.cursos.add(curso);
    }
    
    public long getCursosConcluidosComMediaSete() {
        // Conta cursos concluídos com média >= 7.0
        return this.cursos.stream()
                .filter(c -> c.isConcluido() && c.isAprovado())
                .count();
    }
    
    public void atualizarPlano() {
        long aprovados = getCursosConcluidosComMediaSete();
        if (aprovados >= 12) {
            this.plano = Plano.PREMIUM;
            this.beneficiosPremiumAtivos = true;
        } else {
            this.plano = Plano.BASICO;
            this.beneficiosPremiumAtivos = false;
        }
    }
    
    public String getNome() {
        return nome;
    }
    
    public Plano getPlano() {
        return this.plano;
    }
    
    public List<Curso> getCursos() {
        return cursos;
    }
    
    public boolean isBeneficiosPremiumAtivos() {
        return this.beneficiosPremiumAtivos;
    }
}