package com.foursolutions.controleestoque.ui;

import com.foursolutions.controleestoque.model.Produto;
import com.foursolutions.controleestoque.service.ProdutoService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaPrincipal extends JFrame {
    private final ProdutoService service; // Conexão com as regras de negócio
    private JTable tabela;                // A grade onde mostramos os produtos
    private DefaultTableModel modelo;     // Modelo de dados da tabela
    private JLabel lblLucro;              // Mostra o dinheiro ganho no dia
    private JLabel lblData;               // Mostra a data atual
    private JTextField tfBusca;           // Campo para filtrar produtos

    public TelaPrincipal(ProdutoService service) {
        this.service = service;
        configurarTela();   // Monta a janela
        carregarDados();    // Puxa os produtos do banco
        verificarAlertas(); // Checa se tem estoque baixo
    }

    // Configura o visual da janela (Layout, Botões, Cores)
    private void configurarTela() {
        setTitle("404 Solutions - Controle de Estoque");
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout(10, 10));

        // --- TOPO: Busca e Data ---
        JPanel painelSuperior = new JPanel(new BorderLayout());

        // Lado Esquerdo: Barra de Pesquisa
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblBusca = new JLabel("🔍 Buscar por Nome ou ID:");
        tfBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpar = new JButton("Limpar");

        painelBusca.add(lblBusca);
        painelBusca.add(tfBusca);
        painelBusca.add(btnBuscar);
        painelBusca.add(btnLimpar);

        // Lado Direito: Data Atual (Contexto Gerencial)
        JPanel painelData = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        lblData = new JLabel("📅 " + dataFormatada);
        lblData.setFont(new Font("Arial", Font.PLAIN, 12));
        lblData.setForeground(Color.GRAY);
        painelData.add(lblData);

        painelSuperior.add(painelBusca, BorderLayout.WEST);
        painelSuperior.add(painelData, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);

        // --- CENTRO: Tabela de Produtos ---
        // Definimos as colunas. isCellEditable(false) impede edição direta na célula (segurança!)
        String[] colunas = {"ID", "Nome", "Categoria", "Preço", "Estoque", "Mínimo"};
        modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER); // Adiciona rolagem

        // --- RODAPÉ: Botões de Ação e Lucro ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnCadastrar = new JButton("➕ Cadastrar");
        JButton btnAtualizar = new JButton("📝 Atualizar");
        JButton btnExcluir = new JButton("🗑️ Excluir"); // Na verdade, desativa (Soft Delete)
        JButton btnAddEstoque = new JButton("^ Adicionar");
        JButton btnRemEstoque = new JButton("v Retirar");

        lblLucro = new JLabel("💰 Lucro Diário: R$ 0,00");
        lblLucro.setFont(new Font("Arial", Font.BOLD, 14));
        lblLucro.setForeground(new Color(0, 100, 0)); // Verde para lucro

        // Botão para zerar o caixa (simula fim do dia)
        JButton btnZerarLucro = new JButton("🔄 Zerar Lucro");
        btnZerarLucro.setToolTipText("Simula o fechamento do caixa do dia anterior");
        btnZerarLucro.setBackground(new Color(255, 200, 200));

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(new JSeparator(SwingConstants.VERTICAL));
        painelBotoes.add(btnAddEstoque);
        painelBotoes.add(btnRemEstoque);
        painelBotoes.add(new JSeparator(SwingConstants.VERTICAL));
        painelBotoes.add(lblLucro);
        painelBotoes.add(btnZerarLucro);

        add(painelBotoes, BorderLayout.SOUTH);

        // Conecta os cliques aos métodos (Listeners)
        btnCadastrar.addActionListener(e -> abrirCadastro());
        btnAtualizar.addActionListener(e -> abrirAtualizacao());
        btnExcluir.addActionListener(e -> excluirSelecionado());
        btnAddEstoque.addActionListener(e -> ajustarEstoque(true));
        btnRemEstoque.addActionListener(e -> ajustarEstoque(false));

        btnBuscar.addActionListener(e -> filtrarProdutos());
        btnLimpar.addActionListener(e -> { tfBusca.setText(""); carregarDados(); });
        tfBusca.addActionListener(e -> filtrarProdutos()); // Enter também busca

        btnZerarLucro.addActionListener(e -> confirmarZerarLucro());
    }

    // Atualiza a tabela com os dados vindos do Service
    private void carregarDados() {
        modelo.setRowCount(0); // Limpa a tabela antes de preencher
        for (Produto p : service.listarTodos()) {
            // Formata o preço para R$ e adiciona a linha
            modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getCategoria(),
                    String.format("R$ %.2f", p.getPreco()), p.getEstoque(), p.getEstoqueMinimo()});
        }
        // Atualiza o label de lucro na tela
        lblLucro.setText(String.format("💰 Lucro Diário: R$ %.2f", service.getLucroDiario()));
    }

    // Filtra a tabela localmente conforme o usuário digita
    private void filtrarProdutos() {
        String termo = tfBusca.getText().trim().toLowerCase();
        if (termo.isEmpty()) { carregarDados(); return; }
        modelo.setRowCount(0);
        List<Produto> todos = service.listarTodos();
        for (Produto p : todos) {
            if (String.valueOf(p.getId()).contains(termo) || p.getNome().toLowerCase().contains(termo)) {
                modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getCategoria(),
                        String.format("R$ %.2f", p.getPreco()), p.getEstoque(), p.getEstoqueMinimo()});
            }
        }
        if (modelo.getRowCount() == 0) JOptionPane.showMessageDialog(this, "Nenhum produto encontrado.");
    }

    // Abre popup para cadastrar novo produto
    private void abrirCadastro() {
        JTextField tfNome = new JTextField();
        JTextField tfCat = new JTextField();
        JTextField tfPreco = new JTextField();
        JTextField tfEstoque = new JTextField();
        JTextField tfMinimo = new JTextField();
        Object[] msg = {"Nome:", tfNome, "Categoria:", tfCat, "Preço:", tfPreco,
                "Estoque Inicial:", tfEstoque, "Estoque Mínimo:", tfMinimo};
        if (JOptionPane.showConfirmDialog(this, msg, "Cadastrar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String nome = tfNome.getText().trim();
                if (nome.isEmpty()) { JOptionPane.showMessageDialog(this, "Nome vazio!"); return; }
                // Valida se já existe produto com esse nome
                if (service.nomeJaExiste(nome, null)) {
                    JOptionPane.showMessageDialog(this, "❌ Produto já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Produto p = new Produto(nome, tfCat.getText(), Double.parseDouble(tfPreco.getText()),
                        Integer.parseInt(tfEstoque.getText()), Integer.parseInt(tfMinimo.getText()));
                service.salvar(p); // Salva no banco via Service
                carregarDados();
                verificarAlertas();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Dados inválidos!"); }
        }
    }

    // Abre popup para editar produto existente
    private void abrirAtualizacao() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione um produto."); return; }
        Long id = (Long) modelo.getValueAt(linha, 0);
        Produto p = service.listarTodos().stream().filter(prod -> prod.getId().equals(id)).findFirst().orElse(null);
        if (p == null) return;

        JTextField tfNome = new JTextField(p.getNome());
        JTextField tfCat = new JTextField(p.getCategoria());
        JTextField tfPreco = new JTextField(String.valueOf(p.getPreco()));
        JTextField tfMinimo = new JTextField(String.valueOf(p.getEstoqueMinimo()));

        // Checkbox crucial para Soft Delete (Ativar/Desativar) 
        JCheckBox chkAtivo = new JCheckBox("Produto Ativo?", p.isAtivo());
        chkAtivo.setFont(new Font("Arial", Font.BOLD, 12));

        Object[] msg = {"Nome:", tfNome, "Categoria:", tfCat, "Preço:", tfPreco, "Estoque Mínimo:", tfMinimo, "", chkAtivo};
        if (JOptionPane.showConfirmDialog(this, msg, "Atualizar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String novoNome = tfNome.getText().trim();
                if (service.nomeJaExiste(novoNome, id)) {
                    JOptionPane.showMessageDialog(this, "❌ Nome já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                p.setNome(novoNome);
                p.setCategoria(tfCat.getText());
                p.setPreco(Double.parseDouble(tfPreco.getText()));
                p.setEstoqueMinimo(Integer.parseInt(tfMinimo.getText()));
                p.setAtivo(chkAtivo.isSelected()); // Define se está ativo ou não
                service.salvar(p);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Atualizado com sucesso!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Dados inválidos!"); }
        }
    }

    // Desativa o produto (Soft Delete) em vez de apagar fisicamente
    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione um produto."); return; }
        if (JOptionPane.showConfirmDialog(this, "Desativar produto?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            service.desativarProduto((Long) modelo.getValueAt(linha, 0));
            carregarDados();
        }
    }

    // Adiciona ou remove o estoque. Pergunta se é venda para calcular lucro.
    private void ajustarEstoque(boolean isAdicao) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione um produto."); return; }
        Long id = (Long) modelo.getValueAt(linha, 0);
        String qtdStr = JOptionPane.showInputDialog(this, isAdicao ? "Quantidade a ADICIONAR:" : "Quantidade a RETIRAR:");
        if (qtdStr == null || qtdStr.trim().isEmpty()) return;
        try {
            int qtd = Integer.parseInt(qtdStr);
            if (qtd <= 0) { JOptionPane.showMessageDialog(this, "Valor deve ser > 0"); return; }
            if (isAdicao) {
                JOptionPane.showMessageDialog(this, service.ajustarEstoque(id, qtd, false));
            } else {
                // Pergunta crucial: Foi venda? Isso define se entra dinheiro no caixa.
                int opcao = JOptionPane.showConfirmDialog(this, "Motivo da retirada?\nSIM = Venda\nNÃO = Correção", "Motivo", JOptionPane.YES_NO_OPTION);
                boolean isVenda = (opcao == JOptionPane.YES_OPTION);
                JOptionPane.showMessageDialog(this, service.ajustarEstoque(id, -qtd, isVenda));
            }
            carregarDados();
            verificarAlertas();
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Número inválido!"); }
    }

    // Verifica alertas de estoque baixo ao iniciar ou após mudanças
    private void verificarAlertas() {
        List<String> alertas = service.gerarAlertasEstoque();
        if (!alertas.isEmpty()) {
            StringBuilder sb = new StringBuilder("📦 ALERTAS:\n\n");
            alertas.forEach(a -> sb.append(a).append("\n"));
            JOptionPane.showMessageDialog(this, sb.toString(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Confirmação antes de zerar o lucro do dia
    private void confirmarZerarLucro() {
        int confirmacao = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja ZERAR o lucro diário?\nIsso simula o fechamento do caixa.",
            "Confirmação de Fechamento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (confirmacao == JOptionPane.YES_OPTION) {
            service.zerarLucroDiario();
            carregarDados(); // Atualiza o label de lucro na tela
            JOptionPane.showMessageDialog(this, "Lucro diário zerado com sucesso!");
        }
    }
}
