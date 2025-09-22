# language: pt
Funcionalidade: Upgrade automático de plano do aluno

  Como um sistema de ensino
  Eu quero automaticamente promover alunos para o plano Premium
  Para que eles recebam benefícios adequados ao seu desempenho

  Cenário: Aluno com plano Básico deve ser promovido para Premium após 12 cursos aprovados
    Dado que um aluno possui plano "Básico"
    E que ele possui 11 cursos concluídos com média maior ou igual a 7,0
    Quando ele concluir o 12° curso com média maior ou igual a 7,0
    Então o sistema deve automaticamente alterar seu plano para "Premium"
    E deve ativar os benefícios Premium

  Cenário: Aluno com 12 cursos mas apenas 10 aprovados deve permanecer no plano Básico
    Dado que um aluno possui 12 cursos concluídos
    E 2 cursos têm média menor que 7,0
    Quando o sistema verificar os critérios
    Então o plano deve permanecer "Básico"
    E deve exibir progresso: "10/12 cursos válidos"