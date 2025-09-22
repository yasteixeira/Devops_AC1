# User Stories e BDD

## Integrante 1: Yasmin de Oliveira Teixeira

### User Story

|EU COMO      |PRECISO/QUERO   |PARA                        |
|-------------|----------------|----------------------------|
|Aluno Premium|Receber vouchers|Participar de projetos reais|

### BDD

*Integrante 1 (BDD) relativo a User Story (X)*

|Dado que                      |E                                                   |Quando                                   |Então                                                          |E                                |
|------------------------------|----------------------------------------------------|-----------------------------------------|---------------------------------------------------------------|---------------------------------|
|Um aluno possui plano “Básico”|Que ele possui 11 cursos concluídos com média += 7.0|Ele concluir o 12º curso com média += 7.0|O sistema deve automaticamente alterar seu plano para “Premium”|Deve ativar os benefícios Premium|

*a etapa “E” é opcional.

## Integrante 2: Lucas Carvalho de Souza

### User Story

|EU COMO                 |PRECISO/QUERO                                                                                          |PARA                                                       |
|------------------------|-------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|
|Administrador do sistema|Que o plano do aluno seja automaticamente renovado para premium quando ele conquistar 12 cursos válidos|Garantir que os benefícios sejam concedidos automaticamente|

### BDD

*Integrante 2 (BDD) relativo a User Story (X)*

|Dado que                            |E                       |Quando                          |Então                           |E                                            |
|------------------------------------|------------------------|--------------------------------|--------------------------------|---------------------------------------------|
|Um aluno possui 12 cursos concluídos|2 cursos têm média < 7.0|O sistema verificar os critérios|O plano deve permanecer “Básico”|Deve exibir progresso: “10/12 cursos válidos”|
