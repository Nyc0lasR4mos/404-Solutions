package com.foursolutions.controleestoque.service;

import com.foursolutions.controleestoque.model.Produto;
import com.foursolutions.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired private ProdutoRepository repo;

    private double lucroDiario = 0.0;
    private LocalDate dataControle = LocalDate.now();

    public List<Produto> listarTodos() { 
        return repo.findByAtivoTrue(); // Só lista ativos
    }

    public Produto salvar(Produto p) { 
        return repo.save(p); 
    }

    public void excluir(Long id) { 
        repo.deleteById(id); 
    }

    public void desativarProduto(Long id) {
        Optional<Produto> optionalProduto = repo.findById(id);
        if (optionalProduto.isPresent()) {
            Produto p = optionalProduto.get();
            p.setAtivo(false);
            repo.save(p);
        } else {
            throw new RuntimeException("Produto não encontrado");
        }
    }

    public boolean nomeJaExiste(String nome, Long idIgnorar) {
        if (nome == null || nome.trim().isEmpty()) return false;
        String nomeNormalizado = nome.trim().toLowerCase();
        for (Produto p : repo.findAll()) {
            if (!p.isAtivo()) continue;
            if (p.getNome().trim().toLowerCase().equals(nomeNormalizado)) {
                if (idIgnorar == null || !p.getId().equals(idIgnorar)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String ajustarEstoque(Long id, int quantidade, boolean isVenda) {
        Produto p = repo.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        if (!p.isAtivo()) return "❌ Este produto está inativo.";
        
        int novoEstoque = p.getEstoque() + quantidade;
        if (novoEstoque < 0) return "❌ Estoque insuficiente.";

        p.setEstoque(novoEstoque);
        repo.save(p);

        if (isVenda && quantidade < 0) {
            verificarResetDiario();
            lucroDiario += Math.abs(quantidade) * p.getPreco();
            return "✅ Venda registrada! Lucro atualizado.";
        }
        return "✅ Estoque atualizado.";
    }

    public double getLucroDiario() {
        verificarResetDiario();
        return lucroDiario;
    }

    private void verificarResetDiario() {
        if (!LocalDate.now().equals(dataControle)) {
            lucroDiario = 0.0;
            dataControle = LocalDate.now();
        }
    }

    public List<String> gerarAlertasEstoque() {
        List<String> alertas = new ArrayList<>();
        for (Produto p : repo.findAll()) {
            if (p.isAtivo() && p.getEstoque() <= p.getEstoqueMinimo()) {
                alertas.add("⚠️ " + p.getNome() + " | Estoque: " + p.getEstoque() + " (Mín: " + p.getEstoqueMinimo() + ")");
            }
        }
        return alertas;
    }
}