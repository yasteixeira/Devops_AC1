package com.example.pratica4.service;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {
    
    public void concluirCurso(Aluno aluno, Curso curso, double media) {
        curso.concluir(media);
        aluno.atualizarPlano();
    }
    
    public void verificarUpgradePlano(Aluno aluno) {
        aluno.atualizarPlano();
    }
}