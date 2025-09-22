# language: pt
Funcionalidade: Sistema de Planos do Aluno
  Como um aluno da plataforma
  Eu quero que meu plano seja atualizado automaticamente
  Para ter acesso aos benefícios premium quando elegível

  Contexto:
    Dado que existe um aluno chamado "João Silva"

  Cenário: Aluno inicia com plano básico
    Então o aluno deve ter plano "BASICO"
    E os benefícios premium devem estar "desativados"

  Cenário: Aluno com 11 cursos aprovados não faz upgrade
    Dado que o aluno tem 11 cursos concluídos com média 7,5
    Quando o plano for atualizado
    Então o aluno deve ter plano "BASICO"
    E os benefícios premium devem estar "desativados"

  Cenário: Aluno com 12 cursos aprovados faz upgrade para premium
    Dado que o aluno tem 12 cursos concluídos com média 7,0
    Quando o plano for atualizado
    Então o aluno deve ter plano "PREMIUM"
    E os benefícios premium devem estar "ativados"

  Cenário: Aluno com mais de 12 cursos aprovados mantém premium
    Dado que o aluno tem 15 cursos concluídos com média 8,0
    Quando o plano for atualizado
    Então o aluno deve ter plano "PREMIUM"
    E os benefícios premium devem estar "ativados"

  Cenário: Aluno com 12 cursos mas apenas 10 aprovados não faz upgrade
    Dado que o aluno tem 10 cursos concluídos com média 7,5
    E que o aluno tem 2 cursos concluídos com média 6,0
    Quando o plano for atualizado
    Então o aluno deve ter 12 cursos no total
    E o aluno deve ter 10 cursos aprovados
    E o aluno deve ter plano "BASICO"
    E os benefícios premium devem estar "desativados"

  Cenário: Progresso do aluno é calculado corretamente
    Dado que o aluno tem 8 cursos concluídos com média 7,2
    E que o aluno tem 3 cursos concluídos com média 5,5
    Então o aluno deve ter 11 cursos no total
    E o aluno deve ter 8 cursos aprovados
    E o progresso deve mostrar "8/12 cursos válidos"

  Esquema do Cenário: Validação de limites para upgrade de plano
    Dado que o aluno tem <cursosAprovados> cursos concluídos com média 7,0
    E que o aluno tem <cursosReprovados> cursos concluídos com média 6,5
    Quando o plano for atualizado
    Então o aluno deve ter plano "<planoEsperado>"
    E os benefícios premium devem estar "<beneficiosEsperados>"

    Exemplos:
      | cursosAprovados | cursosReprovados | planoEsperado | beneficiosEsperados |
      | 11              | 1                | BASICO        | desativados         |
      | 12              | 0                | PREMIUM       | ativados            |
      | 12              | 3                | PREMIUM       | ativados            |
      | 20              | 5                | PREMIUM       | ativados            |