## Modelagem

* **Chat** (ElasticSearCusto)

  * Dados
    - id_sala       - Obrigatório
    - id_professor  - Obrigatório
    - data          - Obrigatório
    - mensagem      - Obrigatório

  * Regras
    - Não é possível editar e excluir dos outros usuários
    - é possível apenas editar suas mensagens

* **Pessoa**
  
  * Dados
    - id
    - nome
    - email - Obrigatório
    - senha - Obrigatório
    - login - Obrigatório
    - foto

* **Professor**

  * Dados
    - id
    - id_pessoa
    - salario - Padrão 0

  * Regras
    - O professor pode realizar login tanto com email ou com o login
    - O email e login precisa ser único
    - O professor deve fazer cadastro no ínicio do sistema

* **Aluno**

  * Dados
    - id        - PK
    - Id_pessoa - FK
    - CR        - Padrão 0

  * Regras
    - O email precisa ser único
    - O aluno deve ser cadastro pelo professor


* **Materia**

  * Dados
    - id            - PK
    - nome          - Obrigatório
    - Custo         - Obrigatório
    - id_professor  - FK

* **Aluno X Materia**

  * Dados
    - materia_id    - FK
    - aluno_id      - FK
    - pontuacao     - Padrão 0

  * Regras
    - Um aluno não pode entrar em duas materias repetidas
    - Uma materia não pode ter o mesmo aluno repetidamente

* **Aula**
  * Dados
    - data_inicio
    - Data_final
    - professor_id
    - materia_id

----------

## Endponts

* /**autentificacao**

  * /login (post)

        envio
        `{
          login: string,
          senha: string
        }`

        retorno
        `{ token: string }`

        status: `200 | 404`

  * /cadastro (post)

        envio
        `{
          nome:     string,
          email:    string,
          senha:    string,
          login:    string,
          salario:  float,
          foto:     string
        }`

        retorno
        `{ token: string, mensagem: string }`

        status: `200 | 400`
  
* /**aluno**

  * / (post)

        envio
        `{
          nome:   string,
          email:  string,
          senha:  string,
          login:  string,
          foto:   string,
          CR:     float
        }`

        retorno `{ mensagem: string }`

        status: `200 | 400`

  * / (get)

        envio Vázio

        retorno `{[
          id:     int,
          nome:   string,
          email:  string,
          senha:  string,
          login:  string,
          foto:   string,
          CR:     float
        ],}`

        status: `200 | 500`

  * / (put)

        envio `{
          id:     int,
          nome:   string,
          email:  string,
          senha:  string,
          login:  string,
          foto:   string,
          CR:     string
        }`

        retorno Vázio

        status: `200 | 500`

  * /materia (get)

        envio `id_aluno: int`

        retorno `[{
          id:           int,
          nome:         string,
          Custo:        int,
          professor:    {
            nome:   string,
            foto:   string,
            email:  string
          }
        },]`

        status: `200 | 500`

* /**materia**

  * / (post)

        envio
        `{
          nome:     string
          Custo:    int
        }`

        retorno `{ mensagem: string }`

        status: `200 | 400`

  * / (get)

        envio Vázio

        retorno `[{
          id:   int,
          nome: string,
          Custo:   int
        },]`

        status: `200 | 500`

  * / (put)

        envio `{
          id:   int,
          nome: string,
          Custo:   int
        }`

        retorno Vázio

        status: `200 | 500`

  * / (delete)

        envio `id: int`

        retorno `{ mensagem: string}`

        status: `200 | 500`


  * /vincular (post)

        envio `{ id_aluno: int, id_materia: int }`

        retorno `{ mensagem: string}`

        status: `200 | 500`

  * /alunos (get)

        envio `id_materia: int`

        retorno `[{
          id:     int,
          nome:   string,
          email:  string,
          senha:  string,
          login:  string,
          foto:   string,
          CR:     float
        }]`

        status: `200 | 500`





* **aula/**

-- Obs: aula em andamento é onde não tem data fim
-- 

  * / (post)

        envio
        `{
          materia_id:    int
          professor_id:  int
        }`

        retorno `{ mensagem: string }`

        status: `200 | 400`

  * / get
      envio vázio
        -- manda todas as aulas em aberto

    * /{id}
      envio vázio
        -- manda todas as aulas em aberto do aluno em especifico

* /**Chat/{nomeSala}** -> fodase é a parte do leo

  * /fodase
    - https://www.youtube.com/watCusto?v=WV1WU757G9I
