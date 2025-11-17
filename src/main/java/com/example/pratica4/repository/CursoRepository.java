package com.example.pratica4.repository;

import com.example.pratica4.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para acesso aos dados de Curso.
 *
 * Fornece métodos automáticos do JpaRepository e métodos customizados.
 */
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca cursos por nome (case insensitive)
     * IgnoreCase faz busca sem considerar maiúsculas/minúsculas
     */
    List<Curso> findByNomeContainingIgnoreCase(String nome);

    /**
     * Lista apenas cursos concluídos
     */
    List<Curso> findByConcluidoTrue();

    /**
     * Lista apenas cursos não concluídos
     */
    List<Curso> findByConcluidoFalse();

    /**
     * Lista cursos aprovados (concluído = true E média >= 7.0)
     */
    @Query("SELECT c FROM Curso c WHERE c.concluido = true AND c.media >= 7.0")
    List<Curso> findCursosAprovados();

    /**
     * Lista cursos reprovados (concluído = true E média < 7.0)
     */
    @Query("SELECT c FROM Curso c WHERE c.concluido = true AND c.media < 7.0")
    List<Curso> findCursosReprovados();

    /**
     * Conta quantidade de cursos aprovados
     */
    @Query("SELECT COUNT(c) FROM Curso c WHERE c.concluido = true AND c.media >= 7.0")
    long countCursosAprovados();
}
