package com.foursolutions.controleestoque.model;

import jakarta.persistence.*; // Traz as "etiquetas" que dizem ao Spring: isso é banco de dados!

@Entity // AVISO IMPORTANTE: Esta classe vira uma tabela chamada 'PRODUTO' no banco H2.
public class Produto {
    
    @Id // Este é o RG do produto. Único e intransferível.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco conta sozinho: 1, 2, 3... não precisamos digitar.
    private Long id;
    
    private String nome;       // O que estamos vendendo? (Ex: Funko Goku)
    private String categoria;  // Onde guardamos? (Ex: Colecionáveis)
    private Double preco;      // Quanto custa? (Usamos Double para aceitar centavos)
    private Integer estoque;   // Quantos temos na prateleira agora?
    private Integer estoqueMinimo; // O limite de segurança. Se baixar disso, acende o alerta vermelho!
    private boolean ativo = true; // O segredo do Soft Delete: true = aparece na lista, false = some (mas não apaga).

    // Construtor Vazio: O Hibernate EXIGE isso para criar o objeto quando busca do banco.
    public Produto() {}

    // Construtor Cheio: Usamos isso na hora de cadastrar um produto novo na tela.
    public Produto(String nome, String categoria, Double preco, Integer estoque, Integer estoqueMinimo) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.estoque = estoque;
        this.estoqueMinimo = estoqueMinimo;
        this.ativo = true; // Todo produto nasce ativo por padrão.
    }

    // Getters e Setters: São as "portas" de entrada e saída dos dados privados.
    // Sem eles, a TelaPrincipal não conseguiria ler ou alterar nada aqui dentro.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    
    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}