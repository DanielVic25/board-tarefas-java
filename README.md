# Board de Tarefas - Java CLI

Um sistema de gerenciamento de quadros (boards) de tarefas em linha de comando, desenvolvido para consolidar conceitos de Programação Orientada a Objetos (POO), persistência de dados com JDBC e controle de versões de banco de dados com Liquibase.

## 🚀 Tecnologias Utilizadas

* **Java 21**: Utilização de recursos modernos como `record` para DTOs.
* **JDBC (Java Database Connectivity)**: Gerenciamento de conexões e operações CRUD manuais.
* **PostgreSQL**: Banco de dados relacional utilizado para a persistência.
* **Liquibase**: Automação e versionamento do schema do banco de dados.
* **Maven**: Gerenciamento de dependências e automação do build.
* **Lombok**: Redução de código boilerplate em entidades e DAOs.

## 📋 Funcionalidades

* **Gerenciamento de Boards**: Criação, seleção e exclusão de quadros personalizados.
* **Fluxo de Cards**: Criação de tarefas com título e descrição.
* **Movimentação**: Transferência de cards entre colunas (INITIAL, PENDING, FINAL, CANCEL).
* **Controle de Bloqueios**: Bloqueio e desbloqueio de cards com registro de justificativa.
* **Edição**: Atualização de títulos de cards existentes.

## 🗃️ Modelo de Dados

As migrações do banco de dados abrangem as seguintes estruturas:

* **BOARDS**: Armazena os nomes dos quadros.
* **BOARDS_COLUMNS**: Define as etapas do fluxo, incluindo o tipo de coluna (Kind: INITIAL, PENDING, FINAL, CANCEL) e a ordem de exibição.
* **CARDS**: Contém as informações das tarefas, como título e descrição, vinculadas a uma coluna.
* **BLOCKS**: Armazena o histórico de bloqueios, motivos de interrupção e registros de desbloqueio associados a um card.

## 🏗️ Estrutura do Projeto

O projeto é organizado em camadas para separar responsabilidades:
* **`ui`**: Interface de linha de comando para interação com o usuário.
* **`service`**: Camada de lógica de negócio e controle de transações (commit/rollback).
* **`persistence`**: Contém as classes de acesso a dados (DAOs) e as entidades do banco.
* **`migration`**: Estratégia de execução automática do Liquibase ao iniciar a aplicação.

## 🔧 Configuração

1. Certifique-se de ter o **PostgreSQL** rodando localmente.
2. Configure a URL, usuário e senha no arquivo `src/main/resources/application.properties`.
3. O banco de dados será populado automaticamente na primeira execução através do Liquibase.
