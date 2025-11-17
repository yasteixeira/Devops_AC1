package com.example.pratica4.service;

import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para lógica de negócio relacionada a Aluno.
 *
 * @Transactional - Garante que operações sejam atômicas (tudo ou nada)
 * @RequiredArgsConstructor - Lombok gera construtor com campos final (injeção de dependência)
 */
@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    /**
     * Cria um novo aluno no sistema
     * @param aluno dados do aluno
     * @return aluno salvo com ID
     */
    @Transactional
    public Aluno criarAluno(Aluno aluno) {
        // Validação: CPF único
        if (aluno.getCpf() != null && alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado: " + aluno.getCpf());
        }

        // Validação: Email único
        if (aluno.getEmail() != null && alunoRepository.existsByEmail(aluno.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + aluno.getEmail());
        }

        return alunoRepository.save(aluno);
    }

    /**
     * Busca aluno por ID
     * @param id identificador do aluno
     * @return aluno encontrado
     * @throws EntityNotFoundException se não encontrar
     */
    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
    }

    /**
     * Lista todos os alunos
     * @return lista de alunos
     */
    @Transactional(readOnly = true)
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    /**
     * Atualiza dados de um aluno existente
     * @param id identificador do aluno
     * @param alunoAtualizado dados atualizados
     * @return aluno atualizado
     */
    @Transactional
    public Aluno atualizarAluno(Long id, Aluno alunoAtualizado) {
        Aluno alunoExistente = buscarPorId(id);

        // Atualiza apenas campos permitidos
        if (alunoAtualizado.getNome() != null) {
            alunoExistente.setNome(alunoAtualizado.getNome());
        }

        // CPF e Email não devem ser alterados após cadastro (regra de negócio)
        // Se precisar alterar, adicionar validações aqui

        return alunoRepository.save(alunoExistente);
    }

    /**
     * Remove um aluno do sistema
     * @param id identificador do aluno
     */
    @Transactional
    public void deletarAluno(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }

    /**
     * Adiciona um curso ao aluno
     * @param alunoId ID do aluno
     * @param curso curso a ser adicionado
     * @return aluno com curso adicionado
     */
    @Transactional
    public Aluno adicionarCurso(Long alunoId, Curso curso) {
        Aluno aluno = buscarPorId(alunoId);
        aluno.adicionarCurso(curso);
        return alunoRepository.save(aluno);
    }

    /**
     * Conclui um curso do aluno com uma média
     * Método mantido para compatibilidade com testes BDD
     */
    public void concluirCurso(Aluno aluno, Curso curso, double media) {
        curso.concluir(media);
        aluno.atualizarPlano();
    }

    /**
     * Conclui um curso específico de um aluno
     * Versão que trabalha com IDs (para uso na API REST)
     *
     * @param alunoId ID do aluno
     * @param cursoId ID do curso
     * @param media média obtida (0.0 a 10.0)
     * @return aluno atualizado
     */
    @Transactional
    public Aluno concluirCurso(Long alunoId, Long cursoId, double media) {
        Aluno aluno = buscarPorId(alunoId);

        // Busca o curso dentro da lista de cursos do aluno
        Curso curso = aluno.getCursos().stream()
                .filter(c -> c.getId() != null && c.getId().equals(cursoId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Curso com ID " + cursoId + " não encontrado para o aluno " + alunoId));

        // Conclui o curso com a média
        curso.concluir(media);

        // Atualiza o plano do aluno (verifica se atingiu 12 cursos aprovados)
        aluno.atualizarPlano();

        return alunoRepository.save(aluno);
    }

    /**
     * Verifica e atualiza o plano do aluno
     * Método mantido para compatibilidade com testes BDD
     */
    public void verificarUpgradePlano(Aluno aluno) {
        aluno.atualizarPlano();
    }

    /**
     * Verifica e atualiza o plano de um aluno específico
     * Versão que trabalha com ID (para uso na API REST)
     *
     * @param alunoId ID do aluno
     * @return aluno com plano atualizado
     */
    @Transactional
    public Aluno verificarUpgradePlano(Long alunoId) {
        Aluno aluno = buscarPorId(alunoId);
        aluno.atualizarPlano();
        return alunoRepository.save(aluno);
    }
}
