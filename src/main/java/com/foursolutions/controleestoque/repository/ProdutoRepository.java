package com.foursolutions.controleestoque.repository;

import com.foursolutions.controleestoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrue();
    boolean existsByNomeAndAtivoTrue(String nome);
}