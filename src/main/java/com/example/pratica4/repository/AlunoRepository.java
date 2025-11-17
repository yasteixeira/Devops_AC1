package com.example.pratica4.repository;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Plano;
import com.example.pratica4.model.valueobject.CPF;
import com.example.pratica4.model.valueobject.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para acesso aos dados de Aluno.
 *
 * JpaRepository<Aluno, Long> fornece automaticamente:
 * - save(aluno) - Salvar/atualizar
 * - findById(id) - Buscar por ID
 * - findAll() - Listar todos
 * - deleteById(id) - Deletar por ID
 * - count() - Contar registros
 * - existsById(id) - Verificar se existe
 *
 * Além disso, podemos criar métodos personalizados seguindo convenções de nomenclatura
 * do Spring Data JPA (Query Methods).
 */
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    /**
     * Busca aluno por CPF
     * Spring Data JPA gera automaticamente: SELECT * FROM alunos WHERE cpf = ?
     */
    Optional<Aluno> findByCpf(CPF cpf);

    /**
     * Busca aluno por email
     */
    Optional<Aluno> findByEmail(Email email);

    /**
     * Verifica se já existe aluno com este CPF
     */
    boolean existsByCpf(CPF cpf);

    /**
     * Verifica se já existe aluno com este email
     */
    boolean existsByEmail(Email email);

    /**
     * Lista todos os alunos de um determinado plano
     */
    List<Aluno> findByPlano(Plano plano);

    /**
     * Lista alunos com benefícios premium ativos
     */
    List<Aluno> findByBeneficiosPremiumAtivosTrue();

    /**
     * Query personalizada: Busca alunos próximos do upgrade (10-11 cursos aprovados)
     * @Query permite escrever JPQL (SQL orientado a objetos)
     */
    @Query("SELECT a FROM Aluno a WHERE SIZE(a.cursos) >= 10 AND a.plano = 'BASICO'")
    List<Aluno> findAlunosProximosDoUpgrade();
}
