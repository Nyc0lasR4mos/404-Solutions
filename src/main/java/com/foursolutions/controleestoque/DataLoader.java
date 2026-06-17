package com.foursolutions.controleestoque;

import com.foursolutions.controleestoque.model.Produto;
import com.foursolutions.controleestoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired private ProdutoRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            System.out.println(">>> Carregando dados iniciais... <<<");
            List<Produto> produtos = Arrays.asList(
                new Produto("Booster Pokémon Escarlate e Violeta", "Pokémon TCG", 24.90, 120, 30),
                new Produto("Booster Pokémon Coroa Estelar", "Pokémon TCG", 27.90, 85, 20),
                new Produto("Booster Pokémon Fagulhas Impetuosas", "Pokémon TCG", 29.90, 75, 20),
                new Produto("Booster Yu-Gi-Oh! Alliance Insight", "Yu-Gi-Oh! TCG", 34.90, 60, 15),
                new Produto("Booster Yu-Gi-Oh! Supreme Darkness", "Yu-Gi-Oh! TCG", 39.90, 50, 15),
                new Produto("Booster Yu-Gi-Oh! Rage of the Abyss", "Yu-Gi-Oh! TCG", 36.90, 45, 12),
                new Produto("ETB Pokémon Destinos de Paldea", "Pokémon TCG", 349.90, 18, 5),
                new Produto("ETB Pokémon Coroa Estelar", "Pokémon TCG", 369.90, 15, 5),
                new Produto("Funko Pop! Ichigo Kurosaki (Bleach)", "Colecionáveis", 149.90, 25, 8),
                new Produto("Funko Pop! Rukia Kuchiki (Bleach)", "Colecionáveis", 139.90, 18, 5),
                new Produto("Funko Pop! Goku Super Saiyajin (Dragon Ball)", "Colecionáveis", 159.90, 30, 10),
                new Produto("Funko Pop! Vegeta Over 9000 (Dragon Ball)", "Colecionáveis", 169.90, 20, 5),
                new Produto("Action Figure Naruto Uzumaki Sage Mode", "Action Figures", 289.90, 12, 4),
                new Produto("Action Figure Sasuke Uchiha Rinnegan", "Action Figures", 309.90, 10, 3),
                new Produto("Action Figure Monkey D. Luffy Gear 5", "Action Figures", 399.90, 8, 2),
                new Produto("Action Figure Roronoa Zoro Wano", "Action Figures", 349.90, 10, 3),
                new Produto("Action Figure Portgas D. Ace", "Action Figures", 329.90, 12, 4),
                new Produto("Action Figure Kakashi Hatake Anbu", "Action Figures", 279.90, 14, 4),
                new Produto("Funko Pop! Kenpachi Zaraki (Bleach)", "Colecionáveis", 159.90, 15, 5),
                new Produto("Funko Pop! Gohan Beast (Dragon Ball)", "Colecionáveis", 179.90, 12, 4)
            );
            repository.saveAll(produtos);
            System.out.println(">>> Dados carregados! <<<");
        }
    }
}