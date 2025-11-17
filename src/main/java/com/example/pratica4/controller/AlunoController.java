package com.example.pratica4.controller;

import com.example.pratica4.dto.*;
import com.example.pratica4.model.Aluno;
import com.example.pratica4.model.Curso;
import com.example.pratica4.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller REST para gerenciar Alunos.
 *
 * @RestController - Combina @Controller + @ResponseBody (retorna JSON automaticamente)
 * @RequestMapping - Define prefixo base para todas as rotas (/api/alunos)
 * @CrossOrigin - Permite requisições de qualquer origem (importante para frontend)
 * @Tag - Anotação do Swagger para agrupar endpoints
 */
@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Alunos", description = "API para gerenciamento de alunos e upgrade de planos")
public class AlunoController {

    private final AlunoService alunoService;

    /**
     * POST /api/alunos - Criar novo aluno
     */
    @PostMapping
    @Operation(summary = "Criar novo aluno", description = "Cria um novo aluno no sistema com plano BASICO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou CPF/Email já cadastrado")
    })
    public ResponseEntity<AlunoDTO> criarAluno(
            @Valid @RequestBody CreateAlunoDTO createAlunoDTO) {

        Aluno aluno = createAlunoDTO.toEntity();
        Aluno alunoSalvo = alunoService.criarAluno(aluno);
        AlunoDTO alunoDTO = AlunoDTO.fromEntity(alunoSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(alunoDTO);
    }

    /**
     * GET /api/alunos - Listar todos os alunos
     */
    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna lista completa de alunos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        List<Aluno> alunos = alunoService.listarTodos();
        List<AlunoDTO> alunosDTO = alunos.stream()
                .map(AlunoDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(alunosDTO);
    }

    /**
     * GET /api/alunos/{id} - Buscar aluno por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna dados completos de um aluno específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<AlunoDTO> buscarPorId(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {

        Aluno aluno = alunoService.buscarPorId(id);
        AlunoDTO alunoDTO = AlunoDTO.fromEntity(aluno);

        return ResponseEntity.ok(alunoDTO);
    }

    /**
     * PUT /api/alunos/{id} - Atualizar aluno
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza dados de um aluno existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<AlunoDTO> atualizarAluno(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            @Valid @RequestBody CreateAlunoDTO updateDTO) {

        Aluno alunoAtualizado = updateDTO.toEntity();
        Aluno aluno = alunoService.atualizarAluno(id, alunoAtualizado);
        AlunoDTO alunoDTO = AlunoDTO.fromEntity(aluno);

        return ResponseEntity.ok(alunoDTO);
    }

    /**
     * DELETE /api/alunos/{id} - Deletar aluno
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar aluno", description = "Remove um aluno do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<Void> deletarAluno(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {

        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/alunos/{id}/cursos - Adicionar curso ao aluno
     */
    @PostMapping("/{id}/cursos")
    @Operation(summary = "Adicionar curso", description = "Adiciona um novo curso à lista de cursos do aluno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<AlunoDTO> adicionarCurso(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            @Valid @RequestBody CreateCursoDTO createCursoDTO) {

        Curso curso = createCursoDTO.toEntity();
        Aluno aluno = alunoService.adicionarCurso(id, curso);
        AlunoDTO alunoDTO = AlunoDTO.fromEntity(aluno);

        return ResponseEntity.ok(alunoDTO);
    }

    /**
     * PUT /api/alunos/{alunoId}/cursos/{cursoId}/concluir - Concluir curso com média
     */
    @PutMapping("/{alunoId}/cursos/{cursoId}/concluir")
    @Operation(
            summary = "Concluir curso",
            description = "Marca um curso como concluído, registra a média e verifica upgrade automático de plano"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso concluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno ou curso não encontrado"),
            @ApiResponse(responseCode = "400", description = "Média inválida (deve estar entre 0.0 e 10.0)")
    })
    public ResponseEntity<AlunoDTO> concluirCurso(
            @Parameter(description = "ID do aluno") @PathVariable Long alunoId,
            @Parameter(description = "ID do curso") @PathVariable Long cursoId,
            @Valid @RequestBody ConcluirCursoDTO concluirDTO) {

        Aluno aluno = alunoService.concluirCurso(alunoId, cursoId, concluirDTO.getMedia());
        AlunoDTO alunoDTO = AlunoDTO.fromEntity(aluno);

        return ResponseEntity.ok(alunoDTO);
    }

    /**
     * GET /api/alunos/{id}/progresso - Ver progresso para premium
     */
    @GetMapping("/{id}/progresso")
    @Operation(
            summary = "Consultar progresso",
            description = "Mostra progresso do aluno em direção ao plano Premium (quantos cursos faltam, percentual, etc)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progresso consultado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<ProgressoDTO> consultarProgresso(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {

        Aluno aluno = alunoService.buscarPorId(id);
        ProgressoDTO progressoDTO = ProgressoDTO.fromAluno(aluno);

        return ResponseEntity.ok(progressoDTO);
    }
}
