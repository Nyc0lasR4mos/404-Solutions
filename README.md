# 📦 404 Solutions — Encontramos o que falta.

<div align="center">
  <img width="400" alt="Logo 404 Solutions" src="https://github.com/user-attachments/assets/d961beb0-059a-4973-a264-08b49a4d3f2c" />
</div>

> Protótipo funcional desenvolvido para a atividade **Canes Solutions**, focado em organização, controle de vendas e geração de insights gerenciais para pequenos comércios.

![Java](https://img.shields.io/badge/Java-25-007396?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?style=flat&logo=spring&logoColor=white)
![JPA/Hibernate](https://img.shields.io/badge/JPA/Hibernate-5.6-59666C?style=flat)
![H2 Database](https://img.shields.io/badge/H2-Database-1A5276?style=flat)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-FF9900?style=flat)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white)

---

## 📖 Sobre o Projeto

A **404 Solutions** é uma startup estudantil que desenvolveu este sistema para atender à demanda de um comércio real por um controle de estoque simples, confiável e com visão gerencial imediata. 

O protótipo substitui planilhas manuais por uma aplicação desktop integrada a **Spring Boot + JPA + H2**, garantindo persistência de dados, validação de regras de negócio e geração automática de insights como **alertas de estoque crítico** e **lucro diário**.

---

## ✨ Funcionalidades

- ✅ **CRUD Completo**: Cadastro, consulta, atualização e exclusão lógica de produtos.
- 🔍 **Busca Avançada**: Filtro em tempo real por **Nome** ou **ID** do produto.
- 🔼🔽 **Controle de Estoque Inteligente**: Botões `^` (adicionar) e `v` (retirar) com validação de fluxo.
- 💰 **Cálculo de Lucro Diário**: Ao retirar um item, o sistema pergunta se é **venda** ou **correção**. Apenas vendas somam ao lucro do dia.
- ⚠️ **Alertas Gerenciais**: Cada produto possui um `estoqueMínimo` personalizado. O sistema notifica automaticamente quando um item atinge o limite crítico.
- 🚫 **Prevenção de Duplicatas**: O sistema impede o cadastro de produtos com nomes idênticos.
- ♻️ **Soft Delete (Ativar/Desativar)**: Produtos não são excluídos fisicamente, mas marcados como inativos, preservando o histórico e o ID. Possibilidade de reativação via interface.
- 🗄️ **Persistência em Banco H2**: Dados salvos em arquivo local (`estoque_db.mv.db`), com console web para inspeção em tempo real.
- 📦 **Dados Iniciais Automáticos**: O sistema carrega automaticamente um catálogo base de 20 produtos na primeira execução para facilitar testes e demonstrações.
- 🖥️ **Interface Desktop (Swing)**: Fluxo travado para evitar edições diretas na tabela, garantindo que toda alteração passe pela camada de negócio.

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|--------|------------|
| **Linguagem** | Java 25 |
| **Framework Backend** | Spring Boot 3.2 + Spring Data JPA |
| **Banco de Dados** | H2 (modo arquivo) |
| **Interface Gráfica** | Java Swing |
| **Gerenciamento** | Maven |
| **IDE** | NetBeans (compatível com IntelliJ/Eclipse) |
| **Versionamento** | Git & GitHub |

---

## 🔄 Versionamento e Fluxo de Trabalho

O projeto utiliza **Git** para controle de versão, seguindo boas práticas de commit semântico e organização de branches.

### 🌿 Estrutura de Branches
- `main`: Branch principal, contém a versão estável e pronta para apresentação.

### 💡 Padrão de Commits
Utilizamos mensagens de commit claras para facilitar o rastreamento de alterações:
- `feat:` Nova funcionalidade (ex: `feat: adiciona cálculo de lucro diário`)
- `fix:` Correção de bug (ex: `fix: corrige alerta de estoque negativo`)
- `docs:` Alteração na documentação (ex: `docs: atualiza README`)
- `refactor:` Refatoração de código sem mudança de comportamento

---

## 🚀 Como Executar o Projeto

### 📋 Pré-requisitos
- JDK 25 instalado (ou JDK 17/21 compatível)
- NetBeans, IntelliJ ou Eclipse com suporte a Maven
- Git instalado

### 📥 Clone o Repositório
```bash
git clone https://github.com/Nyc0lasR4mos/404-Solutions.git
cd 404-Solutions

▶️ Rodar a Aplicação
Abra o projeto na sua IDE como Projeto Maven.
Aguarde o download das dependências (primeira execução pode levar alguns minutos).
Execute a classe principal: src/main/java/com/foursolutions/controleestoque/ControleEstoqueApplication.java
O Spring Boot irá inicializar, criar o banco H2 automaticamente e abrir a interface Swing.
Nota: Na primeira execução, o sistema carregará automaticamente 20 produtos de exemplo.

🗃️ Acesso ao Banco de Dados (H2 Console)
O projeto inclui o console web do H2 para inspeção e validação dos dados durante a apresentação ou testes.
Campo
Valor
🔗 URL
http://localhost:8080/h2-console
📌 JDBC URL
jdbc:h2:file:./estoque_db
👤 Usuário
sa
🔑 Senha
(deixe em branco)
💡 Os dados são persistidos no arquivo estoque_db.mv.db na raiz do projeto. Para resetar o banco, basta excluir esse arquivo e rodar a aplicação novamente.

📁 Estrutura do Projeto

404-Solutions/
├── src/main/java/com/foursolutions/controleestoque/
│   ├── ControleEstoqueApplication.java # Bootstrap Spring + Swing (EDT)
│   ├── DataLoader.java                 # Carga inicial de dados
│   ├── model/
│   │   └── Produto.java                # Entidade JPA mapeada
│   ├── repository/
│   │   └── ProdutoRepository.java      # Interface Spring Data JPA
│   ├── service/
│   │   └── ProdutoService.java         # Regras de negócio, lucro e alertas
│   └── ui/
│       └── TelaPrincipal.java          # Interface gráfica Swing
├── src/main/resources/
│   └── application.properties          # Configurações Spring + H2
├── pom.xml                             # Dependências Maven
└── README.md                           # Documentação do projeto

🏗️ Decisões de Arquitetura
Decisão || Motivo Técnico
---|---
Tabela não editável diretamente || Garante que toda alteração passe por validação e regra de negócio no Service, evitando inconsistências.
Swing + Spring Boot || Combina interface desktop nativa com injeção de dependência, transações e JPA enterprise.
SwingUtilities.invokeLater() || Garante que a UI rode na EDT (Event Dispatch Thread), evitando travamentos e erros de concorrência.
Lucro em memória || Para protótipo acadêmico, é eficiente e simples. Em produção, seria persistido em entidade FechamentoDiario com @Scheduled.
Alerta por estoqueMinimo || Evita alarmes falsos. Produtos de giro alto ou baixo têm limites personalizados, gerando insight real para o gestor.
Soft Delete || Preserva a integridade referencial e o histórico de vendas, permitindo reativação de produtos sem perda de dados.

👥 Equipe 404 Solutions
Nome || Função no Projeto
---|---
Nycolas Ramos || Backend & Arquitetura
Leonardo Zanetti || Interface & UX
Heitor Inácio || Documentação & Testes
Matheus Schunck || Levantamento de Requisitos & Entrevista

🏢 Startup: 404 Solutions
🎯 Missão: Entregar soluções digitais simples, funcionais e escaláveis para pequenos comércios.

📄 Licença & Uso Acadêmico
Este projeto foi desenvolvido para fins acadêmicos como parte da atividade Canes Solutions.
Sinta-se livre para estudar, forkar e adaptar o código. Créditos à 404 Solutions são apreciados.
📬 Contato & Links
🌐 Repositório: github.com/Nyc0lasR4mos/404-Solutions
📧 Dúvidas ou sugestões: Abra uma Issue ou Pull Request no repositório.
Feito com ☕ Java, 🍃 Spring e 💡 boas práticas pela 404 Solutions.
```
