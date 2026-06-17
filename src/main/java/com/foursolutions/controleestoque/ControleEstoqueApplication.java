package com.foursolutions.controleestoque;

import com.foursolutions.controleestoque.service.ProdutoService;
import com.foursolutions.controleestoque.ui.TelaPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import javax.swing.SwingUtilities;

@SpringBootApplication
public class ControleEstoqueApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext context = SpringApplication.run(ControleEstoqueApplication.class, args);
        ProdutoService service = context.getBean(ProdutoService.class);
        SwingUtilities.invokeLater(() -> new TelaPrincipal(service).setVisible(true));
    }
}