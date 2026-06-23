package com.foursolutions.controleestoque.service;

import com.foursolutions.controleestoque.model.Produto;
import com.foursolutions.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta classe contém as regras de negócio
public class ProdutoService {
    @Autowired private ProdutoRepository repo; // Injeta o acesso ao banco automaticamente

    private double lucroDiario = 0.0; // Guarda o lucro na memória RAM (rápido)
    private LocalDate dataControle = LocalDate.now();
    private static final String ARQUIVO_LUCRO = "lucro_diario.txt"; // Arquivo para persistir o lucro

    // Roda assim que o app inicia. Recupera o lucro salvo anteriormente.
    @PostConstruct
    public void carregarLucroSalvo() {
        try {
            File file = new File(ARQUIVO_LUCRO);
            if (file.exists()) {
                String conteudo = Files.readString(file.toPath());
                this.lucroDiario = Double.parseDouble(conteudo.trim());
            }
        } catch (Exception e) {
            this.lucroDiario = 0.0; // Se der erro, começa do zero
        }
    }

    // Salva o valor atual do lucro no arquivo de texto
    private void salvarLucroNoArquivo() {
        try {
            Files.writeString(Path.of(ARQUIVO_LUCRO), String.valueOf(this.lucroDiario));
        } catch (IOException e) {
            System.err.println("Erro ao salvar lucro: " + e.getMessage());
        }
    }

    // Método público para salvar produtos (usado pela Tela)
    public Produto salvar(Produto p) {
        return repo.save(p);
    }

    // Método para desativar produto (Soft Delete)
    public void desativarProduto(Long id) {
        Optional<Produto> optionalProduto = repo.findById(id);
        if (optionalProduto.isPresent()) {
            Produto p = optionalProduto.get();
            p.setAtivo(false); // Marca como inativo, não apaga
            repo.save(p);
        } else {
            throw new RuntimeException("Produto não encontrado");
        }
    }

    // Lista apenas produtos ativos para a interface
    public List<Produto> listarTodos() { 
        return repo.findByAtivoTrue(); 
    }

    // Valida se o nome já existe para impedir duplicatas
    public boolean nomeJaExiste(String nome, Long idIgnorar) {
        if (nome == null || nome.trim().isEmpty()) return false;
        String nomeNormalizado = nome.trim().toLowerCase();
        for (Produto p : repo.findAll()) {
            if (!p.isAtivo()) continue;
            if (p.getNome().trim().toLowerCase().equals(nomeNormalizado)) {
                if (idIgnorar == null || !p.getId().equals(idIgnorar)) {
                    return true; // Achou duplicata!
                }
            }
        }
        return false; // Nome livre
    }

    // Atualiza estoque e calcula lucro se for venda
    public String ajustarEstoque(Long id, int quantidade, boolean isVenda) {
        Produto p = repo.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        if (!p.isAtivo()) return "❌ Produto inativo.";
        
        int novoEstoque = p.getEstoque() + quantidade;
        if (novoEstoque < 0) return "❌ Estoque insuficiente.";

        p.setEstoque(novoEstoque);
        repo.save(p); // Salva no banco H2

        if (isVenda && quantidade < 0) { // Se foi retirada POR VENDA...
            verificarResetDiario();
            lucroDiario += Math.abs(quantidade) * p.getPreco(); // Soma ao lucro
            salvarLucroNoArquivo(); // Salva no arquivo imediatamente
            return "✅ Venda registrada! Lucro atualizado.";
        }
        return "✅ Estoque atualizado."; // Se foi correção, só muda o número
    }

    public double getLucroDiario() {
        verificarResetDiario();
        return lucroDiario;
    }

    // Zera o lucro se mudou o dia (meia-noite)
    private void verificarResetDiario() {
        if (!LocalDate.now().equals(dataControle)) {
            lucroDiario = 0.0;
            dataControle = LocalDate.now();
            salvarLucroNoArquivo();
        }
    }

    // Zera o lucro manualmente (botão na tela)
    public void zerarLucroDiario() {
        this.lucroDiario = 0.0;
        salvarLucroNoArquivo();
    }

    // Gera lista de alertas para produtos com estoque baixo
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