package com.foursolutions.controleestoque.repository;

import com.foursolutions.controleestoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // Diz ao Spring: "Esta interface é responsável pelo acesso aos dados no banco"
// JpaRepository<Produto, Long>: 
// - Produto: A entidade que vamos manipular.
// - Long: O tipo da chave primária (ID) dessa entidade.
// Ao estender essa interface, ganhamos de graça métodos como save(), findById(), deleteById(), findAll(), etc.
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    /**
     * Método Mágico do Spring Data JPA.
     * O Spring lê o nome do método e gera automaticamente a query SQL:
     * SELECT * FROM produto WHERE ativo = true
     * 
     * Usamos isso para listar apenas produtos visíveis na tela, 
     * ignorando os que foram desativados (Soft Delete).
     */
    List<Produto> findByAtivoTrue();
    
    /**
     * Outro método mágico para validação de duplicidade.
     * Gera a query: SELECT COUNT(*) FROM produto WHERE nome = ? AND ativo = true
     * 
     * Retorna 'true' se já existir um produto ATIVO com esse nome exato.
     * Usamos antes de cadastrar ou editar para impedir nomes repetidos.
     */
    boolean existsByNomeAndAtivoTrue(String nome);
}